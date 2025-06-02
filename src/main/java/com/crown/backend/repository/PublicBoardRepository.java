package com.crown.backend.repository;

import com.crown.backend.domain.PublicBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicBoardRepository extends JpaRepository<PublicBoard, Long> {}
