package com.memo.post.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.memo.post.domain.Post;

@Mapper
public interface PostMapper {
	public List<Map<String, Object>> selectPostListTest();
	
	// 글 목록
	public List<Post> selectPostListByUserId(int userId);
	// 글 추가
	public void insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	// 글상세
	public Post selectPostByIdAndUserId(
			@Param("userId") int userId,
			@Param("postId") int postId);
}
