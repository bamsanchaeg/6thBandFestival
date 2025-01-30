package com.sixthband.festival.club.controller;

import java.util.*;

import com.sixthband.festival.club.dto.ChatGptReviewDto;
import com.sixthband.festival.club.dto.ClubBoardCommentDto;
import com.sixthband.festival.club.dto.ClubBoardDetailImgDto;
import com.sixthband.festival.club.dto.ClubBoardDto;
import com.sixthband.festival.club.dto.ClubBoardLikeDto;
import com.sixthband.festival.club.dto.ClubBoardReportDto;
import com.sixthband.festival.club.dto.ClubDto;
import com.sixthband.festival.club.dto.ClubPhotoCommentDto;
import com.sixthband.festival.club.dto.ClubPhotoDto;
import com.sixthband.festival.club.dto.ClubPhotoLikeDto;
import com.sixthband.festival.club.dto.ClubScheduleDto;
import com.sixthband.festival.club.dto.ClubScheduleMemberDto;
import com.sixthband.festival.club.dto.ClubVoteDto;
import com.sixthband.festival.club.dto.ClubVoteOptionDto;
import com.sixthband.festival.club.dto.MySelectVoteDto;
import com.sixthband.festival.club.dto.SendMessageDto;
import com.sixthband.festival.club.service.ClubService;
import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.util.ImageUtil;
import com.theokanning.openai.completion.chat.ChatMessage;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/club")
public class RestClubController {
    @Autowired
    private ClubService clubService;

    @RequestMapping("searchClub")
    public RestResponseDto searchClub(@RequestParam("searchWord")String searchWord){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("clubList", clubService.appearClubDto(searchWord));

        return restResponseDto;

    }

    @RequestMapping("appearClubCategoryList")
    public RestResponseDto appearClubCategoryList(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        if(id == 0){
            restResponseDto.add("clubList", clubService.clubListAll());

        }
        else{
            restResponseDto.add("clubList", clubService.appearClubCategoryList(id));

        }

        return restResponseDto;

    }


    @RequestMapping("myClubList")
    public RestResponseDto myClubList(@RequestParam(value = "id", required = false)int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("myClubList",clubService.myClubList(id));

        return restResponseDto;
    }

    @RequestMapping("mySignUpList")
    public RestResponseDto mySignUpClub(@RequestParam(value = "id", required = false)int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("mySignUpList",clubService.mySignUpClub(id));

        return restResponseDto;
    }

    @RequestMapping("clubHome")
    public RestResponseDto clubHome(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("myClubList",clubService.clubDetailInfo(id));

        return restResponseDto;
    }

    @RequestMapping("clubMember")
    public RestResponseDto clubMember(@RequestParam("club_id")int club_id,@RequestParam("user_id")int user_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("myClubMember",clubService.myClubListDetail(club_id,user_id));
        return restResponseDto;
    }

    @RequestMapping("memberClubRegist")
    public RestResponseDto memberClubRegist(@RequestParam("club_id")int club_id,@RequestParam("user_id")int user_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("memberClubRegist",clubService.memberExistRegist(club_id,user_id));

        return restResponseDto;
    }

    @RequestMapping("memberResident")
    public RestResponseDto memberResident(@RequestParam("club_id")int id,@RequestParam("user_id")int user_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("memberClubRegist",clubService.memberResident(id,user_id));

        return restResponseDto;
    }

    @RequestMapping("isMemberRight")
    public RestResponseDto isMemberRight(@RequestParam("club_id")int id,@RequestParam("user_id")int user_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("isMemberRight",clubService.memberExistRegist(id,user_id));

        return restResponseDto;
    }

    @RequestMapping("memberResidentInfo")
    public RestResponseDto memberResidentInfo(@RequestParam("club_id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("residentInfo",clubService.residentInfo(id));

        return restResponseDto;
    }
// 멤버및 회원 존재
    @RequestMapping("memberAndResidentExist")
    public RestResponseDto memberAndResidentExist(ClubDto clubDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("residentInfo",clubService.memberAndResidentExist(clubDto));

        return restResponseDto;
    }

    @RequestMapping("memberList")
    public RestResponseDto memberList(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("memberClubRegist",clubService.memberList(id));
        

        return restResponseDto;
    }

    @RequestMapping("memberReject")
    public RestResponseDto memberReject(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.memberActivationReject(id);

        return restResponseDto;
    }

    @RequestMapping("memberAccept")
    public RestResponseDto memberAccept(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.memberActivationAccept(id);

        return restResponseDto;
    }

    // 내보낸 멤버
    @RequestMapping("memberGetOut")
    public RestResponseDto memberGetOut(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.memberActivationGetOut(id);

        return restResponseDto;
    }

    @RequestMapping("memberCount")
    public RestResponseDto memberCount(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("memberCount", clubService.memberCount(id));

        return restResponseDto;
    }

    @RequestMapping("memberRegistCount")
    public RestResponseDto memberRegistCount(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("memberRegistCount", clubService.memberRegistCount(id));

        return restResponseDto;
    }

    @RequestMapping("memberAcceptCount")
    public RestResponseDto memberAcceptCount(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("memberAcceptCount", clubService.memberAcceptCount(id));

        return restResponseDto;
    }

    @RequestMapping("memberRejectCount")
    public RestResponseDto memberRejectCount(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("memberRejectCount", clubService.memberRejectCount(id));

        return restResponseDto;
    }

    @RequestMapping("boardWriteProcess")
    public RestResponseDto boardWriteProcess(ClubBoardDto clubBoardDto,HttpSession session, @RequestParam(value="uploadFiles", required = false)  MultipartFile[] uploadFiles){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");
        clubBoardDto.setClub_member_id(sessionUser.getId());
        
        clubService.registBoard(clubBoardDto);

        ClubBoardDetailImgDto clubBoardDetailImgDto = new ClubBoardDetailImgDto();
        if(uploadFiles != null && uploadFiles.length >0 ){

        
            List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(uploadFiles);
            for(ImageDto imageDto : multiImage){
                int boardId = clubBoardDto.getId();
                int clubId = clubBoardDto.getClub_id();
                clubBoardDetailImgDto.setClub_id(clubId);
                clubBoardDetailImgDto.setClub_board_id(boardId);
                clubBoardDetailImgDto.setLocation(imageDto.getLocation());

                clubService.clubDetailImage(clubBoardDetailImgDto);
            }
        }
            
        return restResponseDto;

    }

    @RequestMapping("boardListAppear")
    public RestResponseDto boardListAppear(@RequestParam("club_id") int clubId,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "order") int order) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 올바른 메서드 호출로 수정
        List<Map<String, Object>> clubBoardList = clubService.clubBoardList(clubId, (page - 1) * 10, order);
        restResponseDto.add("clubBoardList", clubBoardList);
        restResponseDto.add("boardCount", clubService.countBoards());

        return restResponseDto;
    }

    // @RequestMapping("clubChairmanId")
    // public RestResponseDto clubChairmanId(@RequestParam("clubId")int clubId){
    //     RestResponseDto restResponseDto = new RestResponseDto();
    //     restResponseDto.setResult("success");

    //     restResponseDto.add("clubMemberRegistInfo" ,clubService.clubChairmanId(clubId));

    //     return restResponseDto;
        
    // }

    @RequestMapping("clubInfo")
    public RestResponseDto clubInfo(@RequestParam("clubId")int clubId){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("clubMemberRegistInfo" ,clubService.clubInfo(clubId));

        return restResponseDto;
        
    }

    @RequestMapping("clubMemberList")
    public RestResponseDto clubMemberList(@RequestParam("clubId")int clubId){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("clubMemberRegistInfo" ,clubService.clubMemberList(clubId));

        return restResponseDto;
        
    }

    @RequestMapping("clubBoardDetailPage")
    public RestResponseDto clubBoardDetailPage(@RequestParam("id")int id, @RequestParam("clubId")int clubId){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("clubBoardDetailPage" ,clubService.clubBoardDetailPage(id,clubId));

        return restResponseDto;
        
    }

    // 게시판 신고 등록 
    @RequestMapping("registBoardReport")
    public RestResponseDto registBoardReport(ClubBoardReportDto clubBoardReportDto,HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");
        clubBoardReportDto.setClub_member_id(sessionUser.getId());

        clubService.registBoardReport(clubBoardReportDto);

        return restResponseDto;
        
    }

    // 좋아요.
    @RequestMapping("boardLikeClick")
    public RestResponseDto boardLikeClick(ClubBoardLikeDto clubBoardLikeDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.boardLikeClick(clubBoardLikeDto);

        return restResponseDto;
        
    }

    @RequestMapping("boardDislikeClick")
    public RestResponseDto boardDislikeClick(ClubBoardLikeDto clubBoardLikeDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.boardDislikeClick(clubBoardLikeDto);

        return restResponseDto;
        
    }

    @RequestMapping("existBoardLike")
    public RestResponseDto existBoardLike(ClubBoardLikeDto clubBoardLikeDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("existLike",clubService.existBoardLike(clubBoardLikeDto));

        return restResponseDto;
        
    }

    @RequestMapping("likeTotalCount")
    public RestResponseDto likeTotalCount(@RequestParam("board_id")int board_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("likeTotalCount",clubService.likeTotalCount(board_id));

        return restResponseDto;
        
    }

    @RequestMapping("registBoardComment")
    public RestResponseDto registBoardComment(ClubBoardCommentDto clubBoardCommentDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        clubService.registBoardComment(clubBoardCommentDto);

        return restResponseDto;
        
    }

    @RequestMapping("boardCommentList")
    public RestResponseDto boardCommentList(ClubBoardCommentDto clubBoardCommentDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("boardCommentList",clubService.boardCommentList(clubBoardCommentDto));

        return restResponseDto;
        
    }

    // 댓글 수정 삭제
    @RequestMapping("deleteComment")
    public RestResponseDto deleteComment(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        clubService.deleteComment(id);

        return restResponseDto;
        
    }

    @RequestMapping("updateComment")
    public RestResponseDto updateComment(ClubBoardCommentDto clubBoardCommentDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        clubService.updateComment(clubBoardCommentDto);

        return restResponseDto;
        
    }

    @RequestMapping("photoBookRegist")
    public RestResponseDto photoBookRegist(HttpSession session,ClubPhotoDto clubPhotoDto,@RequestParam("uploadFile")MultipartFile uploadFile){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        String photo = ImageUtil.saveImageAndReturnLocation(uploadFile);
        clubPhotoDto.setClub_photo_img(photo);

        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");
        clubPhotoDto.setClub_member_id(sessionUser.getId());
        
        clubService.registPhoto(clubPhotoDto);

        return restResponseDto;
        
    }

    @RequestMapping("photoBookList")
    public RestResponseDto photoBookList(@RequestParam("club_id")int club_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("photoBookList",clubService.photoBookList(club_id));

        return restResponseDto;  
    }

    @RequestMapping("photoBookLikeExist")
    public RestResponseDto photoBookLikeExist(ClubPhotoLikeDto clubPhotoLikeDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("photoBookLikeExist",clubService.photoBookLikeExist(clubPhotoLikeDto));

        return restResponseDto;  
    }

    @RequestMapping("likePhoto")
    public RestResponseDto photoBookLikeClick(ClubPhotoLikeDto clubPhotoLikeDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.likePhoto(clubPhotoLikeDto);

        return restResponseDto;
    }

    @RequestMapping("unLikePhoto")
    public RestResponseDto unLikePhoto(ClubPhotoLikeDto clubPhotoLikeDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.unLikePhoto(clubPhotoLikeDto);

        return restResponseDto;
    }
    @RequestMapping("photoLikeCount")
    public RestResponseDto unLikePhoto(@RequestParam("photo_id")int photo_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("likeCount",clubService.photoLikeCount(photo_id));

        return restResponseDto;
    }

    
    @RequestMapping("photoCommentRegist")
    public RestResponseDto photoCommentRegist(ClubPhotoCommentDto clubPhotoCommentDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.photoCommentRegist(clubPhotoCommentDto);
        return restResponseDto;
    }
    
    @RequestMapping("photoCommentList")
    public RestResponseDto photoCommentList(ClubPhotoCommentDto clubPhotoCommentDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("photoCommentList", clubService.photoCommentAppear(clubPhotoCommentDto));

        return restResponseDto;
    }

    @RequestMapping("photoDelete")
    public RestResponseDto photoDelete(@RequestParam("photo_id")int photo_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.photoDelete(photo_id);

        return restResponseDto;
    }


    
    @RequestMapping("clubAndRegistMember")
    public RestResponseDto clubAndRegistMember(@RequestParam("club_id") int club_id,@RequestParam("user_id")int user_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("clubAndRegistMember", clubService.clubAndRegistMember(club_id,user_id));

        return restResponseDto;
    }
    
    @RequestMapping("deleteClub")
    public RestResponseDto deleteClub(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        clubService.deleteClub(id);

        return restResponseDto;
    }

    //너무 어려운 등록 부분.............
    @RequestMapping("voteRegist")
    public RestResponseDto voteRegist(HttpSession session,@RequestPart("questionTitle")ClubVoteDto clubVoteDto,
    @RequestPart("voteRequest") List<ClubVoteOptionDto> clubVoteOptionList,
    @RequestPart(value="files", required = false) MultipartFile[] files){
        
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");

        clubVoteDto.setClub_member_id(sessionUser.getId());
        clubService.registVoteTitle(clubVoteDto);
        if (files != null) {
            for (int i = 0; i < clubVoteOptionList.size(); i++) {
                ClubVoteOptionDto clubVoteOptionDto = clubVoteOptionList.get(i);
                clubVoteOptionDto.setClub_member_id(sessionUser.getId());

                int voteId = clubVoteDto.getId();
                clubVoteOptionDto.setVote_id(voteId);

                List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(files);

                if (multiImage != null && i < multiImage.size()) {
                    clubVoteOptionDto.setQuestion_image(multiImage.get(i).getLocation());
                }
                clubService.registVoteQuestion(clubVoteOptionDto);
            }
        } else {
            for (int i = 0; i < clubVoteOptionList.size(); i++) {
                ClubVoteOptionDto clubVoteOptionDto = clubVoteOptionList.get(i);
                clubVoteOptionDto.setClub_member_id(sessionUser.getId());
                int voteId = clubVoteDto.getId();
                clubVoteOptionDto.setVote_id(voteId);
                clubService.registVoteQuestion(clubVoteOptionDto);
                
            }
        }
        return restResponseDto;

    
    }

    @RequestMapping("nowAfterVoteList")
    public RestResponseDto voteNowAfter(@RequestParam("club_id")int club_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("voteList",clubService.nowAfterVote(club_id));

        return restResponseDto;
    }

    @RequestMapping("nowBeforeVoteList")
    public RestResponseDto voteNowBefore(@RequestParam("club_id")int club_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("voteList",clubService.nowBeforeVote(club_id));

        return restResponseDto;
    }

    @RequestMapping("nowSameVoteList")
    public RestResponseDto nowSameVote(@RequestParam("club_id")int club_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("voteList",clubService.nowSameVote(club_id));

        return restResponseDto;
    }

    @RequestMapping("voteDetail")
    public RestResponseDto voteDetail(ClubVoteDto clubVoteDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("voteDetail",clubService.voteDetail(clubVoteDto));

        return restResponseDto;
    }

    @RequestMapping("mySelectVoteOption")
    public RestResponseDto mySelectVoteOption(HttpSession session,MySelectVoteDto mySelectVoteDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");
        mySelectVoteDto.setClub_member_id(sessionUser.getId());
        clubService.mySelectVoteOption(mySelectVoteDto);

        return restResponseDto;
    }

    @RequestMapping("myVoteExist")
    public RestResponseDto myVoteExist(MySelectVoteDto mySelectVoteDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("myVoteExist", clubService.myVoteExist(mySelectVoteDto));
        return restResponseDto;
    }

    @RequestMapping("voteResultByOption")
    public RestResponseDto voteResultByOption(MySelectVoteDto mySelectVoteDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("voteOptionResult", clubService.voteResultByOption(mySelectVoteDto));
        return restResponseDto;
    }
    
    @RequestMapping("voteTotalMember")
    public RestResponseDto voteTotalMember(@RequestParam("vote_id")int vote_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("voteTotalResult", clubService.voteTotalMember(vote_id));
        return restResponseDto;
    }

    @RequestMapping("scheduleRegistProcess")
    public RestResponseDto scheduleRegistProcess(HttpSession session,ClubScheduleDto clubPlanDto,@RequestParam(value="uploadFiles", required = false)MultipartFile uploadFiles){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        if(uploadFiles != null && !uploadFiles.isEmpty() ){

        String image = ImageUtil.saveImageAndReturnLocation(uploadFiles);
        clubPlanDto.setSchedule_image(image);
        }
        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");
        clubPlanDto.setClub_member_id(sessionUser.getId());

        clubService.scheduleRegistProcess(clubPlanDto);
        return restResponseDto;
    }
    
    @RequestMapping("clubScheduleAppear")
    public RestResponseDto clubScheduleAppear(@RequestParam("club_id")int club_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("scheduleAppear", clubService.clubScheduleAppear(club_id));
        return restResponseDto;
    }

    @RequestMapping("clubSchedulDetail")
    public RestResponseDto clubSchedulDetail(@RequestParam("id")int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("clubSchedulDetail", clubService.clubScheduleDetail(id));
        return restResponseDto;
    }
    
    @RequestMapping("scheduleAttend")
    public RestResponseDto scheduleAttend(ClubScheduleMemberDto clubScheduleMemberDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        clubService.attendSchedule(clubScheduleMemberDto);
        return restResponseDto;
    }

    @RequestMapping("scheduleMemberExist")
    public RestResponseDto scheduleMemberExist(ClubScheduleMemberDto clubScheduleMemberDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("scheduleMemberExist", clubService.scheduleMemberExist(clubScheduleMemberDto));
        return restResponseDto;
    }

    
    @RequestMapping("updateMemberActive")
    public RestResponseDto updateMemberActive(@RequestParam("club_id")int club_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        clubService.updateMemberActive(club_id);
        return restResponseDto;
    }

    @RequestMapping("myRegistBoardList")
    public RestResponseDto myRegistBoardList(@RequestParam("id")int user_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("myRegistBoardList", clubService.myRegistBoardList(user_id));
        return restResponseDto;
    }

    @RequestMapping("myRegistBoardCommentList")
    public RestResponseDto myRegistBoardCommentList(@RequestParam("user_id")int user_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("myRegistBoardCommentList", clubService.boardOrphotoComment(user_id));
        return restResponseDto;
    }

    // Gpt
    @RequestMapping("GptContent")
    public RestResponseDto GptContent(ClubBoardCommentDto clubBoardCommentDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        clubService.GptContent(clubBoardCommentDto);

        return restResponseDto;
        
    }
    
    @RequestMapping("chatGptCommentExist")
    public RestResponseDto chatGptCommentExist(ChatGptReviewDto chatGptReviewDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("chatGptCommentExist",clubService.chatGptCommentExist(chatGptReviewDto));

        return restResponseDto;
        
    }

    // 관리자 
    @RequestMapping("adminEachCount")
    public RestResponseDto adminEachCount(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        // 승인된 모임
        restResponseDto.add("clubRegistAcceptCount",clubService.acceptClubCount());
        // 가입된 모임
        restResponseDto.add("clubRegistAcceptBeforeCount",clubService.acceptBeforeClubCount());
        // 신규 게시물
        restResponseDto.add("newBoardCount",clubService.newBoardRegist());

        return restResponseDto;
        
    }

    @RequestMapping("scheduleChatMessage")
    public RestResponseDto scheduleChatMessage(HttpSession session,@RequestBody SendMessageDto SendMessageDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto sessionUser = (UserDto)session.getAttribute("loginUser");
        SendMessageDto.setSender_id(sessionUser.getId());

        clubService.sendMessage(SendMessageDto);
        return restResponseDto;
    }
    @RequestMapping("chatList")
    public RestResponseDto chatList(@RequestParam("schedule_id")int schedule_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        restResponseDto.add("chatList",clubService.chatList(schedule_id));

        return restResponseDto;
    }
    
}
