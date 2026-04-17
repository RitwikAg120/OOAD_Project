package com.medicinetracker.controller;

import com.medicinetracker.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "*")
public class ReminderController {

    private static final Map<Integer, ReminderDTO> reminders = new HashMap<>();
    private static int reminderIdCounter = 500;

    static {
        reminders.put(500, new ReminderDTO(500, "Check Aspirin Stock", "WEEKLY", LocalDate.now().plusDays(3), true));
        reminders.put(501, new ReminderDTO(501, "Paracetamol Inventory Review", "MONTHLY", LocalDate.now().plusDays(24), true));
        reminders.put(502, new ReminderDTO(502, "Vitamin D Refill", "ONCE", LocalDate.now().minusDays(5), false));
    }

    @GetMapping
    public ResponseEntity<List<ReminderDTO>> getAllReminders() {
        return ResponseEntity.ok(new ArrayList<>(reminders.values()));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ReminderDTO>> getActiveReminders() {
        List<ReminderDTO> active = new ArrayList<>();
        for (ReminderDTO r : reminders.values()) {
            if (r.active) {
                active.add(r);
            }
        }
        return ResponseEntity.ok(active);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createReminder(@RequestBody ReminderDTO reminderDTO) {
        reminderDTO.id = ++reminderIdCounter;
        reminders.put(reminderDTO.id, reminderDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Reminder created successfully", reminderDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReminder(@PathVariable int id) {
        if (reminders.remove(id) != null) {
            return ResponseEntity.ok(new ApiResponse(true, "Reminder cancelled"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelReminder(@PathVariable int id) {
        ReminderDTO reminder = reminders.get(id);
        if (reminder != null) {
            reminder.active = false;
            return ResponseEntity.ok(new ApiResponse(true, "Reminder cancelled"));
        }
        return ResponseEntity.notFound().build();
    }

    // Helper class
    @lombok.Data
    @lombok.NoArgsConstructor
    public static class ReminderDTO {
        public int id;
        public String title;
        public String frequency;
        public LocalDate nextDue;
        public boolean active;

        public ReminderDTO(int id, String title, String frequency, LocalDate nextDue, boolean active) {
            this.id = id;
            this.title = title;
            this.frequency = frequency;
            this.nextDue = nextDue;
            this.active = active;
        }
    }
}
