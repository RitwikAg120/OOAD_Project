package rishikesh;

import ritwik.RegularUser;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Reminder Class
 * Author: Rishikesh
 * 
 * Represents a scheduled reminder for a user.
 * Can be set to fire once or on a recurring basis.
 */
public class Reminder {

    private int         reminderId;
    private LocalDate   reminderDate;
    private String      frequency;      // "ONCE", "DAILY", "WEEKLY", "MONTHLY"
    private RegularUser user;
    private boolean     active;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Reminder(int reminderId, LocalDate reminderDate, String frequency, RegularUser user) {
        this.reminderId = reminderId;
        this.reminderDate = reminderDate;
        this.frequency = frequency;
        this.user = user;
        this.active = true;
    }

    // -------------------------------------------------------
    // Queries
    // -------------------------------------------------------
    public boolean isDue() {
        if (!active) return false;
        LocalDate today = LocalDate.now();
        
        switch (frequency) {
            case "ONCE":
                return today.isEqual(reminderDate) || today.isAfter(reminderDate);
            case "DAILY":
                return true;
            case "WEEKLY":
                return ChronoUnit.DAYS.between(reminderDate, today) % 7 == 0;
            case "MONTHLY":
                return today.getDayOfMonth() == reminderDate.getDayOfMonth();
            default:
                return false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public void scheduleReminder() {
        System.out.println("[Scheduler] Reminder scheduled: " + this);
    }

    public void cancelReminder() {
        this.active = false;
    }

    // -------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------
    public int getReminderId() { return reminderId; }
    public LocalDate getReminderDate() { return reminderDate; }
    public String getFrequency() { return frequency; }
    public RegularUser getUser() { return user; }

    @Override
    public String toString() {
        return String.format("Reminder{id=%d, date=%s, freq=%s, user=%s, active=%s}",
                reminderId, reminderDate, frequency, user.getName(), active);
    }
}
