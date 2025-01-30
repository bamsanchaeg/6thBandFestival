package com.sixthband.festival.main.controller;

import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.goods.service.GoodsService;
import com.sixthband.festival.information.service.InformationService;
import com.sixthband.festival.rental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//더이상 forwarding하지않음. return 결과를 json으로 변환해서 문자를 응답함. 포워딩을 하지 않고 Jason을 돌려준다.
//희원이 건들고 있습니다. 사용하실때 말해주세요(8/14)
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
    public RestResponseDto getProductInformation(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("goodsList", goodsService.selectOrderTop4());
        restResponseDto.add("rentalItems",rentalService.getTopRentalItems());
        return restResponseDto;
    }
}
