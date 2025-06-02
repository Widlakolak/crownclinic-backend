package com.crown.backend.service;

import com.crown.backend.domain.User;
import com.crown.backend.dto.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BroadcastMessageService {

    private final UserService userService;
    private final MessageService messageService;

    public void sendToAllDoctors(MessageRequestDto dto, MultipartFile[] files) {
        List<User> doctors = userService.findAll().stream()
                .filter(user -> user.getRole() == User.Role.DOCTOR)
                .toList();

        for (User doctor : doctors) {
            MessageRequestDto updated = new MessageRequestDto(
                    dto.senderId(), doctor.getId(), dto.subject(), dto.content()
            );
            messageService.sendMessage(updated, files);
        }
    }

    public void sendToSelectedDoctors(Long senderId, List<Long> doctorIds, String subject, String content, MultipartFile[] files) {
        for (Long id : doctorIds) {
            MessageRequestDto dto = new MessageRequestDto(senderId, id, subject, content);
            messageService.sendMessage(dto, files);
        }
    }
}
