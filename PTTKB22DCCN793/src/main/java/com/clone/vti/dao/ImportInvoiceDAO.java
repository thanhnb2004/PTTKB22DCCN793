package com.clone.vti.dao;

import com.clone.vti.model.ImportInvoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ImportInvoiceDAO {
    private static final String INSERT_SQL = "INSERT INTO tblImportInvoice(supplier_id, warehouse_staff_id, import_date, total) VALUES (?,?,?,?)";

    public int insert(Connection connection, ImportInvoice invoice) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, invoice.getSupplier().getId());
            statement.setInt(2, invoice.getWarehouseStaff().getId());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(invoice.getImportDate()));
            statement.setBigDecimal(4, invoice.getTotal());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    invoice.setId(id);
                    return id;
                }
            }
            throw new SQLException("Failed to retrieve import invoice id");
        }
    }
}
