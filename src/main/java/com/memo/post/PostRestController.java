package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class PostRestController {
	
	@Autowired
	private PostBO postBO;

	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value = "file", required = false) MultipartFile file, // path가 아닌 file로 들어옴
			HttpSession session) {
		// 세션에서 글쓴이 번호(userId) 가져오기
		// 로그인 검사는 따로 할 예정 - 그냥 int로 쓰기
		int userId = (int)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId"); // 파일 저장을 위해 받아옴. 사실 그냥 userId로 해도 됨
		
		// DB 삽입- BO에 직접 session을 넘기는 건 X. 분리되어야 함
		postBO.addPost(userId, userLoginId, subject, content, file);
		
		// AJAX return
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "성공");
		return result;
		
	} //-- post create
}
