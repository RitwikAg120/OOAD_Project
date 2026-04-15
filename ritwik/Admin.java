package ritwik;

/**
 * Admin Class
 * Author: Ritwik
 * 
 * Represents an admin user with elevated privileges.
 * Can view all inventories and perform system operations.
 */
public class Admin extends User {

    public Admin(int userId, String name, String email, String passwordHash) {
        super(userId, name, email, passwordHash, Role.ADMIN);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                '}';
    }
}
