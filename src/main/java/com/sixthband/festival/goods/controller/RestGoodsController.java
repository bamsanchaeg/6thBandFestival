package com.sixthband.festival.goods.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.goods.dto.GoodsReviewDetailImageDto;
import com.sixthband.festival.goods.dto.GoodsReviewDto;
import com.sixthband.festival.goods.dto.GoodsReviewLikeDto;
import com.sixthband.festival.goods.dto.GoodsWishDto;
import com.sixthband.festival.goods.service.GoodsService;
import com.sixthband.festival.util.ImageUtil;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/userGoods")
public class RestGoodsController {

    @Autowired
    private GoodsService goodsService;

    //goodsList
    @RequestMapping("getUserGoodsList")
    public RestResponseDto getUserGoodsList(@RequestParam(value="p", defaultValue ="1") int p){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("goodsList", goodsService.selectDisCountAndGoods());

        return restResponseDto;
    }

    //wish 에 상품추가
    @RequestMapping("wish")
    public RestResponseDto Wish(GoodsWishDto params, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        params.setUser_id(userDto.getId());

        goodsService.insertWishList(params);

        return restResponseDto;
    }

    //wish 에 상품제거
    @RequestMapping("unWish")
    public RestResponseDto unWish(GoodsWishDto params, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        params.setUser_id(userDto.getId());

        goodsService.deleteWishList(params);

        return restResponseDto;
    }

    //wish 에 상품확인
    @RequestMapping("isWishLiked")
    public RestResponseDto isLiked(@RequestParam("goods_id") int goodsId , GoodsWishDto params, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto)session.getAttribute("loginUser");

        if (userDto == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("redirectUrl", "/login"); 
            return restResponseDto;
        }
        
        params.setUser_id(userDto.getId());
        params.setGoods_id(goodsId);

        boolean x = goodsService.getWishLiked(params);
        restResponseDto.add("isWishLiked", x);

        return restResponseDto;
    }

    //총 wish 수
    @RequestMapping("totalWishLikes")
    public RestResponseDto totalWishLikes(@RequestParam("goods_id") int goods_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        int count = goodsService.totalWishLikes(goods_id);
        restResponseDto.add("count", count);

        return restResponseDto;
    }

    //위시목록
    @RequestMapping("getWishList")
    public RestResponseDto getWishList(@RequestParam("user_id") int user_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<Map<String, Object>> wishList = goodsService.getWishListAll(user_id);

        restResponseDto.add("wishList", wishList);
        restResponseDto.add("userId", user_id);

        return restResponseDto;
    }

    //세션로그인id확인
    @RequestMapping("getSessionUserId")    
    public RestResponseDto getSessionUserId(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto loginUser = (UserDto)session.getAttribute("loginUser");
        if(loginUser != null){
            restResponseDto.setResult("success");
            restResponseDto.add("id", loginUser.getId());
        }else{
            restResponseDto.setResult("fail");
            restResponseDto.add("message", "로그인 하지 않았습니다.");
        }

        return restResponseDto;
    }

    //리뷰 등록록
    @RequestMapping("registerReview")
    public RestResponseDto registerReview(GoodsReviewDto params, HttpSession session,  
        @RequestParam(value = "mainImage", required = false) MultipartFile mainImage, 
        @RequestParam("uploadImage") MultipartFile[] uploadImage,
        @RequestParam("order_id")int order_id, @RequestParam("rating") double rating){

        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto =(UserDto) session.getAttribute("loginUser");

        //대표이미지 저장 (옵션으로 변경)
        if (mainImage != null && !mainImage.isEmpty()) {
            String image = ImageUtil.saveImageAndReturnLocation(mainImage);
            if (image != null && !image.isEmpty()) {
                params.setImage(image);
            }
        }

        goodsService.insertGoodsReview(params, userDto.getId());

        //세션에 사용자 id저장
        session.setAttribute("myId", userDto.getId());

        //설정된 rating값을 GoodsReviewDto에 설정
        params.setRating(rating);
        params.setOrder_id(order_id);

        int reviewId = params.getId();

        //상세 이미지 - 여러장
        if (uploadImage != null && uploadImage.length > 0) {
            List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(uploadImage);

            for (ImageDto imageDto : multiImage) {
                //이미지 경로가 빈 문자열이 아닌 경우에만 DB에 저장
                if (imageDto.getLocation() != null && !imageDto.getLocation().isEmpty()) {
                    GoodsReviewDetailImageDto goodsReviewDetailImageDto = new GoodsReviewDetailImageDto();
                    goodsReviewDetailImageDto.setReview_id(reviewId);
                    goodsReviewDetailImageDto.setMultiple_image(imageDto.getLocation());
                    goodsService.insertGoodsReviewDetailImage(goodsReviewDetailImageDto);
                    }
                }
            }

        return restResponseDto;
    }

    //리뷰 리스트
    @RequestMapping("getReviewList")
    public RestResponseDto getReviewList(@RequestParam("goods_id") int goods_id, HttpSession session){

        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reviewList", goodsService.selectGoodsReviewList(goods_id));

        if(session.getAttribute("loginUser") != null){
            restResponseDto.add("reviewList", goodsService.selectGoodsReviewList(goods_id, session));
        }
        
        return restResponseDto;
    }

    //리뷰 카운트
    @RequestMapping("getCountReviewByGoodsId")
    public RestResponseDto getCountGoodsReviewByGoodsId(@RequestParam("goods_id") int goods_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        int count = goodsService.getCountGoodsReviewByGoodsId(goods_id);
        restResponseDto.add("count", count);

        return restResponseDto;
    }

    //리뷰 평균
    @RequestMapping("getAvgReviewRatingByGoodsId")
    public RestResponseDto getAvgReviewRatingByGoodsId(@RequestParam("goods_id") int goods_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        double avg = goodsService.avgReviewRatingByGoodsId(goods_id);
        restResponseDto.add("avg", avg);

        return restResponseDto;
    }

    //리뷰 삭제
    @RequestMapping("deleteReview")
    public RestResponseDto deleteReview(@RequestParam("id") int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        goodsService.deleteReview(id);

        return restResponseDto;
    }
     
    //리뷰 카운트, 평점 - goodsMainPage:리스트에서 출력
    @RequestMapping("getGoodsReviewSummary")
    public RestResponseDto getGoodsReviewSummary(@RequestParam("goods_id") int goods_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        //카운트
        int reviewCount = goodsService.getCountGoodsReviewByGoodsId(goods_id);
        restResponseDto.add("reviewCount", reviewCount);

        //평점
        double avgRating = goodsService.avgReviewRatingByGoodsId(goods_id);
        restResponseDto.add("avgRating", avgRating);

        return restResponseDto;
    }

    //리뷰 좋아요 토글 (추가/제거)
    @RequestMapping("toggleReviewLike")
    public RestResponseDto toggleReviewLike(@RequestParam("review_id") int reviewId, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        GoodsReviewLikeDto params = new GoodsReviewLikeDto();
        params.setUser_id(userDto.getId());
        params.setReview_id(reviewId);
    
        boolean isLiked = goodsService.getReviewLiked(params);
        if (isLiked) {
            goodsService.deleteGoodsReviewLike(params);
        } else {
            goodsService.insertGoodsReviewLike(params);
        }
        restResponseDto.add("isReviewLiked", !isLiked);
    
        return restResponseDto;
    }
 
    //리뷰후기 좋아요 상태
    @RequestMapping("isReviewLiked")
    public RestResponseDto isReviewLiked(@RequestParam("review_id") int reviewId, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        GoodsReviewLikeDto params = new GoodsReviewLikeDto();
        params.setUser_id(userDto.getId());
        params.setReview_id(reviewId);
        
        restResponseDto.add("reviewId", reviewId);

        boolean isLiked = goodsService.getReviewLiked(params);
        restResponseDto.add("isReviewLiked", isLiked);

        return restResponseDto;
    }

    //리뷰후기 좋아요 총 수(카운트)
    @RequestMapping("totalReviewLikes")
    public RestResponseDto totalReviewLikes(@RequestParam("review_id") int reviewId){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        int reviewCount = goodsService.totalReviewLikes(reviewId);
        restResponseDto.add("reviewCount", reviewCount);

        return restResponseDto;
    }

    //굿즈메인 NEW6
    @RequestMapping("getNewGoodsItems")
    public RestResponseDto getNewGoodsItems(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("goodsList", goodsService.selectNewItem6());
        
        return restResponseDto;
    }
}
