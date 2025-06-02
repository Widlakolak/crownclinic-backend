package com.crown.backend.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ForumMessage {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private ForumThread thread;

    @ManyToOne
    private User author;

    private String content;

    private LocalDateTime sentAt;
}
