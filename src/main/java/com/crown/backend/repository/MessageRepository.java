package com.crown.backend.repository;

import com.crown.backend.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRecipientId(Long recipientId);

    List<Message> findBySenderId(Long senderId);

    List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :user1 AND m.recipient.id = :user2) OR " +
            "(m.sender.id = :user2 AND m.recipient.id = :user1) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findThreadBetween(@Param("user1") Long user1, @Param("user2") Long user2);
}