package ritesh;

import rohit.Medicine;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory Class
 * Author: Ritesh
 * 
 * Represents a user's medicine inventory.
 * Each user has one inventory identified by an id.
 */
public class Inventory {

    private int id;
    private List<Medicine> medicines;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Inventory(int id) {
        this.id = id;
        this.medicines = new ArrayList<>();
    }

    // -------------------------------------------------------
    // Medicine Operations
    // -------------------------------------------------------
    public void addMedicine(Medicine medicine) {
        if (medicine != null) {
            medicines.add(medicine);
        }
    }

    public void removeMedicine(int medicineId) {
        medicines.removeIf(m -> m.getId() == medicineId);
    }

    public void removeMedicine(Medicine medicine) {
        medicines.remove(medicine);
    }

    public Medicine findById(int medicineId) {
        return medicines.stream()
                .filter(m -> m.getId() == medicineId)
                .findFirst()
                .orElse(null);
    }

    public List<Medicine> getMedicines() {
        return new ArrayList<>(medicines);
    }

    // -------------------------------------------------------
    // Queries
    // -------------------------------------------------------
    public int getTotalMedicines() {
        return medicines.size();
    }

    public long getExpiredCount() {
        return medicines.stream()
                .filter(Medicine::isExpired)
                .count();
    }

    public long getLowStockCount() {
        return medicines.stream()
                .filter(Medicine::isLowStock)
                .count();
    }

    // -------------------------------------------------------
    // Display
    // -------------------------------------------------------
    public void listMedicines() {
        System.out.println("\n========== Your Inventory ==========");
        if (medicines.isEmpty()) {
            System.out.println("[No medicines in inventory]");
        } else {
            medicines.forEach(m -> System.out.println("  " + m.getStatusSummary()));
        }
        System.out.println("====================================\n");
    }

    // -------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------
    public int getId() { return id; }
    public int getInventoryId() { return id; }
    
    public void updateMedicine(Medicine medicine) {
        if (medicine != null) {
            int index = medicines.stream()
                    .filter(m -> m.getId() == medicine.getId())
                    .findFirst()
                    .map(medicines::indexOf)
                    .orElse(-1);
            if (index >= 0) {
                medicines.set(index, medicine);
            }
        }
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", medicines=" + medicines.size() +
                '}';
    }
}
