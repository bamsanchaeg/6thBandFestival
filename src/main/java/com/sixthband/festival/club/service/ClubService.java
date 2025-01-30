package com.sixthband.festival.club.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sixthband.festival.club.dto.BoardCategoryDto;
import com.sixthband.festival.club.dto.ChatGptReviewDto;
import com.sixthband.festival.club.dto.ClubBoardCommentDto;
import com.sixthband.festival.club.dto.ClubBoardDetailImgDto;
import com.sixthband.festival.club.dto.ClubBoardDto;
import com.sixthband.festival.club.dto.ClubBoardLikeDto;
import com.sixthband.festival.club.dto.ClubBoardReportDto;
import com.sixthband.festival.club.dto.ClubCategoryDto;
import com.sixthband.festival.club.dto.ClubDto;
import com.sixthband.festival.club.dto.ClubMemberRegistDto;
import com.sixthband.festival.club.dto.ClubPhotoCommentDto;
import com.sixthband.festival.club.dto.ClubPhotoDto;
import com.sixthband.festival.club.dto.ClubPhotoLikeDto;
import com.sixthband.festival.club.dto.ClubScheduleDto;
import com.sixthband.festival.club.dto.ClubScheduleMemberDto;
import com.sixthband.festival.club.dto.ClubVoteDto;
import com.sixthband.festival.club.dto.ClubVoteOptionDto;
import com.sixthband.festival.club.dto.MySelectVoteDto;
import com.sixthband.festival.club.dto.SendMessageDto;
import com.sixthband.festival.club.mapper.ClubSqlMapper;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.user.mapper.UserSqlMapper;
import com.sixthband.festival.util.ImageUtil;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

@Service
public class ClubService {

    @Autowired
    private ClubSqlMapper clubSqlMapper;

    @Autowired
    private UserSqlMapper userSqlMapper;

    public void registClub(ClubDto clubDto){
        clubSqlMapper.clubRegistProcess(clubDto);

    }

    public List<ClubCategoryDto> clubCategoryList(){
        return clubSqlMapper.clubCategoryList();
    }
    
    public List<Map<String,Object>> recommandClubRandom(){
        List<ClubDto> clubList = clubSqlMapper.recommandClubRandom();
        List<Map<String,Object>> recommandClubList = new ArrayList<>();

        for(ClubDto clubDto : clubList){
            int clubCategoryId = clubDto.getClub_category_id();
            String clubCategory = clubSqlMapper.clubCategory(clubCategoryId);
            int userCount = clubSqlMapper.userCount(clubDto.getId());

            Map<String,Object> map = new HashMap<>();
            map.put("clubDto",clubDto);
            map.put("clubCategory",clubCategory);
            map.put("userCount",userCount);

            recommandClubList.add(map);
        }
        return recommandClubList;
    }
// 카테고리 목록별 모임
    public List<Map<String,Object>> appearClubCategoryList(int id){
        List<ClubDto> clubCategoryList = clubSqlMapper.clubCategoryListAll(id);
        List<Map<String,Object>> clubList = new ArrayList<>();

        for(ClubDto clubDto : clubCategoryList){
            int userCount = clubSqlMapper.userCount(clubDto.getId());

            Map<String,Object> map = new HashMap<>();
            map.put("clubDto",clubDto);
            map.put("userCount",userCount);

            clubList.add(map);
        }

        return clubList;
    }

    public List<Map<String,Object>> clubListAll(){
        List<Map<String,Object>> clubList = new ArrayList<>();
        List<ClubDto> clubDtoList = clubSqlMapper.clubListAll();

        for(ClubDto clubDto : clubDtoList){
            int userCount = clubSqlMapper.userCount(clubDto.getId());

            Map<String,Object> map = new HashMap<>();
            map.put("clubDto",clubDto);
            map.put("userCount",userCount);

            clubList.add(map);
        }

        return clubList;
    }


    public List<Map<String,Object>> appearClubList(int p){
        List<Map<String,Object>> clubList = new ArrayList<>();
        List<ClubDto> clubDtoList = clubSqlMapper.clubListAdminAppear((p-1)*10);

        for(ClubDto clubDto : clubDtoList){
            int clubUserId = clubDto.getUser_id();
            UserDto userDto = userSqlMapper.userIdInfo(clubUserId);
            int userCount = clubSqlMapper.userCount(clubDto.getId());
            int clubCategory = clubDto.getClub_category_id();
            String category = clubSqlMapper.clubCategory(clubCategory);
            Map<String,Object> map = new HashMap<>();
            map.put("clubDto",clubDto);
            map.put("userDto",userDto);
            map.put("userCount",userCount);
            map.put("category",category);

            clubList.add(map);
        }

        return clubList;
    }

    public Map<String,Object> clubAdminDetailInfo(int id){
        ClubDto clubDto = clubSqlMapper.clubAdminDetailInfo(id);
        int clubCategory = clubDto.getClub_category_id();
        String category = clubSqlMapper.clubCategory(clubCategory);

        Map<String,Object> map = new HashMap<>();
        map.put("clubDto", clubDto);
        map.put("category",category);
        return map;
    }

    public List<Map<String,Object>> appearClubDto(String searchWord){
        List<Map<String,Object>> clubList = new ArrayList<>();

        List<ClubDto> clubDtoList = clubSqlMapper.clubListAppear(searchWord);
        
        for(ClubDto clubDto : clubDtoList){
            int clubCategoryId = clubDto.getClub_category_id();
            int userCount = clubSqlMapper.userCount(clubDto.getId());
            String clubCategory = clubSqlMapper.clubCategory(clubCategoryId);

            Map<String,Object> map = new HashMap<>();
            map.put("clubDto", clubDto);
            map.put("userCount",userCount);
            map.put("clubCategory",clubCategory);
            clubList.add(map);
        }
        return clubList;
    }

    public void rejectClubId(int id){
        clubSqlMapper.rejectClubUpdate(id);
        
    }

    public void updateApproveClub(int id){
        clubSqlMapper.updateApproveClub(id);
        
    
    }

    public ClubMemberRegistDto  selectExistUser(int id,int user_id){

        return clubSqlMapper.selectExistUser(id,user_id);
    }

    public Map<String,Object> clubDetailInfo(int id){
        Map<String,Object> map = new HashMap<>();
        int userCount = clubSqlMapper.userCount(id);
        ClubDto clubDto = clubSqlMapper.clubDetailInfo(id);
        int chairmanId = clubSqlMapper.clubChairmanId(id);
        map.put("userCount", userCount);
        map.put("clubDto",clubDto);
        map.put("chairmanId",chairmanId);
        
        return map;
    }

    public void clubMemberRegist(ClubMemberRegistDto clubMemberRegistDto){
        clubSqlMapper.clubMemberRegist(clubMemberRegistDto);
    }

    public List<ClubDto> myClubList(int id){
        return clubSqlMapper.myClubList(id);
    }

    public List<Map<String,Object>> mySignUpClub(int id){
        List<ClubMemberRegistDto> signUpMyClub =  clubSqlMapper.mySignUpClub(id);
        List<Map<String,Object>> mySignClub = new ArrayList<>();
        for(ClubMemberRegistDto clubMemberRegistDto : signUpMyClub){
            int clubId = clubMemberRegistDto.getClub_id();
            ClubDto clubDto = clubSqlMapper.clubDetailInfo(clubId);

            Map<String,Object> map = new HashMap<>();
            map.put("clubMemberRegistDto",clubMemberRegistDto);
            map.put("clubDto",clubDto);

            mySignClub.add(map);

        }
        return mySignClub;
    }

    public ClubMemberRegistDto myClubListDetail (int club_id,int user_id){
        return clubSqlMapper.myClubListDetail(club_id,user_id);
    }

    public ClubDto memberResident(int id, int user_id){
        return clubSqlMapper.memberResident(id,user_id);
    }



    public ClubMemberRegistDto memberExistRegist(int club_id,int user_id){
        return clubSqlMapper.memberExistRegist(club_id,user_id);
    }

    // 방장정보
    public Map<String,Object> residentInfo (int id){

        ClubDto clubDto = clubSqlMapper.clubDetailInfo(id);
        int clubResidentInfo = clubDto.getUser_id();
        UserDto userDto = userSqlMapper.userIdInfo(clubResidentInfo);
        Map<String,Object> map = new HashMap<>();

        map.put("clubDto",clubDto);
        map.put("userDto",userDto);

        return map;
    }

    public int memberRegistCount(int club_id){
        int clubRegistCount = clubSqlMapper.memberBeforeAcceptCount(club_id);

        return clubRegistCount;
    }

    public int memberAcceptCount(int club_id){
        int clubAcceptCount = clubSqlMapper.memberAcceptListCount(club_id);

        return clubAcceptCount+1;
    }

    public int memberRejectCount(int club_id){
        int memberRejectCount = clubSqlMapper.memberRejectListCount(club_id);

        return memberRejectCount;
    }


    public List<Map<String,Object>> memberList(int club_id){
        List<ClubMemberRegistDto> memberBeforeAcceptList = clubSqlMapper.memberBeforeAccept(club_id);
        List<ClubMemberRegistDto> memberAcceptList = clubSqlMapper.memberAcceptList(club_id);
        List<ClubMemberRegistDto> memberRejectList = clubSqlMapper.memberRejectList(club_id);

        List<Map<String,Object>> memberList = new ArrayList<>();
// 가입한 회원
        for(ClubMemberRegistDto clubMemberRegistDto : memberBeforeAcceptList){
            int userId = clubMemberRegistDto.getUser_id();
            int clubId = clubMemberRegistDto.getClub_id();

            UserDto userDto = userSqlMapper.userIdInfo(userId);
            ClubDto clubDto = clubSqlMapper.clubDetailInfo(clubId);
            Map<String,Object> map = new HashMap<>();

            map.put("clubMemberRegistDto",clubMemberRegistDto);
            map.put("userDto",userDto);
            map.put("clubDto",clubDto);
            memberList.add(map);

        }
// 수락한 회원
        for(ClubMemberRegistDto acceptMemberDto : memberAcceptList){
            int memberAcceptListCount = clubSqlMapper.memberAcceptListCount(club_id);

            int userId = acceptMemberDto.getUser_id();
            UserDto userDtoAccept = userSqlMapper.userIdInfo(userId);

            Map<String,Object> map = new HashMap<>();
            map.put("acceptMemberDto",acceptMemberDto);
            map.put("userDtoAccept",userDtoAccept);
            map.put("memberAcceptListCount",memberAcceptListCount);

            memberList.add(map);

        }
//거절한 회원
        for(ClubMemberRegistDto rejectMember : memberRejectList){
            int memberRejectListCount = clubSqlMapper.memberRejectListCount(club_id);

            int userId = rejectMember.getUser_id();
            UserDto userDtoReject = userSqlMapper.userIdInfo(userId);

            Map<String,Object> map = new HashMap<>();
            map.put("getOutMemberDto",rejectMember);
            map.put("userDtoGetOut",userDtoReject);
            map.put("memberGetOutListCount",memberRejectListCount);

            memberList.add(map);
            
        }
        return memberList;


    }
    // 모임 방장이 회원 수락 거절
    public void memberActivationReject(int id){
        clubSqlMapper.memberActivationReject(id);
    }
    public void memberActivationAccept(int id){
        clubSqlMapper.memberActivationAccept(id);
    }  
    // 회원이었는데 내보낸 회원
    public void memberActivationGetOut(int id){
        clubSqlMapper.memberActivationGetOut(id);
    }


    public Map<String,Object> memberCount(int id){
        ClubDto clubDto = clubSqlMapper.clubDetailInfo(id);
        int maxMember = clubDto.getMax_members();
        int recentMember = clubSqlMapper.recentMember(id);

        int restMember = maxMember - recentMember;
        Map<String,Object> map = new HashMap<>();

        map.put("clubDto",clubDto);
        map.put("recentMember",recentMember);
        map.put("restMember" ,restMember);
        return map;
    }

    public List<BoardCategoryDto> boardCategoryList(){
        return clubSqlMapper.boardCategoryList();
    }

    public void registBoard(ClubBoardDto clubBoardDto){
        clubSqlMapper.registBoard(clubBoardDto);

    }

    
    public void clubDetailImage(ClubBoardDetailImgDto clubBoardDetailImgDto){
        clubSqlMapper.clubDetailImage(clubBoardDetailImgDto);
    }

    public List<Map<String, Object>> clubBoardList(int club_id, int page, int order) {
        List<ClubBoardDto> clubBoardList = clubSqlMapper.clubBoardList(club_id,page,order);
        List<Map<String,Object>> boardList = new ArrayList<>();
        for(ClubBoardDto clubBoardDto : clubBoardList){
            int boardId = clubBoardDto.getId();
            int clubId = clubBoardDto.getClub_id();
            int userId = clubBoardDto.getClub_member_id();
            int boardCategoryName = clubBoardDto.getClub_category_id();
            int heartTotalCount = clubSqlMapper.likeTotalCount(boardId);
            List<ClubBoardDetailImgDto> clubBoardDetailImage = clubSqlMapper.clubBoardDetailImageList(boardId);
            ClubDto clubDto = clubSqlMapper.clubDetailInfo(clubId);
            UserDto userDto = userSqlMapper.userIdInfo(userId);
            int commentCount = clubSqlMapper.commentCount(clubId,boardId);

            String categoryName = clubSqlMapper.clubBoardCategoryName(boardCategoryName);
            Map<String,Object> map = new HashMap<>();
            map.put("clubBoardDto",clubBoardDto);
            map.put("clubBoardDetailImage",clubBoardDetailImage);
            map.put("categoryName",categoryName);
            map.put("clubDto",clubDto);
            map.put("userDto",userDto);
            map.put("commentCount",commentCount);
            map.put("heartTotalCount",heartTotalCount);

            boardList.add(map);
        }
        return boardList;
        }

    // 페이징 처리
    public int countBoards() {
        return clubSqlMapper.countBoard();
    }

    public ClubDto clubInfo(int id){
        return clubSqlMapper.clubDetailInfo(id);
    }

    public List<Map<String,Object>> clubMemberList(int club_id){
        List<ClubMemberRegistDto> clubMemberList =clubSqlMapper.clubMemberList(club_id);
        List<Map<String,Object>> clubMember = new ArrayList<>();

        for(ClubMemberRegistDto clubMemberRegistDto : clubMemberList){

            int userId = clubMemberRegistDto.getUser_id();
            UserDto userDto = userSqlMapper.userIdInfo(userId);

            Map<String,Object> map = new HashMap<>();

            map.put("clubMemberRegistDto",clubMemberRegistDto);
            map.put("userDto",userDto);

            clubMember.add(map);

        }
        return clubMember;
    }

    public Map<String,Object> memberAndResidentExist(ClubDto clubDto){
        Map<String,Object> map = new HashMap<>();
        boolean residentExist = clubSqlMapper.memberResidentCount(clubDto)>0;
        boolean memberExist = clubSqlMapper.selectExistUserCount(clubDto.getId(),clubDto.getUser_id())>0;
        map.put("residentExist", residentExist);
        map.put("memberExist", memberExist);

        return map;
    }

    // 게시판
    public Map<String,Object> clubBoardDetailPage(int id,int club_id){
        clubSqlMapper.clubBoardClickCount(id);
        ClubBoardDto clubBoardDto =clubSqlMapper.clubBoardDetailPage(id,club_id);
        int categoryId = clubBoardDto.getClub_category_id();
        int boardId = clubBoardDto.getId();
        int memberId = clubBoardDto.getClub_member_id();
        List<ClubBoardDetailImgDto> clubBoardDetailImage = clubSqlMapper.clubBoardDetailImageList(boardId);
        String categoryName = clubSqlMapper.clubBoardCategoryName(categoryId);
        UserDto userDto = userSqlMapper.userIdInfo(memberId);

        Map<String,Object> map = new HashMap<>();

        map.put("clubBoardDto",clubBoardDto);
        map.put("clubBoardDetailImage",clubBoardDetailImage);
        map.put("categoryName",categoryName);
        map.put("userDto",userDto);

        return map;
    }

    public void registBoardReport(ClubBoardReportDto clubBoardReportDto){
        clubSqlMapper.registBoardReport(clubBoardReportDto);
    }

// 신고 리스트 출력
    public List<Map<String,Object>> boardReportList(){
        List<ClubBoardReportDto> clubBoardReportDtoList = clubSqlMapper.boardReportList();
        List<Map<String,Object>> clubBoardReportList = new ArrayList<>();
        for(ClubBoardReportDto clubBoardReportDto : clubBoardReportDtoList){
            int memberId = clubBoardReportDto.getClub_member_id();
            int clubId = clubBoardReportDto.getClub_id();
            int boardId = clubBoardReportDto.getClub_board_id();
            ClubDto clubDto = clubSqlMapper.clubDetailInfo(clubId);
            ClubBoardDto clubBoardDto = clubSqlMapper.clubBoardDetailPage(boardId,clubId);

            UserDto userDto = userSqlMapper.userIdInfo(memberId);
            Map<String,Object> map = new HashMap<>();
            map.put("clubBoardReportDto",clubBoardReportDto);
            map.put("userDto", userDto);
            map.put("clubDto",clubDto);
            map.put("clubBoardDto",clubBoardDto);

            clubBoardReportList.add(map);
        }
        return clubBoardReportList;
    }

    //특정신고 상세 페이지
    public Map<String,Object> boardReportDetail(int id){
        ClubBoardReportDto clubBoardReportDto = clubSqlMapper.boardReportDetail(id);
        int clubId = clubBoardReportDto.getClub_id();
        int boardId = clubBoardReportDto.getClub_board_id();    
        ClubBoardDto clubBoardDto = clubSqlMapper.clubBoardDetailPage(boardId,clubId);
        List<ClubBoardDetailImgDto> clubBoardDetailImgDto = clubSqlMapper.clubBoardDetailImageList(boardId);
        Map<String,Object> map = new HashMap<>();
        map.put("clubBoardReportDto",clubBoardReportDto);
        map.put("clubBoardDto",clubBoardDto);
        map.put("clubBoardDetailImgDto",clubBoardDetailImgDto);

        return map;
    }

    public void deleteBoardAdminProcess(int id){
        clubSqlMapper.deleteBoardAdminProcess(id);
    }

// 좋아요
    public void boardLikeClick(ClubBoardLikeDto clubBoardLikeDto){
        clubSqlMapper.boardLikeClick(clubBoardLikeDto);
    }

    public void boardDislikeClick(ClubBoardLikeDto clubBoardLikeDto){
        clubSqlMapper.boardDislikeClick(clubBoardLikeDto);
    }

    public boolean existBoardLike(ClubBoardLikeDto clubBoardLikeDto){
        return clubSqlMapper.boardLikeUser(clubBoardLikeDto) > 0;
    }

    public int likeTotalCount(int board_id){
        return clubSqlMapper.likeTotalCount(board_id);
    }

    public void registBoardComment(ClubBoardCommentDto clubBoardCommentDto){
        clubSqlMapper.registBoardComment(clubBoardCommentDto);
        // GptContent 호출
        GptContent(clubBoardCommentDto);

    }

    private String openaiAccessKey;

    // Gpt
     @Value("${openAiKey}")
    public void setOpenAiAccessKey(String openaiAccessKey) {
        this.openaiAccessKey = openaiAccessKey;
    }
    public void GptContent(ClubBoardCommentDto clubBoardCommentDto){

        // 서비스 생성
       OpenAiService service = new OpenAiService(openaiAccessKey, Duration.ofSeconds(30));
   
       // ChatGpt에게 보내 내용 세팅
       List<ChatMessage> messages = new ArrayList<>();

       ChatMessage systemMessage = new ChatMessage();
       String review = clubBoardCommentDto.getContent();

       if (review.length() <= 20) {
            System.out.println("내용이 짧아 GPT 요청을 생략합니다.");
            return;
        }
       
        systemMessage.setRole("system");
        systemMessage.setContent("당신은 내가 제시하는 리뷰를 요약해줘야합니다. 최대한 짧게 요약을 해야 합니다. 절대로 20글자를 넘어서서는 안됩니다.");
        messages.add(systemMessage);


        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("user");
        chatMessage.setContent("다음 글을 요약해서 아주 아주 짧은 소감 한 줄 만들어줘!!!!!!!!!! : " + review);
        messages.add(chatMessage);
   
        // 요청
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model("gpt-4o-mini")
            .messages(messages)
            .build();


        // 응답 결과
        String result = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
        System.out.println("이거!!!: " + result);

        ChatGptReviewDto chatGptReviewDto = new ChatGptReviewDto();
        chatGptReviewDto.setGpt_Review(result);
        chatGptReviewDto.setComment_id(clubBoardCommentDto.getId());
        clubSqlMapper.chatGptReviewInsert(chatGptReviewDto);
   }

    public boolean chatGptCommentExist(ChatGptReviewDto chatGptReviewDto){
        return clubSqlMapper.chatGptCommentExist(chatGptReviewDto) > 0;
    }

    
    public List<Map<String,Object>> boardCommentList(ClubBoardCommentDto clubBoardCommentDto){
        List<ClubBoardCommentDto> clubBoardCommentList = clubSqlMapper.boardCommentList(clubBoardCommentDto);
        List<Map<String,Object>> boardCommentList = new ArrayList<>();
        for(ClubBoardCommentDto commentList : clubBoardCommentList){
            int commentId = commentList.getId();
            int userId = commentList.getClub_member_id();
            UserDto userDto = userSqlMapper.userIdInfo(userId);
            int clubId = commentList.getClub_id();
            ClubDto clubDto = clubSqlMapper.clubDetailInfo(clubId);
            // Gpt
            ChatGptReviewDto chatGptReviewDto = clubSqlMapper.gptReview(commentId);
            Map<String,Object> map = new HashMap<>();
            map.put("commentList",commentList);
            map.put("userDto",userDto);
            map.put("clubDto",clubDto);
            map.put("chatGptReviewDto",chatGptReviewDto);

            boardCommentList.add(map);

        }
        return boardCommentList;
    }

    
    public Map<String,Object> clubCategoryId(int id, int club_id){
        Map<String,Object> map = new HashMap<>();
        int categoryId = clubSqlMapper.clubCategoryId(id, club_id);
        String categoryName = clubSqlMapper.clubBoardCategoryName(categoryId);
        map.put("categoryId",categoryId);
        map.put("categoryName",categoryName);

        return map;
    }

    public void updateProcess(ClubBoardDto clubBoardDto){
        clubSqlMapper.updateProcess(clubBoardDto);
    }

    public void deleteProcess(int id,int club_id){
        clubSqlMapper.deleteProcess(id,club_id);
    }
    // 댓글 수정 삭제
    public void deleteComment(int id){
        clubSqlMapper.deleteComment(id);
    }

    public void updateComment(ClubBoardCommentDto clubBoardCommentDto){
        clubSqlMapper.updateComment(clubBoardCommentDto);
    }
//사진첩
    public void registPhoto(ClubPhotoDto clubPhotoDto){
        clubSqlMapper.registPhoto(clubPhotoDto);
    }

    public List<Map<String,Object>> photoBookList(int club_id){
        List<ClubPhotoDto> photoBookList = clubSqlMapper.photoListAppear(club_id);
        List<Map<String,Object>> photoList = new ArrayList<>();

        for(ClubPhotoDto clubPhotoDto : photoBookList){
            int photoId = clubPhotoDto.getId();
            int userId = clubPhotoDto.getClub_member_id();
            UserDto userDto = userSqlMapper.userIdInfo(userId);
            
            int photoLikeCount = clubSqlMapper.photoLikeCount(photoId);
            Map<String,Object> map = new HashMap<>();
            map.put("clubPhotoDto", clubPhotoDto);
            map.put("userDto", userDto);
            map.put("photoLikeCount", photoLikeCount);
            
            photoList.add(map);
        }

        return photoList;
    }

    public boolean photoBookLikeExist(ClubPhotoLikeDto clubPhotoLikeDto){
        return clubSqlMapper.photoBookLikeExist(clubPhotoLikeDto) > 0;
    }

    public void likePhoto(ClubPhotoLikeDto clubPhotoLikeDto){
        clubSqlMapper.likePhoto(clubPhotoLikeDto);
    }
    
    public void unLikePhoto(ClubPhotoLikeDto clubPhotoLikeDto){
        clubSqlMapper.unLikePhoto(clubPhotoLikeDto);
    }

    public void photoCommentRegist(ClubPhotoCommentDto clubPhotoCommentDto){
        clubSqlMapper.photoCommentRegist(clubPhotoCommentDto);
    }

    public int photoLikeCount(int photoId){
        
        return clubSqlMapper.photoLikeCount(photoId);  
    }

    public List<Map<String,Object>> photoCommentAppear(ClubPhotoCommentDto clubPhotoCommentDto){
        List<ClubPhotoCommentDto> clubPhotoCommentList = clubSqlMapper.photoCommentAppear(clubPhotoCommentDto);
        List<Map<String,Object>> photoCommentList = new ArrayList<>();

        for(ClubPhotoCommentDto photoCommentDto : clubPhotoCommentList){

            int userId = photoCommentDto.getClub_member_id();
            int clubId = photoCommentDto.getClub_id();

            UserDto userDto = userSqlMapper.userIdInfo(userId);
            ClubDto clubDto = clubSqlMapper.clubDetailInfo(clubId);

            Map<String,Object> map = new HashMap<>();
            map.put("photoCommentDto", photoCommentDto);
            map.put("userDto", userDto);
            map.put("clubDto", clubDto);

            photoCommentList.add(map);
        }
        return photoCommentList;
    }
    // 사진첩 삭제 기능
    public void photoDelete(int id){
        clubSqlMapper.deletePhoto(id);
        clubSqlMapper.deletePhotoComment(id);
        clubSqlMapper.deletePhotoLike(id);
    }

    public boolean clubAndRegistMember(int club_id,int user_id){
        return clubSqlMapper.clubAndRegistMember(club_id,user_id)>0;
    }

    public void updateClub(ClubDto clubDto){
        clubSqlMapper.updateClub(clubDto);
    }
    
    public void deleteClub(int id){
        clubSqlMapper.deleteClub(id);
    }

    public void registVoteTitle(ClubVoteDto clubVoteDto){
        clubSqlMapper.registVoteTitle(clubVoteDto);
    }

    public void registVoteQuestion(ClubVoteOptionDto clubVoteOptionDto){
        clubSqlMapper.registVoteQuestion(clubVoteOptionDto);
    }

    public List<Map<String,Object>> nowAfterVote(int club_id){
        List<ClubVoteDto> clubVoteList = clubSqlMapper.nowAfterVote(club_id);
        List<Map<String,Object>> voteList = new ArrayList<>();

        for(ClubVoteDto clubVoteDto : clubVoteList){
            int voteId = clubVoteDto.getId();
            List<ClubVoteOptionDto> clubVoteOptionList = clubSqlMapper.voteSelectList(voteId);
            
            Map<String,Object> map = new HashMap<>();
            map.put("clubVoteDto", clubVoteDto);
            map.put("clubVoteOptionList",clubVoteOptionList);
            voteList.add(map);

        }

        return voteList;
    }

    public List<Map<String,Object>> nowBeforeVote(int club_id){
        List<ClubVoteDto> clubVoteList = clubSqlMapper.nowBeforeVote(club_id);
        List<Map<String,Object>> voteList = new ArrayList<>();

        for(ClubVoteDto clubVoteDto : clubVoteList){
            int voteId = clubVoteDto.getId();
            List<ClubVoteOptionDto> clubVoteOptionList = clubSqlMapper.voteSelectList(voteId);
            
            Map<String,Object> map = new HashMap<>();
            map.put("clubVoteDto", clubVoteDto);
            map.put("clubVoteOptionList",clubVoteOptionList);
            voteList.add(map);

        }

        return voteList;
    }

    public List<Map<String,Object>> nowSameVote(int club_id){
        List<ClubVoteDto> clubVoteList = clubSqlMapper.nowSameVote(club_id);
        List<Map<String,Object>> voteList = new ArrayList<>();

        for(ClubVoteDto clubVoteDto : clubVoteList){
            int voteId = clubVoteDto.getId();
            List<ClubVoteOptionDto> clubVoteOptionList = clubSqlMapper.voteSelectList(voteId);
            
            Map<String,Object> map = new HashMap<>();
            map.put("clubVoteDto", clubVoteDto);
            map.put("clubVoteOptionList",clubVoteOptionList);
            voteList.add(map);

        }

        return voteList;
    }


    public Map<String,Object> voteDetail(ClubVoteDto clubVoteDto){

        ClubVoteDto clubVoteTitle = clubSqlMapper.voteDetailTitle(clubVoteDto);
        List<ClubVoteOptionDto> clubVoteOptionList = clubSqlMapper.voteDetailSelectList(clubVoteTitle.getId(),clubVoteTitle.getClub_id());

        Map<String,Object> map = new HashMap<>();
        map.put("clubVoteTitle", clubVoteTitle);
        map.put("clubVoteOptionList",clubVoteOptionList);    

        return map;
    }

    public void mySelectVoteOption(MySelectVoteDto mySelectVoteDto){
        clubSqlMapper.mySelectVoteOption(mySelectVoteDto);
    }

    public boolean myVoteExist(MySelectVoteDto mySelectVoteDto){
        return clubSqlMapper.myVoteExist(mySelectVoteDto) > 0;
    }

    public int voteResultByOption(MySelectVoteDto mySelectVoteDto){
        return clubSqlMapper.voteResultByOption(mySelectVoteDto);
    }

    public int voteTotalMember(int vote_id){
        return clubSqlMapper.voteTotalMember(vote_id);
    }

    public int selectVoteFemale(MySelectVoteDto mySelectVoteDto){
        return clubSqlMapper.selectVoteFemale(mySelectVoteDto);
    }
    public int selectVoteMale(MySelectVoteDto mySelectVoteDto){
        return clubSqlMapper.selectVoteMale(mySelectVoteDto);
    }

    public void scheduleRegistProcess(ClubScheduleDto clubPlanDto){

        clubSqlMapper.clubScheduleRegistProcess(clubPlanDto);
        
        ClubScheduleMemberDto clubScheduleMemberDto = new ClubScheduleMemberDto();
        clubScheduleMemberDto.setClub_id(clubPlanDto.getClub_id());
        clubScheduleMemberDto.setClub_member_id(clubPlanDto.getClub_member_id());
        clubScheduleMemberDto.setSchedule_id(clubPlanDto.getId());
        clubSqlMapper.scheduleMamberRegist(clubScheduleMemberDto);   
    }

    public List<Map<String,Object>> clubScheduleAppear(int club_id){
        List<ClubScheduleDto> planList = clubSqlMapper.clubScheduleAppear(club_id);
        List<Map<String,Object>> clubPlanDetailList = new ArrayList<>();  

        for(ClubScheduleDto clubPlanDto : planList){
            int memberId = clubPlanDto.getClub_member_id();
            UserDto userDto = userSqlMapper.userIdInfo(memberId);
            Map<String,Object> map = new HashMap<>();
            map.put("clubPlanDto",clubPlanDto);
            map.put("userDto", userDto);

            clubPlanDetailList.add(map);

        }
        return clubPlanDetailList;

    }

    public Map<String,Object> clubScheduleDetail(int id){
        ClubScheduleDto clubPlanDto = clubSqlMapper.clubScheduleDetail(id);
        int planId = clubPlanDto.getId();
        int memberId = clubPlanDto.getClub_member_id();
        UserDto userDto = userSqlMapper.userIdInfo(memberId);
        int nowScheduleMember = clubSqlMapper.scheduleTotalMember(planId);
        Map<String,Object> map = new HashMap<>();
        map.put("clubPlanDto", clubPlanDto);
        map.put("userDto", userDto);
        map.put("nowScheduleMember", nowScheduleMember);
        return map;
    }   
    
    public void attendSchedule(ClubScheduleMemberDto clubScheduleMemberDto){
        clubSqlMapper.attendSchedule(clubScheduleMemberDto);

    }

    public boolean scheduleMemberExist(ClubScheduleMemberDto clubScheduleMemberDto){
        return clubSqlMapper.scheduleMemberExist(clubScheduleMemberDto) > 0;
    }

    public void updateMemberActive(int club_id){
        clubSqlMapper.updateMemberActive(club_id);
    }

    public List<Map<String,Object>> myRegistBoardList(int user_id){
        List<ClubBoardDto> clubBoardDtoList = clubSqlMapper.myRegistBoardList(user_id);
        List<Map<String,Object>> boardRegistList = new ArrayList<>();

        for(ClubBoardDto clubBoardDto : clubBoardDtoList){
            int boardId = clubBoardDto.getId();
            int clubId = clubBoardDto.getClub_id();
            int commentCount = clubSqlMapper.commentCount(clubId,boardId);
            List<ClubBoardDetailImgDto> clubBoardDetailImgDto = clubSqlMapper.clubBoardDetailImageList(boardId);

            Map<String,Object> map = new HashMap<>();
            map.put("clubBoardDto",clubBoardDto);
            map.put("commentCount", commentCount);
            map.put("clubBoardDetailImgDto", clubBoardDetailImgDto);

            boardRegistList.add(map);
        }

        return boardRegistList;
    }

    public List<Map<String,Object>> boardOrphotoComment(int user_id){
        return clubSqlMapper.boardOrphotoComment(user_id);
    }

    // 관리자
    public int acceptClubCount(){
        return clubSqlMapper.acceptClubCount();
    }
    public int acceptBeforeClubCount(){
        return clubSqlMapper.acceptBeforeClubCount();
    }

    public int newBoardRegist(){
        return clubSqlMapper.newBoardRegist();
    }

    public int totalClubCount(){
        return clubSqlMapper.totalClubCount();
    }

    public void sendMessage(SendMessageDto sendMessageDto){
        clubSqlMapper.senMessage(sendMessageDto);
    }

    public List<Map<String,Object>> chatList (int schedule_id){
        ClubScheduleDto clubPlanDto = clubSqlMapper.clubScheduleDetail(schedule_id);
        List<SendMessageDto> sendMessageDtoList = clubSqlMapper.messageContent(schedule_id);
        List<Map<String,Object>> chatList = new ArrayList<>();
        for(SendMessageDto sendMessageDto :sendMessageDtoList){
            int memberId = sendMessageDto.getSender_id();
            UserDto userDto = userSqlMapper.userIdInfo(memberId);

            Map<String,Object> map = new HashMap<>();
            map.put("sendMessageDto",sendMessageDto);
            map.put("userDto", userDto);
            map.put("clubPlanDto", clubPlanDto);

            chatList.add(map);
        }
        return chatList;
    }

}
