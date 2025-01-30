package com.sixthband.festival.ticket.service;

import com.sixthband.festival.ticket.dto.*;
import com.sixthband.festival.util.Utils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${kakaoPayKey}")
    private String kakaoPayKey;

    public ReadyResponseDto readyPayment(PaymentResponseDto paymentDto) throws IOException {
        // 요청 데이터 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cid", "TC0ONETIME");
        requestBody.put("partner_order_id", paymentDto.getPartner_order_id());
        requestBody.put("partner_user_id", paymentDto.getPartner_user_id());
        requestBody.put("item_name", paymentDto.getItem_name());
        requestBody.put("quantity", paymentDto.getQuantity());
        requestBody.put("total_amount", paymentDto.getTotal_amount());
        requestBody.put("tax_free_amount", 0);
        requestBody.put("approval_url", Utils.FESTIVAL_URL + "/api/ticket/completed");
        requestBody.put("fail_url", Utils.FESTIVAL_URL);
        requestBody.put("cancel_url", Utils.FESTIVAL_URL);

//        System.out.println(requestBody);
        // HttpEntity : HTTP 요청 또는 응답에 해당하는 Http Header와 Http Body를 포함하는 클래스
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, this.getHeaders());

        // RestTemplate
        // : Rest 방식 API를 호출할 수 있는 Spring 내장 클래스
        //   REST API 호출 이후 응답을 받을 때까지 기다리는 동기 방식 (json, xml 응답)
        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
        // RestTemplate의 postForEntity : POST 요청을 보내고 ResponseEntity로 결과를 반환받는 메소드
        ResponseEntity<ReadyResponseDto> responseEntity = template.postForEntity(url, entity, ReadyResponseDto.class);

//        System.out.println(responseEntity);
        return responseEntity.getBody();
    }

    // 카카오페이 측에 요청 시 헤더부에 필요한 값
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kakaoPayKey);
        headers.set("Content-type", "application/json");

        return headers;
    }

    // 카카오페이 결제 승인
    // 사용자가 결제 수단을 선택하고 비밀번호를 입력해 결제 인증을 완료한 뒤,
    // 최종적으로 결제 완료 처리를 하는 단계
    public AproveResponseDto payApprove(String tid, String pgToken, String oId, String uId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");              // 가맹점 코드(테스트용)
        parameters.put("tid", tid);                       // 결제 고유번호
        parameters.put("partner_order_id", oId);          // 주문번호
        parameters.put("partner_user_id", uId);           // 회원 아이디
        parameters.put("pg_token", pgToken);              // 결제승인 요청을 인증하는 토큰

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        RestTemplate template = new RestTemplate();
        String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
        AproveResponseDto aproveResponseDto = template.postForObject(url, requestEntity, AproveResponseDto.class);

        System.out.println("결제 승인" + aproveResponseDto);

//        payOrderView(tid);
        return aproveResponseDto;
    }

//    결제 조회
//    public TicketPaymentDto payOrderView(String tid) {
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("cid", "TC0ONETIME");              // 가맹점 코드(테스트용)
//        parameters.put("tid", tid);                       // 결제 고유번호
//
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
//
//        RestTemplate template = new RestTemplate();
//        String url = "https://open-api.kakaopay.com/online/v1/payment/order";
//        TicketPaymentDto ticketPaymentDto = template.postForObject(url, requestEntity, TicketPaymentDto.class);
//        System.out.println("주문 완료 : " + ticketPaymentDto);
//        return ticketPaymentDto;
//    }
}