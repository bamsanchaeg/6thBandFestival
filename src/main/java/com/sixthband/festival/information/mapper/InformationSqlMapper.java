package com.sixthband.festival.information.mapper;

import java.util.*;

import com.sixthband.festival.information.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InformationSqlMapper {


    /**페스티벌 정보**/

    //페스티벌 등록
    public void registerFestivalInformation(FestivalDto festivalDto);
    //페스티벌 업데이트
    public void updateFestival(FestivalDto festivalDto);
    //페스티벌 썸네일 등록
    public void createThumbnail(int id,String thumbnail);
    //페스티벌 이미지 등록
    public void registerFestivalImages(FestivalImageDto festivalImageDto);
    //페스티벌 아이디 가져오기
    public int getFestivalIdFromPerformanceDate(int id);

    //페스티벌 정보 불러오기
    public FestivalDto getFestivalInformationDetailById(int id);
    //페스티벌 리스트 불러오기
    public List<FestivalDto> getAllFestivalList();
    //관리자용 페스티벌 리스트 전부 불러오기
    public List<FestivalDto> getAllFestivalListForAdmin();
    //페스티벌 리스트 불러오기(관리자)
    public List<FestivalDto> getFestivalListForAdminMainPage();

    //페스티벌 삭제(아티스트,라인업까지 다 삭제당해야함)
    public void deleteFestivalInformationById(int id);

    /**아티스트 정보**/

    //아티스트 정보등록
    public void registerArtistInformation(ArtistDto artistDto);
    //아티스트 썸네일 등록
    public void createArtistThumbnail(int id, String thumbnail);

    //아티스트 이미지 등록
    public void registerArtistImages(ArtistImageDto artistImageDto);
    //아티스트 정보 불러오기
    public ArtistDto getArtistInformationDerailById(int id);
    //아티스트 디테일에 이미지 불러오기
    public List<ArtistImageDto> getArtistImagesByArtistId(int id);
    //아티스트 리스트 불러오기
    public List<ArtistDto> getAllArtistList();
    //아티스트 정보 삭제하기
    public void deleteArtistById(int id);

    /**라인업 등록에 필요한 값**/

    //라인업에 해당 아티스트가 등록되었는지 확인하기 위해 해당 데이터를 불러오는 작업, 퍼포먼스 아이디 기준으로 페스티벌 아이디 추출한다음에 데이터 불러옴
    public List<Map<String,Object>> getRegisteredArtistListAtLineUp(int id);

    //boolean 값으로 가져오는 것
    public boolean isRegistered(@Param("id")int festivalId,@Param("artistId")int artistId);

    //라인업 등록여부 표시 후 날짜까지 표시
    public Map<String,Object> getArtistLineUpForCheckRegistered(@Param("id")int festivalId, @Param("artistId")int artistId);
    /**라인업 등록**/

    public void registerArtistForLineUp(FestivalLineUpDto festivalLineUpDto);
    public void registerDateForFestival(FestivalPerformanceDateDto festivalPerformanceDateDto);
    //라인업 삭제
    public void deleteDateByDateid(int id);
    public void deleteLineupByDateid(int id);
    //퍼포먼스 날짜 가져오기
    public List<FestivalPerformanceDateDto> getFestivalDateDtoById(@Param("festival_id") int id);

    //페스티벌 전체 라인업 리스트 가져오기
    public List<Map<String,Object>> getFestivalLineUpLists();

    //특정 페스티벌의 라인업 리스트 가져오기, 이미 쿼리문에서 resultType이 map이어서 그대로 돌려주면 됨
    public List<Map<String,Object>> getFestivalLineUpByFestivalId(int id);

    //최신 페스티벌 정보 하나만 가져오기(MIN으로 하나만 나오게 만들어 둠)
    //메인 페이지용 최신 페스티벌 불러오기(관리자용에도 사용예정)
    public FestivalDto getMinDateFestivalList();

    //최신 페스티벌의 라인업 리스트 가져오기
    public List<Map<String,Object>> getLineUpOfLatestFestival();

    //공연 히스토리 가져오기
    public List<FestivalDto> getAllFestivalHistoryList();


}
