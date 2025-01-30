package com.sixthband.festival.goods.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.goods.dto.GoodsCategoryDto;
import com.sixthband.festival.goods.dto.GoodsDetailImageDto;
import com.sixthband.festival.goods.dto.GoodsDiscountDto;
import com.sixthband.festival.goods.dto.GoodsDto;
import com.sixthband.festival.goods.dto.GoodsGptReviewDto;
import com.sixthband.festival.goods.dto.GoodsOrderDto;
import com.sixthband.festival.goods.dto.GoodsReviewDetailImageDto;
import com.sixthband.festival.goods.dto.GoodsReviewDto;
import com.sixthband.festival.goods.dto.GoodsReviewLikeDto;
import com.sixthband.festival.goods.dto.GoodsReviewRecommendDto;
import com.sixthband.festival.goods.dto.GoodsShopperbagDto;
import com.sixthband.festival.goods.dto.GoodsWishDto;
import com.sixthband.festival.goods.mapper.GoodsSqlMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

import jakarta.servlet.http.HttpSession;

@Service
public class GoodsService {
    
    @Autowired
    private GoodsSqlMapper goodsSqlMapper;

    public List<GoodsCategoryDto> goodsCategory(){
        return goodsSqlMapper.goodsCategory();
    } 

    public void insertGoods(GoodsDto goodsDto){
        goodsSqlMapper.insertGoods(goodsDto);
    }

    public List<GoodsDiscountDto> selectDiscountsByGoodsId(int goods_id){
        return goodsSqlMapper.selectDiscountsByGoodsId(goods_id);
    }

    public List<GoodsDto> selectDisCountAndGoods(){
        return goodsSqlMapper.selectDisCountAndGoods();
    }

    public void insertDiscount(GoodsDiscountDto goodsDiscountDto){
        goodsSqlMapper.insertDiscount(goodsDiscountDto);
    }

    public void updateDiscount(GoodsDiscountDto goodsDiscountDto){
        goodsSqlMapper.updateDiscount(goodsDiscountDto);
    }

    public void deleteDiscount(int id){
        goodsSqlMapper.deleteDiscount(id);
    }

    public void goodsDetailImage(GoodsDetailImageDto goodsDetailImageDto){
        goodsSqlMapper.goodsDetailImage(goodsDetailImageDto);
    }

    public void insertGoodsReviewDetailImage(GoodsReviewDetailImageDto goodsReviewDetailImageDto){
        goodsSqlMapper.insertGoodsReviewDetailImage(goodsReviewDetailImageDto);
    }

    public Map<String ,Object> getGoodsDetail(int id){
        
        Map<String, Object> result = new HashMap<>();
        GoodsDto goodsData = goodsSqlMapper.getGoodsDetailById(id);
        result.put("goodsDetail", goodsData);

        return result;
    }

    //goodsList - pagination O
    public List<Map<String, Object>> selectGoodsList(int p){
        List<Map<String, Object>> GoodsList = new ArrayList<>();
        List<GoodsDto> goodsList = goodsSqlMapper.selectGoodsList((p-1)*10);
            if (goodsList == null) {
                return GoodsList;
            }

            for(GoodsDto goodsDto : goodsList) {
                if (goodsDto == null) {
                    continue; 
                }
                int categoryPk = goodsDto.getCategory_id();
                GoodsCategoryDto goodsCategoryDto = goodsSqlMapper.selectIdFromCategory(categoryPk);
                Map<String, Object> map = new HashMap<>();
                map.put("goodsDto", goodsDto);
                map.put("goodsCategoryDto", goodsCategoryDto);

                GoodsList.add(map);
            }

        return GoodsList;
    }

    public List<Map<String, Object>> selectGoodsListAll(){
        List<Map<String, Object>> GoodsList = new ArrayList<>();
        List<GoodsDto> goodsList = goodsSqlMapper.selectGoodsListAll();
            if (goodsList == null) {
                return GoodsList;
            }
            for(GoodsDto goodsDto : goodsList) {
                if (goodsDto == null) {
                    continue; 
                }
                int categoryPk = goodsDto.getCategory_id();
                GoodsCategoryDto goodsCategoryDto = goodsSqlMapper.selectIdFromCategory(categoryPk);
                Map<String, Object> map = new HashMap<>();

                map.put("goodsDto", goodsDto);
                map.put("goodsCategoryDto", goodsCategoryDto);

                GoodsList.add(map);
            }

        return GoodsList;
    }

    //검색된 결과의 카운트
    public int getGoodsListTotalCount(){
        return goodsSqlMapper.getGoodsListTotalCount();
    }

    //굿즈 상세페이지
    public List<Map<String, Object>> selectImageWithGoodsList(int goods_id){
        List<Map<String, Object>> ImageWithGoodsList = new ArrayList<>();
        List<GoodsDetailImageDto> DetailImageList = goodsSqlMapper.selectImageListByGoodsId(goods_id);
            for(GoodsDetailImageDto goodsDetailImageDto : DetailImageList){
                int goodsPk = goodsDetailImageDto.getGoods_id();
                GoodsDto goodsDto = goodsSqlMapper.getGoodsDetailById(goodsPk);

                Map<String, Object> map = new HashMap<>();

                map.put("goodsDetailImageDto", goodsDetailImageDto);
                map.put("goodsDto", goodsDto);

                ImageWithGoodsList.add(map);
            }
        return ImageWithGoodsList;
    }

    //장바구니
    public List<Map<String, Object>> selectGoodsListByCart(int user_id){
        List<Map<String, Object>> CartList = new ArrayList<>();
        List<GoodsShopperbagDto> goodsShopperbagList = goodsSqlMapper.selectGoodsCartListByUserId(user_id);
            for(GoodsShopperbagDto goodsShopperbagDto : goodsShopperbagList){
                GoodsDto goodsDto = goodsSqlMapper.getGoodsDetailById(goodsShopperbagDto.getGoods_id());
                Map<String, Object> map = new HashMap<>();

                map.put("goodsShopperbagDto", goodsShopperbagDto);
                map.put("goodsDto", goodsDto);
                CartList.add(map);
            }

        return CartList;
    }

    //굿즈 수정
    public Map<String, Object> selectGoodsCategoryAndGoods(int id){
        Map<String, Object> map = new HashMap<>();
        GoodsDto goodsDto = goodsSqlMapper.getGoodsDetailById(id);
        map.put("goodsDto", goodsDto);

        return map;
    }

    //굿즈 삭제
    public void deleteGoodsId(int id){
        goodsSqlMapper.deleteGoodsId(id);
    }

    //굿즈 수정
    public void updateGoodsProduct(GoodsDto goodsDto){
        goodsSqlMapper.updateGoodsProduct(goodsDto);
    }

    //주문상태 업데이트
    public void updateOrderStatus(int id, String order_status){
        goodsSqlMapper.updateOrderStatus(id, order_status);
    }

    //장바구니
    public void insertGoodsShopperbag(GoodsShopperbagDto goodsShopperbagDto){
        goodsSqlMapper.insertGoodsShopperbag(goodsShopperbagDto);
    }

    //장바구니 삭제
    public void deleteCartByGoods(int goods_id, int user_id){
        goodsSqlMapper.deleteCartByGoods(goods_id, user_id);
    }
    
    //장바구니 상품 더하기
    public int calculateTotalGoodsPrice(List<Map<String, Object>> selectGoodsList){
        
        //총 상품금액 계산
        int totalGoodsPrice = 0;
        for (Map<String, Object> goods : selectGoodsList) {
            GoodsDto goodsDto = (GoodsDto) goods.get("goodsDto");
            GoodsShopperbagDto goodsShopperbagDto = (GoodsShopperbagDto) goods.get("goodsShopperbagDto");

            if (goodsDto != null && goodsShopperbagDto != null) {
                int price = goodsDto.getPrice();
                int itemCount = goodsShopperbagDto.getItem_count();
                totalGoodsPrice += price * itemCount;
            }
        }   
        return totalGoodsPrice;
    }

    public int calculateTotalOrderAmount(int totalGoodsPrice) {
        //총 주문예상금액 계산(배송비 3000 고정값)
        return totalGoodsPrice + 3000;
    }
    
    public void insertWishList(GoodsWishDto goodsWishDto){
        goodsSqlMapper.insertWishList(goodsWishDto);
    }

    public int deleteWishList(GoodsWishDto goodsWishDto){
        return goodsSqlMapper.deleteWishList(goodsWishDto);
    }

    //위시리스트 상태
    public boolean getWishLiked(GoodsWishDto goodsWishDto){
        return goodsSqlMapper.getWishLiked(goodsWishDto) > 0;
    }

    public int totalWishLikes(int goods_id){
        return goodsSqlMapper.totalWishLikes(goods_id);
    }

    public void insertGoodsOrder(GoodsOrderDto goodsOrderDto){
        goodsSqlMapper.insertGoodsOrder(goodsOrderDto);
    }
    
    //오더리스트
    public List<Map<String, Object>> selectGoodsOrderList(int user_id){
        List<Map<String, Object>> GoodsOrderList = new ArrayList<>();
        List<GoodsOrderDto> goodsOrderList = goodsSqlMapper.selectGoodsOrderList(user_id);
            for(GoodsOrderDto goodsOrderDto : goodsOrderList){
                int goodsPk = goodsOrderDto.getGoods_id();
                GoodsDto goodsDto = goodsSqlMapper.getGoodsDetailById(goodsPk);

                Map<String, Object> map = new HashMap<>();

                map.put("goodsDto", goodsDto);
                map.put("goodsOrderDto", goodsOrderDto);

                GoodsOrderList.add(map);
            }

        return GoodsOrderList;
    }

    //굿즈 주문상태 불러오기(카운트- 해당상태의 모든유저 / admin 메인 대시보드용)
    public Map<String, Integer> countOrdersByStatus() {
        List<Map<String, Object>> results = goodsSqlMapper.countOrdersByStatus();
        Map<String, Integer> statusCounts = new HashMap<>();
            for (Map<String, Object> result : results) {
                String status = (String) result.get("order_status");
                Integer count = ((Long) result.get("status_count")).intValue(); 
                statusCounts.put(status, count);
            }
        return statusCounts;
    }

    //굿즈 주문상태 불러오기(카운트- 해당유저 / user-주문확인용)
    public Map<String, Integer> countOrdersByStatusByUserId(int user_id) {
        List<Map<String, Object>> results = goodsSqlMapper.countOrdersByStatusByUserId(user_id);
        Map<String, Integer> statusCounts = new HashMap<>();
            for (Map<String, Object> result : results) {
                String status = (String) result.get("order_status");
                Integer count = ((Long) result.get("status_count")).intValue();
                statusCounts.put(status, count);
            }
        return statusCounts;
    }

    //주문상세
    public Map<String, Object> selectGoodsOrderDetail(int id, int user_id){

        GoodsOrderDto goodsOrderDto = goodsSqlMapper.selectGoodsOrderDetail(id, user_id);
        if(goodsOrderDto == null){
            return null;
        }

        int goodsPk = goodsOrderDto.getGoods_id();
        GoodsDto goodsDto = goodsSqlMapper.getGoodsDetailById(goodsPk);
        UserDto userDto = goodsSqlMapper.getUserDetailById(user_id);

        Map<String, Object> orderDetail = new HashMap<>();
        orderDetail.put("goodsDto", goodsDto);
        orderDetail.put("goodsOrderDto", goodsOrderDto);
        orderDetail.put("userDto", userDto);

        return orderDetail;
    }

    //리뷰등록
    public void insertGoodsReview(GoodsReviewDto goodsReviewDto, int user_id){
        goodsSqlMapper.insertGoodsReview(goodsReviewDto);
        insertGptReview(goodsReviewDto);
    }

    //Gpt
    private String openaiAccessKey;

    @Value("${openAiKey}")
    public void setOpenAiAccessKey(String openaiAccessKey) {
        this.openaiAccessKey = openaiAccessKey;
    }

    public void insertGptReview(GoodsReviewDto goodsReviewDto){

        String review = goodsReviewDto.getContent();

        OpenAiService service = new OpenAiService(openaiAccessKey, Duration.ofSeconds(30));

        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setRole("system");
        systemMessage.setContent("당신은 내가 제시하는 리뷰를 요약해줘야합니다. 최대한 짧게 요약을 해야 합니다. 20글자를 넘어서는 안됩니다.");
        messages.add(systemMessage);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("user");
        chatMessage.setContent("다음 글을 요약해서 아주 아주 짧은 소감 한 줄 만들어줘! : " + review);
        messages.add(chatMessage);
   
        //요청
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
            .model("gpt-4o-mini")
            .messages(messages)
            .build();

        //응답결과
        String result = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
        System.out.println("결과: " + result);
        
        GoodsGptReviewDto goodsGptReviewDto = new GoodsGptReviewDto();
        goodsGptReviewDto.setGpt_Review(result);
        goodsGptReviewDto.setComment_id(goodsReviewDto.getId());

        goodsSqlMapper.insertGptReview(goodsGptReviewDto);
    }

    //리뷰 작성 확인
    public boolean isGoodsReviewed(int order_id) {
        return goodsSqlMapper.countReviewsByOrderId(order_id) > 0;
    }

    //리뷰 리스트 - 세션정보 X
    public List<Map<String, Object>> selectGoodsReviewList(int goods_id){
        List<Map<String, Object>> GoodsReviewList = new ArrayList<>();
        List<GoodsReviewDto> reviewList = goodsSqlMapper.selectGoodsReviewList(goods_id);
            for(GoodsReviewDto goodsReviewDto : reviewList){

                int orderPk = goodsReviewDto.getOrder_id();
                GoodsOrderDto goodsOrderDto = goodsSqlMapper.selectGoodsOrderDetailById(orderPk);

                int goodsId = goodsOrderDto.getGoods_id();

                GoodsDto goodsDto = goodsSqlMapper.getGoodsDetailById(goodsId);
                String goodsTitle = goodsDto.getGoods_title();

                int userId = goodsOrderDto.getUser_id();

                UserDto userDto = goodsSqlMapper.getUserDetailById(userId);
                String nickName = userDto.getNickname();
                String userImage = userDto.getProfile_img();

                //리뷰에 이미지 리스트 가져오기
                List<GoodsReviewDetailImageDto> reviewDetailImageList = goodsSqlMapper.selectImageListByReviewId(goodsReviewDto.getId());
                List<String> detailImages = new ArrayList<>();
                    for(GoodsReviewDetailImageDto detailImageDto : reviewDetailImageList){
                        detailImages.add(detailImageDto.getMultiple_image());
                    }

                GoodsGptReviewDto goodsGptReviewDto = goodsSqlMapper.goodsGptReview(goodsReviewDto.getId());

                Map<String, Object> map = new HashMap<>();
                map.put("goodsReviewDto", goodsReviewDto);
                map.put("nickName", nickName);
                map.put("goodsTitle", goodsTitle);
                map.put("userId", userId);
                map.put("detailImages", detailImages);
                map.put("userImage", userImage);
                map.put("goodsGptReviewDto", goodsGptReviewDto);
                
                GoodsReviewList.add(map);
                }

        return GoodsReviewList;
    }

    //리뷰 리스트 오버로딩 - 세션정보 O (좋아요 상태 확인출력)
    public List<Map<String, Object>> selectGoodsReviewList(int goods_id, HttpSession session){
        List<Map<String, Object>> GoodsReviewList = new ArrayList<>();
        List<GoodsReviewDto> reviewList = goodsSqlMapper.selectGoodsReviewList(goods_id);
            for(GoodsReviewDto goodsReviewDto : reviewList){

                int orderPk = goodsReviewDto.getOrder_id();

                GoodsOrderDto goodsOrderDto = goodsSqlMapper.selectGoodsOrderDetailById(orderPk);
                int goodsId = goodsOrderDto.getGoods_id();

                GoodsDto goodsDto = goodsSqlMapper.getGoodsDetailById(goodsId);
                String goodsTitle = goodsDto.getGoods_title();

                int userId = goodsOrderDto.getUser_id();
                UserDto userDto = goodsSqlMapper.getUserDetailById(userId);
                String nickName = userDto.getNickname();
                String userImage = userDto.getProfile_img();

                List<GoodsReviewDetailImageDto> reviewDetailImageList = goodsSqlMapper.selectImageListByReviewId(goodsReviewDto.getId());
                List<String> detailImages = new ArrayList<>();
                    for(GoodsReviewDetailImageDto detailImageDto : reviewDetailImageList){
                        detailImages.add(detailImageDto.getMultiple_image());
                    }

                GoodsGptReviewDto goodsGptReviewDto = goodsSqlMapper.goodsGptReview(goodsReviewDto.getId());

                Map<String, Object> map = new HashMap<>();
                map.put("goodsReviewDto", goodsReviewDto);
                map.put("nickName", nickName);
                map.put("goodsTitle", goodsTitle);
                map.put("userId", userId);
                map.put("detailImages", detailImages);
                map.put("userImage", userImage);
                map.put("goodsGptReviewDto", goodsGptReviewDto);

                if(session.getAttribute("loginUser") != null){
                    UserDto userDto2 = (UserDto) session.getAttribute("loginUser");

                    GoodsReviewLikeDto goodsReviewLikeDto = new GoodsReviewLikeDto();
                    goodsReviewLikeDto.setUser_id(userDto2.getId());
                    goodsReviewLikeDto.setReview_id(goodsReviewDto.getId());
                    
                    boolean isReviewLiked = goodsSqlMapper.getReviewLiked(goodsReviewLikeDto) > 0;
                    map.put("reviewLiked", isReviewLiked);
                }
                
                GoodsReviewList.add(map);
            }
            
        return GoodsReviewList;
    }

    //리뷰 검색된 결과의 카운트
    public int getReviewListTotalCount(){
        return goodsSqlMapper.getReviewListTotalCount();
    }
        
    //리뷰 상세이미지
    public List<Map<String, Object>> selectImageListByReviewId(int review_id){
        List<Map<String, Object>> ImageWithReviewList = new ArrayList<>();
        List<GoodsReviewDetailImageDto> ReviewDetailImageList = goodsSqlMapper.selectImageListByReviewId(review_id);

        for(GoodsReviewDetailImageDto goodsReviewDetailImageDto : ReviewDetailImageList){
            int reviewPk = goodsReviewDetailImageDto.getReview_id();
            GoodsReviewDto goodsReviewDto = goodsSqlMapper.getReviewDetailById(reviewPk);

            Map<String, Object> map = new HashMap<>();

            map.put("goodsReviewDetailImageDto", goodsReviewDetailImageDto);
            map.put("goodsReviewDto", goodsReviewDto);

            ImageWithReviewList.add(map);
        }
        
        return ImageWithReviewList;
    }

    //리뷰 카운트
    public int getCountGoodsReviewByGoodsId(int goods_id){
        return goodsSqlMapper.countGoodsReviewByGoodsId(goods_id);
    }

    //리뷰 평균
    public double avgReviewRatingByGoodsId(int goods_id) {
        //평균 별점 조회
        Double avgRating = goodsSqlMapper.avgGoodsReviewRatingByGoodsId(goods_id);
        //null 체크 후 기본값 0.0 반환
        return (avgRating != null) ? avgRating : 0.0;
    }

    public void deleteReview(int id){
        goodsSqlMapper.deleteReview(id);
    }

    public void insertGoodsReviewLike(GoodsReviewLikeDto goodsReviewLikeDto){
        goodsSqlMapper.insertGoodsReviewLike(goodsReviewLikeDto);
    }

    public int deleteGoodsReviewLike(GoodsReviewLikeDto goodsReviewLikeDto){
        return goodsSqlMapper.deleteGoodsReviewLike(goodsReviewLikeDto);
    }

    public boolean getReviewLiked(GoodsReviewLikeDto goodsReviewLikeDto){
        return goodsSqlMapper.getReviewLiked(goodsReviewLikeDto) > 0;
    }

    public int totalReviewLikes(@RequestParam("review_id") int review_id){
        return goodsSqlMapper.totalReviewLikes(review_id);
    }

    //위시리스트 가져오기
    public List<Map<String, Object>> getWishListAll(int user_id){
        List<Map<String, Object>> result = new ArrayList<>();
        List<GoodsWishDto> list = goodsSqlMapper.getWishListAllByUserId(user_id);

        for(GoodsWishDto goodsWishDto : list){
            int goodsId = goodsWishDto.getGoods_id();
            GoodsDto goodsDto = goodsSqlMapper.getGoodsDetailById(goodsId);
            List<GoodsDiscountDto> goodsDiscountList  = goodsSqlMapper.selectDiscountsByGoodsId(goodsId);

            Map<String, Object> map = new HashMap<>();
            map.put("goodsWishDto", goodsWishDto);
            map.put("goodsDto", goodsDto);
            map.put("user_id", goodsWishDto.getUser_id());
            map.put("goodsDiscountList", goodsDiscountList);

            result.add(map);
        }

        return result;
    }

    //Admin

    public List<GoodsOrderDto> getGoodsOrderList(int p) {
        return goodsSqlMapper.getGoodsOrderList((p-1)*10);
    }
    
    public int getOrderListTotalCount(){
        return goodsSqlMapper.getOrderListTotalCount();
    }

    public GoodsOrderDto getGoodsOrderListByOrderId(int order_id){
        return goodsSqlMapper.getGoodsOrderListByOrderId(order_id);
    }

    public List<GoodsReviewDto> getGoodsReviewListByGoodsAndUser(int p){
        int limitStart = (p - 1) * 10;
        return goodsSqlMapper.getGoodsReviewListByGoodsAndUser(limitStart, 10);
    }

    public GoodsReviewDto getReviewDetailAll(int id){
        return goodsSqlMapper.getReviewDetailAll(id);
    }

    public int countReviewAll(){
        return goodsSqlMapper.countReviewAll();
    }

    public int countOrderAll(){
        return goodsSqlMapper.countOrderAll();
    }

    public int countGoodsAll(){
        return goodsSqlMapper.countGoodsAll();
    }

    public void insertReviewRecommend(GoodsReviewRecommendDto goodsReviewRecommendDto){
        goodsSqlMapper.insertReviewRecommend(goodsReviewRecommendDto);
    }

    //판매자 답글삭제 - 개별
    public void deleteReviewRecommend(int id){
        goodsSqlMapper.deleteReviewRecommend(id);
    }

    //판매자 답글삭제 - 모두삭제
    public void deleteReviewRecommendationsByReviewId(int goodsReviewId) {
        goodsSqlMapper.deleteReviewRecommendationsByReviewId(goodsReviewId);
    }

    public List<GoodsDto> getGoodsDetailAll(int id){
        return goodsSqlMapper.getGoodsDetailAll(id);
    }


    //Main
    
    public List<GoodsDto> selectOrderTop4(){
        return goodsSqlMapper.selectOrderTop4();
    }

    public List<GoodsDto> selectNewItem6(){
        return goodsSqlMapper.selectNewItem6();
    }



}
