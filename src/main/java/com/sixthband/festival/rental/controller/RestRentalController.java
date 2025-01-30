package com.sixthband.festival.rental.controller;

import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.rental.dto.*;
import com.sixthband.festival.rental.service.RentalService;
import com.sixthband.festival.util.ImageUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/rental")
public class RestRentalController {

    @Autowired
    private RentalService rentalService;

    // 관리자 모든 대여 상품의 목록을 조회하여 반환
    @GetMapping("getRentalItemList")
    public RestResponseDto getRentalItemList(@RequestParam(value="p", defaultValue = "1") int p) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<Map<String, Object>> rentalItemList = rentalService.getAllRentalItems(p);
        restResponseDto.add("rentalItemList", rentalItemList);

        return restResponseDto;
    }

    // 관리자 특정 대여 상품 삭제
    @DeleteMapping("delete")
    public RestResponseDto delete(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        rentalService.deleteRentalItem(id);

        return restResponseDto;
    }

    // 대여 상품 찜 or 취소
    @PostMapping("toggleLike")
    public RestResponseDto toggleLike(HttpSession session, @RequestBody RentalLikeDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        if (userDto == null) {
            restResponseDto.setResult("fail");
            return restResponseDto;
        }

        params.setUser_id(userDto.getId());
        boolean isLiked = rentalService.getIsLiked(params.getItem_id(), userDto.getId());

        if (isLiked) {
            rentalService.deleteLike(params);
        } else {
            rentalService.insertLike(params);
        }

        restResponseDto.add("isLiked", !isLiked);
        System.out.println(restResponseDto);
        return restResponseDto;
    }

    // 대여 카테고리 목록 조회, 반환
    @GetMapping("getRentalCategories")
    public RestResponseDto getRentalCategories() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<RentalCategoryDto> categories = rentalService.getRentalCategories();
        restResponseDto.add("categories", categories);

        return restResponseDto;
    }

    // 모든 대여 상품 조회, 반환
    @GetMapping("getAllRentalItems")
    public RestResponseDto getAllRentalItems(@RequestParam(value="p", defaultValue = "1") int p) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<Map<String, Object>> items = rentalService.getAllRentalItems(p);
        restResponseDto.add("items", items);

        return restResponseDto;
    }

    // 특정 카테고리에 속한 대여 상품을 조회, 반환
    @GetMapping("getRentalItemsByCategory")
    public RestResponseDto getRentalItemsByCategory(@RequestParam("category_id") int category_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // xml getRentalItemsByCategory에서 id 빼먹어서 카테고리별 조회할 때 All말고는 에러뜸
        // select * from 안 넣고 골라 넣어서
        List<Map<String, Object>> itemsByCategory = rentalService.getRentalItemsByCategory(category_id);


        restResponseDto.add("itemsByCategory", itemsByCategory);
        
        return restResponseDto;
    }

    // 리뷰 작성, 저장
    @PostMapping("review")
    public RestResponseDto review(HttpSession session,
                                  @ModelAttribute RentalReviewDto params,
                                  @RequestParam(value="images", required=false) MultipartFile images){
        //System.out.println("Received review data: " + params);
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        if (userDto == null) {
            restResponseDto.setResult("fail");
            restResponseDto.setReason("로그인을 하셔야 이용 가능합니다. 로그인 하시겠습니까?");
            return restResponseDto;
        }

        // 리뷰 작성자 id 설정
        params.setUser_id(userDto.getId());


        // 이미지가 있는 경우에만 저장 및 경로 설정
        if (images != null && !images.isEmpty()) {
            String image = ImageUtil.saveImageAndReturnLocation(images);
            params.setImage(image); // 이미지 경로 저장
        } else {
            params.setImage(null); // 이미지가 없을 경우 null 설정
        }

        // 리뷰 저장
        boolean success = rentalService.insertReview(params);

        if (success) {
            restResponseDto.setResult("success");
            restResponseDto.setReason("리뷰가 성공적으로 작성되었습니다.");
        } else {
            restResponseDto.setResult("fail");
            restResponseDto.setReason("이미 리뷰를 작성하셨습니다.");
        }

        return restResponseDto;
    }

    // 리뷰 목록
    @GetMapping("getReviewList")
    public RestResponseDto getReviewList(@RequestParam("rental_item_id") int rental_item_id,
                                         @RequestParam(value = "sortOrder", required = false, defaultValue = "latest") String sortOrder) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<Map<String, Object>> reviews = rentalService.getReviewList(rental_item_id, sortOrder);
        // 리뷰 데이터가 올바르게 추가되었는지 확인하기 위해 로그 출력

        restResponseDto.add("rentalReviewList", reviews);

        return restResponseDto;
    }

    // 특정 상품에 대한 리뷰 총 개수
    @GetMapping("getReviewCount")
    public RestResponseDto getReviewCount(@RequestParam("rental_item_id") int rental_item_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        int reviewCount = rentalService.getReviewCountByRentalItemId(rental_item_id);
        restResponseDto.add("reviewCount", reviewCount);

        return restResponseDto;
    }

    // 리뷰 삭제
    @DeleteMapping("deleteReview")
    public RestResponseDto deleteReview(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        rentalService.deleteReview(id);

        return restResponseDto;
    }

    @GetMapping("getReviewByOrderId/{order_Id}")
    public RestResponseDto getReviewByOrderId(@PathVariable int order_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        List<RentalReviewDto> reviewDtoList = rentalService.getReviewsByOrderId(order_id);
        restResponseDto.add("reviewDtoList", reviewDtoList);

        return restResponseDto;
    }

    @GetMapping("getReviewById/{id}")
    public RestResponseDto getReviewById(@PathVariable int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        RentalReviewDto reviewDto = rentalService.getReviewById(id);
        restResponseDto.add("reviewDto", reviewDto);

        return restResponseDto;
    }

    @PostMapping("toggleReviewLike")
    public RestResponseDto toggleReviewLike(HttpSession session, @RequestBody RentalReviewLikeDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        if (userDto == null) {
            restResponseDto.setResult("fail");
            return restResponseDto;
        }

        params.setUser_id(userDto.getId());

        // 리뷰의 좋아요 상태 확인
        boolean isLiked = rentalService.getUserReviewLikeStatus(params.getReview_id(), userDto.getId());

        if (isLiked) {
            rentalService.deleteReviewLike(params); // 이미 좋아요 상태면 취소
        } else {
            rentalService.insertReviewLike(params); // 좋아요 추가
        }

        restResponseDto.add("isReviewLiked", !isLiked);

        return restResponseDto;
    }

    // 리뷰 좋아요 상태 확인
    @GetMapping("checkReviewLikeStatus")
    public RestResponseDto checkReviewLikeStatus(HttpSession session, @RequestParam("review_id") int review_id, @RequestParam("user_id") int user_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        // 로그인된 사용자 확인
        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        if (userDto == null) {
            restResponseDto.setResult("fail");
            restResponseDto.add("message", "로그인이 필요합니다.");
            return restResponseDto;
        }

        // 로그인된 사용자의 ID와 요청된 user_id가 동일한지 확인
        if (userDto.getId() != user_id) {
            restResponseDto.setResult("fail");
            restResponseDto.add("message", "잘못된 사용자 요청입니다.");
            return restResponseDto;
        }

        // 리뷰의 좋아요 상태 확인
        boolean isLiked = rentalService.getUserReviewLikeStatus(review_id, user_id);

        // 좋아요 상태를 응답에 추가
        restResponseDto.add("isReviewLiked", isLiked);
        return restResponseDto;
    }

    // 리뷰 좋아요 개수
    @GetMapping("reviewTotalLikes")
    public RestResponseDto reviewTotalLikes(@RequestParam("review_id") int review_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        int reviewTotalLikes = rentalService.reviewTotalLikes(review_id);

        restResponseDto.add("reviewTotalLikes", reviewTotalLikes);

        return restResponseDto;
    }

    // 리뷰 평균 별점
    @GetMapping("getAverageRating")
    public RestResponseDto getAverageRating(@RequestParam("rental_item_id") int rental_item_id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        double avgRating = rentalService.getAverageRating(rental_item_id);
        restResponseDto.add("avgRating", avgRating);

        return restResponseDto;
    }

    //유저 유효성 검사(로그인했는지 안했는지 파악하기)
    @RequestMapping("getSessionUserId")
    public RestResponseDto getSessionUserId(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        UserDto sessionUser = (UserDto) session.getAttribute("loginUser");
        if(sessionUser != null){
            restResponseDto.setResult("success");
            restResponseDto.add("userInfo",rentalService.getUserInfoBySessionId(sessionUser.getId()));
        }else {
            restResponseDto.setResult("success");
            restResponseDto.add("user_id", null);
        }

        return restResponseDto;

    }

    @GetMapping("getTopLikedItems")
    public RestResponseDto getTopLikedItems() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

         List<RentalItemDto> topLikedItems = rentalService.getTopLikedItems();
         restResponseDto.add("topLikedItems", topLikedItems);

        return restResponseDto;
    }

}
