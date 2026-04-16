package rishikesh;

import ritwik.RegularUser;
import rohit.Medicine;
import ritesh.Inventory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ReminderScheduler Class
 * Author: Rishikesh
 *
 * Central scheduler that:
 *   1. Holds all Reminder objects across all users
 *   2. Fires due reminders on demand (simulating a cron job)
 *   3. Auto-creates EXPIRY_ALERT Reminders for near-expiry medicines
 *   4. Provides a daily digest summary
 */
public class ReminderScheduler {

    private List<Reminder>      allReminders         = new ArrayList<>();
    private NotificationService notificationService  = new NotificationService();
    private int                 reminderIdCounter    = 500;

    // -------------------------------------------------------
    // Reminder Registration
    // -------------------------------------------------------

    /**
     * Registers a new reminder into the scheduler.
     */
    public void register(Reminder reminder) {
        allReminders.add(reminder);
        System.out.println("[Scheduler] Registered: " + reminder);
    }

    /**
     * Creates and schedules a one-time reminder for a user
     * with auto-assigned ID.
     */
    public Reminder createReminder(RegularUser user, LocalDate date, String frequency) {
        Reminder r = new Reminder(++reminderIdCounter, date, frequency, user);
        r.scheduleReminder();
        allReminders.add(r);
        return r;
    }

    // -------------------------------------------------------
    // Auto-Generate Reminders from Inventory
    // -------------------------------------------------------

    /**
     * Scans the given inventory and auto-creates WEEKLY reminders
     * for medicines expiring within the next 30 days.
     *
     * @param inventory the inventory to scan
     * @param user      the user who owns it
     */
    public void autoScheduleExpiryReminders(Inventory inventory, RegularUser user) {
        LocalDate cutoff = LocalDate.now().plusDays(30);

        List<Medicine> nearExpiry = inventory.getMedicines().stream()
                .filter(m -> !m.isExpired()
                          && !m.getExpiryDate().isAfter(cutoff))
                .collect(Collectors.toList());

        if (nearExpiry.isEmpty()) {
            System.out.println("[Scheduler] No near-expiry medicines found. No auto-reminders created.");
            return;
        }

        System.out.println("[Scheduler] Auto-creating expiry reminders for "
                + nearExpiry.size() + " medicine(s):");

        for (Medicine m : nearExpiry) {
            Reminder r = new Reminder(
                    ++reminderIdCounter,
                    m.getExpiryDate().minusDays(7), // 7 days before expiry
                    "ONCE",
                    user
            );
            r.scheduleReminder();
            allReminders.add(r);
            System.out.println("  -> Reminder for '" + m.getName()
                    + "' expiring " + m.getExpiryDate());
        }
    }

    // -------------------------------------------------------
    // Run Due Reminders
    // -------------------------------------------------------

    /**
     * Checks all active reminders and fires notifications
     * for any that are due today or overdue.
     */
    public void runDueReminders() {
        System.out.println("\n[Scheduler] Checking due reminders (today: "
                + LocalDate.now() + ")...");

        List<Reminder> due = allReminders.stream()
                .filter(Reminder::isDue)
                .collect(Collectors.toList());

        if (due.isEmpty()) {
            System.out.println("[Scheduler] No reminders due today.");
            return;
        }

        for (Reminder r : due) {
            Notification n = new Notification(
                    9000 + r.getReminderId(),
                    "Reminder: Please check your medicine inventory. "
                            + "(Freq: " + r.getFrequency() + ")",
                    NotificationType.REMINDER,
                    null
            );
            n.sendNotification();

            // For ONCE reminders, cancel after firing
            if (r.getFrequency().equals("ONCE")) {
                r.cancelReminder();
            }
        }
    }

    // -------------------------------------------------------
    // Daily Digest
    // -------------------------------------------------------

    /**
     * Prints a digest of all scheduled reminders grouped by status.
     */
    public void printDailyDigest() {
        System.out.println("\n========== DAILY REMINDER DIGEST ==========");
        System.out.println("Date: " + LocalDate.now());
        System.out.println("Total Reminders: " + allReminders.size());

        long active    = allReminders.stream().filter(Reminder::isActive).count();
        long cancelled = allReminders.size() - active;

        System.out.println("  Active    : " + active);
        System.out.println("  Cancelled : " + cancelled);
        System.out.println("\n  Upcoming:");

        allReminders.stream()
                .filter(Reminder::isActive)
                .sorted(Comparator.comparing(Reminder::getReminderDate))
                .forEach(r -> System.out.println("    " + r));

        System.out.println("===========================================\n");
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------
    public List<Reminder> getAllReminders()          { return allReminders; }
    public NotificationService getNotificationService() { return notificationService; }
}
