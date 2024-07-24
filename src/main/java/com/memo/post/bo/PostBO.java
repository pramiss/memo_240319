package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j //-- log 사용가능
@Service
public class PostBO {

//	private Logger log = LoggerFactory.getLogger(PostBO.class);
//	private Logger log = LoggerFactory.getLogger(this.getClass());
	
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
	
	// 글상세
	// input: userId, postId
	// output: Post/null
	public Post getPostByIdAndUserId(int userId, int postId) {
		return postMapper.selectPostByPostIdOrUserId(postId, userId);
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
	
	// 글수정
	// input: params, output: X
	public void updatePostByPostId(
			int postId, String subject, String content,
			MultipartFile file, int userId, String userLoginId) {
		
		// 기존글 가져온다.(1. 이미지 교체 시 삭제 위해, 2. (관습적)업데이트 대상이 있는지 확인)
		Post post = postMapper.selectPostByPostIdOrUserId(postId, userId);
		// System.out.println(); -> 웹에선 사용금지!!
		if (post == null) {
			log.warn("[글 수정] post is null. userId:{}, postId:{}", userId, postId);
			return;
		}
		
		// 파일이 있으면
		// 1) 새 이미지를 업로드
		// 2) 1번 단계가 성공하면 기존 이미지가 있을 때 삭제
		
		String imagePath = null;
		
		if (file != null) {
			// 새 이미지 업로드
			imagePath = fileManagerService.uploadFile(file, userLoginId);
			
			// 이미지업로드성공(not null) AND 기존이미지존재 => 기존 이미지 삭제
			if (imagePath != null && post.getImagePath() != null) {
				// 폴더와 이미지 제거(서버에서)
				fileManagerService.deleteFile(post.getImagePath());
			}
		}
		
		// DB update
		postMapper.updatePostByPostId(postId, subject, content, imagePath);
		
	} //-- 글수정
	
	// 글삭제
	// input: postId, output: void
	public void deletePostByPostId(int postId) {
		// 삭제할 post 조회
		Post post = postMapper.selectPostByPostIdOrUserId(postId, null);
		if (post == null) {
			log.warn("[글 삭제] post is null. postId:{}", postId);
			return;
		}
		
		// post에 이미지가 있는지 확인 -> 있으면 삭제 아님 말고.
		if (post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}
		
		// 해당 글 삭제
		postMapper.deletePostByPostId(postId);
	}
}
