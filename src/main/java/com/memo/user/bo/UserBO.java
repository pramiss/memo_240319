package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.user.entity.UserEntity;
import com.memo.user.repository.UserRepository;

@Service
public class UserBO {
	
	@Autowired
	UserRepository userRepository;
	
	// input: loginId, output: UserEntity/null
	public UserEntity getUserEntityByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}
	
	// input: loginId&password, output: UserEntity/null
	public UserEntity getUserEntityByLoginIdAndPassword(String loginId, String password) {
		return userRepository.findByLoginIdAndPassword(loginId, password);
	}
	
	// input: 4params, output: UserEntity/null
	public UserEntity addUser(String loginId, String password, String name, String email) {
		// JPA는 save-builder로 entity로 넘긴다.
		return userRepository.save(UserEntity.builder()
				.loginId(loginId)
				.password(password)
				.name(name)
				.email(email)
				.build());
		// save 후 저장한 entity를 돌려줌
	}
}
