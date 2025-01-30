package com.sixthband.festival.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sixthband.festival.dto.OAuthToken;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.user.service.UserService;
import com.sixthband.festival.util.Utils;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    // 카카오 간편 로그인
    @RequestMapping("auth/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code, HttpSession session) { // 데이터를 리턴해주는 컨트롤러 함수

        // POST 방식으로 key=value 데이터를 요청 (카카오쪽으로)
		// 이 때 필요한 라이브러리가 RestTemplate, 얘를 쓰면 http 요청을 편하게 할 수 있다.
		RestTemplate rt = new RestTemplate();

		// HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 만들어 같이 보내줘야 한다.
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// body 데이터를 담을 오브젝트인 MultiValueMap를 만들어보자
		// body는 보통 key, value의 쌍으로 이루어지기 때문에 자바에서 제공해주는 MultiValueMap 타입을 사용한다.
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code"); // 고정값
		params.add("client_id", "880b331520fbca0de4ceadc4f809be72"); // 정리선 계정
		params.add("redirect_uri", "https://festival.null-pointer-exception.com/auth/kakao/callback");
		params.add("code", code);

		// 요청하기 위해 헤더(Header)와 데이터(Body)를 합친다.
		// kakaoTokenRequest는 데이터(Body)와 헤더(Header)를 Entity가 된다.
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		// POST 방식으로 Http 요청한다. 그리고 response 변수의 응답 받는다.
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
				HttpMethod.POST, // 요청할 방식
				kakaoTokenRequest, // 요청할 때 보낼 데이터
				String.class // 요청 시 반환되는 데이터 타입
		);

        // 여기 일단 추가해보자
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;

        try{
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());

        
        // 요청 후 추가 + 
		RestTemplate rt2 = new RestTemplate();

		// HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 만들어 같이 보내줘야 한다.
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);
		// POST 방식으로 Http 요청한다. 그리고 response 변수의 응답 받는다.
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST, // 요청할 방식
				kakaoProfileRequest2, // 요청할 때 보낼 데이터
				String.class // 요청 시 반환되는 데이터 타입
		);;

        //return response2.getBody(); // 정보 확인용
        // return response.getBody();
		// return "카카오 토큰 요청 완료 : 토큰 요청에 대한 응답 : "+response;


        // 로그인 처리를 위해 json 으로 변환 필요
        String responseBody = response2.getBody();
        JsonNode jsonNode = null;

        try{
            jsonNode = objectMapper.readTree(responseBody);
        } catch(IOException e) {
            e.printStackTrace();
        }

        if(jsonNode == null) {
            return "Error: Unable to parse user profile";
        }

        String kakaoAccountName = jsonNode.get("id").asText();
        String kakaoNickname = jsonNode.path("properties").path("nickname").asText();

        UserDto kakaoUser = new UserDto();
        kakaoUser.setAccount_name(kakaoAccountName);
        kakaoUser.setPassword("카카오");
        kakaoUser.setNickname(kakaoNickname);
        
        if(kakaoUser.getProfile_img() == null){
            kakaoUser.setProfile_img("/public/img/profile.png");
        }

        // 회원 유무 확인
        boolean isExistKaKaoAccount = userService.isExistAccountName(kakaoAccountName);
        if(!isExistKaKaoAccount) { // 회원이 아닌 경우 회원가입
            userService.userRegist(kakaoUser);
        }

        // 로그인 처리
        UserDto loginUser = userService.loginUserExist(kakaoUser);
        if(loginUser == null){
            return "user/loginFail";
        } else{
            session.setAttribute("loginUser", loginUser);

            UserDto userInfo = (UserDto) session.getAttribute("loginUser");
            String active = userInfo.getIs_active();

            if (active.equals("Y")) {

                // 여기서 사실은 정보수정 페이지로 넘겨야 함..! 필수값 입력하도록..!
                // + 비밀번호는 변경 못하도록 해야함!!

                return "redirect:" + Utils.FESTIVAL_URL;

            } else {
                // 계정 정지를 먹으면 아예 세션날려버리기.. or 세션에서 getIs_active 해야하는지?
                session.invalidate(); // 세션 날리기
                return "redirect:" + Utils.FESTIVAL_URL + "/user/notActivePage";
            }
        }
    }
}
