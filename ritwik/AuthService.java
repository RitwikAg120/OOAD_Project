package ritwik;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthService Class
 * Author: Ritwik
 *
 * Central authentication service. Manages user registration,
 * credential validation, and session lifecycle.
 *
 * Acts as the single source of truth for who is logged in.
 */
public class AuthService {

    // email -> User
    private Map<String, User> userStore    = new HashMap<>();
    // sessionToken -> UserSession
    private Map<String, UserSession> sessions = new HashMap<>();

    // -------------------------------------------------------
    // User Registration
    // -------------------------------------------------------

    /**
     * Registers a new user into the auth system.
     * Duplicate emails are rejected.
     *
     * @param user the user to register
     * @return true if registered successfully
     */
    public boolean registerUser(User user) {
        if (userStore.containsKey(user.getEmail())) {
            System.out.println("[AuthService] Email already registered: " + user.getEmail());
            return false;
        }
        userStore.put(user.getEmail(), user);
        System.out.println("[AuthService] Registered: " + user.getName()
                + " (" + user.getRole() + ")");
        return true;
    }

    // -------------------------------------------------------
    // Login / Logout
    // -------------------------------------------------------

    /**
     * Authenticates a user by email + plain-text password.
     * Returns a UserSession if valid, null otherwise.
     *
     * @param email    the user's email
     * @param password the plain-text password
     * @return a new UserSession, or null on failure
     */
    public UserSession login(String email, String password) {
        User user = userStore.get(email);
        if (user == null) {
            System.out.println("[AuthService] No account found for: " + email);
            return null;
        }

        String inputHash = Integer.toHexString(password.hashCode());
        if (!user.getPasswordHash().equals(inputHash)) {
            System.out.println("[AuthService] Incorrect password for: " + email);
            return null;
        }

        // Invalidate any existing session for this user
        sessions.values().stream()
                .filter(s -> s.getUser().getEmail().equals(email) && s.isActive())
                .forEach(UserSession::invalidate);

        UserSession session = new UserSession(user);
        sessions.put(session.getSessionToken(), session);
        System.out.println("[AuthService] Login successful. Welcome, " + user.getName() + "!");
        return session;
    }

    /**
     * Logs out the given session by its token.
     *
     * @param sessionToken the token to invalidate
     */
    public void logout(String sessionToken) {
        UserSession session = sessions.get(sessionToken);
        if (session != null) {
            session.invalidate();
            user(session).logout();
        } else {
            System.out.println("[AuthService] Session not found: " + sessionToken);
        }
    }

    // -------------------------------------------------------
    // Session Validation
    // -------------------------------------------------------

    /**
     * Validates a session token and returns the session if active.
     *
     * @param token the session token
     * @return the UserSession if valid, null otherwise
     */
    public UserSession validateSession(String token) {
        UserSession session = sessions.get(token);
        if (session == null || !session.isValid()) {
            System.out.println("[AuthService] Invalid or expired session.");
            return null;
        }
        return session;
    }

    /**
     * Returns whether the given session has ADMIN privileges.
     */
    public boolean isAdmin(String token) {
        UserSession session = validateSession(token);
        return session != null && session.getUser().getRole() == Role.ADMIN;
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------

    private User user(UserSession s) { return s.getUser(); }

    /**
     * Lists all currently active sessions (admin use).
     */
    public void listActiveSessions() {
        System.out.println("\n[AuthService] Active Sessions:");
        sessions.values().stream()
                .filter(UserSession::isValid)
                .forEach(UserSession::printInfo);
    }

    public int getTotalUsers()         { return userStore.size(); }
    public Map<String, User> getUsers() { return userStore; }
}
