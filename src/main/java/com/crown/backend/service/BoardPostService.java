package com.crown.backend.service;

import com.crown.backend.domain.BoardPost;
import com.crown.backend.domain.User;
import com.crown.backend.repository.BoardPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardPostService {

    private final BoardPostRepository repository;
    private final UserService userService;

    public BoardPost createPost(String title, String content, Long authorId) {
        User author = userService.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Brak użytkownika"));

        BoardPost post = BoardPost.builder()
                .title(title)
                .content(content)
                .author(author)
                .postedAt(LocalDateTime.now())
                .build();

        return repository.save(post);
    }

    public List<BoardPost> getAllPosts() {
        return repository.findAll();
    }
}
