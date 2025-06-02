package com.crown.backend.controller;

import com.crown.backend.dto.BoardPostDto;
import com.crown.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/public")
    public ResponseEntity<BoardPostDto> createPublicPost(
            @RequestPart("content") String content,
            @RequestPart("authorId") Long authorId,
            @RequestPart("attachments") MultipartFile[] files) {

        return ResponseEntity.ok(boardService.createPublicPost(content, authorId, files));
    }

    @GetMapping("/public")
    public List<BoardPostDto> getAllPublicPosts() {
        return boardService.getAllPublicPosts();
    }

    @PostMapping("/private")
    public ResponseEntity<BoardPostDto> createPrivatePost(
            @RequestPart("content") String content,
            @RequestPart("authorId") Long authorId,
            @RequestPart("recipientIds") List<Long> recipients,
            @RequestPart("attachments") MultipartFile[] files) {

        return ResponseEntity.ok(boardService.createPrivatePost(content, authorId, recipients, files));
    }

    @GetMapping("/private/{userId}")
    public List<BoardPostDto> getPrivateBoardForUser(@PathVariable Long userId) {
        return boardService.getPrivatePostsVisibleTo(userId);
    }
}
