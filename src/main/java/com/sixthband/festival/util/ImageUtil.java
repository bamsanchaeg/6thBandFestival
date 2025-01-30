package com.sixthband.festival.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.sixthband.festival.dto.ImageDto;

public class ImageUtil {
    
    // input에서 받아온 파일을 서버에 저장하고 그 경로를 반환합니다. 하나의 이미지를 불러올 때 유용합니다.
    static public String saveImageAndReturnLocation(MultipartFile inputImage){
       return saveImageAndReturnDto(inputImage).getLocation();
    }

    // input에서 받아온 여러 파일을 모두 서버에 저장하고 그걸 ImageDto로 반환합니다. 여러개의 이미지를 불러올 때 유용합니다. 
    static public List<ImageDto> saveImageAndReturnDtoList(MultipartFile[] inputImages) {
        List<ImageDto> result = new ArrayList<>();
        for(MultipartFile inputImage : inputImages){
            ImageDto dto = saveImageAndReturnDto(inputImage);
            result.add(dto);
        }
        return result;  
    }

    // 이미지를 정리하는데 필요한 함수
    static private ImageDto saveImageAndReturnDto(MultipartFile inputImage){
        String originalFilename = inputImage.getOriginalFilename();
        String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        String filePath = "/sixthBandImage/";

        String existDate = localDateTime.format(format);
        String Path = filePath + existDate + "/";

        File dir = new File(Path);

        if(!dir.exists()){
            dir.mkdirs();
            // 디렉토리 생성시 권한 설정 (읽기, 쓰기, 실행 권한)
            dir.setReadable(true, false);
            dir.setWritable(true, false);
            dir.setExecutable(true, false);
        }

        UUID uuid = UUID.randomUUID();
        String newFilename = uuid.toString();
        newFilename += extention;

        try {
            Path newFilePath = Paths.get(Path +"/" + newFilename);
            Files.write(newFilePath, inputImage.getBytes());

            // 파일 권한 설정 (읽기, 쓰기 권한)
            File file = newFilePath.toFile();
            file.setReadable(true, false);
            file.setWritable(true, false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageDto dto = new ImageDto();
        dto.setLocation(existDate + "/" + newFilename);
        dto.setOrigin_filename(originalFilename);

        return dto;
    }
}
