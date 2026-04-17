package rohit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MedicineValidator Class
 * Author: Rohit
 *
 * Validates a Medicine object before it is persisted to an Inventory.
 * Returns a list of validation errors (empty = valid).
 *
 * Rules enforced:
 *   - Name must not be blank
 *   - Manufacturer must not be blank
 *   - Batch number must not be blank and must match pattern [A-Z]{2,5}-\d{4}-\d{3}
 *   - Quantity must be >= 0
 *   - MinimumStockLevel must be >= 1
 *   - ExpiryDate must not be null
 *   - Warns (not error) if medicine is already expired
 */
public class MedicineValidator {

    private static final String BATCH_PATTERN = "^[A-Z0-9]{2,8}-\\d{4}-\\d{3}$";

    /**
     * Validates the given medicine.
     *
     * @param medicine the medicine to validate
     * @return list of error strings (empty means valid)
     */
    public static List<String> validate(Medicine medicine) {
        List<String> errors = new ArrayList<>();

        if (medicine == null) {
            errors.add("Medicine object must not be null.");
            return errors;
        }

        // Name
        if (medicine.getName() == null || medicine.getName().isBlank()) {
            errors.add("Medicine name must not be blank.");
        }

        // Manufacturer
        if (medicine.getManufacturer() == null || medicine.getManufacturer().isBlank()) {
            errors.add("Manufacturer must not be blank.");
        }

        // Batch number
        if (medicine.getBatchNumber() == null || medicine.getBatchNumber().isBlank()) {
            errors.add("Batch number must not be blank.");
        } else if (!medicine.getBatchNumber().matches(BATCH_PATTERN)) {
            errors.add("Batch number '" + medicine.getBatchNumber()
                    + "' is invalid. Expected format: XX-YYYY-NNN (e.g. CPL-2024-001).");
        }

        // Quantity
        if (medicine.getQuantity() < 0) {
            errors.add("Quantity must be >= 0 (got " + medicine.getQuantity() + ").");
        }

        // Minimum stock level
        if (medicine.getMinimumStockLevel() < 1) {
            errors.add("Minimum stock level must be >= 1 (got "
                    + medicine.getMinimumStockLevel() + ").");
        }

        // Expiry date
        if (medicine.getExpiryDate() == null) {
            errors.add("Expiry date must not be null.");
        }

        return errors;
    }

    /**
     * Prints validation results to stdout.
     *
     * @param medicine the medicine to check
     * @return true if valid, false if there are errors
     */
    public static boolean validateAndPrint(Medicine medicine) {
        List<String> errors = validate(medicine);

        if (errors.isEmpty()) {
            // Check for warnings (non-blocking)
            if (medicine.getExpiryDate() != null && medicine.isExpired()) {
                System.out.println("[Validator] ⚠ WARNING: '"
                        + medicine.getName() + "' is already expired ("
                        + medicine.getExpiryDate() + "). Adding anyway.");
            } else if (medicine.getExpiryDate() != null
                    && medicine.getExpiryDate().isBefore(LocalDate.now().plusDays(30))) {
                System.out.println("[Validator] ⚠ WARNING: '"
                        + medicine.getName() + "' expires within 30 days ("
                        + medicine.getExpiryDate() + ").");
            }
            return true;
        }

        System.out.println("[Validator] ✗ Validation failed for '"
                + (medicine.getName() != null ? medicine.getName() : "UNKNOWN") + "':");
        errors.forEach(e -> System.out.println("   - " + e));
        return false;
    }
}
