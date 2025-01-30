package com.sixthband.festival.rental.service;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.rental.dto.*;
import com.sixthband.festival.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sixthband.festival.rental.mapper.RentalSqlMapper;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

@Service
public class RentalService {

    @Autowired
    private RentalSqlMapper rentalSqlMapper;

    private String openaiAccessKey;

    @Value("${openAiKey}")
    public void setOpenAiAccessKey(String openaiAccessKey) {
        this.openaiAccessKey = openaiAccessKey;
    }


    public List<RentalCategoryDto> getRentalCategories(){
        return rentalSqlMapper.getRentalCategories();
    }

    public List<Map<String, Object>> getRentalItemsByCategory(int category_id) {
        List<Map<String, Object>> itemList = new ArrayList<>();
        List<RentalItemDto> rentalItems = rentalSqlMapper.getRentalItemsByCategory(category_id);
        for(RentalItemDto rentalItemDto : rentalItems) {
            RentalCategoryDto rentalCategoryDto = rentalSqlMapper.getByCategoryId(category_id);

            Map<String, Object> map = new HashMap<>();
            map.put("rentalItemDto", rentalItemDto);
            map.put("rentalCategoryDto", rentalCategoryDto);

            //System.out.println("RentalItemDto: " + rentalItemDto);
            //System.out.println("RentalCategoryDto: " + rentalCategoryDto);

            itemList.add(map);
        }
        return itemList;
    }

    public void insertRentalItem(RentalItemDto rentalItemDto){
        rentalSqlMapper.insertRentalItem(rentalItemDto);
    }

    public void insertDetailImage(RentalDetailImageDto rentalDetailImageDto){
        rentalSqlMapper.insertDetailImage(rentalDetailImageDto);
    }

    public List<RentalDetailImageDto> getImagesByRentalItemId(int rental_item_id){

       return rentalSqlMapper.getImagesByRentalItemId(rental_item_id);

    }

    // 대여 상품 리스트 All
    public List<Map<String, Object>> getAllRentalItemsAll(){

        List<Map<String, Object>> itemList = new ArrayList<>();

        List<RentalItemDto> rentalItems = rentalSqlMapper.getAllRentalItemsAll();

        for(RentalItemDto rentalItemDto : rentalItems){
            int categoryPk = rentalItemDto.getCategory_id();
            RentalCategoryDto rentalCategoryDto = rentalSqlMapper.getByCategoryId(categoryPk);

            Map<String, Object> map = new HashMap<>();

            map.put("rentalItemDto", rentalItemDto);
            map.put("rentalCategoryDto", rentalCategoryDto);

            itemList.add(map);
        }
        return itemList;
    }

    // 대여 상품 리스트
    public List<Map<String, Object>> getAllRentalItems(int p){
        
        List<Map<String, Object>> itemList = new ArrayList<>();

        List<RentalItemDto> rentalItems = rentalSqlMapper.getAllRentalItems((p-1)*10);

        for(RentalItemDto rentalItemDto : rentalItems){
            int categoryPk = rentalItemDto.getCategory_id();
            RentalCategoryDto rentalCategoryDto = rentalSqlMapper.getByCategoryId(categoryPk);

            Map<String, Object> map = new HashMap<>();

            map.put("rentalItemDto", rentalItemDto);
            map.put("rentalCategoryDto", rentalCategoryDto);

            itemList.add(map);
        }
        return itemList;
    }

    public int getRentalItemCount(){
       return rentalSqlMapper.getRentalItemTotalCount();
    }

    // 대여상품 정보 조회
    public Map<String, Object> getRentalItemById(int id){

        Map<String, Object> map = new HashMap<>();
        RentalItemDto rentalItemDto = rentalSqlMapper.getByItemId(id);

        map.put("rentalItemDto", rentalItemDto);

        return map;
    }

    // 대여상품 수정
    public void updateRentalItem(RentalItemDto rentalItemDto){
        rentalSqlMapper.updateRentalItem(rentalItemDto);
    }

    // 대여상품 삭제
    public void deleteRentalItem(int id){

        System.out.println("삭제하는 ID: " + id);
        rentalSqlMapper.deleteRentalItemById(id);
    }

    // 대여내역 등록
    public void insertRentalOrder(RentalOrderDto rentalOrderDto){

        rentalOrderDto.setRental_order_status("대여 중");
        rentalSqlMapper.insertRentalOrder(rentalOrderDto);
        System.out.println("Saved RentalOrder ID: " + rentalOrderDto.getId());
    }

    // 대여내역 리스트
    public List<Map<String, Object>> getOrderList(int p){

        List<Map<String, Object>> result = new ArrayList<>();

        // 모든 대여 주문 내역 조회
        List<RentalOrderDto> orderList = rentalSqlMapper.getRentalOrderAll((p-1)*10);

        for(RentalOrderDto rentalOrderDto : orderList){
            // 사용자 정보 조회
            UserDto userDto = rentalSqlMapper.getUserById(rentalOrderDto.getUser_id());
            // 상품 정보 조회
            RentalItemDto rentalItemDto = rentalSqlMapper.getByItemId(rentalOrderDto.getRental_item_id());

            Map<String, Object> map = new HashMap<>();
            map.put("rentalOrderDto", rentalOrderDto);
            map.put("userDto", userDto);
            map.put("rentalItemDto",rentalItemDto);
            map.put("created_at", rentalOrderDto.getCreated_at());
            map.put("image", rentalItemDto.getImage()); // 상품 이미지 추가

            result.add(map);
        }

        return result;
    }

    public int getRentalOrderCount(){
        return rentalSqlMapper.getRentalOrderTotalCount();
    }

    // 로그인한 사용자 대여 내역 리스트
    public List<Map<String, Object>> getOrderListByUserId(int user_id){

        List<Map<String, Object>> result = new ArrayList<>();

        // 사용자 id에 해당하는 대여 내역 리스트 조회
        List<RentalOrderDto> orderList = rentalSqlMapper.getRentalOrderByUserId(user_id);

        for(RentalOrderDto rentalOrderDto : orderList){
            // 사용자 정보 조회
            UserDto userDto = rentalSqlMapper.getUserById(rentalOrderDto.getUser_id());
            // 상품 정보 조회
            RentalItemDto rentalItemDto = rentalSqlMapper.getByItemId(rentalOrderDto.getRental_item_id());

            Map<String, Object> map = new HashMap<>();
            map.put("rentalOrderDto", rentalOrderDto); // 대여 내역 정보 추가
            map.put("userDto", userDto); // 사용자 정보 추가
            map.put("rentalItemDto",rentalItemDto); // 대여 상품 정보 추가
            map.put("created_at", rentalOrderDto.getCreated_at()); // 대여일시 추가

            result.add(map);
        }
        return result;
    }

    // 대여내역 상세정보 조회
     public Map<String, Object> getOrder(int id){

        Map<String, Object> map = new HashMap<>();
        // 대여 주문 정보 조회
        RentalOrderDto rentalOrderDto = rentalSqlMapper.getRentalOrderById(id);
        // 대여 주문에 연결된 사용자 id 가져와서 사용자 정보 조회
        UserDto userDto = rentalSqlMapper.getUserById(rentalOrderDto.getUser_id());
        // 대여 상품 정보 조회
        RentalItemDto rentalItemDto = rentalSqlMapper.getByItemId(rentalOrderDto.getRental_item_id());

        map.put("rentalOrderDto", rentalOrderDto);
        map.put("userDto", userDto);
        map.put("rentalItemDto", rentalItemDto);
        map.put("created_at",rentalOrderDto.getCreated_at());
        map.put("image", rentalItemDto.getImage()); // 대표이미지 추가

        return map;
     }

     // 대여내역 수정
     public void updateOrder(RentalOrderDto rentalOrderDto){
        rentalSqlMapper.updateRentalOrder(rentalOrderDto);
     }

     // 대여내역 삭제
    public void deleteOrder(int id){
        rentalSqlMapper.deleteRentalOrderById(id);
    }

    // 찜하기
    public void insertLike(RentalLikeDto rentalLikeDto){
        rentalSqlMapper.insertLike(rentalLikeDto);
    }

    // 찜 취소
    public int deleteLike(RentalLikeDto rentalLikeDto){
        return rentalSqlMapper.deleteLike(rentalLikeDto);
    }

    // 찜 상태 확인
    public boolean getIsLiked(int item_id, int user_id){
        return rentalSqlMapper.getIsLiked(item_id, user_id) > 0;
    }

    // 찜하기 총 수
    public int totalLikes(int item_id){
        return rentalSqlMapper.totalLikes(item_id);
    }

    // 리뷰 등록
    public boolean insertReview(RentalReviewDto rentalReviewDto){

        // 특정 사용자와 주문에 대한 리뷰 개수 확인
       int reviewCount = rentalSqlMapper.countUserReview(rentalReviewDto.getUser_id(), rentalReviewDto.getOrder_id());

       if(reviewCount > 0) {
           // 이미 리뷰 작성
           return false;
       }

        // 리뷰 삽입
        rentalSqlMapper.insertReview(rentalReviewDto);
        gptContent(rentalReviewDto);

        return true;
    }


    public void gptContent(RentalReviewDto rentalReviewDto){

        String review = rentalReviewDto.getContent();
        //...


        // 서비스 생성
        OpenAiService service = new OpenAiService(openaiAccessKey, Duration.ofSeconds(30));

        // ChatGpt에게 보내 내용 세팅
        List<ChatMessage> messages = new ArrayList<>();


        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent("당신은 내가 제시하는 리뷰를 요약해줘야합니다. 최대한 짧게 요약을 해야 합니다. 절대로 20글자를 넘어서서는 안됩니다.");
        messages.add(systemMessage);


        ChatMessage chatMessage = new ChatMessage();
        // chatMessage.setContent("다음 글을 정말 짧게 요약해줘(무조건 10자 이내로 부탁해!!!!!!!!!!) : " + review + " ");
        // chatMessage.setContent("다음 글을 5글자 이내의 3개의 단어로 요약해줘!!!!!!!!!! : " + review + " ");
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
        System.out.println("결과확인: " + result);

        RentalChatGptReviewDto rentalChatGptReviewDto = new RentalChatGptReviewDto();
        rentalChatGptReviewDto.setGpt_Review(result);
        rentalChatGptReviewDto.setComment_id(rentalReviewDto.getId());
        rentalSqlMapper.insertRentalChatGptReview(rentalChatGptReviewDto);
    }

    // 닉네임 마스킹
    public String maskNickname(String nickname){
        if(nickname == null || nickname.length() < 3) {
            return nickname;
        }

        // 마지막 두 글자를 *로 대체
        int length = nickname.length();
        String maskedNickname = nickname.substring(0, length - 2) + "**";

        return maskedNickname;
    }

    // 리뷰 목록
    public List<Map<String, Object>> getReviewList(int rental_item_id, String sortOrder){

        List<Map<String, Object>> rentalReviewList = new ArrayList<>();

        // 정렬된 리뷰 목록 조회
        List<RentalReviewDto> reviewList = getSortedReviews(rental_item_id, sortOrder);

        // 리뷰에 대해 추가 정보 조회, 리스트에 추가
        for(RentalReviewDto rentalReviewDto : reviewList) {

            // 리뷰에 해당하는 주문 정보 조회
            RentalOrderDto rentalOrderDto = rentalSqlMapper.getRentalOrderById(rentalReviewDto.getOrder_id());

            // 주문 정보에서 렌탈 상품 id 가져옴
            int rentalItemId = rentalOrderDto.getRental_item_id();

            // 렌탈 상품 id를 사용해서 상품 상세 정보 조회, 상품 제목 가져옴
            RentalItemDto rentalItemDto = rentalSqlMapper.getByItemId(rentalItemId);
            String rentalTitle = rentalItemDto.getTitle();

            // 주문 정보에서 사용자 id 가져옴
            int userId = rentalOrderDto.getUser_id();
            // 사용자 id 사용해서 사용자 정보 조회, 닉네임 가져옴
            UserDto userDto = rentalSqlMapper.getUserById(userId);
            String nickName = userDto.getNickname();
            String profileImg = userDto.getProfile_img();

            // 닉네임 마스킹
            String maskedNickName = maskNickname(nickName);

            // 리뷰 좋아요 카운트 가져오기
            int likeCount = rentalSqlMapper.getReviewTotalLikes(rentalReviewDto.getId());

            // gpt
            RentalChatGptReviewDto rentalChatGptReviewDto = rentalSqlMapper.rentalGptReview(rentalReviewDto.getId());

            // 리뷰랑 관련된 정보를 map에 저장
            Map<String, Object> map = new HashMap<>();
            map.put("rentalReviewDto", rentalReviewDto);
            map.put("rentalTitle", rentalTitle);
            map.put("nickName", maskedNickName);
            map.put("userId", userId);
            map.put("likeCount", likeCount);
            map.put("profileImg", profileImg); // 프로필 이미지 추가
            map.put("rentalChatGptReviewDto", rentalChatGptReviewDto);

            rentalReviewList.add(map);
        }

        return rentalReviewList;

    }

    // 리뷰 정렬순
    public List<RentalReviewDto> getSortedReviews(int id, String sortOrder) {
        return rentalSqlMapper.getReviewsByCriteria(id, sortOrder);
    }

    // 특정 상품에 대한 리뷰 총 개수 조회
    public int getReviewCountByRentalItemId(int rental_item_id) {
        return rentalSqlMapper.getReviewCountByRentalItemId(rental_item_id);
    }

    // 리뷰 삭제
    public void deleteReview(int id) {
        rentalSqlMapper.deleteReview(id);
    }

    // 특정 대여 주문에 대한 리뷰 목록 조회(사용자가 작성한 리뷰 목록 확인)
    public List<RentalReviewDto> getReviewsByOrderId(int order_id) {
        return rentalSqlMapper.getReviewsByOrderId(order_id);
    }

    // 특정 리뷰 조회(사용자가 작성한 리뷰 상세 확인)
    public RentalReviewDto getReviewById(int id) {
        return rentalSqlMapper.getReviewById(id);
    }

    // 리뷰 좋아요
    public void insertReviewLike(RentalReviewLikeDto rentalReviewLikeDto) {
        rentalSqlMapper.insertReviewLike(rentalReviewLikeDto);
    }

    // 리뷰 좋아요 취소
    public int deleteReviewLike(RentalReviewLikeDto rentalReviewLikeDto) {
        return rentalSqlMapper.deleteReviewLike(rentalReviewLikeDto);
    }

    // 리뷰 좋아요 확인
    public boolean getUserReviewLikeStatus(int review_id, int user_id) {
        return rentalSqlMapper.getUserReviewLikeStatus(review_id, user_id) > 0;
    }

    // 리뷰 좋아요 총 개수
    public int reviewTotalLikes(int review_id) {
        return rentalSqlMapper.getReviewTotalLikes(review_id);
    }

    // 리뷰 평균 별점
    public double getAverageRating(int rental_item_id) {
        return rentalSqlMapper.getAverageRating(rental_item_id);
    }

    //유저 정보 가져오기
    public Map<String,Object> getUserInfoBySessionId(int userId){
        return rentalSqlMapper.getUserInfoBySessionId(userId);
    }

    // 가장 많이 대여한 상품 top4 가져오기
    public List<RentalItemDto> getTopRentalItems() {

        return rentalSqlMapper.getTopRentalItems();
    }

    public List<RentalItemDto> getTopLikedItems() {
        return rentalSqlMapper.getTopLikedItems();
    }

    public int getRentalOrderStatusCount(String status) {
        return rentalSqlMapper.getRentalOrderStatusCount(status);
    }

    // 리뷰 작성 확인
    public boolean isReviewWritten(int order_id, int user_id) {
        int userReviewCount = rentalSqlMapper.getUserReviewCount(order_id, user_id);
        return userReviewCount > 0;
    }

}
