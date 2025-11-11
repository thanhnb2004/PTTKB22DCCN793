package com.clone.vti.dao;

import com.clone.vti.model.Staff;
import com.clone.vti.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StaffDAO {
    private static final String FIND_BY_USERNAME_SQL = "SELECT * FROM tblStaff WHERE username = ?";

    public Optional<Staff> findByUsername(String username) {
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME_SQL)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapStaff(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Failed to load staff by username", e);
        }
    }

    private Staff mapStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getInt("id"));
        staff.setFullName(rs.getString("full_name"));
        staff.setUsername(rs.getString("username"));
        staff.setPasswordHash(rs.getString("password_hash"));
        staff.setPhoneNumber(rs.getString("phone_number"));
        staff.setEmail(rs.getString("email"));
        staff.setAddress(rs.getString("address"));
        staff.setRole(rs.getString("role"));
        return staff;
    }
}
