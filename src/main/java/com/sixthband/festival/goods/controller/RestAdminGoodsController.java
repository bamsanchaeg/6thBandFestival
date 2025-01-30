package com.sixthband.festival.goods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.goods.dto.GoodsDetailImageDto;
import com.sixthband.festival.goods.dto.GoodsDiscountDto;
import com.sixthband.festival.goods.dto.GoodsDto;
import com.sixthband.festival.goods.dto.GoodsReviewRecommendDto;
import com.sixthband.festival.goods.service.GoodsService;
import com.sixthband.festival.util.ImageUtil;

@RestController
@RequestMapping("api/goods")
//Admin
public class RestAdminGoodsController {

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("registerGoods")
    public RestResponseDto registerGoodsProduct(GoodsDto params, @RequestParam(name="mainImage") MultipartFile mainImage, @RequestParam(name="uploadImage") MultipartFile[] uploadImage){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        //대표이미지
        String image = ImageUtil.saveImageAndReturnLocation(mainImage);
        params.setImage(image);
        goodsService.insertGoods(params);

        //상세이미지
        GoodsDetailImageDto goodsDetailImageDto = new GoodsDetailImageDto();
        List<ImageDto> multiImage = ImageUtil.saveImageAndReturnDtoList(uploadImage);
        for (ImageDto imageDto : multiImage) {
            int goodsId = params.getId();
            goodsDetailImageDto.setGoods_id(goodsId);
            goodsDetailImageDto.setMultiple_image(imageDto.getLocation());
            goodsService.goodsDetailImage(goodsDetailImageDto);
        }

        return restResponseDto;
    }

    //GoodsList
    @RequestMapping("getGoodsList")
    public RestResponseDto getGoodsList(@RequestParam(value="p", defaultValue ="1")int p){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("goodsList", goodsService.selectGoodsList(p));

        return restResponseDto;
    }

    //GoodsDetail
    @RequestMapping("getGoodsDetail")
    public RestResponseDto getGoodsDetailAll(@RequestParam("id") int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("goodsAll", goodsService.getGoodsDetailAll(id));
        restResponseDto.add("goodsDiscounts", goodsService.selectDiscountsByGoodsId(id));
        

        return restResponseDto;
    }

    //GoodsList count All
    @RequestMapping("countGoods")
    public RestResponseDto countGoods(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("countGoods", goodsService.countGoodsAll());

        return restResponseDto;
    }

    //Delete
    @RequestMapping("deleteGoodsId")
    public RestResponseDto deleteGoodsId(@RequestParam("id") int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        goodsService.deleteGoodsId(id);

        return restResponseDto;
    }

    //probably later - or Reset thymleaf
    @RequestMapping("updateGoodsProduct")
    public RestResponseDto updateGoodsProduct(GoodsDto params){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        goodsService.updateGoodsProduct(params);

        return restResponseDto;
    }

    //Discount
    @RequestMapping("getDiscountsByGoodsId")
    public RestResponseDto getDiscountsByGoodsId(@RequestParam("goods_id") int goods_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("disCountList", goodsService.selectDiscountsByGoodsId(goods_id));

        return restResponseDto;
    }

    //Discount insert
    @RequestMapping("/discounts")
    public RestResponseDto insertDiscount(@RequestBody GoodsDiscountDto goodsDiscountDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        goodsService.insertDiscount(goodsDiscountDto);

        return restResponseDto;
    }

    //Discount update
    @PutMapping("/discounts")
    public RestResponseDto updateDiscount(@RequestBody GoodsDiscountDto goodsDiscountDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        goodsService.updateDiscount(goodsDiscountDto);

        return restResponseDto;
    }

    //Discount delete
    @DeleteMapping("/discounts")
    public RestResponseDto deleteDiscount(@RequestParam("id") int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        
        goodsService.deleteDiscount(id);

        return restResponseDto;
    }

    //OrderList 주문리스트
    @RequestMapping("getOrderList")
    public RestResponseDto getOrderList(@RequestParam(value="p", defaultValue ="1") int p){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("orderList", goodsService.getGoodsOrderList(p));

        return restResponseDto;
    }

    //주문상태업데이트
    @RequestMapping("updateOrderStatus")
    public RestResponseDto updateOrderStatus(@RequestParam("id") int id, @RequestParam("order_status") String order_status){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        try {
            goodsService.updateOrderStatus(id, order_status);

            return restResponseDto;
        } catch (Exception e) {
            e.printStackTrace();

            restResponseDto.setResult("fail");
            return restResponseDto;
        }
    }

    //OrderCount
    @RequestMapping("orderCount")
    public RestResponseDto orderCount(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("orderCount", goodsService.countOrderAll());

        return restResponseDto;
    }

    //OrderListDetail
    @RequestMapping("getOrderListDetail")
    public RestResponseDto getAdminOrderList(@RequestParam("order_id") int order_id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("orderListDetail", goodsService.getGoodsOrderListByOrderId(order_id));

        return restResponseDto;
    }

    //ReviewList
    @RequestMapping("getReviewList")
    public RestResponseDto getReviewList(@RequestParam(value="p", defaultValue ="1") int p){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reviewList", goodsService.getGoodsReviewListByGoodsAndUser(p));

        return restResponseDto;
    } 

    //ReviewCount
    @RequestMapping("reviewCount")
    public RestResponseDto reviewCount(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reviewCount", goodsService.countReviewAll());

        return restResponseDto;
    }
    
    //Delete
    @RequestMapping("deleteReview")
    public RestResponseDto deleteReview(@RequestParam("id") int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        goodsService.deleteReview(id);

        return restResponseDto;
    }

    //ReviewDetail
    @RequestMapping("reviewDetail")
    public RestResponseDto reviewDetail(@RequestParam("id") int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("reviewDetail", goodsService.getReviewDetailAll(id));

        return restResponseDto;
    }

    //ReviewRecommend
    @RequestMapping("insertReviewRecommend")
    public RestResponseDto insertReviewRecommend(GoodsReviewRecommendDto goodsReviewRecommendDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        goodsService.insertReviewRecommend(goodsReviewRecommendDto);

        return restResponseDto;
    }
    
    @RequestMapping("deleteReviewRecommend")
    public RestResponseDto deleteReviewRecommend(@RequestParam("id") int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        goodsService.deleteReviewRecommend(id);

        return restResponseDto;
    }

    //DeleteReviewRecommend - All
    @RequestMapping("deleteReviewRecommendations")
    public RestResponseDto deleteReviewRecommendations(@RequestParam("goodsReviewId") int goodsReviewId) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        goodsService.deleteReviewRecommendationsByReviewId(goodsReviewId);

        return restResponseDto;
    }

    //Pagination calculation - Order List
    @RequestMapping("getStartAndEndPageNumber")
    public RestResponseDto aaa(@RequestParam(value="p", defaultValue ="1") int p) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        int totalCount = goodsService.getOrderListTotalCount();
        int lastPageNumber = (int) Math.ceil(totalCount/10.0);
        int startPage = ((p-1)/5)*5+1;
        int endPage = ((p-1)/5+1)*5;
            if(endPage > lastPageNumber) {
                endPage = lastPageNumber;
            }

        restResponseDto.add("startPage", startPage);
        restResponseDto.add("endPage", endPage);
        restResponseDto.add("lastPageNumber", lastPageNumber);
        

        return restResponseDto;
    }

    //Pagination calculation - Review List
    @RequestMapping("getReviewStartAndEndPageNumber")
    public RestResponseDto getReviewStartAndEndPageNumber(@RequestParam(value="p", defaultValue ="1") int p) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        int totalCount = goodsService.getReviewListTotalCount();
        int lastPageNumber = (int) Math.ceil(totalCount/10.0);
        int startPage = ((p-1)/5)*5+1;
        int endPage = ((p-1)/5+1)*5;
            if(endPage > lastPageNumber) {
                endPage = lastPageNumber;
            }

        restResponseDto.add("startPage", startPage);
        restResponseDto.add("endPage", endPage);
        restResponseDto.add("lastPageNumber", lastPageNumber);

        return restResponseDto;
    }


}
