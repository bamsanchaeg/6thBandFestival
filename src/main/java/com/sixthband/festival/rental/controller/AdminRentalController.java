package com.sixthband.festival.rental.controller;

import com.sixthband.festival.goods.controller.AdminGoodsController;
import com.sixthband.festival.rental.dto.RentalOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.rental.dto.RentalCategoryDto;
import com.sixthband.festival.rental.dto.RentalDetailImageDto;
import com.sixthband.festival.rental.dto.RentalItemDto;
import com.sixthband.festival.rental.service.RentalService;
import com.sixthband.festival.util.ImageUtil;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("admin/rental")
@RequiredArgsConstructor
public class AdminRentalController {

    private final RentalService rentalService;

    // 관리자 대여 상품 리스트
    @RequestMapping("")
    public String rentalAdminPage(Model model, @RequestParam(value="p", defaultValue = "1") int p){


        List<Map<String, Object>> rentalItemList = rentalService.getAllRentalItems(p);
        model.addAttribute("rentalItemList", rentalItemList);

        int totalCount = rentalService.getRentalItemCount();
        model.addAttribute("totalCount", totalCount);

        int lastPageNumber = (int)Math.ceil(totalCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);

        model.addAttribute("currentPage", p);

        int startPage = ((p-1)/5)*5+1;
        int endPage = ((p-1)/5+1)*5;

        if(endPage > lastPageNumber) {
            endPage = lastPageNumber;
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "admin/rentalAdminPage";
    }

    // 관리자 대여 상품 등록 양식
    @GetMapping("itemRegister")
    public String itemRegister(Model model){

        List<RentalCategoryDto> category = rentalService.getRentalCategories();
        model.addAttribute("rentalCategory", category);

        return "admin/rental/itemRegister";

    }

    // 관리자 대여 상품 등록 process
    @PostMapping("itemRegister")
    public String itemRegister(RentalItemDto params,
                               @RequestParam(value="images") MultipartFile images,
                               @RequestParam(value="multiImage") MultipartFile[] multiImage){
        
        // 대표이미지 저장, 경로 설정
        String image = ImageUtil.saveImageAndReturnLocation(images);
        params.setImage(image);

        // 대여 상품 정보 저장
        rentalService.insertRentalItem(params);
        // System.out.println(params);
        // System.out.println("id : " + params.getId());

        // 상세이미지 저장, 경로 설정(여러 개 이미지 처리)
        List<ImageDto> multiImageDto = ImageUtil.saveImageAndReturnDtoList(multiImage);
        for(ImageDto imageDto : multiImageDto){
            // System.out.println("렌탈아이디 : " + rentalItemDto.getId());
            RentalDetailImageDto rentalDetailImageDto = new RentalDetailImageDto();
            int rentalId = params.getId();
            rentalDetailImageDto.setRental_item_id(rentalId);
            rentalDetailImageDto.setDetail_image(imageDto.getLocation());
            rentalService.insertDetailImage(rentalDetailImageDto);
        }
        return "redirect:/admin/rental";
    }

    // 관리자 대여 상품 수정 양식
    @GetMapping("itemEdit")
    public String itemEdit(@RequestParam("id") int id, Model model){

       // 대여 상품 정보 조회, 모델에 추가
       Map<String, Object> rentalItemInfo = rentalService.getRentalItemById(id);
       model.addAttribute("rentalItemInfo", rentalItemInfo);

       // 대여 카테고리 리스트 조회, 모델에 추가
       List<RentalCategoryDto> categoryList = rentalService.getRentalCategories();
       model.addAttribute("categoryList", categoryList);

        return "admin/rental/itemEdit";
    }

    // 관리자 대여 상품 수정 process
    // 상품 수정 시 이미지 받아야 하니까 이미지 경로 설정하고 필요한 파라미터 받음
    @PostMapping("itemEdit")
    public String itemEdit(RentalItemDto params,
                           @RequestParam(value="images", required = false) MultipartFile images,
                           @RequestParam(value="multiImage", required = false) MultipartFile[] multiImage,
                           @RequestParam(value = "existingImage") String existingImage){

        // 대표이미지 처리
        if (images != null && !images.isEmpty()) {
            // 새로운 이미지가 업로드된 경우
            String image = ImageUtil.saveImageAndReturnLocation(images);
            params.setImage(image);
        } else {
            // 새로운 이미지가 업로드되지 않은 경우, 기존 이미지 사용
            params.setImage(existingImage);
        }

        // 대여 상품 정보 업데이트
        rentalService.updateRentalItem(params);

        // 상세 이미지 처리
        if (multiImage != null && multiImage.length > 0 && !multiImage[0].isEmpty()) {
            // 새로운 상세 이미지가 업로드된 경우
            List<ImageDto> multiImageDto = ImageUtil.saveImageAndReturnDtoList(multiImage);
            for(ImageDto imageDto : multiImageDto){
                RentalDetailImageDto rentalDetailImageDto = new RentalDetailImageDto();
                rentalDetailImageDto.setRental_item_id(params.getId());
                rentalDetailImageDto.setDetail_image(imageDto.getLocation());
                rentalService.insertDetailImage(rentalDetailImageDto);
            }
        }

        return "redirect:/admin/rental";
    }

    // 관리자 대여 상품 삭제 process
    @PostMapping("delete/{id}")
    public String deleteItem(@PathVariable int id){
        rentalService.deleteRentalItem(id);
        
        return "redirect:/admin/rental";
    }

    // 관리자 대여 상품 상세 양식
    @GetMapping("itemDetail")
    public String itemDetail(@RequestParam("id") int id, Model model){

        // 대여 상품 정보 조회, 모델에 추가
        Map<String, Object> rentalItemInfo = rentalService.getRentalItemById(id);
        model.addAttribute("rentalItemInfo", rentalItemInfo);

        // 대여 카테고리 리스트 조회, 모델에 추가
        List<RentalCategoryDto> categoryList = rentalService.getRentalCategories();
        model.addAttribute("categoryList", categoryList);

        // 상세 이미지 목록 조회, 모델에 추가
        List<RentalDetailImageDto> detailImages = rentalService.getImagesByRentalItemId(id);
        System.out.println("Detail Images count: " + detailImages.size());
        model.addAttribute("detailImages", detailImages);

        return "admin/rental/itemDetail";
    }

    // 관리자 대여 내역 리스트
    @GetMapping("orderList")
    public String orderList(Model model, @RequestParam(value="p", defaultValue = "1") int p) {

        List<Map<String, Object>> orderList = rentalService.getOrderList(p);
        model.addAttribute("orderList", orderList);

        int totalCount = rentalService.getRentalOrderCount();
        model.addAttribute("totalCount", totalCount);

        int lastPageNumber = (int)Math.ceil(totalCount/10.0);
        model.addAttribute("lastPageNumber", lastPageNumber);

        model.addAttribute("currentPage", p);

        int startPage = ((p-1)/5)*5+1;
        int endPage = ((p-1)/5+1)*5;

        if(endPage > lastPageNumber) {
            endPage = lastPageNumber;
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/rental/orderList";
    }

    // 관리자 대여 내역 수정 양식
    @GetMapping("orderEdit")
    public String orderEdit(@RequestParam("id") int id, Model model){

        Map<String, Object> orderDetail = rentalService.getOrder(id);
        model.addAttribute("orderDetail", orderDetail);

        return "admin/rental/orderEdit";
    }

    // 관리자 대여 내역 수정 process
    @PostMapping("orderEdit")
    public String orderEdit(RentalOrderDto params){

        rentalService.updateOrder(params);

        return "redirect:/admin/rental/orderList";
    }

    // 관리자 대여 내역 삭제 process
    @PostMapping("deleteOrder")
    public String deleteOrder(@RequestParam("id") int id){

        rentalService.deleteOrder(id);

        return "redirect:/admin/rental/orderList";
    }

    // 관리자 대여 내역 상세 양식
    @GetMapping("orderDetail")
    public String orderDetail(@RequestParam("id") int id, Model model){

        // 대여 내역 정보 조회, 모델에 추가
        Map<String, Object> orderDetail = rentalService.getOrder(id);
        model.addAttribute("orderDetail", orderDetail);

        return "admin/rental/orderDetail";
    }

    // 관리자 대여 내역 상세에서 수정
    @PostMapping("orderDetail")
    public String orderDetail(RentalOrderDto params){

        // 대여 상황 업데이트
        rentalService.updateOrder(params);

        return "redirect:/admin/rental/orderDetail?id=" + params.getId();
    }

    @RequestMapping("deleteRentalItemById")
    public String deleteRentalItemById(int id){
        System.out.println("아이디 출력" + id);
        rentalService.deleteRentalItem(id);
        return "redirect:/admin/rental";
    }

}

