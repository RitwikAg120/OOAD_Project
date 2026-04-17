package com.medicinetracker.controller;

import com.medicinetracker.dto.InventoryDTO;
import com.medicinetracker.dto.MedicineDTO;
import com.medicinetracker.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    private static final Map<Integer, InventoryDTO> inventories = new HashMap<>();

    static {
        // Sample inventories
        InventoryDTO inv1 = new InventoryDTO();
        inv1.setId(200);
        // Will be populated from medicines
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventories() {
        return ResponseEntity.ok(new ArrayList<>(inventories.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable int id) {
        InventoryDTO inventory = new InventoryDTO();
        inventory.setId(id);
        
        // Fetch all medicines for this inventory (simplified)
        List<MedicineDTO> medicines = new ArrayList<>();
        inventory.setMedicines(medicines);
        inventory.setTotalMedicines(medicines.size());
        inventory.setExpiredCount(medicines.stream().filter(m -> m.getStatus().equals("EXPIRED")).count());
        inventory.setLowStockCount(medicines.stream().filter(m -> m.getStatus().equals("LOW_STOCK")).count());
        
        return ResponseEntity.ok(inventory);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        inventories.put(inventoryDTO.getId(), inventoryDTO);
        return ResponseEntity.ok(new ApiResponse(true, "Inventory created successfully", inventoryDTO));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getInventoryStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInventories", inventories.size());
        stats.put("totalMedicines", 24);
        stats.put("expiredMedicines", 2);
        stats.put("expiringMedicines", 5);
        stats.put("lowStockMedicines", 3);
        return ResponseEntity.ok(stats);
    }
}
