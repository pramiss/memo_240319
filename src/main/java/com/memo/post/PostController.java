package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	
	@Autowired
	private PostBO postBO;
	
	@GetMapping("/post-list-view")
	public String postListView(
			HttpSession session,
			Model model) {
		// 로그인 여부 확인
		Integer userId = (Integer)session.getAttribute("userId");
		
		// 로그인X => 로그인 화면으로 redirect
		if (userId == null) {
			return "redirect:/user/sign-in-view";
		}
		
		// DB 조회 - 글목록
		List<Post> postList = postBO.getPostListByUserId(userId);
		
		// 모델에 담기
		model.addAttribute("postList", postList);
		
		// 로그인O
		return "post/postList";
	}
	
	/**
	 * 글쓰기 화면
	 * @return
	 */
	@GetMapping("/post-create-view")
	public String postCreateView() {
		return "post/postCreate";
	}
}
