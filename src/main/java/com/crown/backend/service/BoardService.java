package com.crown.backend.service;

import com.crown.backend.domain.PrivateBoard;
import com.crown.backend.domain.PublicBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final PublicBoardService publicBoardService;
    private final PrivateBoardService privateBoardService;

    public List<PublicBoard> getAllPublicBoards() {
        return publicBoardService.getAllBoards();
    }

    public List<PrivateBoard> getPrivateBoardsForUser(Long userId) {
        return privateBoardService.getBoardsForUser(userId);
    }
}

