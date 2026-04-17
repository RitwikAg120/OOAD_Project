package rohit;

import java.time.LocalDate;

/**
 * MedicineFactory Class
 * Author: Rohit
 *
 * Factory utility for constructing common pre-configured Medicine objects.
 * Removes boilerplate from test code and the CLI layer.
 * Uses an internal auto-incrementing ID counter.
 *
 * Each factory method accepts only the variable fields (expiry, qty)
 * and fills in category, standard minimum stock, and manufacturer.
 */
public class MedicineFactory {

    private static int idCounter = 2000;

    private static int nextId() { return ++idCounter; }

    // -------------------------------------------------------
    // Factory Methods
    // -------------------------------------------------------

    public static Medicine createTablet(String name, String manufacturer,
                                        String batch, LocalDate expiry, int qty) {
        return new Medicine(nextId(), name, manufacturer,
                MedicineCategory.TABLET, batch, expiry, qty, 10);
    }

    public static Medicine createSyrup(String name, String manufacturer,
                                       String batch, LocalDate expiry, int qty) {
        return new Medicine(nextId(), name, manufacturer,
                MedicineCategory.SYRUP, batch, expiry, qty, 5);
    }

    public static Medicine createInjection(String name, String manufacturer,
                                            String batch, LocalDate expiry, int qty) {
        return new Medicine(nextId(), name, manufacturer,
                MedicineCategory.INJECTION, batch, expiry, qty, 3);
    }

    public static Medicine createOintment(String name, String manufacturer,
                                           String batch, LocalDate expiry, int qty) {
        return new Medicine(nextId(), name, manufacturer,
                MedicineCategory.OINTMENT, batch, expiry, qty, 4);
    }

    /**
     * Generic builder — uses OTHER category with caller-specified min stock.
     */
    public static Medicine create(String name, String manufacturer, String batch,
                                   MedicineCategory category, LocalDate expiry,
                                   int qty, int minStock) {
        return new Medicine(nextId(), name, manufacturer, category, batch,
                expiry, qty, minStock);
    }

    // -------------------------------------------------------
    // Sample Inventory Seed (for demos / unit tests)
    // -------------------------------------------------------

    /**
     * Returns a pre-built list of diverse sample medicines
     * useful for populating demo inventories.
     */
    public static java.util.List<Medicine> buildSampleInventory() {
        java.util.List<Medicine> list = new java.util.ArrayList<>();

        list.add(createTablet("Aspirin 75mg",      "Bayer",      "BAY-001",
                LocalDate.now().plusMonths(8), 50));
        list.add(createTablet("Metformin 500mg",   "Sun Pharma", "SUN-101",
                LocalDate.now().plusDays(20),  8));   // near expiry, ok stock
        list.add(createSyrup("Dolo Suspension",    "Micro Labs", "MCR-202",
                LocalDate.now().minusDays(5),  3));   // expired, low stock
        list.add(createInjection("Insulin Glargine","Novo Nordisk","NNO-303",
                LocalDate.now().plusYears(1),  10));
        list.add(createOintment("Neosporin",       "J&J",        "JNJ-404",
                LocalDate.now().plusMonths(18), 2));  // low stock
        list.add(createTablet("Cetirizine 10mg",   "Cipla",      "CPL-505",
                LocalDate.now().plusMonths(3),  25));

        return list;
    }
}
