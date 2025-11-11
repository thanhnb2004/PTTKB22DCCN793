package com.clone.vti.service;

import com.clone.vti.dao.ImportInvoiceDAO;
import com.clone.vti.dao.ImportProductDAO;
import com.clone.vti.dao.ProductDAO;
import com.clone.vti.dao.SupplierDAO;
import com.clone.vti.model.ImportInvoice;
import com.clone.vti.model.ImportProduct;
import com.clone.vti.model.Product;
import com.clone.vti.model.Staff;
import com.clone.vti.model.Supplier;
import com.clone.vti.util.DBConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WarehouseImportService {
    public static final String ROLE_WAREHOUSE = "WAREHOUSE";

    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final ImportInvoiceDAO importInvoiceDAO = new ImportInvoiceDAO();
    private final ImportProductDAO importProductDAO = new ImportProductDAO();

    public ImportInvoice createImportInvoice(Staff staff, Supplier supplierInput, List<ImportProductRequest> productRequests)
            throws ServiceException {
        if (staff == null) {
            throw new ServiceException("Bạn cần đăng nhập để thực hiện chức năng này");
        }
        if (staff.getRole() == null || !ROLE_WAREHOUSE.equalsIgnoreCase(staff.getRole())) {
            throw new ServiceException("Chức năng chỉ dành cho nhân viên kho");
        }
        if (supplierInput == null) {
            throw new ServiceException("Vui lòng chọn hoặc thêm mới nhà cung cấp");
        }
        if (productRequests == null || productRequests.isEmpty()) {
            throw new ServiceException("Vui lòng thêm ít nhất một sản phẩm");
        }

        try (Connection connection = DBConnectionUtil.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Supplier supplier = resolveSupplier(connection, supplierInput);
                List<ImportProduct> importProducts = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;

                for (ImportProductRequest request : productRequests) {
                    if (request == null || request.getQuantity() <= 0) {
                        continue;
                    }
                    BigDecimal price = request.getPrice();
                    if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new ServiceException("Đơn giá phải lớn hơn 0");
                    }
                    Product product = resolveProduct(connection, request);
                    ImportProduct importProduct = new ImportProduct();
                    importProduct.setProduct(product);
                    importProduct.setQuantity(request.getQuantity());
                    importProduct.setPrice(price);
                    importProduct.setNote(request.getNote());
                    importProducts.add(importProduct);
                    total = total.add(price.multiply(BigDecimal.valueOf(request.getQuantity())));
                }

                if (importProducts.isEmpty()) {
                    throw new ServiceException("Vui lòng thêm ít nhất một sản phẩm hợp lệ");
                }

                ImportInvoice invoice = new ImportInvoice();
                invoice.setSupplier(supplier);
                invoice.setWarehouseStaff(staff);
                invoice.setImportDate(LocalDateTime.now());
                invoice.setTotal(total);
                invoice.getProducts().addAll(importProducts);

                int invoiceId = importInvoiceDAO.insert(connection, invoice);
                for (ImportProduct importProduct : importProducts) {
                    importProductDAO.insert(connection, invoiceId, importProduct);
                    productDAO.increaseStock(connection,
                            importProduct.getProduct().getId(),
                            importProduct.getQuantity(),
                            importProduct.getPrice());
                }
                connection.commit();
                return invoice;
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
                throw new ServiceException("Không thể tạo phiếu nhập kho", e);
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // Ignored because the connection is about to close.
                }
            }
        } catch (SQLException e) {
            throw new ServiceException("Không thể kết nối tới cơ sở dữ liệu", e);
        }
    }

    private Supplier resolveSupplier(Connection connection, Supplier supplierInput) throws SQLException, ServiceException {
        if (supplierInput.getId() > 0) {
            Optional<Supplier> supplier = supplierDAO.findById(connection, supplierInput.getId());
            return supplier.orElseThrow(() -> new ServiceException("Không tìm thấy nhà cung cấp"));
        }
        if (supplierInput.getName() == null || supplierInput.getName().isBlank()) {
            throw new ServiceException("Tên nhà cung cấp không được để trống");
        }
        Supplier supplier = new Supplier();
        supplier.setName(supplierInput.getName().trim());
        supplier.setEmail(supplierInput.getEmail());
        supplier.setPhoneNumber(supplierInput.getPhoneNumber());
        supplier.setAddress(supplierInput.getAddress());
        return supplierDAO.create(connection, supplier);
    }

    private Product resolveProduct(Connection connection, ImportProductRequest request) throws SQLException, ServiceException {
        if (request.getProductId() != null) {
            Optional<Product> product = productDAO.findById(connection, request.getProductId());
            return product.orElseThrow(() -> new ServiceException("Không tìm thấy sản phẩm"));
        }
        if (request.getProductName() == null || request.getProductName().isBlank()) {
            throw new ServiceException("Tên sản phẩm không được để trống");
        }
        Product product = new Product();
        product.setName(request.getProductName().trim());
        product.setType(request.getProductType());
        product.setUnit(request.getUnit());
        product.setStockQuantity(0);
        product.setUnitPrice(request.getPrice());
        return productDAO.create(connection, product);
    }

    public static class ImportProductRequest {
        private Integer productId;
        private String productName;
        private String productType;
        private String unit;
        private int quantity;
        private BigDecimal price;
        private String note;

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }
}
