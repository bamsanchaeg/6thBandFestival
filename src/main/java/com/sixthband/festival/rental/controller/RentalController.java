package com.sixthband.festival.rental.controller;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.rental.dto.*;
import com.sixthband.festival.rental.service.RentalService;
import com.sixthband.festival.util.Utils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // 상품 메인
    @GetMapping("preview")
    public String preview(Model model) {

        // 가장 많이 대여한 상품 top3
        List<RentalItemDto> topRentalItems = rentalService.getTopRentalItems();
        model.addAttribute("topItems", topRentalItems);

        // 가장 많이 찜한 대여 상품 limit
        List<RentalItemDto> topLikedItems = rentalService.getTopLikedItems();
        model.addAttribute("topLikedItems", topLikedItems);

        return "rental/preview";
    }

    // 상품 리스트
    @GetMapping("list")
    public String list(Model model){

        List<Map<String, Object>> rentalItemList = rentalService.getAllRentalItemsAll();
        model.addAttribute("rentalItemList", rentalItemList);

        return "rental/list";
    }

    // 상품 상세
    @GetMapping("details")
    public String details(Model model, @RequestParam("id") int id, @RequestParam(value = "review_id", required = false) Integer review_id, HttpSession session){

        // 대여 상품 정보 조회, 모델에 추가
        Map<String, Object> rentalItemInfo = rentalService.getRentalItemById(id);
        model.addAttribute("rentalItemInfo", rentalItemInfo);

        // 상세 이미지 목록 조회, 모델에 추가
        List<RentalDetailImageDto> detailImages = rentalService.getImagesByRentalItemId(id);
        //System.out.println("Detail Images count: " + detailImages.size());
        model.addAttribute("detailImages", detailImages);

        // 찜하기 수
        int totalLikes = rentalService.totalLikes(id);
        model.addAttribute("totalLikes", totalLikes);

        // 로그인 확인, 찜 체크
        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        if(userDto != null) {
            int userPk = userDto.getId();
            // 해당 글에 회원이 찜을 했는지 확인
            boolean isLiked = rentalService.getIsLiked(id, userPk);
            model.addAttribute("isLiked", isLiked);
            //System.out.println(isLiked);

          if(review_id != null) {
              boolean isReviewLiked = rentalService.getUserReviewLikeStatus(review_id, userPk);
            model.addAttribute("isReviewLiked", isReviewLiked);

            // 리뷰 좋아요 카운트 추가
              int reviewLikeCount = rentalService.reviewTotalLikes(review_id);
              model.addAttribute("reviewLikeCount", reviewLikeCount);
          } else {
              model.addAttribute("isReviewLiked", false);
              model.addAttribute("reviewLikeCount", 0);
          }


        } else {
            // 비회원인 경우 찜 여부를 false로 설정
            model.addAttribute("isLiked", false);
            model.addAttribute("isReviewLiked", false); // 비회원인 경우 리뷰 좋아요도 false
            model.addAttribute("reviewLikeCount", 0);
        }

        return "rental/details";
    }

    // 찜, 찜 취소
    @PostMapping("toggleLike")
    public String toggleLike(HttpSession session, RentalLikeDto params){

        UserDto userDto = (UserDto) session.getAttribute("loginUser");

        // 로그인 x 찜 누르면 로그인 페이지로
        if(userDto == null){
//            return "redirect:/user/loginPage";
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        params.setUser_id(userDto.getId());

       boolean isLiked = rentalService.getIsLiked(params.getItem_id(), userDto.getId());
       if(isLiked) {
           rentalService.deleteLike(params); // 찜 취소
       } else {
           rentalService.insertLike(params); // 찜
       }

//        return "redirect:/rental/details?id=" + params.getItem_id();
        return "redirect:" + Utils.FESTIVAL_URL + "/rental/details?id=" + params.getItem_id();
    }

    @GetMapping("order")
    public String order(Model model, RentalOrderDto params) {

        // 대여 상품 정보 조회, 모델에 추가
        Map<String, Object> rentalItemInfo = rentalService.getRentalItemById(params.getRental_item_id());
        // rentalItemInfo 맵에서 RentalItemDto 객체 가져와서 rentalItemDto 변수에 할당
        // 가져온 값 Object 타입, RentalItemDto 타입으로 캐스팅
        RentalItemDto rentalItemDto = (RentalItemDto) rentalItemInfo.get("rentalItemDto");
        // rentalItemDto 객체에서 가격 가져옴, 이거 사용해서 총 금액 계산
        int cost = rentalItemDto.getPrice();
        model.addAttribute("rentalItemInfo", rentalItemInfo);

        // 상세 이미지 목록 조회, 모델에 추가
        List<RentalDetailImageDto> detailImages = rentalService.getImagesByRentalItemId(params.getRental_item_id());
        // System.out.println("Detail Images count: " + detailImages.size());
        model.addAttribute("detailImages", detailImages);

        int count = params.getCount();
        int rental_days = params.getRental_days();
        String payment_method = params.getPayment_method();
        int total_price = cost * count;

        model.addAttribute("count", count);
        model.addAttribute("rental_days", rental_days);
        model.addAttribute("cost", cost);
        model.addAttribute("payment_method", payment_method);
        model.addAttribute("total_price", total_price);

        return "rental/order";
    }

    @GetMapping("payment-success")
    public String paymentSuccess(Model model, RentalOrderDto params){

        model.addAttribute("payment_method", params.getPayment_method());
        model.addAttribute("total_price", params.getTotal_price());

        return "rental/payment-success";
    }

    @PostMapping("payment-success")
    public String processPayment(Model model, HttpSession session, RentalOrderDto params){

        UserDto orderUser = (UserDto) session.getAttribute("loginUser");

        if(orderUser == null){
//            return "redirect:/user/loginPage";
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        // 대여 상품 정보 조회, 모델에 추가
        Map<String, Object> rentalItemInfo = rentalService.getRentalItemById(params.getRental_item_id());
        // rentalItemInfo 맵에서 RentalItemDto 객체 가져와서 rentalItemDto 변수에 할당
        // 가져온 값 Object 타입, RentalItemDto 타입으로 캐스팅
        RentalItemDto rentalItemDto = (RentalItemDto) rentalItemInfo.get("rentalItemDto");
        // rentalItemDto 객체에서 가격 가져옴, 이거 사용해서 총 금액 계산
        int cost = rentalItemDto.getPrice();
        model.addAttribute("rentalItemInfo", rentalItemInfo);

        int count = params.getCount();
        int rental_days = params.getRental_days();
        String payment_method = params.getPayment_method();
        int total_price = cost * count;

        model.addAttribute("count", count);
        model.addAttribute("rental_days", rental_days);
        model.addAttribute("cost", cost);
        model.addAttribute("payment_method", payment_method);
        model.addAttribute("total_price", total_price);
        model.addAttribute("rental_location", rentalItemDto.getRental_location());

        // 대여 내역 정보 저장
        params.setUser_id(orderUser.getId());
        params.setTotal_price(total_price);
        params.setRental_order_status("completed");
        params.setReceiver(orderUser.getName());
        params.setPayment_method(payment_method);
        rentalService.insertRentalOrder(params);

        // 저장된 대여 내역 정보 모델에 추가
        model.addAttribute("orderData", params);

        System.out.println("Saved RentalOrder ID: " + params.getId());

        return "rental/payment-success";
    }

    @GetMapping("myRentals")
    public String myRentals(Model model, HttpSession session){

        UserDto orderUser = (UserDto) session.getAttribute("loginUser");

        if(orderUser == null){
//            return "redirect:/user/loginPage";
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

         List<Map<String, Object>> orderList  = rentalService.getOrderListByUserId(orderUser.getId());
         model.addAttribute("orderList", orderList);


        return "rental/myRentals";
    }

    @GetMapping("myRentalsInfo")
    public String myRentalsInfo(@RequestParam("id") int id, Model model, HttpSession session){

        UserDto orderUser = (UserDto) session.getAttribute("loginUser");

        if(orderUser == null){
//            return "redirect:/user/loginPage";
            return "redirect:" + Utils.FESTIVAL_URL + "/user/loginPage";
        }

        // 주문 상세 정보 가져오기
        Map<String, Object> rentalOrderDetail = rentalService.getOrder(id);
        model.addAttribute("rentalOrderDetail", rentalOrderDetail);

        // 해당 주문에 대한 리뷰 작성 여부 확인
        boolean isReviewWritten = rentalService.isReviewWritten(id, orderUser.getId());
        model.addAttribute("isReviewWritten", isReviewWritten);

        return "rental/myRentalsInfo";
    }

    @GetMapping("review")
    public String review(@RequestParam("id") int id, Model model){
        System.out.println("Review Page ID: " + id);
        Map<String, Object> rentalOrderDetail = rentalService.getOrder(id);
        model.addAttribute("rentalOrderDetail", rentalOrderDetail);
        model.addAttribute("image", rentalOrderDetail.get("image"));

        return "rental/review";
    }
}
