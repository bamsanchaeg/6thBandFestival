package com.sixthband.festival.funding.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sixthband.festival.funding.dto.ApproveResponseDto;
import com.sixthband.festival.funding.dto.ReadyResponseDto;
import com.sixthband.festival.funding.dto.RequestKakaoPayDto;
import com.sixthband.festival.util.Utils;

@Service
public class KakaoPayService {

    // 카카오페이 KEY
    private static final String SECRET_KEY = "SECRET_KEY";
    
    // 카카오페이 결제창 연결
    public ReadyResponseDto readyPayment(RequestKakaoPayDto requestKakaoPayDto) {
        // Map을 생성하여 JSON 문자열로 변환
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", "TC0ONETIME"); // 가맹점 코드(테스트용)
        requestBody.put("partner_order_id", requestKakaoPayDto.getPartner_order_id()); // 주문번호
        requestBody.put("partner_user_id", requestKakaoPayDto.getPartner_user_id()); // 회원 pk
        requestBody.put("item_name", requestKakaoPayDto.getItem_name()); // 상품명
        requestBody.put("quantity", String.valueOf(requestKakaoPayDto.getTotal_quantity())); // 상품 수량
        requestBody.put("total_amount", requestKakaoPayDto.getTotal_amount()); // 상품 총액
        requestBody.put("tax_free_amount", "0"); // 상품 비과세 금액
        requestBody.put("approval_url", Utils.FESTIVAL_URL + "/api/funding/payCompleted"); // 결제 성공시 url > 모바일은 ip 주소로.. 나중에 url 로..
        requestBody.put("fail_url", Utils.FESTIVAL_URL + "/api/funding/payFail"); // 결제 취소시 url
        requestBody.put("cancel_url", Utils.FESTIVAL_URL + "/api/funding/payCancel"); // 결제 실패시 url

        System.out.println("requestBody - 보내는 정보 ::: " + requestBody);

        // HttpEntity : HTTP 요청 또는 응답에 해당하는 Http Header와 Http Body를 포함하는 클래스
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        // RestTemplate의 postForEntity : POST 요청을 보내고 ResponseEntity로 결과를 반환받는 메소드
        ResponseEntity<ReadyResponseDto> responseEntity = template.postForEntity(url, requestEntity, ReadyResponseDto.class);

        System.out.println("responseEntity - 받는 정보 ::: " + responseEntity); // tid, pc_url 있음

        return responseEntity.getBody();
    }

    // 카카오페이 측에 요청 시 헤더부에 필요한 값
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + SECRET_KEY);
        headers.set("Content-type", "application/json");

        return headers;
    }

    // 카카오페이 결제 승인 :: 사용자가 결제 수단을 선택하고 비밀번호를 입력해 결제 인증을 완료한 뒤, 최종적으로 결제 완료 처리를 하는 단계
    public ApproveResponseDto payApprove(String tid, String pgToken, String partner_order_id, String partner_user_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");              // 가맹점 코드(테스트용)
        parameters.put("tid", tid);                       // 결제 고유번호
        parameters.put("partner_order_id", partner_order_id); // 주문번호
        parameters.put("partner_user_id", partner_user_id);    // 회원 아이디
        parameters.put("pg_token", pgToken);              // 결제승인 요청을 인증하는 토큰

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 url
        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        ApproveResponseDto approveResponse = template.postForObject(url, requestEntity, ApproveResponseDto.class);
        
        System.out.println("결제승인 응답객체 - approveResponse ::: " + approveResponse);

        return approveResponse;
    }
}
