package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {

	@Autowired
	private UserBO userBO;
	
	
	/**
	 * 아이디 중복확인 API
	 * @param loginId
	 * @return
	 */
	@RequestMapping("/is-duplicated-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		
		// DB 조회
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		
		// AJAX 리턴
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		if (user != null) { // 중복
			result.put("is_duplicated_id", true);
		} else {			// 중복X
			result.put("is_duplicated_id", false);
		}
		return result;
	} //-- 아이디 중복확인 API
	
	/**
	 * 회원가입 API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
		// password md5 알고리즘 => hashing (복호화 불가)
		// aaaa => 74b8733745420d4d33f80c4663dc5e5
		String hashedPassword = EncryptUtils.md5(password);
		
		// db insert
		UserEntity user = userBO.addUser(loginId, hashedPassword, name, email);
		
		// AJAX 리턴
		Map<String, Object> result = new HashMap<>();
		if (user != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패했습니다.");
		}
		
		return result;
	} //-- 회원가입 API
	
	// 로그인 API
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		// hashed password
		String hashedPassword = EncryptUtils.md5(password);
		
		// DB 조회(JPA): loginId, hashedPassword => UserEntity/null
		UserEntity user = userBO.getUserEntityByLoginIdAndPassword(loginId, hashedPassword);
		
		// !! 로그인 처리 및 응답값
		Map<String, Object> result = new HashMap<>();
		if (user != null) { // 로그인 성공
			// 세션에 사용자 정보를 담는다.(사용자(브라우저) 각각 마다)
			HttpSession session = request.getSession(); // 비어있는 session 주머니, HttpSession으로 하면 굳이 안해도 됨
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
			
			result.put("code", 200);
			result.put("result", "성공");			
		} else { // 로그인 실패
			result.put("code", 403); // (403: 권한없음)
			result.put("error_message", "로그인 정보를 다시 확인 해주세요.");
		}
		
		// AJAX return
		return result;
	}
}
