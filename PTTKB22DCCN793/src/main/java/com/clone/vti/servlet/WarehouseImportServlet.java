package com.clone.vti.servlet;

import com.clone.vti.dao.ProductDAO;
import com.clone.vti.dao.SupplierDAO;
import com.clone.vti.model.ImportInvoice;
import com.clone.vti.model.Staff;
import com.clone.vti.model.Supplier;
import com.clone.vti.service.ServiceException;
import com.clone.vti.service.WarehouseImportService;
import com.clone.vti.util.SessionAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "warehouseImportServlet", urlPatterns = "/warehouse/import")
public class WarehouseImportServlet extends HttpServlet {
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final WarehouseImportService warehouseImportService = new WarehouseImportService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Staff staff = (Staff) req.getSession().getAttribute(SessionAttributes.LOGGED_STAFF);
        if (staff == null) {
            resp.sendRedirect(req.getContextPath() + "/staff/login");
            return;
        }

        String supplierKeyword = req.getParameter("supplierKeyword");
        String productKeyword = req.getParameter("productKeyword");
        req.setAttribute("supplierKeyword", supplierKeyword);
        req.setAttribute("productKeyword", productKeyword);
        req.setAttribute("suppliers", supplierDAO.searchByName(supplierKeyword));
        req.setAttribute("products", productDAO.searchByName(productKeyword));
        req.setAttribute("formSupplier", new Supplier());
        req.setAttribute("productRequests", List.of(new WarehouseImportService.ImportProductRequest()));
        req.getRequestDispatcher("/WEB-INF/views/warehouse/import.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Staff staff = (Staff) req.getSession().getAttribute(SessionAttributes.LOGGED_STAFF);
        if (staff == null) {
            resp.sendRedirect(req.getContextPath() + "/staff/login");
            return;
        }

        List<String> errors = new ArrayList<>();
        Supplier supplier = new Supplier();
        String supplierIdText = req.getParameter("supplierId");
        if (supplierIdText != null && !supplierIdText.isBlank()) {
            try {
                supplier.setId(Integer.parseInt(supplierIdText));
            } catch (NumberFormatException e) {
                errors.add("Mã nhà cung cấp không hợp lệ");
            }
        } else {
            supplier.setName(req.getParameter("newSupplierName"));
            supplier.setEmail(req.getParameter("newSupplierEmail"));
            supplier.setPhoneNumber(req.getParameter("newSupplierPhone"));
            supplier.setAddress(req.getParameter("newSupplierAddress"));
        }

        List<WarehouseImportService.ImportProductRequest> productRequests = new ArrayList<>();
        String[] productIds = req.getParameterValues("productId");
        String[] productNames = req.getParameterValues("productName");
        String[] productTypes = req.getParameterValues("productType");
        String[] units = req.getParameterValues("unit");
        String[] quantities = req.getParameterValues("quantity");
        String[] prices = req.getParameterValues("price");
        String[] notes = req.getParameterValues("note");
        int length = determineLength(productIds, productNames, quantities, prices);

        for (int i = 0; i < length; i++) {
            WarehouseImportService.ImportProductRequest requestProduct = new WarehouseImportService.ImportProductRequest();
            Integer productId = parseInteger(getValue(productIds, i));
            if (productId != null && productId > 0) {
                requestProduct.setProductId(productId);
            } else {
                requestProduct.setProductName(getValue(productNames, i));
                requestProduct.setProductType(getValue(productTypes, i));
                requestProduct.setUnit(getValue(units, i));
            }
            Integer quantity = parseInteger(getValue(quantities, i));
            if (quantity != null) {
                requestProduct.setQuantity(quantity);
            } else {
                requestProduct.setQuantity(0);
            }
            BigDecimal price = parseBigDecimal(getValue(prices, i));
            requestProduct.setPrice(price);
            requestProduct.setNote(getValue(notes, i));
            productRequests.add(requestProduct);
        }

        if (errors.isEmpty()) {
            try {
                ImportInvoice invoice = warehouseImportService.createImportInvoice(staff, supplier, productRequests);
                req.setAttribute("invoice", invoice);
                req.getRequestDispatcher("/WEB-INF/views/warehouse/import_success.jsp").forward(req, resp);
                return;
            } catch (ServiceException e) {
                errors.add(e.getMessage());
            }
        }

        req.setAttribute("errors", errors);
        req.setAttribute("supplierKeyword", req.getParameter("supplierKeyword"));
        req.setAttribute("productKeyword", req.getParameter("productKeyword"));
        req.setAttribute("formSupplier", supplier);
        req.setAttribute("productRequests", productRequests);
        req.setAttribute("suppliers", supplierDAO.searchByName(null));
        req.setAttribute("products", productDAO.searchByName(null));
        req.getRequestDispatcher("/WEB-INF/views/warehouse/import.jsp").forward(req, resp);
    }

    private int determineLength(String[]... arrays) {
        int max = 0;
        for (String[] array : arrays) {
            if (array != null && array.length > max) {
                max = array.length;
            }
        }
        return max;
    }

    private String getValue(String[] array, int index) {
        if (array == null || index >= array.length) {
            return null;
        }
        return array[index];
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
