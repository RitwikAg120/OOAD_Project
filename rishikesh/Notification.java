package rishikesh;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Notification Class
 * Author: Rishikesh
 * 
 * Represents a notification sent to a user.
 * Tracks notification type, message, and timestamp.
 */
public class Notification {

    private int                 notificationId;
    private String              message;
    private NotificationType    type;
    private LocalDateTime       timestamp;
    private String              recipient;
    private boolean             isRead;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Notification(int notificationId, String message, NotificationType type, String recipient) {
        this.notificationId = notificationId;
        this.message = message;
        this.type = type;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    // -------------------------------------------------------
    // Operations
    // -------------------------------------------------------
    public void sendNotification() {
        System.out.println("[" + type + "] " + message);
    }

    public void markAsRead() {
        this.isRead = true;
    }

    // -------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------
    public int getNotificationId() { return notificationId; }
    public String getMessage() { return message; }
    public NotificationType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getRecipient() { return recipient; }
    public boolean isRead() { return isRead; }

    @Override
    public String toString() {
        return notificationId + " | [" + type + "] " + message + " (" + timestamp.format(FMT) + ")";
    }
}
