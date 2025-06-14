package ce.mnu.wptc.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Value("${file.upload-dir}") // application.properties의 설정값을 가져옴
    private String uploadDir;

    // 파일을 서버에 저장하고, 웹에서 접근 가능한 경로를 반환
    public String storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 파일 이름이 겹치지 않도록 UUID로 고유한 이름 생성
        String originalFilename = multipartFile.getOriginalFilename();
        String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // 실제 파일 저장
        File storeFile = new File(uploadDir + storedFilename);
        multipartFile.transferTo(storeFile);

        // 데이터베이스에 저장될, 웹에서 접근 가능한 경로 반환
        // (예: /images/uuid_파일명.jpg)
        return "/images/" + storedFilename;
    }
}