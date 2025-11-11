package com.clone.vti.dao;

import com.clone.vti.model.ImportProduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImportProductDAO {
    private static final String INSERT_SQL = "INSERT INTO tblImportProduct(import_invoice_id, product_id, quantity, price, note) VALUES (?,?,?,?,?)";

    public void insert(Connection connection, int invoiceId, ImportProduct importProduct) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setInt(1, invoiceId);
            statement.setInt(2, importProduct.getProduct().getId());
            statement.setInt(3, importProduct.getQuantity());
            statement.setBigDecimal(4, importProduct.getPrice());
            statement.setString(5, importProduct.getNote());
            statement.executeUpdate();
        }
    }
}
