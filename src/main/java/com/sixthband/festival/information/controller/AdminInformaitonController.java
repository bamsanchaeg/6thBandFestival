package com.sixthband.festival.information.controller;

import com.sixthband.festival.dto.ImageDto;
import com.sixthband.festival.information.dto.*;
import com.sixthband.festival.information.service.InformationService;
import com.sixthband.festival.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 이미지 등록할때 Multipart 파일이 String으로 변환되기 직전에 thumbnail과 충돌해서 에러가 뜨는 부분 주의
 **/
@Controller
@RequestMapping("admin/information")
public class AdminInformaitonController {

    @Autowired
    InformationService informationService;

    //주소창 링크 잡는 것은 requestMapping 으로 이제야 이해됨, 아래 리턴하는건 html 경로
    @RequestMapping("")
    public String informationAdminPage(Model model) {
        return "admin/informationAdminPage";
    }

    //라인업 세부정보 등록페이지(아티스트와 페스티벌 연결해주는 페이지)
    @RequestMapping("festivalLineUpRegisterPage")
    public String festivalLineUpRegisterPage() {
        return "admin/information/performanceInformationRegisterPage";
    }

    //라인업 세부정보 등록 프로세스
    @RequestMapping("festivalLineUpRegisterProcess")
    public String festivalLineUpRegisterProcess(FestivalPerformanceDateDto festivalPerformanceDateDto) {

        //페스티벌 날짜로 카테고리 생성
        informationService.registerDateForFestival(festivalPerformanceDateDto);
        return "redirect:/admin/information/festivalDetailPage?id=";
    }

    //페스티벌 상세페이지
    @RequestMapping("festivalDetailPage")
    public String festivalDetailPageForAdmin(Model model, int id) {
        Map<String, Object> festivalData = informationService.getFestivalDetailById(id);
        model.addAttribute("festivalData", festivalData);

        return "admin/information/festivalDetailPage";
    }

    //페스티벌 게시물 업데이트, 원래 페이지로 돌아갈 수 있게 경로 구성
    @RequestMapping("updateRegisteredFestivalPage")
    public String updateRegisteredPate(Model model, int id) {

        Map<String, Object> festivalData = informationService.getFestivalDetailById(id);
        model.addAttribute("festivalData", festivalData);

        return "admin/information/updateRegisteredFestivalPage";
    }

    @RequestMapping("festivalDataUpdateProcess")
    public String festivalDataUpdateProcess(FestivalDto params, int id) {
        informationService.updateFestival(params, id);
        return "redirect:./festivalListPage";
    }

    //업데이트되는 프로세스 추가해야함(7/12)
    @RequestMapping("updateFestivalInformationProcess")
    public String updateFestivalInformationProcess(FestivalDto festivalDto) {

        int festivalId = festivalDto.getId();
        return "redirect:/admin/information/festivalDetailPage?=id" + festivalId;

    }

    @RequestMapping("artistRegisterPage")
    public String artistRegisterPage() {
        return "admin/information/artistRegisterPage";
    }


    @RequestMapping("artistRegisterProcess")
    public String artistRegisterProcess(ArtistDto artistDto,
                                        @RequestParam("thumbnail_image") MultipartFile image,
                                        List<MultipartFile> artistImages) {

        String thumbnailUrl = informationService.saveThumbnail(image);
        artistDto.setThumbnail(thumbnailUrl);

        //artistDto등록
        informationService.registerArtistInformation(artistDto);
        //썸네일 등록
        informationService.registerThumbnailForArtist(artistDto.getId(), thumbnailUrl);
        informationService.registerArtistImages(artistImages, artistDto.getId());

        return "admin/information/artistListPage";
    }

    //라인업 세부정보에서 아티스트 등록 페이지, id 값이 필요한데 이 부분은 RestAPI에서 처리해줌
    @RequestMapping("artistRegisterForLineUp")
    public String artistRegisterForLineUp() {
        return "admin/information/artistLineUpRegisterPage";
    }

    //라인업 세부등록페이지에서 페스티벌 디테일 페이지임. 날짜등록페이지
    @RequestMapping("registerPerformanceDate")
    public String registerPerformanceDate() {
        return "admin/information/performanceInformationRegisterPage";
    }

    @RequestMapping("festivalRegisterPage")
    public String festivalRegisterPage() {
        return "admin/information/festivalRegisterPage";
    }


    @RequestMapping("festivalInformationRegisterProcess")
    public String festivalInformationRegisterProcess(FestivalDto festivalDto,
                                                     @RequestParam("thumbnail_image") MultipartFile image
    ) {

        String thumbnailUrl = informationService.saveThumbnail(image);
        festivalDto.setThumbnail(thumbnailUrl);

        //festivalDto 등록
        informationService.registerFestivalInformation(festivalDto);
        //thumbNail등록
        informationService.registerThumbnailForFestival(festivalDto.getId(), thumbnailUrl);

        System.out.println("페스티벌 정보" + festivalDto);
        System.out.println("썸네일 정보" + image);

        return "admin/informationAdminPage";
    }

    @RequestMapping("festivalListPage")
    public String festivalListPage(Model model) {
        //페스티벌 리스트 불러오기
        List<Map<String, Object>> festivalDataList = informationService.getFestivallList();
        model.addAttribute("festivalDataList", festivalDataList);
        return "admin/information/festivalListPage";
    }


    //페스티벌 삭제 프로세스
    @RequestMapping("festivalDeleteProcess")
    public String festivalDeleteProcess(int id) {
        informationService.deleteFestivalInformation(id);
        return "redirect:./festivalListPage";
    }

    //아티스트 삭제 프로세스
    @RequestMapping("artistDeleteProcess")
    public String artistDeleteProcess(int id) {
        informationService.deleteArtistById(id);
        return "redirect:./artistListPage";
    }

    //아티스트 관리페이지
    @RequestMapping("artistListPage")
    public String artistListPage() {
        return "admin/information/artistListPage";
    }

    @RequestMapping("artistDetailPage")
    public String artistDetailPage() {
        return "admin/information/artistDetailPage";
    }

    @RequestMapping("artistUpdatePage")
    public String artistUpdatePage(Model model, int id) {

        Map<String, Object> Artistmap = informationService.getArtistDetailById(id);
        model.addAttribute("artistDetail", Artistmap);
        return "/admin/information/artistUpdateRegisterPage";
    }


}
