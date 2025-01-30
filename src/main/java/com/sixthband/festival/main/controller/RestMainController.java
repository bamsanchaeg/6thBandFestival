package com.sixthband.festival.main.controller;

import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.goods.service.GoodsService;
import com.sixthband.festival.information.service.InformationService;
import com.sixthband.festival.rental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/main")
public class RestMainController {

    @Autowired
    InformationService informationService;

    @Autowired
    RentalService rentalService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("getProductInformation")
    public RestResponseDto getProductInformation() {

        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");
        restResponseDto.add("goodsList", goodsService.selectOrderTop4());
        restResponseDto.add("rentalItems", rentalService.getTopRentalItems());

        return restResponseDto;
    }
}
