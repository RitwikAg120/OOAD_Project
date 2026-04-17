package com.medicinetracker.controller;

import com.medicinetracker.dto.MedicineDTO;
import com.medicinetracker.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "*")
public class MedicineController {

    private static final Map<Integer, MedicineDTO> medicines = new HashMap<>();
    private static int medicineIdCounter = 2000;

    static {
        // Sample medicines
        MedicineDTO med1 = new MedicineDTO();
        med1.setId(1001);
        med1.setName("Aspirin 500mg");
        med1.setManufacturer("Bayer");
        med1.setCategory("TABLET");
        med1.setBatchNumber("ASP-2025-001");
        med1.setExpiryDate(LocalDate.now().plusDays(8));
        med1.setQuantity(15);
        med1.setMinimumStock(10);
        med1.setStatus("EXPIRING");
        med1.setDaysUntilExpiry(8);
        medicines.put(1001, med1);

        MedicineDTO med2 = new MedicineDTO();
        med2.setId(1002);
        med2.setName("Cough Syrup");
        med2.setManufacturer("Cipla");
        med2.setCategory("SYRUP");
        med2.setBatchNumber("SYRUP-2025-002");
        med2.setExpiryDate(LocalDate.now().plusDays(43));
        med2.setQuantity(3);
        med2.setMinimumStock(5);
        med2.setStatus("EXPIRING");
        med2.setDaysUntilExpiry(43);
        medicines.put(1002, med2);

        MedicineDTO med3 = new MedicineDTO();
        med3.setId(1003);
        med3.setName("Paracetamol 650mg");
        med3.setManufacturer("GSK");
        med3.setCategory("TABLET");
        med3.setBatchNumber("PARA-2025-003");
        med3.setExpiryDate(LocalDate.now().plusDays(60));
        med3.setQuantity(50);
        med3.setMinimumStock(20);
        med3.setStatus("OK");
        med3.setDaysUntilExpiry(60);
        medicines.put(1003, med3);

        MedicineDTO med4 = new MedicineDTO();
        med4.setId(1004);
        med4.setName("Amoxicillin 500mg");
        med4.setManufacturer("Cipla");
        med4.setCategory("TABLET");
        med4.setBatchNumber("AMOX-2025-004");
        med4.setExpiryDate(LocalDate.now().plusDays(15));
        med4.setQuantity(2);
        med4.setMinimumStock(10);
        med4.setStatus("LOW_STOCK");
        med4.setDaysUntilExpiry(15);
        medicines.put(1004, med4);

        MedicineDTO med5 = new MedicineDTO();
        med5.setId(1005);
        med5.setName("Vitamin D Syrup");
        med5.setManufacturer("Abbott");
        med5.setCategory("SYRUP");
        med5.setBatchNumber("VIT-2025-005");
        med5.setExpiryDate(LocalDate.now().minusDays(5));
        med5.setQuantity(12);
        med5.setMinimumStock(5);
        med5.setStatus("EXPIRED");
        med5.setDaysUntilExpiry(-5);
        medicines.put(1005, med5);
    }

    @GetMapping
    public ResponseEntity<List<MedicineDTO>> getAllMedicines() {
        return ResponseEntity.ok(new ArrayList<>(medicines.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDTO> getMedicineById(@PathVariable int id) {
        MedicineDTO medicine = medicines.get(id);
        if (medicine != null) {
            return ResponseEntity.ok(medicine);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addMedicine(@RequestBody MedicineDTO medicineDTO) {
        medicineDTO.setId(++medicineIdCounter);
        medicineDTO.setDaysUntilExpiry(ChronoUnit.DAYS.between(LocalDate.now(), medicineDTO.getExpiryDate()));
        updateStatus(medicineDTO);
        medicines.put(medicineDTO.getId(), medicineDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Medicine added successfully", medicineDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMedicine(@PathVariable int id, @RequestBody MedicineDTO medicineDTO) {
        if (medicines.containsKey(id)) {
            medicineDTO.setId(id);
            medicineDTO.setDaysUntilExpiry(ChronoUnit.DAYS.between(LocalDate.now(), medicineDTO.getExpiryDate()));
            updateStatus(medicineDTO);
            medicines.put(id, medicineDTO);
            return ResponseEntity.ok(new ApiResponse(true, "Medicine updated successfully", medicineDTO));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMedicine(@PathVariable int id) {
        if (medicines.remove(id) != null) {
            return ResponseEntity.ok(new ApiResponse(true, "Medicine deleted successfully"));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search/name/{keyword}")
    public ResponseEntity<List<MedicineDTO>> searchByName(@PathVariable String keyword) {
        List<MedicineDTO> results = medicines.values().stream()
                .filter(m -> m.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search/category/{category}")
    public ResponseEntity<List<MedicineDTO>> searchByCategory(@PathVariable String category) {
        List<MedicineDTO> results = medicines.values().stream()
                .filter(m -> m.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/expiring/{days}")
    public ResponseEntity<List<MedicineDTO>> getNearExpiry(@PathVariable int days) {
        LocalDate cutoff = LocalDate.now().plusDays(days);
        List<MedicineDTO> results = medicines.values().stream()
                .filter(m -> !m.getStatus().equals("EXPIRED") && m.getExpiryDate().isBefore(cutoff) || m.getExpiryDate().isEqual(cutoff))
                .sorted(Comparator.comparing(MedicineDTO::getExpiryDate))
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<MedicineDTO>> getLowStockMedicines() {
        List<MedicineDTO> results = medicines.values().stream()
                .filter(m -> m.getQuantity() < m.getMinimumStock())
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    private void updateStatus(MedicineDTO medicine) {
        if (medicine.getExpiryDate().isBefore(LocalDate.now())) {
            medicine.setStatus("EXPIRED");
        } else if (medicine.getExpiryDate().isBefore(LocalDate.now().plusDays(30))) {
            medicine.setStatus("EXPIRING");
        } else if (medicine.getQuantity() < medicine.getMinimumStock()) {
            medicine.setStatus("LOW_STOCK");
        } else {
            medicine.setStatus("OK");
        }
    }
}
