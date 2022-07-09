package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.PostDto;
import com.example.shoppingmall.dto.PostSearchCondition;
import com.example.shoppingmall.entity.Post;
import com.example.shoppingmall.repository.PostQueryRepository;
import com.example.shoppingmall.repository.PostRepository;
import com.example.shoppingmall.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;


    @GetMapping("/noticeBoard")
    public String showNoticeBoard(@ModelAttribute PostSearchCondition postSearchCondition,
                                  Pageable pageable, Model model) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), 10);
        Page<PostDto> page = postQueryRepository.showPageList(pageRequest);
        model.addAttribute("page", page);
        return "board/noticeBoard";
    }

    @GetMapping("/noticeBoard/search")
    public String searchPosts(@ModelAttribute PostSearchCondition postSearchCondition, Pageable pageable,
                              Model model) {
        System.out.println("postSearchCondition = " + postSearchCondition);
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), 10);
        //스프링데이터JPA의 pageable이 제공하는 Sort기능으로 querydsl에서 동적조회로 사용하는 게 어려워
        // PageRequest.of(pageable.getPageNumber(), 9, Sort.by(Sort.Direction.DESC, "createdDate")
        // 를 안쓰고 PageRequest.of(pageable.getPageNumber(), 9)를 쓴 후 정렬은 메소드 내에서 직접처리.
        Page<PostDto> page = postQueryRepository.searchPostDtos(postSearchCondition, pageRequest);
        model.addAttribute("page", page);
        return "board/noticeBoard";
    }

    @GetMapping("/noticeBoard/write")
    public String showWriteForm(@ModelAttribute PostDto postDto, Model model) {
        return "board/boardWrite";
    }

    @PostMapping("/noticeBoard/write")
    public String createPost(@ModelAttribute PostDto postDto, Model model) {
        Post post = new Post(postDto.getTitle(), postDto.getContent(), postDto.getWriter(), 0);
        postRepository.save(post);
        return "redirect:/noticeBoard";
    }

    /**클릭시 조회수 증가 Controller 메소드.
     * 검색한 상태에서 클릭할 때 해당 페이지 유지하기엔 RequestParam으로 넘겨야 될 게 많아 구현 안했고
     * 그나마 편의성을 위해 미검색시, 클릭해도 그 페이지 유지하게 구현해놓음.
     */
    @GetMapping("/noticeBoard/read")
    public String readPost(@RequestParam("id") Long postId, @RequestParam("page") int pageNumber, RedirectAttributes redirectAttributes) {

        Post findPost = postService.findById(postId);
        postService.addRead(findPost);
        redirectAttributes.addAttribute("page", pageNumber);
        return "redirect:/noticeBoard/search";
    }
}
