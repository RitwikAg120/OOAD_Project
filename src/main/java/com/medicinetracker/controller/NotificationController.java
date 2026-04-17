package com.medicinetracker.controller;

import com.medicinetracker.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private static final Map<Integer, NotificationDTO> notifications = new HashMap<>();
    private static int notificationIdCounter = 9000;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");

    static {
        notifications.put(9000, new NotificationDTO(9000, "Aspirin Expiring Soon", "Your Aspirin 500mg expires on April 15, 2026", "EXPIRY", LocalDateTime.now().minusHours(2), false));
        notifications.put(9001, new NotificationDTO(9001, "Low Stock Alert", "Amoxicillin stock is below minimum level", "LOW_STOCK", LocalDateTime.now().minusHours(5), false));
        notifications.put(9002, new NotificationDTO(9002, "Cough Syrup Expiring", "Your Cough Syrup expires on May 20, 2026", "EXPIRY", LocalDateTime.now().minusDays(1), false));
        notifications.put(9003, new NotificationDTO(9003, "Reminder Due", "Weekly inventory check reminder is due", "REMINDER", LocalDateTime.now().minusDays(3), true));
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(new ArrayList<>(notifications.values()));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        List<NotificationDTO> unread = new ArrayList<>();
        for (NotificationDTO n : notifications.values()) {
            if (!n.isRead) {
                unread.add(n);
            }
        }
        return ResponseEntity.ok(unread);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable int id) {
        NotificationDTO notification = notifications.get(id);
        if (notification != null) {
            notification.isRead = true;
            return ResponseEntity.ok(new ApiResponse(true, "Notification marked as read"));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getNotificationCount() {
        Map<String, Object> count = new HashMap<>();
        long unreadCount = notifications.values().stream().filter(n -> !n.isRead).count();
        count.put("total", notifications.size());
        count.put("unread", unreadCount);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable int id) {
        if (notifications.remove(id) != null) {
            return ResponseEntity.ok(new ApiResponse(true, "Notification deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    // Helper class
    @lombok.Data
    @lombok.NoArgsConstructor
    public static class NotificationDTO {
        public int id;
        public String title;
        public String message;
        public String type;
        public LocalDateTime timestamp;
        public boolean isRead;

        public NotificationDTO(int id, String title, String message, String type, LocalDateTime timestamp, boolean isRead) {
            this.id = id;
            this.title = title;
            this.message = message;
            this.type = type;
            this.timestamp = timestamp;
            this.isRead = isRead;
        }

        public String getFormattedTime() {
            return timestamp.format(FMT);
        }
    }
}
