package com.sixthband.festival.ticket.mapper;

import com.sixthband.festival.information.dto.FestivalDto;
import com.sixthband.festival.ticket.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

@Mapper
public interface TicketSqlMapper {

//    페스티벌 리스트 가져오기
    public List<FestivalDto> selectAllFestivalList();
//    페스티벌 하나 가져오기
    public FestivalDto selectFestivalById(int id);
//    티켓 정보 insert
    public void insertTicket(TicketInfoDto ticketInfoDto);
//    티켓 리스트 가져오기
    public List<Map<String, Object>> selectAllTicketList(@Param("page") int page);
//    티켓 하나 가져오기
    public Map<String, Object> selectTicketById(int id);
//    티켓 삭제
    public void deleteTicketById(int id);
//    할인 등록
    public void insertDiscount(TicketEarlybirdDiscountDto ticketEarlybirdDiscountDto);
//    할인 삭제
    public void deleteDiscountById(int id);
//    티켓 구매 수량 구하기
    public int selectTicketPurchaseQuantity(int id);
//    할인 적용
    public TicketEarlybirdDiscountDto selectEarlybirdDiscount(int id);
//    남은 수량 update
    public void updateRemainingQuantity(@Param("count") int count, @Param("id") int id);
//    관리자 티켓 구매 내역 리스트
    public List<Map<String, Object>> selectBookingList(@Param("page") int page,
                                                       @Param("isPayment") String isPayment,
                                                       @Param("searchOption") String searchOption,
                                                       @Param("searchWord") String searchWord);
//    관리자 티켓 구매 내역
    public Map<String, Object> selectAdminBookingById(int id);
//    관리자 리뷰관리
    public List<Map<String, Object>> selectAdminReviewAll(@Param("page") int page);


//    페스티벌 id로 티켓 1일권 정보 가져오기
    public List<TicketInfoDto> selectTicketOneDayListByFestivalId(int festival_id);
//    페스티벌 id로 티켓 3일권 정보 가져오기
    public TicketInfoDto selectTicketAllDayByFestivalId(int festival_id);
//    페스티벌 1일권 티켓 최소가
    public int selectMinYouthPriceByOneDay(int festival_id);
//    페스티벌 3일권 티켓 최소가
    public int selectMinYouthPriceByAllDay(int festival_id);
//    티켓 구매 insert
    public void insertTicketBooking(TicketBookingDto ticketBookingDto);

//    티켓 구매 내역(회원)
    public List<Map<String, Object>> selectBookingByUserId(int user_id);
//    티켓 내역2
    public Map<String, Object> selectBookingById(int id);
//    티켓 구매 완료(회원기준)
    public List<Map<String, Object>> selectIsUseNBookingByUserId(int user_id);
//    티켓 사용 완료(회원기준)
    public List<Map<String, Object>> selectIsUseYBookingByUserId(int user_id);

//    오늘 기준 제일 빠른 페스티벌
    public FestivalDto selectMinStartingDateFestivalInfo();
//    티켓 구매자 판별
    public int isBookingTickets(int user_id);
//    구매한 티켓 정보
    public List<Map<String, Object>> selectBookingDataByUserId(int user_id);
//    리뷰 등록
    public void insertFestivalReview(TicketReviewDto ticketReviewDto);
//    리뷰 이미지 등록
    public void insertFestivalReviewImages(TicketReviewImageDto ticketReviewImageDto);
//    리뷰 목록 가져오기
    public List<Map<String, Object>> selectAllReviewList(
                                                        @Param("page") int page,
                                                        @Param("sort") int sort,
                                                        @Param("searchWord") String searchWord
                                                        );
//    리뷰 하나 가져오기
    public Map<String, Object> selectReviewInfoById(int id);
//    리뷰 아이디로 이미지 가져오기
    public List<TicketReviewImageDto> selectReviewImagesByReviewId(int id);

//    리뷰 좋아요 등록
    public void insertReviewLike(TicketReviewLikeDto ticketReviewLikeDto);
//    리뷰 좋아요 확인
    public int isReviewLike(TicketReviewLikeDto ticketReviewLikeDto);
//    리뷰 좋아요 삭제
    public void deleteReviewLikeByUserIdAndReviewId(TicketReviewLikeDto ticketReviewLikeDto);
//    리뷰 좋아요 개수
    public int selectReviewLikeCount(int review_id);
//    조회수 처리
    public void updateReviewReadCount(int id);
//    리뷰 삭제
    public void deleteReviewById(int id);
//    리뷰 수정
    public void updateReviewById(TicketReviewDto ticketReviewDto);
//    좋아요 top3
    public List<Map<String, Object>> selectReviewListLikeCountBestThree();
//    현재 글 기준 앞 뒤 5개
    public List<Map<String, Object>> selectReviewListFiveById(int id);

//    마이페이지에 자기가 쓴 리뷰 보이게
    public List<Map<String, Object>> selectReviewListByUserId(int id);
//    마이페이지에 자기가 좋아요한 리뷰 보이게
    public List<Map<String, Object>> selectLikeReviewListByUserId(int id);

//    qr 입장확인
    public void updateIsUseByBookingId(int id);

//    페이징
    public int countTickets();
    public int countBooking(@Param("isPayment") String isPayment,
                            @Param("searchOption") String searchOption,
                            @Param("searchWord") String searchWord);
    public int countReview();

//    결제 상태
    public void updatePaymentStatusY(int id);
    public void insertTicketPayment(TicketPaymentDto ticketPaymentDto);

//    그래프
    public List<Map<String, Object>> selectDailySales();
    public List<Map<String, Object>> selectMonthlySales();
    public List<Map<String, Object>> selectAnnualSales();

    public int selectDayTypeOneDay(int id);
    public int selectDayTypeAllDay(int id);

    public int selectOneDaySales(int id);
    public int selectAllDaySales(int id);

    public List<Map<String, Object>> selectOneDayAge(int id);
    public int selectOneDayGenderF(int id);
    public int selectOneDayGenderM(int id);

    public List<Map<String, Object>> selectAllDayAge(int id);
    public int selectAllDayGenderF(int id);
    public int selectAllDayGenderM(int id);

//    보충
    public int selectAllTicketCount();
    public int selectAllBookingCount();
    public int selectAllReviewCount();

//    gpt 리뷰 요약용
    public TicketReviewDto selectGptReviewById(int id);

//    리뷰 개수
    public int selectTicketReviewCount();
//    메인 대시보드용
    public Map<String,Object> selectMainDashBoardTickets();

//    검색된 리뷰 개수
    public int selectSearchCount(@Param("searchWord") String searchWord);
}