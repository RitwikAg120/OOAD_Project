package ritwik;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * UserSession Class
 * Author: Ritwik
 *
 * Represents an active login session for a User.
 * Tracks session token, login time, and expiry.
 * In production, this would be stored in Redis or a JWT store.
 */
public class UserSession {

    private String        sessionToken;
    private User          user;
    private LocalDateTime loginTime;
    private LocalDateTime expiryTime;
    private boolean       active;

    private static final int SESSION_DURATION_HOURS = 8;
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public UserSession(User user) {
        this.user         = user;
        this.sessionToken = UUID.randomUUID().toString();
        this.loginTime    = LocalDateTime.now();
        this.expiryTime   = loginTime.plusHours(SESSION_DURATION_HOURS);
        this.active       = true;
    }

    // -------------------------------------------------------
    // Methods
    // -------------------------------------------------------

    /**
     * Checks whether this session is still valid (active + not expired).
     */
    public boolean isValid() {
        if (!active) return false;
        if (LocalDateTime.now().isAfter(expiryTime)) {
            active = false;
            return false;
        }
        return true;
    }

    /**
     * Invalidates this session (logout or timeout).
     */
    public void invalidate() {
        this.active = false;
        System.out.println("[Session] Session invalidated for: " + user.getName());
    }

    /**
     * Prints a summary of this session.
     */
    public void printInfo() {
        System.out.println("[Session] User    : " + user.getName() + " (" + user.getRole() + ")");
        System.out.println("[Session] Token   : " + sessionToken.substring(0, 8) + "...");
        System.out.println("[Session] Login   : " + loginTime.format(FMT));
        System.out.println("[Session] Expires : " + expiryTime.format(FMT));
        System.out.println("[Session] Valid   : " + isValid());
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------
    public String getSessionToken()      { return sessionToken; }
    public User getUser()                { return user; }
    public LocalDateTime getLoginTime()  { return loginTime; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public boolean isActive()            { return active; }
}
