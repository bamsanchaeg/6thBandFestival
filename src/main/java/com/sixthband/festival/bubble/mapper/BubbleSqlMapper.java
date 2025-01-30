package com.sixthband.festival.bubble.mapper;

import com.sixthband.festival.bubble.dto.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.*;

@Mapper
public interface BubbleSqlMapper {

    /**
     * 버블 게시물
     **/
    //버블 포스트 등록
    public void registerBubblePost(BubblePostDto bubblePostDto);

    //버블 이미지 등록, 이미지에 직접적으로 post_id랑 다 등록해서 넣어줘야함
    public void registerBubbleImages(BubbleImageDto bubbleImageDto);

    //게시물 디테일 가져오기
    public Map<String, Object> getBubblePostDetail(int id);

    //버블 리스트 가져오기
    public List<Map<String, Object>> getBubblePostList();

    //버블 이미지 리스트 가져오기(가로로 배열할 것)
    public List<BubbleImageDto> getBubbleImagesByPostId();

    //내가 팔로우한 버블 계정들의 게시물들 가져오기
    public List<Map<String, Object>> getPostsFromMyFollowings(int userId);

    //버블 등록된 아티스트 정보 가져오기
    public List<Map<String, Object>> getArtistUserList();

    /**
     * 버블 좋아요
     **/
    //버블 좋아요 생성
    public void createLikeForBubble(LikeDto likeDto);

    //좋아요 여부
    public boolean LikeOrUnlike(int post_id, int user_id);

    //좋아요 갯수 카운트
    public int coutLike();

    //유저가 좋아한 게시물 리스트 가져오기
    public List<Map<String, Object>> selectMyLikePost(int user_id);

    /**
     * 버블 팔로우
     **/
    //유저 팔로우
    public void followUser(int following_id, int follower_id);

    //유저 언팔로우
    public void unFollowUser(int following_id, int follower_id);

    //팔로우 여부 확인하기
    public boolean followOrUnFollow(int following_id, int follower_id);


    /**
     * 버블 댓글
     **/
    //댓글삭제
    public void unLike(int post_id, int user_id);

    //버블 댓글 생성
    public void createdComment(BubbleCommentDto bubbleCommentDto);

    //버블 댓글 갯수 가져오기
    public int getCommentCountByPostId(int id);

    //댓글 리스트 가져오기
    public List<Map<String, Object>> getCommentListByPostId(int id);

    /**
     * 버블 마이페이지 아이디마다 작성한 게시물 가져오기
     **/
    public Map<String, Object> getBubbleUserInfoById(int id);

    public Map<String, Object> getBubbleUserMyInfoById(int id);

    public List<Map<String, Object>> getBubbleImagesInfoById(int id);

    /**
     * 버블 채팅
     **/
    //보낸사람과 받는사람으로 챗 리스트 가져오기, 이걸로 채팅방 만들어야함
    public List<Map<String, Object>> selectChatListBySenderIdAndReceiverId(int receiver, int sender);

    //버블 채팅생성
    public void createBubbleMessage(BubbleChatDto bubbleUserChatDto);

    /**
     * 버블 세션
     **/
    //세션 정보 가져오기
    public Map<String, Object> getSessionUserInfoById(int id);

    //세션 정보로 알람 리스트 가져오기
    public List<Map<String, Object>> getMyAlarmsBySessionId(int user_id);

    /**
     * 유저의 채팅 리스트가져오기
     **/
    public List<Map<String, Object>> getChatListByUserId(int userId);

    /**
     * 아티스트 신청 및 프로세스
     **/
    //아티스트 신청
    public void applyArtistForm(BubbleArtistDto bubbleArtistDto);

    //신분증 이미지 등록
    public void registerIdentification(int id, String identification_image);

    //아티스트 신청 리스트 가져오기
    public List<Map<String, Object>> getArtistApplicationList();

    //아티스트 신청정보 디테일
    public BubbleArtistDto getArtistApplication(int id);

    //아티스트 신청 정보 승인
    public void updateApplyStatement(int id);


}


