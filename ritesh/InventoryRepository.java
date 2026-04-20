package ritesh;

import rohit.Medicine;
import rohit.MedicineCategory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * InventoryRepository Class
 * Author: Ritesh
 *
 * Simulates a database repository layer for Inventory objects.
 * In production this would use JDBC / JPA / Hibernate.
 *
 * Supports:
 *   - Multi-inventory storage (one per user)
 *   - Search by name, category, or batch number
 *   - Medicines expiring within N days ("near expiry")
 *   - Stock audit report across all inventories
 */
public class InventoryRepository {

    // inventoryId -> Inventory
    private Map<Integer, Inventory> store = new HashMap<>();

    // -------------------------------------------------------
    // Inventory CRUD
    // -------------------------------------------------------

    public void save(Inventory inventory) {
        store.put(inventory.getInventoryId(), inventory);
        System.out.println("[InventoryRepo] Saved inventory #" + inventory.getInventoryId());
    }

    public Inventory findById(int inventoryId) {
        return store.getOrDefault(inventoryId, null);
    }

    public void delete(int inventoryId) {
        if (store.remove(inventoryId) != null) {
            System.out.println("[InventoryRepo] Deleted inventory #" + inventoryId);
        }
    }

    public Collection<Inventory> findAll() {
        return store.values();
    }

    // -------------------------------------------------------
    // Medicine Search (cross-inventory)
    // -------------------------------------------------------

    /**
     * Searches all inventories for medicines whose name
     * contains the given keyword (case-insensitive).
     */
    public List<Medicine> searchByName(String keyword) {
        String kw = keyword.toLowerCase();
        return store.values().stream()
                .flatMap(inv -> inv.getMedicines().stream())
                .filter(m -> m.getName().toLowerCase().contains(kw))
                .collect(Collectors.toList());
    }

    /**
     * Finds all medicines of a specific category across all inventories.
     */
    public List<Medicine> searchByCategory(MedicineCategory category) {
        return store.values().stream()
                .flatMap(inv -> inv.getMedicines().stream())
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }

    /**
     * Finds a medicine by its exact batch number across all inventories.
     */
    public Optional<Medicine> searchByBatch(String batchNumber) {
        return store.values().stream()
                .flatMap(inv -> inv.getMedicines().stream())
                .filter(m -> m.getBatchNumber().equalsIgnoreCase(batchNumber))
                .findFirst();
    }

    // -------------------------------------------------------
    // Expiry Queries
    // -------------------------------------------------------

    /**
     * Returns all medicines expiring within the next N days,
     * across all inventories (not yet expired).
     *
     * @param days number of days ahead to look
     */
    public List<Medicine> getNearExpiry(int days) {
        LocalDate cutoff = LocalDate.now().plusDays(days);
        return store.values().stream()
                .flatMap(inv -> inv.getMedicines().stream())
                .filter(m -> !m.isExpired()
                          && !m.getExpiryDate().isAfter(cutoff))
                .sorted(Comparator.comparing(Medicine::getExpiryDate))
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    // Audit Report
    // -------------------------------------------------------

    /**
     * Prints a full stock audit across every inventory.
     */
    public void printAuditReport() {
        System.out.println("\n========== STOCK AUDIT REPORT ==========");
        System.out.println("Date: " + LocalDate.now());
        System.out.println("Total Inventories: " + store.size());

        int totalMedicines = 0;
        int totalExpired   = 0;
        int totalLowStock  = 0;

        for (Inventory inv : store.values()) {
            System.out.println("\n  Inventory #" + inv.getInventoryId()
                    + " | Medicines: " + inv.getMedicines().size());
            for (Medicine m : inv.getMedicines()) {
                System.out.println("    " + m.getStatusSummary());
                totalMedicines++;
                if (m.isExpired())   totalExpired++;
                if (m.isLowStock())  totalLowStock++;
            }
        }

        System.out.println("\n-----------------------------------------");
        System.out.println("  Total Medicines : " + totalMedicines);
        System.out.println("  Expired         : " + totalExpired);
        System.out.println("  Low Stock       : " + totalLowStock);
        System.out.println("  OK              : " + (totalMedicines - totalExpired - totalLowStock));
        System.out.println("=========================================\n");
    }
}
