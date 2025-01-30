package com.sixthband.festival.goods.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sixthband.festival.dto.UserDto;
import com.sixthband.festival.goods.dto.GoodsDiscountDto;
import com.sixthband.festival.goods.dto.GoodsDto;
import com.sixthband.festival.goods.dto.GoodsOrderDto;
import com.sixthband.festival.goods.dto.GoodsShopperbagDto;
import com.sixthband.festival.goods.dto.GoodsWishDto;
import com.sixthband.festival.goods.service.GoodsService;
import com.sixthband.festival.util.Utils;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    //사용자 - App main
    @RequestMapping("mainPage")
    public String goodsMainPage(){
        return "goods/goodsMainPage";
    }

    //굿즈리스트
    @RequestMapping("goodsList")
    public String mainPage(HttpSession session, Model model, @RequestParam(value="p", defaultValue ="1")int p){

        if(session.getAttribute("loginUser") == null){
            return "goods/goodsList";
        }

        List<Map<String, Object>> selectGoodsList  = goodsService.selectGoodsList(p);
        model.addAttribute("selectGoodsList", selectGoodsList);

        return "goods/goodsList"; 
    }

    //상세페이지
    @RequestMapping("goodsDetailPage")
    public String goodsDetailPage(Model model, @RequestParam("id")int id, HttpSession session){

        //대표이미지, 상품명, 가격출력
        Map<String, Object> goodsDetail  = goodsService.getGoodsDetail(id);
        model.addAttribute("goodsDetail", goodsDetail);

        //상세이미지 출력
        List<Map<String, Object>> selectImageWithGoodsList = goodsService.selectImageWithGoodsList(id);
        model.addAttribute("selectImageWithGoodsList", selectImageWithGoodsList);

        //할인 정보 추가
        List<GoodsDiscountDto> discounts = goodsService.selectDiscountsByGoodsId(id);
        model.addAttribute("discounts", discounts);

        if(session.getAttribute("loginUser") == null){
            return "goods/goodsDetailPage";

        } else {
            GoodsWishDto goodsWishDto = new GoodsWishDto();
            goodsWishDto.setGoods_id(id);
    
            UserDto userDto = (UserDto) session.getAttribute("loginUser");
            goodsWishDto.setUser_id(userDto.getId());
            boolean isLiked = goodsService.getWishLiked(goodsWishDto); 

            model.addAttribute("isLiked", isLiked);
            return "goods/goodsDetailPage";
        }
        
    }

    //goodsDetailPage에서 장바구니 or 바로결제 보내기
    @RequestMapping("goodsCartProcess")
    public String goodsBuyingProcess(HttpSession session , GoodsShopperbagDto goodsShopperbagDto , @RequestParam("cart") String cart, Model model, GoodsDto goodsDto){

        if (session.getAttribute("loginUser") == null) {
            return "redirect:"+ Utils.FESTIVAL_URL +"/user/loginPage";
        }

        UserDto userDto = (UserDto) session.getAttribute("loginUser");

        //유저 정보를 모델에 추가
        if (userDto != null) {
            model.addAttribute("user_name", userDto.getName());
            model.addAttribute("user_email", userDto.getEmail());
            model.addAttribute("user_phone", userDto.getPhone());
        }
    
        //장바구니
        if(cart.equals("cart")){
            goodsShopperbagDto.setUser_id(userDto.getId());
            goodsService.insertGoodsShopperbag(goodsShopperbagDto);
            return "redirect:"+ Utils.FESTIVAL_URL +"/goods/goodsCart?id=" + userDto.getId();

        } else{ 
            //결제페이지
            goodsShopperbagDto.setUser_id(userDto.getId());
    
            Map<String ,Object> goodsOrder = goodsService.getGoodsDetail(goodsShopperbagDto.getGoods_id());
            model.addAttribute("goodsOrder", goodsOrder);
            
            GoodsDto getGoodsDto = (GoodsDto) goodsOrder.get("goodsDetail");

            String goods_title = getGoodsDto.getGoods_title();
            int price = getGoodsDto.getPrice();
            int item_count = goodsShopperbagDto.getItem_count();
            String image = getGoodsDto.getImage(); 
            int id = getGoodsDto.getId();

            model.addAttribute("goods_title", goods_title);
            model.addAttribute("price", price);
            model.addAttribute("item_count", item_count);
            model.addAttribute("image", image);
            model.addAttribute("id", id);
            
            int totalPrice = getGoodsDto.getPrice() * goodsShopperbagDto.getItem_count();
            int totalPayment = totalPrice + 3000;
    
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("totalPayment", totalPayment);
    
            return "goods/goodsOrderPayment";
        }

    }

    //장바구니
    @RequestMapping("goodsCart")
    public String goodsCart(@RequestParam("id") int id, Model model, HttpSession session){

        model.addAttribute("id", id);

        UserDto userDto = (UserDto) session.getAttribute("loginUser");

        //장바구니 리스트 가져오기
        List<Map<String, Object>> selectGoodsList = goodsService.selectGoodsListByCart(userDto.getId());
        model.addAttribute("selectGoodsList", selectGoodsList);

        //총 상품금액
        int totalGoodsPrice = goodsService.calculateTotalGoodsPrice(selectGoodsList);
        //총 상품금액 + 배송비
        int totalOrderAmount = goodsService.calculateTotalOrderAmount(totalGoodsPrice);

        model.addAttribute("totalGoodsPrice", totalGoodsPrice);
        model.addAttribute("totalOrderAmount", totalOrderAmount);
    
        return "goods/goodsCart";
   }

   //장바구니 아이템 삭제
   @RequestMapping("deleteCartProcess")
   public String deleteCartProcess(@RequestParam("goods_id") int goods_id, HttpSession session){

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        int user_id = userDto.getId();

        goodsService.deleteCartByGoods(goods_id, user_id);
    
        return "redirect:"+ Utils.FESTIVAL_URL +"/goods/goodsCart?id=" + user_id;
    }

    //위시리스트
    @RequestMapping("wishList")
    public String wishList(HttpSession session, @ModelAttribute GoodsWishDto params){

        UserDto userDto = (UserDto) session.getAttribute("loginUser");

        if(userDto == null){
            return "redirect:"+ Utils.FESTIVAL_URL +"/user/loginPage";
        }

        params.setUser_id(userDto.getId());

        boolean isLiked = goodsService.getWishLiked(params);
        if(isLiked) {
            goodsService.deleteWishList(params); //위시취소
        } else { 
            goodsService.insertWishList(params); //위시
        }

        return "redirect:"+ Utils.FESTIVAL_URL +"/goods/goodsDetailPage?id=" + params.getGoods_id();
    }

    //장바구니에서 결제페이지로 보내기
    @RequestMapping("goodsOrderPayment")
    public String goodsOrderPayment(Model model, @RequestParam("goods_title") String goods_title, 
                                                @RequestParam("price") int price, 
                                                @RequestParam("item_count") int item_count,
                                                @RequestParam("image") String image,
                                                @RequestParam("id") int id,
                                                GoodsOrderDto goodsOrderDto,
                                                HttpSession session)
    {
        UserDto loginUser = (UserDto)session.getAttribute("loginUser");
        model.addAttribute("user_name", loginUser.getName());
        model.addAttribute("user_phone", loginUser.getPhone());

        if (loginUser != null && loginUser.getEmail() != null) {
            String[] emailParts = loginUser.getEmail().split("@");
            model.addAttribute("user_email_prefix", emailParts[0]);
            model.addAttribute("user_email_suffix", emailParts.length > 1 ? emailParts[1] : "");
        }
    
        Map<String ,Object> goodsOrder = goodsService.getGoodsDetail(goodsOrderDto.getGoods_id());
        model.addAttribute("goodsOrder", goodsOrder);
        model.addAttribute("goods_title", goods_title);
        model.addAttribute("price", price);
        model.addAttribute("item_count", item_count);
        model.addAttribute("image", image);
        model.addAttribute("id", id);

        int totalPrice = price * item_count;
        int totalPayment = totalPrice + 3000;

        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalPayment", totalPayment);
       
        return "goods/goodsOrderPayment";
    }

    //주문 프로세스
    @RequestMapping("ProductOrderProcess")
    public String ProductOrderProcess(GoodsOrderDto goodsOrderDto, HttpSession session, Model model){

        UserDto userDto = (UserDto)session.getAttribute("loginUser");
        goodsOrderDto.setUser_id(userDto.getId());

        goodsService.insertGoodsOrder(goodsOrderDto);

        int totalOrderAmount = (goodsOrderDto.getOrder_price() + 3000);
        goodsOrderDto.setOrder_price(totalOrderAmount);
        model.addAttribute("totalOrderAmount", totalOrderAmount);

        model.addAttribute("goodsOrderDto", goodsOrderDto);

        return "goods/goodsPaymentComplete";
    }
    
    //결제완료
    @RequestMapping("PaymentComplete")
    public String goodsPaymentComplete(){

        return "goods/goodsPaymentComplete";
    }

    //결제내역확인
    @RequestMapping("MyPayment")
    public String goodsMyPayment(Model model, HttpSession session){

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        
        if (userDto == null){
            return "redirect:"+ Utils.FESTIVAL_URL +"/user/loginPage";
        }

        int user_id = userDto.getId();

        List<Map<String, Object>> goodsOrderList = goodsService.selectGoodsOrderList(user_id);
        model.addAttribute("goodsOrderList", goodsOrderList);

        Map<String, Integer> statusCounts = goodsService.countOrdersByStatusByUserId(user_id);
        model.addAttribute("statusCounts", statusCounts);

        return "goods/goodsMyPayment";
    }

    //결제내역상세
    @RequestMapping("MyPaymentDetail")
    public String goosMyPaymentDetail(Model model, HttpSession session, @RequestParam("id") int id){

        UserDto userDto = (UserDto) session.getAttribute("loginUser");
        
        if (userDto == null){
            return "redirect:"+ Utils.FESTIVAL_URL +"/user/loginPage";
        }

        int user_id = userDto.getId();

        Map<String, Object> orderDetail = goodsService.selectGoodsOrderDetail(id, user_id);
        if(orderDetail == null){
            return "redirect:"+ Utils.FESTIVAL_URL +"/goods/mainPage";
        }

        GoodsDto goodsDto = (GoodsDto) orderDetail.get("goodsDto");
        GoodsOrderDto goodsOrderDto = (GoodsOrderDto) orderDetail.get("goodsOrderDto");
        boolean isReviewed = goodsService.isGoodsReviewed(goodsOrderDto.getId());

        int price = goodsDto.getPrice();
        int item_count = goodsOrderDto.getGoods_order_count();
        int totalPrice = price * item_count;
        int totalPayment = totalPrice + 3000;

        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("totalPayment", totalPayment);
        model.addAttribute("isReviewed", isReviewed);

        return "goods/goosMyPaymentDetail";
    }

    //리뷰
    @RequestMapping("review")
    public String goodsReview(GoodsOrderDto params, Model model,@RequestParam("id") int id){
        model.addAttribute("id", id);
        return "goods/goodsReview";
    }    

    //위시리스트 모아보기
    @RequestMapping("myWishList")
    public String goodsWishListPage(){
        return "goods/goodsWishList";
    }

}


