package com.sixthband.festival.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class MacOsUtil {
    /**
     * 파일 저장 메소드
     * @param file
     * MultipartFile
     * @param rootPath
     * 저장할 공간이 되는 경로
     * (mac. /Users/test/sixthBandImage/(맡은 기능 이름)
     * @param detailPath
     * 저장할 공간의 세부 경로 없다면 빈 문자열 전달
     * @param nickname
     * 파일 저장에 사용되는 파라미터, 사용자의 닉네임_밀리초로 저장됨
     * @return 저장될 상대 경로
     **/

    //이 코드는 MacOs를 위한다기보다는 위의 ImageUtil의 다른 버전이라고 생각해주시면 될 것 같습니다.

    public static String saveImageForMacOs(MultipartFile file, String rootPath, String detailPath, String nickname){
        String imagePath = MacOsUtil.generateSaveDirMacOs(rootPath, detailPath);
        String imageFilename = MacOsUtil.generateFilenameMacOs(file, nickname);
        String savePath = rootPath + File.separator + imagePath + imageFilename;

        try{
            file.transferTo(new File(savePath));
            return imagePath +imageFilename;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //파일 경로생성,File객체를 생성하여 rootPath/detailPath조합으로 생성됩니다. 자세한 부분은 상단 주석 참고.
    public static String generateSaveDirMacOs(String rootPath, String detailPath){
        File saveDirectory = new File(rootPath + File.separator + detailPath);

        // saveDirectory가 존재하지 않는 경우 mikdir메서드를 사용하여 해당 디렉토리와 모든 상위 디렉토리를 생성합니다.
        // mkdirs 메서드는 자바의 File 클래스에서 제공하는 메서드로, 디렉토리 생성을 목적으로 사용됩니다.
        // 핵심적인 특징은 부모 디렉토리가 존재하지 않아도 재귀적으로 생성한다는 점이며 mkdir보다 안전하다고 하네요.
        if(!saveDirectory.exists()) saveDirectory.mkdirs();

        return detailPath + File.separator;
    }



    public static String generateFilenameMacOs(MultipartFile file, String nickname){

        //파일명 충돌회피하는 생성, 사용자의 닉네임 + 밀리초로 저장
        String orgFilename = file.getOriginalFilename();
        long currentTime = System.currentTimeMillis();
        //파일네임 = 사용자닉네임 + 현재시각
        String filename = nickname + "_" + currentTime;

        //확장자 추출
        try{
            String ext = orgFilename.substring(orgFilename.lastIndexOf("."));
            return filename + ext;
        }catch(NullPointerException e){
            //orgFilename변수가 null인 경우 처리
            e.printStackTrace();
            return null;
        }
    }


}
