import ritwik.*;
import ritesh.*;
import rohit.*;
import rishikesh.*;

import java.time.LocalDate;
import java.util.*;

/**
 * MedicineTrackerApp - Interactive CLI
 * =====================================
 * Full console-based application integrating all four modules:
 *
 *   Ritwik    -> AuthService, UserSession, User management
 *   Ritesh    -> Inventory, InventoryRepository
 *   Rohit     -> Medicine, MedicineFactory, MedicineValidator
 *   Rishikesh -> NotificationService, ReminderScheduler
 *
 * Run from /out directory:  java MedicineTrackerApp
 */
public class MedicineTrackerApp {

    // -------------------------------------------------------
    // Shared Services (wired once at startup)
    // -------------------------------------------------------
    private static AuthService          authService    = new AuthService();
    private static InventoryRepository  inventoryRepo  = new InventoryRepository();
    private static ReminderScheduler    scheduler      = new ReminderScheduler();
    private static Scanner              sc             = new Scanner(System.in);

    // Session state
    private static UserSession  currentSession  = null;
    private static Inventory    userInventory   = null;

    // -------------------------------------------------------
    // Entry Point
    // -------------------------------------------------------
    public static void main(String[] args) {
        banner();
        seedDemoData();

        boolean running = true;
        while (running) {
            if (currentSession == null || !currentSession.isValid()) {
                running = showAuthMenu();
            } else {
                if (currentSession.getUser().getRole() == Role.ADMIN) {
                    showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }
        System.out.println("\nGoodbye! Stay healthy. 💊");
    }

    // -------------------------------------------------------
    // Auth Menu (pre-login)
    // -------------------------------------------------------
    private static boolean showAuthMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║        MAIN MENU             ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1. Login                    ║");
        System.out.println("║  2. Register                 ║");
        System.out.println("║  0. Exit                     ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("Choice: ");

        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1" -> doLogin();
            case "2" -> doRegister();
            case "0" -> { return false; }
            default  -> System.out.println("[!] Invalid choice.");
        }
        return true;
    }

    // -------------------------------------------------------
    // Regular User Menu
    // -------------------------------------------------------
    private static void showUserMenu() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  MEDICINE TRACKER  [" + pad(currentSession.getUser().getName(), 18) + "]  │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│  1. View My Inventory                   │");
        System.out.println("│  2. Add Medicine                        │");
        System.out.println("│  3. Remove Medicine                     │");
        System.out.println("│  4. Update Medicine Quantity            │");
        System.out.println("│  5. Search Medicine by Name             │");
        System.out.println("│  6. Check Expiry Alerts                 │");
        System.out.println("│  7. Set a Reminder                      │");
        System.out.println("│  8. Run Due Reminders                   │");
        System.out.println("│  9. Near-Expiry Report (next 30 days)   │");
        System.out.println("│  0. Logout                              │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("Choice: ");

        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1" -> viewInventory();
            case "2" -> addMedicine();
            case "3" -> removeMedicine();
            case "4" -> updateQuantity();
            case "5" -> searchMedicine();
            case "6" -> runNotifications();
            case "7" -> setReminder();
            case "8" -> scheduler.runDueReminders();
            case "9" -> nearExpiryReport();
            case "0" -> doLogout();
            default  -> System.out.println("[!] Invalid choice.");
        }
    }

    // -------------------------------------------------------
    // Admin Menu
    // -------------------------------------------------------
    private static void showAdminMenu() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  ADMIN PANEL  [" + pad(currentSession.getUser().getName(), 22) + "]  │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│  1. Full Stock Audit Report             │");
        System.out.println("│  2. View All Sessions                   │");
        System.out.println("│  3. View All Users                      │");
        System.out.println("│  4. Reminder Daily Digest               │");
        System.out.println("│  0. Logout                              │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.print("Choice: ");

        String choice = sc.nextLine().trim();
        switch (choice) {
            case "1" -> inventoryRepo.printAuditReport();
            case "2" -> authService.listActiveSessions();
            case "3" -> authService.getUsers().values().forEach(System.out::println);
            case "4" -> scheduler.printDailyDigest();
            case "0" -> doLogout();
            default  -> System.out.println("[!] Invalid choice.");
        }
    }

    // -------------------------------------------------------
    // Auth Actions (Ritwik)
    // -------------------------------------------------------
    private static void doLogin() {
        System.out.print("Email   : ");
        String email = sc.nextLine().trim();
        System.out.print("Password: ");
        String pass = sc.nextLine().trim();

        currentSession = authService.login(email, pass);
        if (currentSession != null) {
            // Load or create inventory for this user
            int invId = currentSession.getUser().getUserId() * 100;
            userInventory = inventoryRepo.findById(invId);
            if (userInventory == null) {
                userInventory = new Inventory(invId);
                inventoryRepo.save(userInventory);
            }
        }
    }

    private static void doRegister() {
        System.out.print("Full Name : ");
        String name = sc.nextLine().trim();
        System.out.print("Email     : ");
        String email = sc.nextLine().trim();
        System.out.print("Password  : ");
        String pass = sc.nextLine().trim();

        // Static ID based on current user count + 10
        int id = authService.getTotalUsers() + 10;
        String hash = Integer.toHexString(pass.hashCode());
        RegularUser newUser = new RegularUser(id, name, email, hash);
        authService.registerUser(newUser);
        System.out.println("[App] Registration complete. Please login.");
    }

    private static void doLogout() {
        if (currentSession != null) {
            authService.logout(currentSession.getSessionToken());
            currentSession = null;
            userInventory  = null;
        }
    }

    // -------------------------------------------------------
    // Inventory Actions (Ritesh + Rohit)
    // -------------------------------------------------------
    private static void viewInventory() {
        userInventory.listMedicines();
    }

    private static void addMedicine() {
        System.out.println("\n-- Add Medicine --");
        System.out.print("Name         : ");
        String name = sc.nextLine().trim();
        System.out.print("Manufacturer : ");
        String mfr  = sc.nextLine().trim();
        System.out.print("Batch No.    : ");
        String batch = sc.nextLine().trim();
        System.out.println("Category (1=TABLET 2=SYRUP 3=INJECTION 4=OINTMENT 5=OTHER): ");
        System.out.print("Choice: ");
        MedicineCategory cat = parseCategory(sc.nextLine().trim());
        System.out.print("Expiry (YYYY-MM-DD): ");
        LocalDate expiry;
        try { expiry = LocalDate.parse(sc.nextLine().trim()); }
        catch (Exception e) { System.out.println("[!] Invalid date."); return; }
        System.out.print("Quantity    : ");
        int qty;
        try { qty = Integer.parseInt(sc.nextLine().trim()); }
        catch (Exception e) { System.out.println("[!] Invalid quantity."); return; }
        System.out.print("Min Stock   : ");
        int min;
        try { min = Integer.parseInt(sc.nextLine().trim()); }
        catch (Exception e) { System.out.println("[!] Invalid min stock."); return; }

        Medicine m = MedicineFactory.create(name, mfr, batch, cat, expiry, qty, min);

        // Rohit's validator gates the add
        if (MedicineValidator.validateAndPrint(m)) {
            userInventory.addMedicine(m);
        }
    }

    private static void removeMedicine() {
        System.out.print("Medicine ID to remove: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            userInventory.removeMedicine(id);
        } catch (Exception e) {
            System.out.println("[!] Invalid ID.");
        }
    }

    private static void updateQuantity() {
        System.out.print("Medicine ID : ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());
            Medicine m = userInventory.findById(id);
            if (m == null) { System.out.println("[!] Not found."); return; }
            System.out.print("New Quantity: ");
            int qty = Integer.parseInt(sc.nextLine().trim());
            m.setQuantity(qty);
            userInventory.updateMedicine(m);
        } catch (Exception e) {
            System.out.println("[!] Invalid input.");
        }
    }

    private static void searchMedicine() {
        System.out.print("Search keyword: ");
        String kw = sc.nextLine().trim();
        List<Medicine> results = inventoryRepo.searchByName(kw);
        if (results.isEmpty()) {
            System.out.println("[Search] No medicines found for: " + kw);
        } else {
            System.out.println("[Search] Results (" + results.size() + "):");
            results.forEach(m -> System.out.println("  " + m.getStatusSummary()));
        }
    }

    // -------------------------------------------------------
    // Notification / Reminder Actions (Rishikesh)
    // -------------------------------------------------------
    private static void runNotifications() {
        NotificationService ns = new NotificationService();
        ns.scanAndNotify(userInventory);
    }

    private static void setReminder() {
        if (!(currentSession.getUser() instanceof RegularUser ru)) {
            System.out.println("[!] Only regular users can set reminders.");
            return;
        }
        System.out.print("Reminder date (YYYY-MM-DD): ");
        LocalDate date;
        try { date = LocalDate.parse(sc.nextLine().trim()); }
        catch (Exception e) { System.out.println("[!] Invalid date."); return; }
        System.out.print("Frequency (DAILY/WEEKLY/MONTHLY/ONCE): ");
        String freq = sc.nextLine().trim().toUpperCase();
        scheduler.createReminder(ru, date, freq);
    }

    private static void nearExpiryReport() {
        List<Medicine> nearExpiry = inventoryRepo.getNearExpiry(30);
        System.out.println("\n[Near Expiry] Medicines expiring within 30 days:");
        if (nearExpiry.isEmpty()) {
            System.out.println("  None found — all medicines are safe.");
        } else {
            nearExpiry.forEach(m -> System.out.println("  " + m.getStatusSummary()));
        }
    }

    // -------------------------------------------------------
    // Demo Data Seed
    // -------------------------------------------------------
    private static void seedDemoData() {
        // Admin (Ritwik)
        Admin admin = new Admin(1, "Ritwik", "ritwik@admin.com",
                Integer.toHexString("admin123".hashCode()));
        authService.registerUser(admin);

        // Regular users
        RegularUser rishikesh = new RegularUser(2, "Rishikesh", "rishi@home.com",
                Integer.toHexString("pass456".hashCode()));
        RegularUser rohit = new RegularUser(3, "Rohit", "rohit@home.com",
                Integer.toHexString("rohit789".hashCode()));
        RegularUser ritesh = new RegularUser(4, "Ritesh", "ritesh@home.com",
                Integer.toHexString("ritesh000".hashCode()));

        authService.registerUser(rishikesh);
        authService.registerUser(rohit);
        authService.registerUser(ritesh);

        // Seed inventories
        Inventory inv2 = new Inventory(200);
        MedicineFactory.buildSampleInventory().forEach(inv2::addMedicine);
        inventoryRepo.save(inv2);

        Inventory inv3 = new Inventory(300);
        inv3.addMedicine(MedicineFactory.createTablet("Amoxicillin 500mg", "Cipla",
                "CPL-2025-099", LocalDate.now().plusMonths(6), 30));
        inventoryRepo.save(inv3);

        System.out.println("\n[Demo] Seed complete. Login credentials:");
        System.out.println("  Admin  : ritwik@admin.com  / admin123");
        System.out.println("  User 1 : rishi@home.com    / pass456");
        System.out.println("  User 2 : rohit@home.com    / rohit789");
        System.out.println("  User 3 : ritesh@home.com   / ritesh000");
    }

    // -------------------------------------------------------
    // Helpers
    // -------------------------------------------------------
    private static void banner() {
        System.out.println("╔═══════════════════════════════════════════════╗");
        System.out.println("║   Medicine Expiry & Home Inventory Tracker    ║");
        System.out.println("║   Team: Ritwik | Ritesh | Rohit | Rishikesh  ║");
        System.out.println("╚═══════════════════════════════════════════════╝");
    }

    private static String pad(String s, int len) {
        if (s.length() >= len) return s.substring(0, len);
        return s + " ".repeat(len - s.length());
    }

    private static MedicineCategory parseCategory(String s) {
        return switch (s) {
            case "1" -> MedicineCategory.TABLET;
            case "2" -> MedicineCategory.SYRUP;
            case "3" -> MedicineCategory.INJECTION;
            case "4" -> MedicineCategory.OINTMENT;
            default  -> MedicineCategory.OTHER;
        };
    }
}
