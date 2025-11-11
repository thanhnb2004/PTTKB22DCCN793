package com.clone.vti.dao;

import com.clone.vti.model.Product;
import com.clone.vti.util.DBConnectionUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {
    private static final String SEARCH_SQL = "SELECT * FROM tblProduct WHERE ? IS NULL OR LOWER(name) LIKE ? ORDER BY name LIMIT 20";
    private static final String INSERT_SQL = "INSERT INTO tblProduct(name, type, unit, stock_quantity, unit_price) VALUES (?,?,?,?,?)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM tblProduct WHERE id = ?";
    private static final String UPDATE_STOCK_SQL = "UPDATE tblProduct SET stock_quantity = stock_quantity + ?, unit_price = ? WHERE id = ?";

    public List<Product> searchByName(String keyword) {
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
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to search products", e);
        }
    }

    public Product create(Connection connection, Product product) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getType());
            statement.setString(3, product.getUnit());
            statement.setInt(4, product.getStockQuantity());
            statement.setBigDecimal(5, product.getUnitPrice());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getInt(1));
                }
            }
            return product;
        }
    }

    public Optional<Product> findById(Connection connection, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapProduct(rs));
                }
            }
            return Optional.empty();
        }
    }

    public void increaseStock(Connection connection, int productId, int quantity, BigDecimal price) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_STOCK_SQL)) {
            statement.setInt(1, quantity);
            statement.setBigDecimal(2, price);
            statement.setInt(3, productId);
            statement.executeUpdate();
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setType(rs.getString("type"));
        product.setUnit(rs.getString("unit"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setUnitPrice(rs.getBigDecimal("unit_price"));
        return product;
    }
}
