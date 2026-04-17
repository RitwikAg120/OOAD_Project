package rohit;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Medicine Class
 * Author: Rohit
 * 
 * Represents a medicine with all its attributes.
 * Tracks expiry status and provides validation utilities.
 */
public class Medicine {

    private int                 id;
    private String              name;
    private String              manufacturer;
    private MedicineCategory    category;
    private String              batchNumber;
    private LocalDate           expiryDate;
    private int                 quantity;
    private int                 minimumStock;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------
    public Medicine(int id, String name, String manufacturer, MedicineCategory category,
                   String batchNumber, LocalDate expiryDate, int quantity, int minimumStock) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.category = category;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
    }

    // -------------------------------------------------------
    // Queries
    // -------------------------------------------------------
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public long daysUntilExpiry() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    public boolean isNearExpiry(int days) {
        long daysRemaining = daysUntilExpiry();
        return daysRemaining >= 0 && daysRemaining <= days;
    }

    public boolean isLowStock() {
        return quantity < minimumStock;
    }

    public String getStatusSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s (%s)", id, name, category));

        if (isExpired()) {
            sb.append(" [EXPIRED]");
        } else if (isNearExpiry(30)) {
            sb.append(String.format(" [EXPIRING IN %d DAYS]", daysUntilExpiry()));
        }

        if (isLowStock()) {
            sb.append(" [LOW STOCK: ").append(quantity).append("/").append(minimumStock).append("]");
        }

        return sb.toString();
    }

    // -------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------
    public int getId() { return id; }
    public String getName() { return name; }
    public String getManufacturer() { return manufacturer; }
    public MedicineCategory getCategory() { return category; }
    public String getBatchNumber() { return batchNumber; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public int getQuantity() { return quantity; }
    public int getMinimumStock() { return minimumStock; }
    public int getMinimumStockLevel() { return minimumStock; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setMinimumStock(int minimumStock) { this.minimumStock = minimumStock; }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", batch='" + batchNumber + '\'' +
                ", expiry=" + expiryDate +
                ", qty=" + quantity +
                ", minStock=" + minimumStock +
                '}';
    }
}
