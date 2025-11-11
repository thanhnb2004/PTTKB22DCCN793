package com.clone.vti.service;

import com.clone.vti.dao.CustomerDAO;
import com.clone.vti.model.Customer;
import com.clone.vti.util.PasswordUtil;

import java.time.LocalDate;

public class CustomerService {
    private final CustomerDAO customerDAO = new CustomerDAO();

    public Customer register(String fullName,
                             String email,
                             String rawPassword,
                             String phoneNumber,
                             LocalDate dateOfBirth,
                             String address) throws ServiceException {
        if (fullName == null || fullName.isBlank()) {
            throw new ServiceException("Tên khách hàng không được để trống");
        }
        if (email == null || email.isBlank()) {
            throw new ServiceException("Email không được để trống");
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new ServiceException("Mật khẩu phải có ít nhất 6 ký tự");
        }
        if (customerDAO.existsByEmail(email)) {
            throw new ServiceException("Email đã tồn tại trong hệ thống");
        }

        Customer customer = new Customer();
        customer.setFullName(fullName.trim());
        customer.setEmail(email.trim().toLowerCase());
        customer.setPasswordHash(PasswordUtil.hashPassword(email.trim().toLowerCase(), rawPassword));
        customer.setPhoneNumber(phoneNumber);
        customer.setDateOfBirth(dateOfBirth);
        customer.setAddress(address);
        customer.setMember(true);
        return customerDAO.create(customer);
    }
}
