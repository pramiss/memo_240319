package com.memo.post.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.memo.post.domain.Post;

@Mapper
public interface PostMapper {
	public List<Map<String, Object>> selectPostListTest();
	
	// 글 리스트 가져오기
	public List<Post> selectPostListByUserId(int userId);
	// 글 1개 가져오기
	public Post selectPostByPostIdOrUserId(
			@Param("postId") int postId,
			@Param("userId") Integer userId);
	// 글 추가
	public void insertPost(
			@Param("userId") int userId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	// 글 수정
	public void updatePostByPostId(
			@Param("postId") int postId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	// 글삭제
	public void deletePostByPostId(int postId);
	
}
