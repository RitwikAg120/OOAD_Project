package ritwik;

/**
 * RegularUser Class
 * Author: Ritwik
 * 
 * Represents a regular (non-admin) user of the Medicine Tracker system.
 * Can manage personal inventory and set reminders.
 */
public class RegularUser extends User {

    public RegularUser(int userId, String name, String email, String passwordHash) {
        super(userId, name, email, passwordHash, Role.USER);
    }

    @Override
    public String toString() {
        return "RegularUser{" +
                "id=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
