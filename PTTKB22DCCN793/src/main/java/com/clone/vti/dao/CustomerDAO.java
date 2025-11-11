package com.clone.vti.dao;

import com.clone.vti.model.Customer;
import com.clone.vti.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

public class CustomerDAO {
    private static final String INSERT_SQL = "INSERT INTO tblCustomer(full_name, email, password_hash, phone_number, date_of_birth, address, is_member) "
            + "VALUES (?,?,?,?,?,?,?)";
    private static final String EXISTS_SQL = "SELECT 1 FROM tblCustomer WHERE email = ?";
    private static final String FIND_BY_EMAIL_SQL = "SELECT * FROM tblCustomer WHERE email = ?";

    public boolean existsByEmail(String email) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_SQL)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DaoException("Failed to check customer existence", e);
        }
    }

    public Customer create(Customer customer) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, customer.getFullName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPasswordHash());
            statement.setString(4, customer.getPhoneNumber());
            LocalDate dob = customer.getDateOfBirth();
            if (dob != null) {
                statement.setDate(5, Date.valueOf(dob));
            } else {
                statement.setNull(5, java.sql.Types.DATE);
            }
            statement.setString(6, customer.getAddress());
            statement.setBoolean(7, customer.isMember());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    customer.setId(rs.getInt(1));
                }
            }
            return customer;
        } catch (SQLException e) {
            throw new DaoException("Failed to create customer", e);
        }
    }

    public Optional<Customer> findByEmail(String email) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL_SQL)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCustomer(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to load customer by email", e);
        }
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setFullName(rs.getString("full_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPasswordHash(rs.getString("password_hash"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            customer.setDateOfBirth(dob.toLocalDate());
        }
        customer.setAddress(rs.getString("address"));
        customer.setMember(rs.getBoolean("is_member"));
        return customer;
    }
}
