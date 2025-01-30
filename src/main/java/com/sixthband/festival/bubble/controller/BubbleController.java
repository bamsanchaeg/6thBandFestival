package com.sixthband.festival.bubble.controller;

import com.sixthband.festival.bubble.dto.BubbleArtistDto;
import com.sixthband.festival.bubble.dto.BubblePostDto;
import com.sixthband.festival.bubble.service.BubbleService;
import com.sixthband.festival.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("bubble")
public class BubbleController {

    @Autowired
    BubbleService bubbleService;

    @RequestMapping("postWritingPage")
    public String postWritingPage(){
        return "bubble/postWritingPage";
    }


    //유저 아이디 정보 받아와야함. 이 부분 세션에서 가져오기.(8.2)
    @RequestMapping("postWritingProcess")
    public String postWritingProcess(BubblePostDto bubblePostDto,
                                     List<MultipartFile> postImages,
                                     HttpSession session){
        UserDto sessionUser = (UserDto) session.getAttribute("loginUser");
        if(sessionUser == null){
            System.out.println("로그인 정보가 없습니다.");
            return "bubble/bubbleMySubscribePage";
        }
        //세션 아이디 넣어주기
        bubbleService.registerBubblePost(bubblePostDto,sessionUser.getId());
        bubbleService.registerBubbleImages(postImages,bubblePostDto.getId());
        return "redirect:./bubbleCommonPage";
    }

    //아티스트 정보 신청하기
    //Html에서 멀티파트 파일 받아올때 DTO에 있는 이름 그대로 하게되면 스트링 변환전에 멀티파일 이미지가 그대로 입력되어서 이미지 입력이 안됨.
    @RequestMapping("applyArtistProcess")
    public String applyArtistProcess(HttpSession session,
                                     BubbleArtistDto bubbleArtistDto,
                                     @RequestParam("image") MultipartFile image){

        UserDto sessionUser = (UserDto) session.getAttribute("loginUser");
        if(sessionUser == null){
            System.out.println("로그인 정보가 없습니다.");
            return "bubble/bubbleCommonPage";
        }

        int getUserId = sessionUser.getId();
        System.out.println("getUserId");
        // 이미지 처리
        String identificationUrl = null;
        if (image != null && !image.isEmpty()) {
            identificationUrl = bubbleService.registerArtistIdentification(image);
        }

        //DTO에 url설정해주기
        bubbleArtistDto.setIdentification_image(identificationUrl);
        //세션 아이디 넣어주기
        bubbleService.applyArtistForm(getUserId,bubbleArtistDto);

        return "bubble/applyArtistDonePage";
    }

    //내가 구독한 버블 페이지로 보내는 곳
    @RequestMapping("bubbleMainPage")
    public String bubbleMainPage(){
        return "bubble/bubbleMySubscribePage";
    }

    @RequestMapping("bubbleCommonPage")
    public String mainPage(){

        return "bubble/bubbleCommonPage";
    }


    @RequestMapping("postDetailPage")
    public String postDetailPage(){
        return "bubble/bubbleDetailPage";
    }

    @RequestMapping("bubbleIntroPage")
    public String bubbleIntroPage(){

        return "bubble/bubbleIntroPage";
    }

    @RequestMapping("bubbleChatPage")
    public String bubbleChatPage(){
        return "bubble/bubbleChatPage";
    }

    //채팅리스트 페이지
    @RequestMapping("bubbleChatListPage")
    public String bubbleChatListPage(){
        return "bubble/bubbleChatListPage";
    }

    @RequestMapping("bubbleMyPage")
    public String bubbleMyPage(){
        return "bubble/bubbleMyUserPage";
    }

    //유저 개인페이지로 이동
    @RequestMapping("bubbleUserPrivatePage")
    public String bubbleUserPrivatePage(){ return "bubble/bubbleUserPrivatePage";}

    //버블 디테일 페이지
    @RequestMapping("bubbleDetailPage")
    public String bubbleDetailPage(){
        return "bubble/bubbleDetailPage";
    }


    @RequestMapping("bubbleMyLikePage")
    public String bubbleMyLikePage(){
        return "bubble/myLikePage";
    }

    @RequestMapping("bubbleMyAlarmPage")
    public String bubbleMyAlarmPage(){
        return "bubble/myAlarmPage";
    }

    //아티스트 요청
    @RequestMapping("artistApplyPage")
    public String artistApplyPage(){
        return "bubble/applyArtistPage";
    }

    @RequestMapping("artistApplyDonePage")
    public String artistApplyDonePage(){
        return "bubble/applyArtistDonePage";
    }


}
