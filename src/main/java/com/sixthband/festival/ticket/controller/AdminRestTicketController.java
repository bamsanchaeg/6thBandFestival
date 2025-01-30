package com.sixthband.festival.ticket.controller;

import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.ticket.dto.TicketEarlybirdDiscountDto;
import com.sixthband.festival.ticket.dto.TicketInfoDto;
import com.sixthband.festival.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/ticket")
public class AdminRestTicketController {

    @Autowired
    private TicketService ticketService;

//    등록된 페스티벌 정보 가져오기
    @RequestMapping("getFestivalAllList")
    public RestResponseDto getFestivalAllList() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("festivalList", ticketService.getAllFestivalList());
//      /api/admin/ticket/getFestivalAllList
        return restResponseDto;
    }

//    특정 페스티벌 정보 가져오기
    @RequestMapping("getFestivalById")
    public RestResponseDto getFestivalById(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("festivalInfo", ticketService.getFestivalById(id));
//      /api/admin/ticket/getFestivalById?id=3
        return restResponseDto;
    }

//    티켓 등록
    @RequestMapping("createTicketInfo")
    public RestResponseDto createTicketInfo(TicketInfoDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.createTicketInfo(params);

//      /api/admin/ticket/createTicketInfo
        return restResponseDto;
    }

//    할인 등록
    @RequestMapping("createDiscount")
    public RestResponseDto createDiscount(TicketEarlybirdDiscountDto params) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.createDiscount(params);

//      /api/admin/ticket/createDiscount
        return restResponseDto;
    }

//    할인 정보 삭제
    @RequestMapping("deleteDiscount")
    public RestResponseDto deleteDiscount(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.deleteDiscount(id);
//      /api/admin/ticket/deleteDiscount
        return restResponseDto;
    }

//    할인 정보 리로드
    @RequestMapping("getDiscountInfo")
    public RestResponseDto getDiscountInfo(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("discount", ticketService.getEarlybirdDiscount(id));
//      /api/admin/ticket/getDiscountInfo
        return restResponseDto;
    }

//    리뷰 관리
    @RequestMapping("getReviewList")
    public RestResponseDto getReviewList(@RequestParam(value = "page", defaultValue = "1") int page) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reviewList", ticketService.getAdminReviewAll((page - 1) * 15));
        restResponseDto.add("reviewCount", ticketService.countReview());
//      /api/admin/ticket/getReviewList
        return restResponseDto;
    }

//    리뷰 하나
    @RequestMapping("getReviewListById")
    public RestResponseDto getReviewListById(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reviewInfo", ticketService.getReviewInfoById(id));
        restResponseDto.add("reviewImages", ticketService.getReviewImagesByReviewId(id));
//      /api/admin/ticket/getReviewListById
        return restResponseDto;
    }

//    리뷰 삭제
    @RequestMapping("deleteReview")
    public RestResponseDto deleteReview(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        ticketService.deleteReviewById(id);
//      /api/admin/ticket/deleteReview
        return restResponseDto;
    }

    @RequestMapping("qrCheck")
    public RestResponseDto qrCheck(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("qrInfo", ticketService.getAdminBookingById(id));
//      /api/admin/ticket/qrCheck
        return restResponseDto;
    }

//    그래프
    @RequestMapping("getDailySales")
    public RestResponseDto getDailySales() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("dailySales", ticketService.getDailySales());
//      /api/admin/ticket/getDailySales
        return restResponseDto;
    }

    @RequestMapping("getMonthlySales")
    public RestResponseDto getMonthlySales() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("monthlySales", ticketService.getMonthlySales());
//      /api/admin/ticket/getMonthlySales
        return restResponseDto;
    }

    @RequestMapping("getAnnualSales")
    public RestResponseDto getAnnualSales() {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("annualSales", ticketService.getAnnualSales());
//      /api/admin/ticket/getAnnualSales
        return restResponseDto;
    }

    @RequestMapping("getDayTypeRatio")
    public RestResponseDto getDayTypeRatio(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("oneDay", ticketService.getOneDayRatio(id));
        restResponseDto.add("allDay", ticketService.getAllDayRatio(id));
//      /api/admin/ticket/getDayTypeRatio
        return restResponseDto;
    }

    @RequestMapping("getOneDaySales")
    public RestResponseDto getOneDaySales(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("age", ticketService.getOneDayAge(id));
        restResponseDto.add("female", ticketService.getOneDayGenderF(id));
        restResponseDto.add("male", ticketService.getOneDayGenderM(id));
//      /api/admin/ticket/getOneDaySales
        return restResponseDto;
    }

    @RequestMapping("getAllDaySales")
    public RestResponseDto getAllDaySales(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("age", ticketService.getAllDayAge(id));
        restResponseDto.add("female", ticketService.getAllDayGenderF(id));
        restResponseDto.add("male", ticketService.getAllDayGenderM(id));
//      /api/admin/ticket/getAllDaySales
        return restResponseDto;
    }

//    구매 상세 정보
    @RequestMapping("getBookingAdmin")
    public RestResponseDto getBookingAdmin(@RequestParam("id") int id) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("bookingInfo", ticketService.getAdminBookingById(id));
    //      /api/admin/ticket/getBookingAdmin
        return restResponseDto;
    }

//    리뷰 상세 정보
//    @RequestMapping("getReviewInfoAdmin")
//    public RestResponseDto getReviewInfoAdmin(@RequestParam("id") int id) {
//        RestResponseDto restResponseDto = new RestResponseDto();
//        restResponseDto.setResult("success");
//
//        restResponseDto.add("reviewInfo", ticketService.getReviewInfoById(id));
////      /api/admin/ticket/getReviewInfoAdmin
//        return restResponseDto;
//    }
}
