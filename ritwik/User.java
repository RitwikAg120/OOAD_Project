package ritwik;

/**
 * User Abstract Class
 * Author: Ritwik
 * 
 * Base class for all users in the system.
 * Provides common user fields and properties.
 */
public abstract class User {
    
    private int     userId;
    private String  name;
    private String  email;
    private String  passwordHash;
    private Role    role;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public User(int userId, String name, String email, String passwordHash, Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // -------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public void logout() {
        System.out.println("[" + name + "] Logged out.");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
