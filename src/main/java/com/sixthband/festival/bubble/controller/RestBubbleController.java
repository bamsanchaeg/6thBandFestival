package com.sixthband.festival.bubble.controller;


import com.sixthband.festival.bubble.dto.BubbleChatDto;
import com.sixthband.festival.bubble.dto.BubbleCommentDto;
import com.sixthband.festival.bubble.service.BubbleService;
import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

import java.util.HashMap;

@RestController
@RequestMapping("api/bubble")
public class RestBubbleController {

    @Autowired
    BubbleService bubbleService;

    //메인 페이지에 출력될 게시물 리스트
    @RequestMapping("myBubblePage")
    public RestResponseDto myBubblePage(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        //유저 아이디 설정 잘 보세요........... 이거 안봐서 고생함
        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        // 세션에 사용자 정보가 없는 경우 예외 처리
        if (userDto == null) {
            restResponseDto.add("postList", bubbleService.getBubbleListForMainPage());
        }else{

            restResponseDto.add("postList",bubbleService.getBubbleListForMainPage(userDto.getId()));
        }

        return restResponseDto;

    }

    //게시물 디테일 가져오기
    @RequestMapping("getBubbleDetail")
    public RestResponseDto getBubbleDetail(int id,
                                           HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();

        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        if(userDto == null){
            restResponseDto.setResult("success");
            restResponseDto.add("bubbleDetail",bubbleService.getBubbleDetailWithOutUserId(id));
        }else{
            restResponseDto.setResult("success");
            restResponseDto.add("bubbleDetail",bubbleService.getBubbleDetailWithUserId(id,userDto.getId()));
        }

        return restResponseDto;
    }

    //내가 팔로우한 유저들 게시물만 모아보는 페이지
    @RequestMapping("followingBubblePage")
    public RestResponseDto followingBubblePage(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        //유저 아이디 설정 잘 보세요........... 이거 안봐서 고생함
        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        try {
            if(userDto == null){
                restResponseDto.setResult("error");
                System.out.println("로그인 정보가 없습니다.");
            }else{
                restResponseDto.setResult("success");
                restResponseDto.add("sessionId",userDto.getId());
                restResponseDto.add("postList", bubbleService.getBubbleListFromMyFollower(userDto.getId()));
            }
        }catch (Exception e){
            restResponseDto.setResult("error");
            System.out.println("게시물 로딩에 실패했습니다. 오류 : " + e.getMessage());
            e.printStackTrace();
            return restResponseDto;
        }

        // 세션에 사용자 정보가 없는 경우 예외 처리
        return restResponseDto;

    }



    //보이드로 하게되면 Json 파싱을 시도할때 오류가 발생할 수 있다. 이 경우 파싱이 안돼서 결국 RestResponseDto로 해결함...
    @RequestMapping("registerComment")
    public RestResponseDto registerComment(BubbleCommentDto bubbleCommentDto,HttpSession session, int post_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        try{
            //유저 아이디 설정 잘 보세요........... 이거 안봐서 고생함
            UserDto userDto = (UserDto)session.getAttribute("loginUser");
            // 세션에 사용자 정보가 없는 경우 예외 처리
            if (userDto == null) {
                throw new IllegalStateException("로그인 정보가 필요합니다.");
            }
            //세션에서 사용자 ID 가져오기
            int userIdObject = userDto.getId();
            bubbleCommentDto.setUser_id(userIdObject);
            bubbleService.registerComment(bubbleCommentDto,post_id);
            restResponseDto.setResult("success");
            return restResponseDto;

        }catch (Exception e){
            // 예외 발생 시 오류 응답 생성
            restResponseDto.setResult("error");
            System.out.println("댓글이 등록되지 않았습니다.");
            return restResponseDto;
        }

    }

    //댓글 생성
    @RequestMapping("getCommentListByPostId")
    public RestResponseDto getCommentListByPostId(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("CommentList",bubbleService.getBubbleCommentListByPostId(id));
        return restResponseDto;
    }

    //좋아요 생성
    @RequestMapping(value = "/createLike", method = RequestMethod.GET)
    public RestResponseDto createLike(@RequestParam("id")int id,HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        if(session == null){
            System.out.println("로그인 정보가 필요합니다.");
            restResponseDto.setResult("error");
            return restResponseDto;
        }
        try{
            UserDto userDto =(UserDto) session.getAttribute("loginUser");
            int userId = userDto.getId();
            System.out.println("유저아이디" + userId);
            System.out.println("포스트 아이디" + id);
            bubbleService.createLike(id,userId);
            restResponseDto.setResult("success");
            return restResponseDto;
        }catch (Exception e){
            // 예외 발생 시 오류 응답 생성
            restResponseDto.setResult("error");
            System.out.println("좋아요가 등록되지 않았습니다. 오류" + e.getMessage());
            e.printStackTrace();
            return restResponseDto;
        }
    }

    //좋아요 삭제.. 이건 한페이지 안에 어떻게 구현해야할지 고민해봐야할듯
    @RequestMapping("deleteLike")
    public RestResponseDto deleteLike(@RequestParam("post_id") int id, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        try{

            UserDto userDto =(UserDto) session.getAttribute("loginUser");
            int userId = userDto.getId();
            bubbleService.deleteLike(id, userId);
            restResponseDto.setResult("success");
            return restResponseDto;

        }catch (Exception e){
            // 예외 발생 시 오류 응답 생성
            restResponseDto.setResult("error");
            System.out.println("좋아요가 삭제되지 않았습니다. 오류" + e.getMessage());
            e.printStackTrace();
            return restResponseDto;
        }

    }

    //유저 유효성 검사
    @RequestMapping("getSessionUserId")
    public RestResponseDto getSessionUserId(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        UserDto sessionUser = (UserDto) session.getAttribute("loginUser");
        if(sessionUser != null){
            restResponseDto.setResult("success");
            restResponseDto.add("userInfo",bubbleService.getSessionUserInfoById(sessionUser.getId()));
        }else {
            restResponseDto.setResult("success");
            restResponseDto.add("user_id", null);
        }

        return restResponseDto;

    }


    @RequestMapping("sendMessage")
    public RestResponseDto sendMessage(HttpSession session,
                                       BubbleChatDto bubbleChatDto,
                                       @RequestParam("receiver")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        try{
            restResponseDto.setResult("success");
            UserDto sessionUser = (UserDto) session.getAttribute("loginUser");
            if(sessionUser != null){
                int sender = sessionUser.getId();
                //sender아이디 설정 및 받는 사람 아이디 설정함.
                bubbleService.createBubbleMessage(sender,id,bubbleChatDto);
            }else {
                restResponseDto.add("sender", null);
                return restResponseDto;  // sessionUser가 null인 경우 메서드 종료
            }
        }catch (Exception e){
            restResponseDto.setResult("error");
            System.out.println("메세지가 등록되지 않았습니다. 오류 :" + e.getMessage());
            e.printStackTrace();

        }
        return restResponseDto;
    }


    @RequestMapping("sentChatListUserToUser")
    public RestResponseDto sentChatListUserToUser(HttpSession session,@RequestParam("receiver") int id){

        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        UserDto sessionUser = (UserDto) session.getAttribute("loginUser");

        if(sessionUser != null){
            int sender = sessionUser.getId();
            restResponseDto.add("messageList",bubbleService.selectChatListBySenderIdAndReceiverId(sender,id));
            restResponseDto.add("receiverInfo",bubbleService.getReceiverInfoById(id));
            return restResponseDto;

        }else {
            restResponseDto.add("sender", null);
            restResponseDto.add("등록에 실패하셨습니다.", null);
            return restResponseDto;  // sessionUser가 null인 경우 메서드 종료
        }

    }


    //인스타 마이페이지
    @RequestMapping("bubbleUserMyPage")
    public RestResponseDto bubbleUserPage(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        try {
            UserDto userDto =(UserDto) session.getAttribute("loginUser");
            int userId = userDto.getId();
            restResponseDto.add("userInfo",bubbleService.getBubbleUserPrivateInfoById(userId));
            restResponseDto.setResult("success");
        }catch (Exception e){
            restResponseDto.add("로그인 정보가 없습니다.", null);
            System.out.println("로그인 정보를 불러오지 못했습니다. 오류:" + e.getMessage());
            e.printStackTrace();
        }

        return restResponseDto;
    }

    //유저 개인 정보 페이지 불러오기, 유저가 쓴 게시물하고 이미지까지 다 불러옴
    @RequestMapping("bubbleUserPrivatePage")
    public RestResponseDto bubbleUserPrivatePage(HttpSession session,
                                                 @RequestParam("following_id")int following_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        UserDto userDto =(UserDto) session.getAttribute("loginUser");

        if(userDto == null){
            restResponseDto.setResult("success");
            //아 이거 이름이 같으면 같은 이름의 맵에 정보를 여러개 넣을 수 있겠구나. 하지만 다 덮어쓸듯...
            Map<String,Object> sessionStatement = new HashMap<>();
            sessionStatement.put("sessionNull",null);
            restResponseDto.add("sessionStatement",sessionStatement);
            restResponseDto.add("userInfo",bubbleService.getBubbleUserInfoById(following_id));
            restResponseDto.add("followStatement",bubbleService.followOrUnFollowUser(following_id,0));
        }else{
            restResponseDto.setResult("success");
            restResponseDto.add("sessionStatement",bubbleService.getBubbleUserInfoById(userDto.getId()));
            restResponseDto.add("userInfo",bubbleService.getBubbleUserInfoById(following_id));
            restResponseDto.add("followStatement",bubbleService.followOrUnFollowUser(userDto.getId(),following_id));
        }

        return restResponseDto;
    }

    //유저의 채팅 리스트 불러오기
    @RequestMapping("getBubbleUserChatRoomList")
    public RestResponseDto getBubbleUserChatRoomList(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        UserDto sessionUser = (UserDto) session.getAttribute("loginUser");

        if (sessionUser != null) {
            restResponseDto.setResult("success");
            restResponseDto.add("chatRoomList",bubbleService.getChatListByUserId(sessionUser.getId()));
            return restResponseDto;

        }else {
            restResponseDto.add("sender", null);
            restResponseDto.add("채팅 리스트 등록에 실패했습니다.", null);
            return restResponseDto;  // sessionUser가 null인 경우 메서드 종료
        }
    }

    /**유저 팔로우, 언팔로우**/
    @RequestMapping("followUser")
    public RestResponseDto followUser (HttpSession session,@RequestParam("following_id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        if (userDto != null) {
            restResponseDto.setResult("success");
            bubbleService.followUser(userDto.getId(), id);
            return restResponseDto;

        } else {
            restResponseDto.setResult("error");
            return restResponseDto;  // sessionUser가 null인 경우 메서드 종료
        }

    }

    @RequestMapping("unfollowUser")
    public RestResponseDto unfollowUser(HttpSession session,@RequestParam("following_id") int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        UserDto userDto =(UserDto) session.getAttribute("loginUser");
        if (userDto != null) {
            restResponseDto.setResult("success");
            bubbleService.unfollowUser(userDto.getId(),id);
            return restResponseDto;

        }else {
            restResponseDto.setResult("error");
            return restResponseDto;  // sessionUser가 null인 경우 메서드 종료
        }
    }

    //상단 메뉴바 내가 좋아한 게시물 리스트 불러오기
    @RequestMapping("selectMyLikePost")
    public RestResponseDto selectMyLikePost(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        UserDto userDto =(UserDto) session.getAttribute("loginUser");
        if (userDto != null) {
            restResponseDto.setResult("success");
            restResponseDto.add("postList",bubbleService.selectMyLikePost(userDto.getId()));
            return restResponseDto;

        }else {
            restResponseDto.setResult("error");
            return restResponseDto;  // sessionUser가 null인 경우 메서드 종료
        }
    }

    //세션아이디로 알람받아오기
    @RequestMapping("getMyAlarmsBySessionId")
    public RestResponseDto getMyAlarmsBySessionId(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        UserDto userDto =(UserDto) session.getAttribute("loginUser");
        try{
            if (userDto != null) {
                restResponseDto.setResult("success");
                restResponseDto.add("alarmList",bubbleService.getMyAlarmsBySessionId(userDto.getId()));
                return restResponseDto;

            }else {
                restResponseDto.setResult("error");
                return restResponseDto;  // sessionUser가 null인 경우 메서드 종료
            }
        }catch (Exception e){
            restResponseDto.add("로그인 정보가 없습니다.", null);
            System.out.println("로그인 정보를 불러오지 못했습니다. 오류:" + e.getMessage());
            e.printStackTrace();
        }

        return restResponseDto;
    }

    /**관리자페이지 아티스트 승인**/

    //아티스트 리스트 불러오기
    @RequestMapping("getArtistApplyList")
    public RestResponseDto getArtistApplyList(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("applications",bubbleService.getArtistApplicationList());
        return restResponseDto;
    }

    @RequestMapping("getApplicationDetail")
    public RestResponseDto getApplicationDetail(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("applicationDetail",bubbleService.getArtistApplication(id));
        return restResponseDto;
    }

    @RequestMapping("updateStatement")
    public RestResponseDto updateStatement(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        bubbleService.updateApplyStatement(id);
        return restResponseDto;
    }

}
