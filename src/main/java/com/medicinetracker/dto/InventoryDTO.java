package com.medicinetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class InventoryDTO {
    private int id;
    private int totalMedicines;
    private long expiredCount;
    private long lowStockCount;
    private List<MedicineDTO> medicines;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTotalMedicines() { return totalMedicines; }
    public void setTotalMedicines(int totalMedicines) { this.totalMedicines = totalMedicines; }

    public long getExpiredCount() { return expiredCount; }
    public void setExpiredCount(long expiredCount) { this.expiredCount = expiredCount; }

    public long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(long lowStockCount) { this.lowStockCount = lowStockCount; }

    public List<MedicineDTO> getMedicines() { return medicines; }
    public void setMedicines(List<MedicineDTO> medicines) { this.medicines = medicines; }
}
