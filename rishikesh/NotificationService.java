package rishikesh;

import ritesh.Inventory;
import rohit.Medicine;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationService Class
 * Author: Rishikesh
 * 
 * Central service for managing and sending notifications.
 * Stores notification history and provides retrieval methods.
 */
public class NotificationService {

    private List<Notification> notificationLog;
    
    private static int notificationIdCounter = 9000;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public NotificationService() {
        this.notificationLog = new ArrayList<>();
    }

    // -------------------------------------------------------
    // Notification Management
    // -------------------------------------------------------
    public void sendNotification(String message, NotificationType type, String recipient) {
        Notification n = new Notification(++notificationIdCounter, message, type, recipient);
        n.sendNotification();
        notificationLog.add(n);
    }

    public void sendNotification(Notification notification) {
        notification.sendNotification();
        notificationLog.add(notification);
    }

    public void markAsRead(int notificationId) {
        notificationLog.stream()
                .filter(n -> n.getNotificationId() == notificationId)
                .findFirst()
                .ifPresent(Notification::markAsRead);
    }

    // -------------------------------------------------------
    // Inventory Scanning
    // -------------------------------------------------------
    public void scanAndNotify(Inventory inventory) {
        System.out.println("\n[NotificationService] Scanning inventory for alerts...");
        
        List<Medicine> expired = new ArrayList<>();
        List<Medicine> nearExpiry = new ArrayList<>();
        List<Medicine> lowStock = new ArrayList<>();

        for (Medicine m : inventory.getMedicines()) {
            if (m.isExpired()) {
                expired.add(m);
            } else if (m.isNearExpiry(30)) {
                nearExpiry.add(m);
            }
            if (m.isLowStock()) {
                lowStock.add(m);
            }
        }

        if (!expired.isEmpty()) {
            for (Medicine m : expired) {
                sendNotification("Medicine EXPIRED: " + m.getName() + " (Batch: " + m.getBatchNumber() + ")",
                        NotificationType.EXPIRY_ALERT, null);
            }
        }

        if (!nearExpiry.isEmpty()) {
            for (Medicine m : nearExpiry) {
                sendNotification("Medicine EXPIRING SOON: " + m.getName() + " on " + m.getExpiryDate(),
                        NotificationType.EXPIRY_ALERT, null);
            }
        }

        if (!lowStock.isEmpty()) {
            for (Medicine m : lowStock) {
                sendNotification("LOW STOCK: " + m.getName() + " (" + m.getQuantity() + "/" + m.getMinimumStock() + ")",
                        NotificationType.LOW_STOCK_ALERT, null);
            }
        }

        if (expired.isEmpty() && nearExpiry.isEmpty() && lowStock.isEmpty()) {
            System.out.println("[NotificationService] No alerts found.");
        }
    }

    // -------------------------------------------------------
    // Queries
    // -------------------------------------------------------
    public List<Notification> getNotificationLog() {
        return new ArrayList<>(notificationLog);
    }

    public List<Notification> getUnreadNotifications() {
        return notificationLog.stream()
                .filter(n -> !n.isRead())
                .toList();
    }

    public long getNotificationCount() {
        return notificationLog.size();
    }

    public void printNotificationLog() {
        System.out.println("\n========== NOTIFICATION LOG ==========");
        if (notificationLog.isEmpty()) {
            System.out.println("(No notifications)");
        } else {
            notificationLog.forEach(System.out::println);
        }
        System.out.println("=====================================\n");
    }

    @Override
    public String toString() {
        return "NotificationService{" +
                "notifications=" + notificationLog.size() +
                "}";
    }
}
