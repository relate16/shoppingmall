package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.PostDto;
import com.example.shoppingmall.entity.Post;
import com.example.shoppingmall.exception.NotFoundException;
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

    @Transactional
    public void addRead(Post post) {
        post.addRead();
    }

    @Transactional
    public void createPost(PostDto postDto) {
        Post post = new Post(postDto.getTitle(), postDto.getContent(), postDto.getWriter(), 0);
        postRepository.save(post);
    }

    public Post findById(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElseThrow(() -> new NotFoundException("해당 post가 없습니다."));
        return post;
    }
}
