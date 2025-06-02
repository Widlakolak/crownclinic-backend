package com.crown.backend.controller;

import com.crown.backend.domain.DoctorAvailability;
import com.crown.backend.service.DoctorAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService availabilityService;

    @GetMapping
    public List<DoctorAvailability> getAll() {
        return availabilityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorAvailability> getById(@PathVariable Long id) {
        return availabilityService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DoctorAvailability> create(@RequestBody @Valid DoctorAvailability availability) {
        return ResponseEntity.ok(availabilityService.save(availability));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        availabilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
