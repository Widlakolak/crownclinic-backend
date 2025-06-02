package com.crown.backend.repository;

import com.crown.backend.domain.PrivateBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateBoardRepository extends JpaRepository<PrivateBoard, Long> {}