package com.memo.post.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.ToString;

@ToString
@Data // lombok으로 자동 getter/setter
public class Post {
	// field
	private int id;
	private int userId;
	private String subject;
	private String content;
	private String imagePath;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
