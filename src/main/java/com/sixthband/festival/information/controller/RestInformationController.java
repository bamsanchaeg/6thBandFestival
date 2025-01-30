package com.sixthband.festival.information.controller;

import java.util.*;

import com.sixthband.festival.dto.RestResponseDto;
import com.sixthband.festival.information.dto.FestivalLineUpDto;
import com.sixthband.festival.information.dto.FestivalPerformanceDateDto;
import com.sixthband.festival.information.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//더이상 forwarding하지않음. return 결과를 json으로 변환해서 문자를 응답함. 포워딩을 하지 않고 Jason을 돌려준다.
@RestController
@RequestMapping("api/information")
public class RestInformationController {

    @Autowired
    private InformationService informationService;

    //Json으로 변환해서 응답하는 법 시작.
    //프론트엔드 개발자가 활용할 링크로 api 생성


    @RequestMapping("getFestivalList")
    public RestResponseDto getFestivalList(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("FestivalList",informationService.getFestivallList());
        return restResponseDto;
    }

    //과거 페스티벌 내역 불러오기, 오늘날짜 기준으로 이미 끝난 페스티벌 리스트
    @RequestMapping("getFestivalHistoryList")
    public RestResponseDto getFestivalHistoryList(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");;
        restResponseDto.add("FestivalList",informationService.getFestivalHistoryList());
        return restResponseDto;
    }

    @RequestMapping("getFestivalDetail")
    public RestResponseDto getFestivalDetail(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");

        restResponseDto.add("festivalDetail",informationService.getFestivalDetailById(id));
        return restResponseDto;
    }

    //관리자페이지 출력
    @RequestMapping("getAdminMainPage")
    public RestResponseDto getAdminMainPage(){

        //여기서 출력하는건 쿼리로 갯수제한해서 관리자페이지 메인에서 리스트 출력.
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("adminMainList",informationService.getFestivalListForAdminMainPage());

        return restResponseDto;
    }

    //관리자 페이지 페스티벌 디테일 페이지
    @RequestMapping("festivalDetailPageForAdmin")
    public RestResponseDto festivalDetailPageForAdmin(int id){
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");
        restResponseDto.add("festivalData",informationService.getFestivalDetailById(id));

        return restResponseDto;
    }

    //관리자 페이지 festivalList
    @RequestMapping("festivalListPageForAdmin")
    public RestResponseDto festivalListPageForAdmin(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("festivalDataList",informationService.getFestivallListAdmin());
        return restResponseDto;
    }

    //festival라인업에 필요한 정보를 넘겨줄 RestAPI, 댓글 형식으로 가져오면 하단 날짜가 업데이트가 되게 한다.
    //해당 페스티벌의 라인업에 등록된 아티스트까지 불러와서 유효성 검사 처리
    @GetMapping("festivalLineupRegister")
    public RestResponseDto festivalLineupRegister(int id){
        RestResponseDto restResponseDto = new RestResponseDto();

        restResponseDto.setResult("success");
        restResponseDto.add("festivalData",informationService.getFestivalDetailById(id));
        restResponseDto.add("PerformanceDate",informationService.getPerformanceDateListByFestivalId(id));
        restResponseDto.add("artistList",informationService.getArtistListForLineUpRegister());
        return restResponseDto;
    }

    @GetMapping("/getIsRegister")
    public RestResponseDto getIsRegister(int id,int artistId){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("isRegistered",informationService.isRegistered(id,artistId));
        restResponseDto.add("RegisteredInformation",informationService.getArtistLineUpForCheckRegistered(id,artistId));
        return restResponseDto;
    }

    //라인업 등록을 위한 아티스트 리스트를 가져온다. 여기서 이미 등록된 아티스트 리스트를 걸러내는 작업도 필요할듯
    @RequestMapping("festivalArtistListForAdmin")
    public RestResponseDto festivalArtistListForAdmin(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("registeredArtists",informationService.getRegisteredArtistListAtLineUp(id));
        restResponseDto.add("artistList",informationService.getArtistListForLineUpRegister());

        return restResponseDto;
    }



    //페스티벌 라인업 날짜 등록해주는, 자바스크립트에서 댓글형식으로 처리해줌
    @RequestMapping("festivalDateRegister")
    public RestResponseDto festivalDateRegister(FestivalPerformanceDateDto festivalPerformanceDateDto){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        informationService.registerDateForFestival(festivalPerformanceDateDto);
        return restResponseDto;
    }

    //페스티벌 라인업 등록 쭉 해주는 restAPI, html 태그에 대해서 이해가 잘 되어야함
    //RequetParam으로 받아야할 데이터의 name을 선언해주지 않아서 데이터를 못받아오고있었음.. 정신차리세용
    @RequestMapping("artistRegisterForLineUpProcess")
    public RestResponseDto artistRegisterForLineUpProcess(@RequestParam(value= "performance_id", defaultValue = "0") int id,
                                                          @RequestParam(value="artist_id") int[] artistIds){

        System.out.println("라인업 데이터 출력2 = " + Arrays.toString(artistIds));
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        informationService.registerArtistForLineUp(id,artistIds);

        return restResponseDto;
    }

    //특정 페스티벌의 라인업 리스트 출력,  //모든 라인업 데이터 가져와서 아티스트가 이 라인업에 등록되었는지 확인해야함.
    @RequestMapping("getLineupListByFestivalId")
    public RestResponseDto getLineupListByFestivalId(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("lineUpList",informationService.getFestivalLineUpById(id));
        return restResponseDto;
    }


    //라인업 전체 리스트 출력
    @RequestMapping("getLineUpOfLatestFestival")
    public RestResponseDto getLineUpOfLatestFestival(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("lineUpList",informationService.getFestivalList());
        return restResponseDto;
    }

    @RequestMapping("getArtistDetailByArtistId")
    public RestResponseDto getArtistDetailByArtistId(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("artistData",informationService.getArtistDetailById(id));
        return restResponseDto;
    }

    //관리자페이지용 information 메인페이지 페스티벌 리스트 출력
    //여기서 출력하는건 쿼리로 갯수제한해서 관리자페이지 메인에서 리스트 출력.
    @RequestMapping("getFestivalListForAdmin")
    public RestResponseDto getFestivalListForAdmin(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("upcomingFestival",informationService.getMinDateFestivalList());
        restResponseDto.add("festivalDataList",informationService.getFestivalListForAdminMainPage());

        return restResponseDto;
    }

    //페스티벌 삭제 프로세스
    @RequestMapping("festivalDeleteProcess")
    public void festivalDeleteProcess(int id){
        informationService.deleteFestivalInformation(id);
    }

    @RequestMapping("deleteLinupAndDateByDateid")
    public void deleteLinupAndDateByDateid(int id){
        informationService.deleteLineUpAndDeleteDateByid(id);

    }

    //아티스트 리스트 가져오기
    @RequestMapping("getAllArtistListForAdmin")
    public RestResponseDto getAllArtistListForAdmin(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        //라인업 등록을 위한 라인업 등록용 아티스트 가져오기라고 얘기했지만 아티스트 관리를 위해서도 가져옴
        restResponseDto.add("artistList",informationService.getArtistListForLineUpRegister());
        return restResponseDto;
    }


    //아티스트 디테일 가져오기
    @RequestMapping("getArtistDetailById")
    public RestResponseDto getArtistDetailById(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("artistDetail",informationService.getArtistDetailById(id));
        return restResponseDto;
    }

    @RequestMapping("getFestivalDetailForUpdate")
    public RestResponseDto getFestivalDetailForUpdate(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("festivalDetail",informationService.getFestivalDetailById(id));
        return restResponseDto;
    }

    @RequestMapping("getFestivalAndLineUpOfMain")
    public RestResponseDto getFestivalAndLineUpOfMain(){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        restResponseDto.add("FestivalInfo", informationService.getMinDateFestivalList());
        //festivalinfo에서 아이디 받아서 못가져오나
        restResponseDto.add("LineUpInfo",informationService.getFestivalList());
        return restResponseDto;

    }

    @RequestMapping("artistDeleteProcess")
    public RestResponseDto artistDeleteProcess(int id){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setResult("success");
        informationService.deleteArtistById(id);
        return restResponseDto;
    }






}
