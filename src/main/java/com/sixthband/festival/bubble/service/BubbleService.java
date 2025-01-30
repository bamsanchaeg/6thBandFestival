package com.sixthband.festival.bubble.service;

import com.sixthband.festival.bubble.dto.*;
import com.sixthband.festival.bubble.mapper.BubbleSqlMapper;
import com.sixthband.festival.util.MacOsUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import java.io.File;
import java.nio.file.Paths;

@Service
public class BubbleService {

    //AppConfig 설정시 MacOS환경설정 문제로 설정한 파일경로
    //private final String rootPath = Paths.get(File.separator,"Users","test","sixthBandImage").toString();
    /**
     * 배포용 rootPath
     **/
    private final String rootPath = "/sixthBandImage";

    @Autowired
    private BubbleSqlMapper bubbleSqlMapper;


    //게시물 등록
    public void registerBubblePost(BubblePostDto bubblePostDto, int id) {
        bubblePostDto.setUser_id(id);
        bubbleSqlMapper.registerBubblePost(bubblePostDto);
    }

    //이미지 등록
    public void registerBubbleImages(List<MultipartFile> postImages, @Param("post_id") int id) {
        String bubbleImagePath = MacOsUtil.generateSaveDirMacOs(rootPath, "bubble" + File.separator + "images");
        for (MultipartFile bubbleImage : postImages) {
            if (bubbleImage.isEmpty()) {
                continue;
            }
            try {
                String orgFilename = bubbleImage.getOriginalFilename();
                String ext = orgFilename.substring(orgFilename.lastIndexOf("."));
                String imageFileName = UUID.randomUUID() + ext;
                bubbleImage.transferTo(new File(rootPath + File.separator + bubbleImagePath + imageFileName));
                BubbleImageDto bubbleImageDto = new BubbleImageDto();
                //직접 넣어주는 방법으로 진행
                bubbleImageDto.setPost_id(id);
                bubbleImageDto.setImage_url(bubbleImagePath + imageFileName);
                bubbleSqlMapper.registerBubbleImages(bubbleImageDto);

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }


    //메인 리스트 불러오기, 이미지 데이터까지 다 넣어줌. 세션이 없을때 적용되는 서비스
    //세션이 없을 시 유저 아이디는 0으로 설정해둬서 넣음.
    public List<Map<String, Object>> getBubbleListForMainPage() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> bubblePostDtoList = bubbleSqlMapper.getBubblePostList();
        for (Map<String, Object> bubblePostDto : bubblePostDtoList) {
            Map<String, Object> map = new HashMap<>();
            int postId = (int) bubblePostDto.get("post_id");
            //세션이 없어서 유저 아이디 0으로 설정
            boolean likeOrUnlike = bubbleSqlMapper.LikeOrUnlike(postId, 0);
            int countLike = bubbleSqlMapper.getCommentCountByPostId(postId);
            map.put("numberOfComment", countLike);
            map.put("LikeOrUnLike", likeOrUnlike);
            map.put("postDto", bubblePostDto);
            result.add(map);
        }

        return result;
    }


    //메인 리스트 불러오기, 이미지 데이터까지 다 넣어줌. 오버라이딩 된 서비스, 세션 아이디가 있을떄 적용됨
    //여기서 해당 아이디에 따라 해당 게시물 좋아요했는지 안했는지까지 다 검수해줌
    public List<Map<String, Object>> getBubbleListForMainPage(int userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> bubblePostDtoList = bubbleSqlMapper.getBubblePostList();
        for (Map<String, Object> bubblePostDto : bubblePostDtoList) {
            Map<String, Object> map = new HashMap<>();
            //여기서 왜 "post_id" 가 나오는걸까, Map 구조의 키 이름에서 필요한 아이디인듯. 쿼리에서 필요한 post_id 값을 실행시키는 것이기 때문에
            //"post_id"라는 키는 각 게시물의 고유 식별자(ID)를 나타낸다.
            int postId = (int) bubblePostDto.get("post_id");
            boolean likeOrUnlike = bubbleSqlMapper.LikeOrUnlike(postId, userId);
            int countLike = bubbleSqlMapper.getCommentCountByPostId(postId);
            map.put("numberOfComment", countLike);
            map.put("LikeOrUnLike", likeOrUnlike);
            map.put("postDto", bubblePostDto);
            result.add(map);
        }

        return result;
    }

    /**
     * 게시물 디테일 불러오기
     **/
    public Map<String, Object> getBubbleDetailWithOutUserId(int id) {
        Map<String, Object> result = new HashMap<>();
        //세션이 없어서 유저 아이디 0으로 설정
        boolean likeOrUnlike = bubbleSqlMapper.LikeOrUnlike(id, 0);
        int countLike = bubbleSqlMapper.getCommentCountByPostId(id);
        result.put("numberOfComment", countLike);
        result.put("LikeOrUnLike", likeOrUnlike);
        result.put("bubbleDetail", bubbleSqlMapper.getBubblePostDetail(id));
        return result;
    }

    /**
     * 게시물 디테일 불러오기
     **/
    public Map<String, Object> getBubbleDetailWithUserId(int id, int user_id) {
        Map<String, Object> result = new HashMap<>();
        //세션이 없어서 유저 아이디 0으로 설정
        boolean likeOrUnlike = bubbleSqlMapper.LikeOrUnlike(id, user_id);
        int countLike = bubbleSqlMapper.getCommentCountByPostId(id);
        result.put("numberOfComment", countLike);
        result.put("LikeOrUnLike", likeOrUnlike);
        result.put("bubbleDetail", bubbleSqlMapper.getBubblePostDetail(id));
        return result;
    }


    //유저가 팔로우하는 계정이 올린 게시물 리스트만 보기
    public List<Map<String, Object>> getBubbleListFromMyFollower(int userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> bubblePostDtoList = bubbleSqlMapper.getPostsFromMyFollowings(userId);

        for (Map<String, Object> bubblePostDto : bubblePostDtoList) {

            Map<String, Object> map = new HashMap<>();
            //좋아요 안좋아요 여부 파악
            int postId = (int) bubblePostDto.get("post_id");
            boolean likeOrUnlike = bubbleSqlMapper.LikeOrUnlike(postId, userId);
            int countLike = bubbleSqlMapper.getCommentCountByPostId(postId);
            map.put("numberOfComment", countLike);
            map.put("LikeOrUnLike", likeOrUnlike);
            map.put("postDto", bubblePostDto);
            result.add(map);
        }
        return result;
    }

    //덧글 등록하기
    public void registerComment(BubbleCommentDto bubbleCommentDto, int post_id) {
        bubbleCommentDto.setPost_id(post_id);
        bubbleSqlMapper.createdComment(bubbleCommentDto);
    }

    //덧글 가져오기
    public List<Map<String, Object>> getBubbleCommentListByPostId(@Param("post_id") int id) {

        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> commentList = bubbleSqlMapper.getCommentListByPostId(id);

        Map<String, Object> commentCount = new HashMap<>();
        int commentCountNum = bubbleSqlMapper.getCommentCountByPostId(id);

        commentCount.put("commentCount", commentCountNum);

        for (Map<String, Object> commentDto : commentList) {
            Map<String, Object> map = new HashMap<>();
            map.put("comment", commentDto);
            result.add(map);
        }
        result.add(commentCount);
        return result;

    }

    //아티스트 정보 가져오기
    public List<Map<String, Object>> getArtistList() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> artistList = bubbleSqlMapper.getArtistUserList();
        for (Map<String, Object> artistDto : artistList) {
            Map<String, Object> map = new HashMap<>();
            map.put("Artist", artistDto);
            result.add(map);
        }
        return result;
    }

    //좋아요 생성
    public void createLike(int id, int userId) {
        LikeDto likeDto = new LikeDto();
        likeDto.setPost_id(id);
        likeDto.setUser_id(userId);
        bubbleSqlMapper.createLikeForBubble(likeDto);
    }

    //좋아요 했는지 여부 확인하기
    public boolean LikeOrUnlike(int post_id, int user_id) {
        return bubbleSqlMapper.LikeOrUnlike(post_id, user_id);
    }

    //좋아요 삭제하기
    public void deleteLike(@Param("post_id") int id, int userId) {
        bubbleSqlMapper.unLike(id, userId);
    }

    //내가 좋아요 한 게시물 리스트 가져오기
    public List<Map<String, Object>> selectMyLikePost(int user_id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> postList = bubbleSqlMapper.selectMyLikePost(user_id);
        for (Map<String, Object> likedPost : postList) {
            Map<String, Object> map = new HashMap<>();
            map.put("postDto", likedPost);
            result.add(map);
        }
        return result;
    }

    //나에게 팔로우,좋아요 한 알람들 받아보기
    public List<Map<String, Object>> getMyAlarmsBySessionId(int user_id) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> alarmList = bubbleSqlMapper.getMyAlarmsBySessionId(user_id);
        for (Map<String, Object> alarm : alarmList) {
            Map<String, Object> map = new HashMap<>();
            map.put("alarmDto", alarm);
            result.add(map);
        }

        return result;
    }

    //댓글 생성하기
    public void createBubbleMessage(int artistId, int userId, BubbleChatDto bubbleUserChatDto) {
        //여기서 receiver의 아이디를 어떻게 받아오는지?
        bubbleUserChatDto.setReceiver(userId);
        bubbleUserChatDto.setSender(artistId);
        bubbleSqlMapper.createBubbleMessage(bubbleUserChatDto);
    }

    //버블 마이페이지 데이터 가져오기(세션이 없을때는 유저아이디만 받아옴)
    public Map<String, Object> getBubbleUserPrivateInfoById(int id) {
        //결과물 맵 데이터
        Map<String, Object> result = new HashMap<>();
        //유저 정보 넣어주기
        Map<String, Object> userInfoMap = bubbleSqlMapper.getBubbleUserMyInfoById(id);
        result.put("userId", userInfoMap);

        /**유저 이미지 불러오기 **/
        //새로운 이미지 리스트를 가져옴
        List<Map<String, Object>> userImagesOfPosts = bubbleSqlMapper.getBubbleImagesInfoById(id);
        // postImages라는 키에 이미지 리스트를 담기 위한 새로운 맵, 자바스크립트에서 이미지 개별의 배열을 풀어주기 위해 선택한 방법
        //List로 넣게되면 애초부터 배열이라서 Map 구조로 한번 넣어봄.. 0부터 시작하는걸 잊지마렴
        // 최종 구조로 변환, 가끔 껍데기 없이 생짜로 데이터 집어넣는 경우가 있어서 이렇게 맵 구조로 설정해주는게 중요한듯.
        // 맵은 키-값 쌍으로 데이터를 저장하며, 값으로는 어떤 객체든 올 수 있다. 따라서 리스트를 값으로 저장하는 것도 가능하다.
        result.put("postImages", userImagesOfPosts);

        //이제 JASON을 보면 "userInfo, PostImages" 이름의 데이터 덩어리가 두 개 들어가있다.
        return result;
    }

    //버블 마이페이지 데이터 가져오기, 팔언팔 여부 확인하기(세션이 만약 있을때, 없을떄와 같이 분기처리해줌. 위에 보면 알아요.)
    //팔로잉 아이디는
    public Map<String, Object> getBubbleUserInfoById(@Param("following_id") int profileId) {
        //결과물 맵 데이터
        Map<String, Object> result = new HashMap<>();
        //유저 정보 넣어주기
        Map<String, Object> userInfoMap = bubbleSqlMapper.getBubbleUserInfoById(profileId);
        //"userInfo 라는 이름의 카테고리로 묶인 맵을 넣어주기."
        result.put("userId", userInfoMap);

        /**유저 이미지 불러오기 **/
        //새로운 이미지 리스트를 가져옴
        List<Map<String, Object>> userImagesOfPosts = bubbleSqlMapper.getBubbleImagesInfoById(profileId);
        // postImages라는 키에 이미지 리스트를 담기 위한 새로운 맵, 자바스크립트에서 이미지 개별의 배열을 풀어주기 위해 선택한 방법
        //List로 넣게되면 애초부터 배열이라서 Map 구조로 한번 넣어봄.. 0부터 시작하는걸 잊지마렴
        // 최종 구조로 변환, 가끔 껍데기 없이 생짜로 데이터 집어넣는 경우가 있어서 이렇게 맵 구조로 설정해주는게 중요한듯.
        // 맵은 키-값 쌍으로 데이터를 저장하며, 값으로는 어떤 객체든 올 수 있다. 따라서 리스트를 값으로 저장하는 것도 가능하다.
        result.put("postImages", userImagesOfPosts);

        //이제 JASON을 보면 "userInfo, PostImages" 이름의 데이터 덩어리가 두 개 들어가있다.
        return result;
    }

    //유저 간편정보만 가져오기
    public Map<String, Object> getReceiverInfoById(int id) {
        return bubbleSqlMapper.getBubbleUserInfoById(id);
    }

    //버블 채팅 리스트 가쟈오기, 메신저 받는 상대방 정보도 가져오기
    public List<Map<String, Object>> selectChatListBySenderIdAndReceiverId(int receiver, int sender) {
        List<Map<String, Object>> result = new ArrayList<>();
        //채팅방 받는사람 유저 정보 넣어주기

        List<Map<String, Object>> chatmessagesList = bubbleSqlMapper.selectChatListBySenderIdAndReceiverId(receiver,
                sender);
        for (Map<String, Object> message : chatmessagesList) {
            Map<String, Object> map = new HashMap<>();
            map.put("Message", message);
            result.add(map);
        }
        return result;
    }

    //세션 유저정보 가져오기
    public Map<String, Object> getSessionUserInfoById(int id) {
        return bubbleSqlMapper.getSessionUserInfoById(id);
    }

    //유저의 채팅리스트 가져오기
    public List<Map<String, Object>> getChatListByUserId(int userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> userChatRoomList = bubbleSqlMapper.getChatListByUserId(userId);
        for (Map<String, Object> userChat : userChatRoomList) {
            Map<String, Object> map = new HashMap<>();
            map.put("chatRoom", userChat);
            result.add(map);
        }
        return result;
    }

    /**
     * 유저 팔로우 생성 및 삭제
     **/

    //팔로우
    public void followUser(int follower_id, int following_id) {
        bubbleSqlMapper.followUser(follower_id, following_id);
    }

    //언팔로우
    public void unfollowUser(int follower_id, int following_id) {
        bubbleSqlMapper.unFollowUser(follower_id, following_id);
    }

    //팔언팔 여부 확인하기
    public Map<String, Object> followOrUnFollowUser(int profileId, int follower_id) {

        /**세션으로 팔언팔 여부 확인하기**/
        boolean followOrUnFollow = bubbleSqlMapper.followOrUnFollow(profileId, follower_id);
        Map<String, Object> map = new HashMap<>();
        map.put("follow", followOrUnFollow);
        return map;

    }

    /**
     * 아티스트 신청파트 서비스
     **/
    public void applyArtistForm(@Param("user_id") int getUserId, BubbleArtistDto bubbleArtistDto) {
        bubbleArtistDto.setUser_id(getUserId);
        bubbleSqlMapper.applyArtistForm(bubbleArtistDto);
    }

    //신분증 이미지 제출
    public String registerArtistIdentification(MultipartFile image) {

        if (image == null || image.isEmpty()) {
            // 이미지가 없으면 null을 반환하거나 기본값을 반환
            return null;
        }
        //하위폴더생성은 thumbnail로 생성하는 변수
        String identificationPath = MacOsUtil.generateSaveDirMacOs(rootPath,
                "bubble" + File.separator + "artistIdentification");

        //확장자 추출, 확장자 이름 넣어서 하는 법, 이 부분의 폴더링은 날짜별로 진행하지 않았습니다.
        //orgFilename 메서드를 사용하여 업로드된 파일의 원본 이름을 문자열에 저장합니다.
        String orgFilename = image.getOriginalFilename();

        //orgFilename에서 확장자를 추출하여 새로운 문자열 변수 ext에 저장합니다. 이 변수에는 업로드된 파일의 확장자명만 포함됩니다.
        String ext = orgFilename.substring(orgFilename.lastIndexOf("."));
        //따라서 thumbnailFilename은 UUID + 확장자명의 String 값으로 리턴된다고 이해하면 될 것 같습니다.
        //MacOsUtil쪽에선 사용자 닉네임 + 밀리초로 짜여져있는데 관리자 등록 단일 이미지라 아직 세션값은 필요없어 썸네일은 UUID로 진행했습니다.
        String identificationFilename = UUID.randomUUID() + ext;
        try {
            System.out.println(identificationPath + identificationFilename);
            image.transferTo(new File(rootPath + File.separator + identificationPath + identificationFilename));
            System.out.println(identificationPath + identificationFilename);
            return identificationPath + identificationFilename;
        } catch (Exception e) {
            return null;
        }
    }

    public void registerIdentification(int id, @Param("identification_image") String url) {
        bubbleSqlMapper.registerIdentification(id, url);
    }

    //아티스트 정보 리스트 불러오기
    //코드에서 BubbleArtistDto 객체를 Map으로 직접 캐스팅하려고 시도했다면,
    //이 부분에서 ClassCastException이 발생합니다. 예를 들어, DTO 객체를 Map으로 변환하거나 직접 캐스팅하려고 하면 이 문제가 발생할 수 있습니다.
    public List<Map<String, Object>> getArtistApplicationList() {
        return bubbleSqlMapper.getArtistApplicationList();
    }

    //Y로 상태 업데이트하기
    public void updateApplyStatement(int id) {
        bubbleSqlMapper.updateApplyStatement(id);
    }

    public BubbleArtistDto getArtistApplication(int id) {
        return bubbleSqlMapper.getArtistApplication(id);
    }


}
