package com.crown.backend.controller;

import com.crown.backend.domain.BoardMessage;
import com.crown.backend.domain.PublicBoard;
import com.crown.backend.dto.BoardMessageDto;
import com.crown.backend.service.PublicBoardService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/public-boards")
@RequiredArgsConstructor
public class PublicBoardController {

    private final PublicBoardService publicBoardService;

    @PostMapping
    public ResponseEntity<PublicBoard> createBoard(@RequestParam String title) {
        PublicBoard board = publicBoardService.createBoard(title);
        return new ResponseEntity<>(board, HttpStatus.CREATED);
    }

    @PostMapping("/{boardId}/messages")
    public ResponseEntity<BoardMessage> postMessage(@PathVariable Long boardId,
                                                    @RequestBody BoardMessageDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(publicBoardService.postMessage(boardId, dto));
    }

    @GetMapping("/{boardId}/messages")
    public List<BoardMessage> getMessages(@PathVariable Long boardId) {
        return publicBoardService.getMessages(boardId);
    }

    @GetMapping
    public ResponseEntity<List<PublicBoard>> getAllBoards() {
        List<PublicBoard> boards = publicBoardService.getAllBoards();
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }
}

