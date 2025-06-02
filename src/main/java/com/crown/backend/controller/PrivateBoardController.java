package com.crown.backend.controller;


import com.crown.backend.domain.BoardMessage;
import com.crown.backend.domain.PrivateBoard;
import com.crown.backend.dto.BoardMessageDto;
import com.crown.backend.dto.PrivateBoardDto;
import com.crown.backend.service.PrivateBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private-boards")
@RequiredArgsConstructor
public class PrivateBoardController {

    private final PrivateBoardService privateBoardService;

    @PostMapping
    public ResponseEntity<PrivateBoard> createBoard(@RequestBody PrivateBoardDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(privateBoardService.createBoard(dto));
    }

    @PostMapping("/{boardId}/messages")
    public ResponseEntity<BoardMessage> postMessage(@PathVariable Long boardId,
                                                    @RequestBody BoardMessageDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(privateBoardService.postMessage(boardId, dto));
    }

    @GetMapping("/{boardId}/messages")
    public List<BoardMessage> getMessages(@PathVariable Long boardId) {
        return privateBoardService.getMessages(boardId);
    }
}