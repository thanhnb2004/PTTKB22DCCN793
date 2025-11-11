package com.clone.vti.servlet;

import com.clone.vti.model.Customer;
import com.clone.vti.service.CustomerService;
import com.clone.vti.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "customerRegisterServlet", urlPatterns = "/customer/register")
public class CustomerRegisterServlet extends HttpServlet {
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<String> errors = new ArrayList<>();

        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String phoneNumber = req.getParameter("phoneNumber");
        String dobText = req.getParameter("dateOfBirth");
        String address = req.getParameter("address");

        LocalDate dateOfBirth = null;
        if (dobText != null && !dobText.isBlank()) {
            try {
                dateOfBirth = LocalDate.parse(dobText);
            } catch (DateTimeParseException e) {
                errors.add("Ngày sinh không hợp lệ (định dạng yyyy-MM-dd)");
            }
        }

        if (errors.isEmpty()) {
            try {
                Customer customer = customerService.register(fullName, email, password, phoneNumber, dateOfBirth, address);
                req.setAttribute("customer", customer);
                req.getRequestDispatcher("/WEB-INF/views/customer/register_success.jsp").forward(req, resp);
                return;
            } catch (ServiceException e) {
                errors.add(e.getMessage());
            }
        }

        req.setAttribute("errors", errors);
        req.getRequestDispatcher("/WEB-INF/views/customer/register.jsp").forward(req, resp);
    }
}
