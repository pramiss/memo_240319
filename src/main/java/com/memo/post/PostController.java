package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글목록
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/post-list-view")
	public String postListView(
			@RequestParam(value = "prevId", required = false) Integer prevIdParam,
			@RequestParam(value = "nextId", required = false) Integer nextIdParam,
			HttpSession session,
			Model model) {
		// 로그인 여부 확인
		Integer userId = (Integer)session.getAttribute("userId");
		
		// 로그인X => 로그인 화면으로 redirect
		if (userId == null) {
			return "redirect:/user/sign-in-view";
		}
		
		// DB 조회 - 글목록
		List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
		int prevId = 0;
		int nextId = 0;
		if (postList.isEmpty() == false) { // 글 목록이 비어있지 않을 때
			prevId = postList.get(0).getId(); // 첫번째칸 id
			nextId = postList.get(postList.size() - 1).getId(); // 마지막칸 id
			
			// 이전 방향의 끝인가? 그러면 0
			// prevId와 테이블의 제일 큰 숫자와 같으면 이전의 끝페이지
			if (postBO.isPrevLastPageByUserId(userId, prevId)) {
				prevId = 0;
			}
			
			// 다음 방향의 끝인가? 그러면 0
			// nextId와 테이블의 제일 작은 숫자가 같으면 다음의 끝페이지
			if (postBO.isNextLastPageByUserId(userId, prevId)) {
				nextId = 0;
			}
		}
		
		// 모델에 담기
		model.addAttribute("prevId", prevId);
		model.addAttribute("nextId", nextId);
		model.addAttribute("postList", postList);
		
		// 로그인O
		return "post/postList";
	} //-- 글목록
	
	/**
	 * 글쓰기 화면
	 * @return
	 */
	@GetMapping("/post-create-view")
	public String postCreateView() {
		return "post/postCreate";
	} //-- 글쓰기
	
	@GetMapping("/post-detail-view")
	public String postDetailVeiw(
			@RequestParam("postId") int postId,
			Model model,
			HttpSession session) {
		// DB 조회 - userId, postId
		int userId = (int)session.getAttribute("userId");
		Post post = postBO.getPostByIdAndUserId(userId, postId);
		
		// model 담기
		model.addAttribute("post", post);
		
		// 화면이동
		return "post/postDetail";
	}
}
