package com.sixthband.festival.ticket.controller;

import com.sixthband.festival.ticket.dto.TicketInfoDto;
import com.sixthband.festival.ticket.service.TicketService;
import com.sixthband.festival.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/admin/ticket")
public class AdminTicketController {

    @Autowired
    private TicketService ticketService;

    @RequestMapping("")
    public String ticketAdminPage(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        List<Map<String, Object>> ticketList = ticketService.getAllTicketInfoList((page - 1) * 15);
        model.addAttribute("ticketList", ticketList);

        int ticketsCount = ticketService.countTickets();
//        Math.ceil.. 올림하여 정수로 반환 (30/20 하면 1.5나오는데 소수점 버리면 1페이지만 나옴)
        int pageNumber = (int) Math.ceil(ticketsCount/15.0);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("page", page);

        int startPage = ((page - 1) / 5) * 5 + 1;
        int endPage = ((page - 1) / 5 + 1) * 5;

        if (endPage > pageNumber) {
            endPage = pageNumber;
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("allCount", ticketService.getAllTicketCount());

        return "admin/ticket/ticketAdminPage";
    }

//    티켓 정보 삭제
    @RequestMapping("deleteTicket")
    public String deleteTicket(@RequestParam("id") int id) {
        ticketService.deleteTicketInfo(id);
        return "redirect:" + Utils.FESTIVAL_URL + "/admin/ticket";
    }

    @RequestMapping("adminInfoPage")
    public String ticketAdminInfoPage(Model model, @RequestParam("id") int id) {

        model.addAttribute("ticketInfo", ticketService.getTicketInfoById(id));
        model.addAttribute("purchaseQuantity", ticketService.getTicketPurchaseQuantity(id));
        return "admin/ticket/ticketAdminInfoPage";
    }

    @RequestMapping("bookingPage")
    public String bookingPage(Model model, @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value="isPayment", required=false) String isPayment,
                              @RequestParam(value="searchOption", required=false) String searchOption,
                              @RequestParam(value="searchWord", required=false) String searchWord) {
        model.addAttribute("bookingList", ticketService.getBookingList((page - 1) * 11, isPayment, searchOption, searchWord));
        int bookingCount = ticketService.countBooking(isPayment, searchOption, searchWord);
//        Mth.ceil.. 올림하여 정수로 반환 (30/20 하면 1.5나오는데 소수점 버리면 1페이지만 나옴)
        int pageNumber = (int) Math.ceil(bookingCount/11.0);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("page", page);

        int startPage = ((page - 1) / 5) * 5 + 1;
        int endPage = ((page - 1) / 5 + 1) * 5;

        if (endPage > pageNumber) {
            endPage = pageNumber;
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        model.addAttribute("bookingCount", bookingCount);

        return "admin/ticket/bookingPage";
    }

    @RequestMapping("bookingInfoPage")
    public String bookingInfoPage(@RequestParam("id") int id, Model model) {
        model.addAttribute("bookingInfo", ticketService.getAdminBookingById(id));

        return "admin/ticket/bookingInfoPage";
    }

    @RequestMapping("reviewAdminPage")
    public String reviewAdminPage(Model model) {
        model.addAttribute("reviewCount", ticketService.getAllReviewCount());
        return "admin/ticket/reviewAdminPage";
    }

    @RequestMapping("statisticsPage")
    public String statisticsPage() {
        return "admin/ticket/ticketStatisticsPage";
    }

    @RequestMapping("reviewAdminInfoPage")
    public String reviewAdminInfoPage(@RequestParam("id") int id, Model model) {
        model.addAttribute("reviewInfo", ticketService.getReviewInfoById(id));
        model.addAttribute("reviewImages", ticketService.getReviewImagesByReviewId(id));
        model.addAttribute("likeCount", ticketService.getReviewLikeCount(id));
        return "admin/ticket/reviewAdminInfoPage";
    }

    @RequestMapping("deleteReview")
    public String deleteReview(@RequestParam("id") int id) {
        ticketService.deleteReviewById(id);
        return "redirect:" + Utils.FESTIVAL_URL + "/admin/ticket/reviewAdminPage";
    }

}