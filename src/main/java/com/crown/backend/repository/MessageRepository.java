package com.crown.backend.repository;

import com.crown.backend.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderId(Long senderId);

    @Query("""
        SELECT m FROM Message m 
        JOIN m.conversation c 
        JOIN c.participants p 
        WHERE p.id = :userId AND m.sender.id <> :userId
        ORDER BY m.sentAt DESC
    """)
    List<Message> findInboxForUser(@Param("userId") Long userId);

    @Query("""
        SELECT m FROM Message m
        JOIN m.conversation c
        JOIN c.participants p1
        JOIN c.participants p2
        WHERE p1.id = :user1 AND p2.id = :user2
        ORDER BY m.sentAt
    """)
    List<Message> findConversationThread(@Param("user1") Long user1, @Param("user2") Long user2);
}
