package com.crown.backend.service;

import com.crown.backend.domain.PrivateBoard;
import com.crown.backend.domain.User;
import com.crown.backend.repository.PrivateBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateBoardService {

    private final PrivateBoardRepository privateBoardRepository;
    private final UserService userService;

    public PrivateBoard createBoard(String title, List<Long> userIds) {
        List<User> members = userIds.stream()
                .map(id -> userService.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id)))
                .toList();

        PrivateBoard board = PrivateBoard.builder()
                .title(title)
                .members(members)
                .build();

        return privateBoardRepository.save(board);
    }

    public List<PrivateBoard> getBoardsForUser(Long userId) {
        return privateBoardRepository.findByMembers_Id(userId);
    }
}

