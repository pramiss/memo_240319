package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

@Service
public class PostBO {

	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManagerService;
	
	// 글목록
	// input: 로그인 된 사람의 userId
	// output: List<Post>
	public List<Post> getPostListByUserId(int userId) {
		return postMapper.selectPostListByUserId(userId);
	}
	
	// 글저장
	// input: userId, subject, content, imagePath
	// output: rowCount
	public void addPost(int userId, String userLoginId,
			String subject, String content, MultipartFile file) {
		String imagePath = null;
		
		// 로컬에 파일 업로드 및 이미지 경로 반환
		if (file != null) {
			// 업로드 할 이미지가 있을 때에만 업로드
			imagePath = fileManagerService.uploadFile(file, userLoginId);
		}
		
		// DB 저장
		postMapper.insertPost(userId, subject, content, imagePath);
	}
}
