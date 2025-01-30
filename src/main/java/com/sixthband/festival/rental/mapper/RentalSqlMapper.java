package com.sixthband.festival.rental.mapper;

import java.util.List;
import java.util.Map;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.rental.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface RentalSqlMapper {

    // 모든 카테고리 조회
    public List<RentalCategoryDto> getRentalCategories();

    // 특정 카테고리에 속하는 렌탈 아이템 조회
    public List<RentalItemDto> getRentalItemsByCategory(int category_id);

    // 특정 카테고리 id에 해당하는 대여 카테고리 조회
    public RentalCategoryDto getByCategoryId(int id);

    // 상세이미지 등록
    public void insertDetailImage(RentalDetailImageDto rentalDetailImageDto);

    // 상세이미지 삭제
    public void deleteItemImages(int rental_item_id);

    // 특정 상품에 해당하는 상세 이미지
    public List<RentalDetailImageDto> getImagesByRentalItemId(int rental_item_id);

    // 대여상품 리스트 가져오기
    public List<RentalItemDto> getAllRentalItems(@Param("pageIndex") int pageIndex);

    // 대여상품 리스트 가져오기All
    public List<RentalItemDto> getAllRentalItemsAll();

    // 대여상품 페이지네이션
    public int getRentalItemTotalCount();

    // 대여상품 상세
    public RentalItemDto getByItemId(int id);

    // 대여상품 등록
    public void insertRentalItem(RentalItemDto rentalItemDto);

    // 대여상품 수정
    public void updateRentalItem(RentalItemDto rentalItemDto);

    // 대여상품 삭제
    public void deleteRentalItemById(int id);

    // 대여내역 등록
    public void insertRentalOrder(RentalOrderDto rentalOrderDto);

    // 대여내역 목록
    public List<RentalOrderDto> getRentalOrderAll(@Param("pageIndex") int pageIndex);

    // 대여내역 페이지네이션
    public int getRentalOrderTotalCount();

    // 대여내역 상세 조회
    public RentalOrderDto getRentalOrderById(int id);

    // 로그인한 사용자 대여내역 리스트
    public List<RentalOrderDto> getRentalOrderByUserId(int user_id);

    // 대여내역 수정
    public void updateRentalOrder(RentalOrderDto rentalOrderDto);

    // 대여내역 삭제
    public void deleteRentalOrderById(int id);

    // 사용자 id 가져오기
    public UserDto getUserById(int id);

    // 찜하기
    public void insertLike(RentalLikeDto rentalLikeDto);
    // 찜 취소
    public int deleteLike(RentalLikeDto rentalLikeDto);
    // 찜 상태 확인
    public int getIsLiked(@Param("item_id") int item_id,
                          @Param("user_id") int user_id);
    // 찜하기 수
    public int totalLikes(int item_id);

    // 리뷰 등록
    public void insertReview(RentalReviewDto rentalReviewDto);

    // 리뷰 목록
    public List<RentalReviewDto> getAllReviews();

    // 특정 상품에 대한 리뷰
    public List<RentalReviewDto> getReviewsByRentalItemId(@Param("rental_item_id") int rental_item_id);

    // 특정 상품에 대한 리뷰 총 개수
    public int getReviewCountByRentalItemId(int rental_item_id);

    // 리뷰 삭제
    public void deleteReview(int id);

    // 특정 대여 주문과 회원에 대한 리뷰 개수 확인
    public int countUserReview(@Param("user_id") int user_id,
                               @Param("order_id") int order_id);

    // 특정 대여 주문에 대한 리뷰 목록 조회(사용자가 작성한 리뷰 목록 확인)
    public List<RentalReviewDto> getReviewsByOrderId(@Param("order_id") int order_id);

    // 특정 리뷰 조회(사용자가 작성한 리뷰 상세 확인)
    public  RentalReviewDto getReviewById(@Param("id") int id);

    // 리뷰 좋아요
    public void insertReviewLike(RentalReviewLikeDto rentalReviewLikeDto);

    // 리뷰 좋아요 취소
    public int deleteReviewLike(RentalReviewLikeDto rentalReviewLikeDto);

    // 리뷰 좋아요 확인
    public int getUserReviewLikeStatus(@Param("review_id") int review_id,
                                       @Param("user_id") int user_id);

    // 리뷰 좋아요 총 수
    public int getReviewTotalLikes(@Param("review_id") int review_id);

    // 리뷰 평균 별점(소수점 첫째 자리까지 반올림해서 반환)
    public double getAverageRating(@Param("rental_item_id") int rental_item_id);

    // 유저 정보 가져오기
    public Map<String,Object> getUserInfoBySessionId(int userId);

    // 가장 많이 대여한 상품 top4
    public List<RentalItemDto> getTopRentalItems();

    // 가장 많이 찜한 대여 상품 limit 6
    public List<RentalItemDto> getTopLikedItems();

    // 주문 상태 개수
    public int getRentalOrderStatusCount(String status);

    // 리뷰 작성 확인
    public int getUserReviewCount(@Param("order_id") int order_id,
                                  @Param("user_id") int user_id);

    // Gpt
    public void insertRentalChatGptReview(RentalChatGptReviewDto rentalChatGptReviewDto);
    public RentalChatGptReviewDto rentalGptReview(int comment_id);

    // 리뷰탭 정렬
    List<RentalReviewDto> getReviewsByCriteria(@Param("id") int id, @Param("sortOrder") String sortOrder);

}
