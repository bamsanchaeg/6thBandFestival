package com.sixthband.festival.information.service;

import com.sixthband.festival.information.dto.*;
import com.sixthband.festival.information.mapper.InformationSqlMapper;
import com.sixthband.festival.util.MacOsUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

@Service
public class InformationService {

    /**파일 경로 추가완료.
     * ImageDtoUtil에서는 filePath가 상대경로설정이라 MacOs에서 적용이 되지 않았던 것 같고...Config에서 경로 지정만 되어 문제가 생기지 않았나 싶습니다.
     * 보통 MacOs의 User폴더에는 작성 권한이 필요해 하위 폴더인 test 폴더를 이전 프로젝트에서 따로 생성했었는데, 해당 방법으로 절대경로를 생성하니 잘 됩니다.
     * 다만 날짜별 폴더링이 아닌 thumbnail과 Images로 구분이 될 것 같아 해당 부분은 규칙이 필요하면 수정하도록 하겠습니다.
     * (mac. /Users/test/sixthBandImage/(맡은 기능 이름) **/

    //rootPath는 final로 고정되어 수정되지않습니다.
    //여기서 separator는 OS에 맞춘 경로 분기법입니다.
    //File.separator 상수는 운영 체제에 따라 적절한 경로 구분자를 반환합니다 (예: Windows에서는 '', Linux에서는 '/').
    //경로 문자열은 여러 문자열을 쉼표로 구분하여 연결합니다. 아래 변수'rootPath'는 다음과 같은 경로를 생성합니다. "Users/test/sixthBandImage"
//    private final String rootPath = Paths.get(File.separator,"Users","test","sixthBandImage").toString();
    /**배포용 rootPath**/
    private final String rootPath = "/sixthBandImage";

    @Autowired
    private InformationSqlMapper informationSqlMapper;

    //festivalDto에 대한 id와 thumbnail url 값을 받아와서 한 부분에서 같이 업데이트 처리 해줄예정
    public void registerFestivalInformation(FestivalDto festivalDto){
        informationSqlMapper.registerFestivalInformation(festivalDto);
    }

    //페스티벌 수정 및 업데이트
    public void updateFestival(FestivalDto  festivalDto, int id){
        FestivalDto festivalDtotemp = new FestivalDto();
        festivalDtotemp.setId(id);
        informationSqlMapper.updateFestival(festivalDto);
    }

    //이미지 등록 및 경로반환, 썸네일 삽입, 쿼리로 festivalDto에 업데이트
    public void registerThumbnailForFestival(int id, String thumbnail){
        informationSqlMapper.createThumbnail(id,thumbnail);
    }


    //썸네일 등록, 아티스트 페스티벌 상관없이 등록
    public String saveThumbnail(MultipartFile thumbnail){
        //하위폴더생성은 thumbnail로 생성하는 변수
        String thumbnailPath = MacOsUtil.generateSaveDirMacOs(rootPath,"information"+File.separator+"thumbnail");
        System.out.println("rootPath = " + rootPath);
        //확장자 추출, 확장자 이름 넣어서 하는 법, 이 부분의 폴더링은 날짜별로 진행하지 않았습니다.
        //orgFilename 메서드를 사용하여 업로드된 파일의 원본 이름을 문자열에 저장합니다.
        String orgFilename = thumbnail.getOriginalFilename();
        //orgFilename에서 확장자를 추출하여 새로운 문자열 변수 ext에 저장합니다. 이 변수에는 업로드된 파일의 확장자명만 포함됩니다.
        String ext = orgFilename.substring(orgFilename.lastIndexOf("."));
        //따라서 thumbnailFilename은 UUID + 확장자명의 String 값으로 리턴된다고 이해하면 될 것 같습니다.
        //MacOsUtil쪽에선 사용자 닉네임 + 밀리초로 짜여져있는데 관리자 등록 단일 이미지라 아직 세션값은 필요없어 썸네일은 UUID로 진행했습니다.
        String thumbnailFilename = UUID.randomUUID() + ext;
        try{
            System.out.println(thumbnailPath + thumbnailFilename);
            thumbnail.transferTo(new File(rootPath + File.separator + thumbnailPath + thumbnailFilename));
            return thumbnailPath + thumbnailFilename;
        }catch (Exception e){
            return null;
        }
    }

    //SELECT 구문으로 List를 받아온다면 아래처럼 For 문으로 풀어줘야한다.
    //페스티벌 메인 페이지 용 페스티벌 리스트 페이지. 가장 가까운 메인 페스티벌 하나만 걸어놓는 용임.
    public List<Map<String,Object>> getFestivallList(){
        //새로운 리스트 공간 생성
        List<Map<String,Object>> result = new ArrayList<>();
        //festivalDto에 해당 값을 다 넣어준다.
        List<FestivalDto> festivalDtoList = informationSqlMapper.getAllFestivalList();
        //뭉쳐져있는 List를 맵에 정보를 뜯어서 개별로 다 담아준다.
        for (FestivalDto festivalDto : festivalDtoList){
            Map<String,Object> map = new HashMap<>();
            map.put("festivalDto",festivalDto);
            result.add(map);
        }
        return result;
    }


    //SELECT 구문으로 List를 받아온다면 아래처럼 For 문으로 풀어줘야한다.
    public List<Map<String,Object>> getFestivallListAdmin(){
        //새로운 리스트 공간 생성
        List<Map<String,Object>> result = new ArrayList<>();
        //festivalDto에 해당 값을 다 넣어준다.
        List<FestivalDto> festivalDtoList = informationSqlMapper.getAllFestivalListForAdmin();
        //뭉쳐져있는 List를 맵에 정보를 뜯어서 개별로 다 담아준다.
        for (FestivalDto festivalDto : festivalDtoList){
            Map<String,Object> map = new HashMap<>();
            map.put("festivalDto",festivalDto);
            result.add(map);
        }
        return result;
    }

    public List<Map<String,Object>> getFestivalHistoryList(){

        List<Map<String,Object>> result = new ArrayList<>();
        List<FestivalDto> festivalDtoList = informationSqlMapper.getAllFestivalHistoryList();
        for(FestivalDto festivalDto : festivalDtoList){
            Map<String,Object> map = new HashMap<>();
            map.put("festivalDto",festivalDto);
            result.add(map);
        }
        return result;
    }


    //공연 리스트 불러오기 for 관리자 메인페이지
    public List<Map<String,Object>> getFestivalListForAdminMainPage(){

        List<Map<String,Object>> result = new ArrayList<>();
        List<FestivalDto> festivalDtoList = informationSqlMapper.getFestivalListForAdminMainPage();
        for(FestivalDto festivalDto: festivalDtoList){
            Map<String,Object> map = new HashMap<>();
            map.put("festivalDto",festivalDto);
            result.add(map);
        }

        return result;
    }

    //라인업을 위한 아티스트 리스트 불러오기
    public List<Map<String,Object>> getArtistListForLineUpRegister(){
        List<Map<String,Object>> result = new ArrayList<>();
        List<ArtistDto> artistDtoList = informationSqlMapper.getAllArtistList();
        for(ArtistDto artistDto : artistDtoList){
            Map<String,Object> map = new HashMap<>();
            map.put("artistDto",artistDto);
            result.add(map);
        }
        return result;
    }

    public Map<String,Object> getFestivalDetailById(@Param("id")int id){
        Map<String,Object> map = new HashMap<>();
        //DTO에 받은 값을 넣어주기.. 리턴값을 넣어줘야하면 void면 안된다.
        FestivalDto festivalDto = informationSqlMapper.getFestivalInformationDetailById(id);
        map.put("festivalDetail",festivalDto);
        return map;
    }


    public void deleteFestivalInformation(@Param("id")int id){
        informationSqlMapper.deleteFestivalInformationById(id);
    }

    public void registerArtistInformation(ArtistDto artistDto){
        informationSqlMapper.registerArtistInformation(artistDto);
    }

    //아티스트 이미지 등록
    public void registerArtistImages(List<MultipartFile> artistImages, @Param("id") int artistId){
        String artistImagePath = MacOsUtil.generateSaveDirMacOs(rootPath,"information" + File.separator + "images");

        for(MultipartFile artistImage : artistImages){
            if(artistImage.isEmpty()){
                continue;
            }
            try{

                String orgFilename = artistImage.getOriginalFilename();
                String ext = orgFilename.substring(orgFilename.lastIndexOf("."));
                String imageFileName = UUID.randomUUID() + ext;
                artistImage.transferTo(new File(rootPath + File.separator + artistImagePath + imageFileName));
                ArtistImageDto artistImageDto = new ArtistImageDto();
                //각각 받아주지 못해서 직접 잡아다가 넣어주는 작업 진행...
                artistImageDto.setArtist_id(artistId);
                artistImageDto.setImages_url(artistImagePath + imageFileName);
                informationSqlMapper.registerArtistImages(artistImageDto);
            } catch (Exception e){
                e.printStackTrace();
                return;
            }
        }

    }

    //아티스트 썸네일 등록
    public void registerThumbnailForArtist(@Param("id") int id,String thumbnail){
        informationSqlMapper.createArtistThumbnail(id,thumbnail);
    }

    //아티스트 정보 삭제
    public void deleteArtistById(int id){
        informationSqlMapper.deleteArtistById(id);
    }

    //날짜 등록
    public void registerDateForFestival(FestivalPerformanceDateDto festivalPerformanceDateDto){
        informationSqlMapper.registerDateForFestival(festivalPerformanceDateDto);

    }

    //날짜 정보 불러오기
    public List<Map<String,Object>> getPerformanceDateListByFestivalId(@Param("festival_id") int id){
        List<Map<String,Object>> result = new ArrayList<>();
        List<FestivalPerformanceDateDto> festivalPerformanceDateDtos = informationSqlMapper.getFestivalDateDtoById(id);

        for(FestivalPerformanceDateDto festivalDateDto : festivalPerformanceDateDtos){
            Map<String,Object> map = new HashMap<>();
            map.put("festivalDate", festivalDateDto);
            result.add(map);
        }
        return result;

    }


    //xml 파일에서 performance_date생성된 pk는 performance_id로 받아오고, 아티스트 여러개 선택된 것은 배열로 받아온다.
    //배열로 가져온 숫자를 하나하나 등록해주기.
    public void registerArtistForLineUp(int id,int[] artistIds){
        if(artistIds != null){
            for(int artistId: artistIds){
                FestivalLineUpDto festivalLineUpDto = new FestivalLineUpDto();
                festivalLineUpDto.setPerformance_id(id);
                festivalLineUpDto.setArtist_id(artistId);
                informationSqlMapper.registerArtistForLineUp(festivalLineUpDto);
            }
        }

    }

    //라인업 삭제하기
    public void deleteLineUpAndDeleteDateByid(int id){
        try {
            informationSqlMapper.deleteDateByDateid(id);
            System.out.println("Deleted from festival_information_performanceDate for id: " + id);
        }catch (Exception e){
            System.err.println("Error deleting from festival_information_performanceDate for id: " + id + ": " + e.getMessage());
        }
        try {
            informationSqlMapper.deleteLineupByDateid(id);
            System.out.println("Deleted from festival_information_lineup for id: " + id);
        }catch (Exception e){
            System.err.println("Error deleting from festival_information_lineup for id: " + id + ": " + e.getMessage());
            // 로그를 남기거나 필요한 경우 추가 처리를 수행할 수 있습니다.

        }


    }

    //곧 시작하는 페스티벌 리스트 가져오기.
    public FestivalDto getMinDateFestivalList(){
        return informationSqlMapper.getMinDateFestivalList();
    }

    //Map 타입으로 쿼리 데이터를 받기때문에 그냥 자동생성해서 리턴해준다. 따라서 리턴을 SQLMapper에 있는 기능만 리턴할수있게 함.
    public List<Map<String,Object>> getFestivalLineUpById(int id){
         return informationSqlMapper.getFestivalLineUpByFestivalId(id);
    }

    public List<Map<String,Object>> getFestivalList(){
       return informationSqlMapper.getLineUpOfLatestFestival();
    }


    /**유효성 검사에 필요한 라인업에 등록된 아티스트 리스트 가져오기**/
    public List<Map<String,Object>> getRegisteredArtistListAtLineUp(int id){
        return informationSqlMapper.getRegisteredArtistListAtLineUp(id);
    }

    //등록된 아티스트 리스트인가?
    public boolean isRegistered(int id,int artistId){
        return informationSqlMapper.isRegistered(id,artistId);
    }

    //아티스트 등록된 페스티벌 정보 불러오기
    public Map<String,Object> getArtistLineUpForCheckRegistered(int id,int artistId){
        return informationSqlMapper.getArtistLineUpForCheckRegistered(id,artistId);
    }


    //아티스트 디테일 가져오기
    public Map<String,Object> getArtistDetailById(int id){
        Map<String,Object> map = new HashMap<>();
        ArtistDto artistDto = informationSqlMapper.getArtistInformationDerailById(id);
        //아티스트 이미지 가져오기
        List<ArtistImageDto> artistImageDtoList = informationSqlMapper.getArtistImagesByArtistId(id);
        map.put("artistDetail", artistDto);
        map.put("artistImages", artistImageDtoList);

        return map;
    }

}
