package com.sixthband.festival.goods.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.goods.dto.GoodsCategoryDto;
import com.sixthband.festival.goods.dto.GoodsDetailImageDto;
import com.sixthband.festival.goods.dto.GoodsDto;
import com.sixthband.festival.goods.dto.GoodsOrderDto;
import com.sixthband.festival.goods.dto.GoodsReviewDto;
import com.sixthband.festival.goods.service.GoodsService;
import com.sixthband.festival.util.ImageUtil;

@Controller
@RequestMapping("/admin/goods")
public class AdminGoodsController {

    @Autowired
    private GoodsService goodsService;

    //Admin - 메인페이지
    @RequestMapping("")
    public String adminGoodsMainPage(Model model, @RequestParam(value="p", defaultValue ="1") int p){

        List<Map<String, Object>> selectGoodsList  = goodsService.selectGoodsList(p);
        model.addAttribute("selectGoodsList", selectGoodsList);

        int countGoods = goodsService.countGoodsAll();
        model.addAttribute("countGoods", countGoods);

        int totalCount = goodsService.getGoodsListTotalCount();

        int lastPageNumber = (int) Math.ceil(totalCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);

        model.addAttribute("currentPage", p);

        int startPage = ((p-1)/5)*5+1;
        int endPage = ((p-1)/5+1)*5;
            if(endPage > lastPageNumber) {
                endPage = lastPageNumber;
            }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/goods/adminProductList";
    }

    //상품상세
    @RequestMapping("adminProductDetail")
    public String adminProductDetail(Model model, @RequestParam("id") int id){
        
        List<GoodsDto> goodsDetail = goodsService.getGoodsDetailAll(id);
        model.addAttribute("goodsDetail", goodsDetail);
        
        return "admin/goods/adminProductDetail";
    }

    //상품등록
    @RequestMapping("adminProductRegistration")
    public String adminProductRegistration(Model model){

        List<GoodsCategoryDto> category = goodsService.goodsCategory();
        model.addAttribute("goodsCategory", category);

        return "admin/goods/adminProductRegistration";
    }

    //상품등록 form
    @RequestMapping("adminProductRegistrationProcess")
    public String adminProductRegistrationProcess(GoodsDto goodsDto, @RequestParam(name="mainImage") MultipartFile mainImage, @RequestParam(name="uploadImage") MultipartFile[] uploadImage){
        String image = ImageUtil.saveImageAndReturnLocation(mainImage);
        goodsDto.setImage(image);
        goodsService.insertGoods(goodsDto);

        //이미지
        GoodsDetailImageDto goodsDetailImageDto = new GoodsDetailImageDto();
        List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(uploadImage);
        for (ImageDto imageDto : multiImage) {
            int goodsId = goodsDto.getId();
            goodsDetailImageDto.setGoods_id(goodsId);
            goodsDetailImageDto.setMultiple_image(imageDto.getLocation());
            goodsService.goodsDetailImage(goodsDetailImageDto);
        }

        return "redirect:/admin/goods";
    }

    //상품수정
    @RequestMapping("adminProductModification")
    public String adminProductModification(Model model, @RequestParam("id") int id){

        Map<String, Object> goodsDataMap = goodsService.selectGoodsCategoryAndGoods(id);
        model.addAttribute("goodsDataMap", goodsDataMap);

        List<GoodsCategoryDto> categoryListData = goodsService.goodsCategory();
        model.addAttribute("categoryListData", categoryListData);
        
        return "admin/goods/adminProductModification";
    }

    //상품수정프로세스
    @RequestMapping("adminProductModificationProcess")
    public String adminProductModificationProcess(GoodsDto goodsDto){
        goodsService.updateGoodsProduct(goodsDto);
        return "redirect:/admin/goods";
    }

    //상품삭제프로세스
    @RequestMapping("adminDeleteProductProcess")
    public String adminDeleteProductProcess(@RequestParam("id") int id){
        goodsService.deleteGoodsId(id);
        return "redirect:/admin/goods";
    }

    //주문리스트
    @RequestMapping("adminOrderList")
    public String getGoodsOrderList(Model model, @RequestParam(value="p", defaultValue ="1") int p) {

        List<GoodsOrderDto> orderList = goodsService.getGoodsOrderList(p);
        model.addAttribute("orderList", orderList);

        int countOrder = goodsService.countOrderAll();
        model.addAttribute("countOrder", countOrder);
        
        int totalCount = goodsService.getOrderListTotalCount();

        int lastPageNumber = (int) Math.ceil(totalCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);

        model.addAttribute("currentPage", p);

        int startPage = ((p-1)/5)*5+1;
        int endPage = ((p-1)/5+1)*5;

            if(endPage > lastPageNumber) {
                endPage = lastPageNumber;
            }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/goods/adminOrderList";
    }

    //주문리스트 상세페이지
    @RequestMapping("adminOrderListDetail")
    public String getGoodsOrderListByOrderId(@RequestParam("order_id") int order_id, Model model){

        GoodsOrderDto orderDetail = goodsService.getGoodsOrderListByOrderId(order_id);
        model.addAttribute("orderDetail", orderDetail);

        return "admin/goods/adminOrderListDetail";
    }

    //리뷰리스트
    @RequestMapping("adminReviewList")
    public String getGoodsReviewList(Model model, @RequestParam(value="p", defaultValue ="1") int p){
        
        List<GoodsReviewDto> reviewList = goodsService.getGoodsReviewListByGoodsAndUser(p);
        model.addAttribute("reviewList", reviewList);

        int countReviewAll = goodsService.countReviewAll();
        model.addAttribute("countReview", countReviewAll);
        
        int totalCount = goodsService.getReviewListTotalCount();
        int lastPageNumber = (int) Math.ceil(totalCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);
        model.addAttribute("currentPage", p);

        int startPage = ((p-1)/5)*5+1;
        int endPage = ((p-1)/5+1)*5;
            if(endPage > lastPageNumber) {
                endPage = lastPageNumber;
            }
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/goods/adminReviewList";
    }

    //리뷰리스트 상세페이지
    @RequestMapping("adminReviewListDetail")
    public String getReviewDetailList(Model model,@RequestParam("id") int id){
        GoodsReviewDto reviewDetail = goodsService.getReviewDetailAll(id);
        model.addAttribute("reviewDetail", reviewDetail);
        return "admin/goods/adminReviewListDetail";
    }

}
