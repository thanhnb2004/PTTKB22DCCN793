package com.clone.vti.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ImportInvoice {
    private int id;
    private Supplier supplier;
    private Staff warehouseStaff;
    private LocalDateTime importDate;
    private BigDecimal total;
    private final List<ImportProduct> products = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Staff getWarehouseStaff() {
        return warehouseStaff;
    }

    public void setWarehouseStaff(Staff warehouseStaff) {
        this.warehouseStaff = warehouseStaff;
    }

    public LocalDateTime getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDateTime importDate) {
        this.importDate = importDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<ImportProduct> getProducts() {
        return products;
    }
}
