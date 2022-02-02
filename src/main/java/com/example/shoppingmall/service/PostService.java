package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Post;
import com.example.shoppingmall.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post findById(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElseThrow(() -> new IllegalStateException("Post에 찾는 id가 없습니다."));
        return post;
    }

    @Transactional
    public void addRead(Post post) {
        post.addRead();
    }
}
