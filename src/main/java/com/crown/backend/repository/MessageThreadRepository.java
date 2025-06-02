package com.crown.backend.repository;

import com.crown.backend.domain.MessageThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageThreadRepository extends JpaRepository<MessageThread, Long> {
}
