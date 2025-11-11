package com.clone.vti.servlet;

import com.clone.vti.util.SessionAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "staffLogoutServlet", urlPatterns = "/staff/logout")
public class StaffLogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute(SessionAttributes.LOGGED_STAFF);
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
