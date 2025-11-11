package com.clone.vti.service;

import com.clone.vti.dao.StaffDAO;
import com.clone.vti.model.Staff;
import com.clone.vti.util.PasswordUtil;

public class StaffService {
    private final StaffDAO staffDAO = new StaffDAO();

    public Staff authenticate(String username, String rawPassword) throws ServiceException {
        if (username == null || username.isBlank()) {
            throw new ServiceException("Vui lòng nhập tên đăng nhập");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ServiceException("Vui lòng nhập mật khẩu");
        }
        return staffDAO.findByUsername(username.trim())
                .filter(staff -> PasswordUtil.matches(staff.getUsername(), rawPassword, staff.getPasswordHash()))
                .map(staff -> {
                    staff.setPasswordHash(null);
                    return staff;
                })
                .orElseThrow(() -> new ServiceException("Sai thông tin đăng nhập"));
    }
}
