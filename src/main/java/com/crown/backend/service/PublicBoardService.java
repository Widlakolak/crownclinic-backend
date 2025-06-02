package com.crown.backend.service;

import com.crown.backend.domain.PublicBoard;
import com.crown.backend.repository.PublicBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicBoardService {

    private final PublicBoardRepository publicBoardRepository;

    public PublicBoard createBoard(String title) {
        PublicBoard board = PublicBoard.builder()
                .title(title)
                .build();
        return publicBoardRepository.save(board);
    }

    public List<PublicBoard> getAllBoards() {
        return publicBoardRepository.findAll();
    }
}
