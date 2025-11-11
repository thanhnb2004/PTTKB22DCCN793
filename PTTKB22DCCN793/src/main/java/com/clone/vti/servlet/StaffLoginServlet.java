package com.clone.vti.servlet;

import com.clone.vti.model.Staff;
import com.clone.vti.service.ServiceException;
import com.clone.vti.service.StaffService;
import com.clone.vti.util.SessionAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "staffLoginServlet", urlPatterns = "/staff/login")
public class StaffLoginServlet extends HttpServlet {
    private final StaffService staffService = new StaffService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/staff/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<String> errors = new ArrayList<>();
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            Staff staff = staffService.authenticate(username, password);
            HttpSession session = req.getSession(true);
            session.setAttribute(SessionAttributes.LOGGED_STAFF, staff);
            resp.sendRedirect(req.getContextPath() + "/warehouse/import");
            return;
        } catch (ServiceException e) {
            errors.add(e.getMessage());
        }

        req.setAttribute("errors", errors);
        req.getRequestDispatcher("/WEB-INF/views/staff/login.jsp").forward(req, resp);
    }
}
