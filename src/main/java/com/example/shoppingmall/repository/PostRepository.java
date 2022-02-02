package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
