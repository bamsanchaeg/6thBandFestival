package com.sixthband.festival.club.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
import com.sixthband.festival.dto.UserDto;

@Mapper
public interface ClubSqlMapper {

    public void clubRegistProcess(ClubDto clubDto);
    public List<ClubCategoryDto> clubCategoryList();
    public List<ClubDto> recommandClubRandom();
    public List<ClubDto> clubCategoryListAll(int categoryId);
    public List<ClubDto> clubListAll();
    // 관리자 목록
    public List<ClubDto> clubListAdminAppear(@Param("pageIndex") int pageIndex);
    public ClubDto clubAdminDetailInfo(int id);
    //사용자 목록
    public List<ClubDto> clubListAppear(String searchWord);
    public String clubCategory(int id);
    public void clubClickCount(int id);
    public void rejectClubUpdate(int id);
    public void updateApproveClub(int id);
    public ClubMemberRegistDto selectExistUser(@Param("club_id")int club_id,@Param("user_id")int user_id);
    public ClubDto clubDetailInfo(int id);
    public int clubChairmanId(int id);
    public int userCount(int id);
    public void clubMemberRegist(ClubMemberRegistDto clubMemberRegistDto);
    public List<ClubDto> myClubList(int user_id);
    public List<ClubMemberRegistDto> mySignUpClub(int user_id);
    public ClubMemberRegistDto myClubListDetail(@Param("club_id")int club_id,@Param("user_id")int user_id);
    public ClubDto memberResident(@Param("id")int id,@Param("user_id")int user_id);
    public ClubMemberRegistDto memberExistRegist(@Param("club_id")int club_id,@Param("user_id")int user_id);
    public List<ClubMemberRegistDto> memberBeforeAccept(@Param("id")int id);
    public List<ClubMemberRegistDto> memberAcceptList(@Param("id")int id);
    public List<ClubMemberRegistDto> memberRejectList(@Param("id")int id);
    public int memberBeforeAcceptCount(@Param("id")int id);
    public int memberAcceptListCount(@Param("id")int id);
    public int memberRejectListCount(@Param("id")int id);
    public void memberActivationReject(@Param("id")int id);
    public void memberActivationGetOut(@Param("id")int id);
    public void memberActivationAccept(@Param("id")int id);
    public List<ClubMemberRegistDto> clubMemberList(int club_id);
    public int recentMember(int id);
    public int memberResidentCount(ClubDto clubDto);
    public int selectExistUserCount(@Param("club_id")int club_id, @Param("user_id")int user_id);
// 게시판 시작
    public List<BoardCategoryDto> boardCategoryList();
    public void registBoard(ClubBoardDto clubBoardDto);
    public void clubDetailImage(ClubBoardDetailImgDto clubBoardDetailImgDto);
    public List<ClubBoardDto> clubBoardList(@Param("club_id") int clubId,@Param("page")int page, @Param("order") int order);
    public List<ClubBoardDetailImgDto> clubBoardDetailImageList(int club_board_id);
    public String clubBoardCategoryName(int id);
    public ClubBoardDto clubBoardDetailPage(@Param("id") int id,@Param("club_id")int club_id);
//신고...
    public void registBoardReport(ClubBoardReportDto clubBoardReportDto);
    public List<ClubBoardReportDto> boardReportList();
    public ClubBoardReportDto boardReportDetail(int id);
    public void deleteBoardAdminProcess(int id);

//게시판 좋아요
    public void boardLikeClick(ClubBoardLikeDto clubBoardLikeDto);
    public void boardDislikeClick(ClubBoardLikeDto clubBoardLikeDto);
    public int boardLikeUser(ClubBoardLikeDto clubBoardLikeDto);
    public void clubBoardClickCount(int id);
    public int likeTotalCount(int board_id);
    public void registBoardComment(ClubBoardCommentDto clubBoardCommentDto);
    public List<ClubBoardCommentDto> boardCommentList(ClubBoardCommentDto clubBoardCommentDto);
    public int clubCategoryId(@Param("id") int id,@Param("club_id")int club_id);
    public void updateProcess(ClubBoardDto clubBoardDto);
    public void deleteProcess(@Param("id") int id,@Param("club_id")int club_id);
    public int commentCount(@Param("club_id")int club_id,@Param("club_board_id") int club_board_id);
// 댓글 수정 삭제
    public void deleteComment(int id);
    public void updateComment(ClubBoardCommentDto clubBoardCommentDto);
    public void registPhoto(ClubPhotoDto clubPhotoDto);
    // 사진첩
    public List<ClubPhotoDto> photoListAppear(int club_id);
    public int photoBookLikeExist(ClubPhotoLikeDto clubPhotoLikeDto);
    public void likePhoto(ClubPhotoLikeDto clubPhotoLikeDto);
    public void unLikePhoto(ClubPhotoLikeDto clubPhotoLikeDto);
    public int photoLikeCount(int club_photo_id);
    public void photoCommentRegist(ClubPhotoCommentDto clubPhotoCommentDto);
    public List<ClubPhotoCommentDto> photoCommentAppear(ClubPhotoCommentDto clubPhotoCommentDto);
    public void deletePhoto(int id);
    public void deletePhotoComment(int club_photo_id);
    public void deletePhotoLike(int club_photo_id);
//방장및 가입 회원
    public int clubAndRegistMember(@Param("club_id")int club_id,@Param("user_id")int user_id);
    public void updateClub(ClubDto clubDto);
    public void deleteClub(int id);
    public void registVoteTitle(ClubVoteDto clubVoteDto);
    public void registVoteQuestion(ClubVoteOptionDto clubVoteOptionDto);

    public List<ClubVoteDto> nowAfterVote(int club_id);
    public List<ClubVoteDto> nowBeforeVote(int club_id);
    public List<ClubVoteDto> nowSameVote(int club_id);

    public List<ClubVoteOptionDto> voteSelectList(int vote_id);
    public ClubVoteDto voteDetailTitle(ClubVoteDto clubVoteDto);
    public List<ClubVoteOptionDto> voteDetailSelectList(@Param("vote_id")int vote_id,@Param("club_id")int club_id);
    public void mySelectVoteOption(MySelectVoteDto mySelectVoteDto);
    public int myVoteExist(MySelectVoteDto mySelectVoteDto);
    public int voteResultByOption(MySelectVoteDto mySelectVoteDto);
    public int voteTotalMember(int vote_id);
    public int selectVoteFemale(MySelectVoteDto mySelectVoteDto);
    public int selectVoteMale(MySelectVoteDto mySelectVoteDto);
    public void clubScheduleRegistProcess(ClubScheduleDto clubPlanDto);
    public void scheduleMamberRegist(ClubScheduleMemberDto clubScheduleMemberDto); 
    public List<ClubScheduleDto> clubScheduleAppear(int club_id);
    public ClubScheduleDto clubScheduleDetail(int id);
    public int scheduleTotalMember(int schedule_id);
    public void attendSchedule(ClubScheduleMemberDto clubScheduleMemberDto);
    public int scheduleMemberExist(ClubScheduleMemberDto clubScheduleMemberDto);
    public void updateMemberActive(int club_id);
    // paginatioon
    public int countBoard();
    // 내가작성한 페이지...
    public List<ClubBoardDto> myRegistBoardList(int club_member_id);
    public int boardCommentCount(int board_id);
    public List<ClubBoardCommentDto> myRegistBoardComment(int club_member_id);
    public ClubBoardDto clubBoardDetail(int id);
    public List<ClubPhotoCommentDto> myRegistPhotoComment(int club_member_id);
    public ClubPhotoDto myRegistPhotoId(int id);
    public List<Map<String,Object>> boardOrphotoComment(int club_member_id);

    // Gpt
    public void GptContent(ClubBoardCommentDto clubBoardCommentDto);
    public List<ClubBoardCommentDto> boardIdCommentList(int boardId);
    public void chatGptReviewInsert(ChatGptReviewDto chatGptReviewDto);
    public int chatGptCommentExist(ChatGptReviewDto chatGptReviewDto);
    public ClubBoardCommentDto commentId(int id);
    public ChatGptReviewDto gptReview(int comment_id);
    public int acceptClubCount();
    public int acceptBeforeClubCount();
    public int newBoardRegist();
    public int totalClubCount();
    public void senMessage(SendMessageDto sendMessageDto);
    public List<SendMessageDto> messageContent(int schedule_id);
}
