package com.clone.vti.dao;

import com.clone.vti.model.Supplier;
import com.clone.vti.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SupplierDAO {
    private static final String SEARCH_SQL = "SELECT * FROM tblSupplier WHERE ? IS NULL OR LOWER(name) LIKE ? ORDER BY name LIMIT 20";
    private static final String INSERT_SQL = "INSERT INTO tblSupplier(name, email, phone_number, address) VALUES (?,?,?,?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM tblSupplier WHERE id = ?";

    public List<Supplier> searchByName(String keyword) {
        String processedKeyword = (keyword == null || keyword.isBlank()) ? null : "%" + keyword.toLowerCase().trim() + "%";
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_SQL)) {
            if (processedKeyword == null) {
                statement.setNull(1, java.sql.Types.VARCHAR);
                statement.setNull(2, java.sql.Types.VARCHAR);
            } else {
                statement.setString(1, "");
                statement.setString(2, processedKeyword);
            }
            try (ResultSet rs = statement.executeQuery()) {
                List<Supplier> suppliers = new ArrayList<>();
                while (rs.next()) {
                    suppliers.add(mapSupplier(rs));
                }
                return suppliers;
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to search suppliers", e);
        }
    }

    public Supplier create(Connection connection, Supplier supplier) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getEmail());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getAddress());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    supplier.setId(rs.getInt(1));
                }
            }
            return supplier;
        }
    }

    public Optional<Supplier> findById(Connection connection, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapSupplier(rs));
                }
            }
            return Optional.empty();
        }
    }

    private Supplier mapSupplier(ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getInt("id"));
        supplier.setName(rs.getString("name"));
        supplier.setEmail(rs.getString("email"));
        supplier.setPhoneNumber(rs.getString("phone_number"));
        supplier.setAddress(rs.getString("address"));
        return supplier;
    }
}
