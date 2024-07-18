package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileManagerService {
	
	// 실제 업로드가 된 이미지가 저장될 서버의 경로 - 주의: 마지막에 "/" 넣기
//	public static final String FILE_UPLOAD_PATH = "D:\\배진하\\6_spring_project\\memo\\memo_workspace\\images/"; // 학원
	public static final String FILE_UPLOAD_PATH = "C:\\github\\Marondal\\6_project\\memo\\memo_workspace\\images/"; // 집
	
	// input: MultipartFile, userLoginId
	// output: String(이미지 경로)
	public String uploadFile(MultipartFile file, String loginId) {
		// 파일 디렉토리 이름, 예: aaaa_1702802640514/sun.png (디렉토리/이미지파일)
		// 폴더(= 디렉토리) 생성 => "aaaa_1702802640514"
		String directoryName = loginId + "_" + System.currentTimeMillis();

		// 기본 디렉토리 + 파일 디렉토리, D:\\배진하\\6_spring_project\\memo\\memo_workspace\\images/aaaa_1702802640514/sun.png
		String filePath = FILE_UPLOAD_PATH + directoryName + "/";
		
		// ^^ 여기까진 디렉토리를 String으로만 생성한것
		// vv String directoryName으로 실제 디렉토리 생성
		
		// 생성된 디렉토리로 실제 폴더 생성
		File directory = new File(filePath);
		if (directory.mkdir() == false) {
			// 디렉토리 생성에 실패하면 경로를 null로 리턴
			return null;
		}
		
		// 파일 업로드 : 생성 디렉토리에 이미지 파일을 넣어야 함
		try {
			// 1. 파일을 byte 데이터로 변환
			byte[] bytes =  file.getBytes();
			// 2. 파일의 최종경로를 path에 저장
			// ☆☆☆ 한글명으로 된 이미지는 업로드 불가!! 영문자로 바꿔주기!! ☆☆☆
			Path path = Paths.get(filePath + file.getOriginalFilename());
			// 3. 최종경로에 바이트데이터(실제파일)를 올림
			Files.write(path, bytes);
		} catch (IOException e) {
			// 에러가 나면 그냥 null 리턴하면 됨
			e.printStackTrace();
			return null;
		}
		
		// 일단 폴더에 이미지만 저장됨
		// ☆☆☆ 아직 images 폴더와 WAS가 연결되지 않음
		// 그래서 브라우저에서 해당 경로로 이미지를 요청해도 찾을 수 없음
		
		
		// 이미지 파일 경로까지 붙인 이미지 url path 반환
		// 주소는 이렇게 될 것이다.(예언)
		// /images/aaaa_1721207982638/sun.png <= DB에 저장되는 경로
		// 하지만 아직 매핑이 안되있어서 해당 경로에 접속해도 이미지가 안나옴 (http://localhost/images/aaaa_1721207982638/sun.png) 
		return "/images/" + directoryName + "/" + file.getOriginalFilename();
	}
}
