package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

@Service
public class PostBO {

	@Autowired
	private PostMapper postMapper;
	
	// 글목록
	// input: 로그인 된 사람의 userId
	// output: List<Post>
	public List<Post> getPostListByUserId(int userId) {
		return postMapper.selectPostListByUserId(userId);
	}
	
	// 글삽입
	// input: userId, subject, content, imagePath
	// output: 추가된 Post
	public Post addPost(int userId, String subject, String content, String imagePath) {
		return postMapper.insertPost(userId, subject, content, imagePath);
	}
}
