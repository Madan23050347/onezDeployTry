package com.onez.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.onez.model.OrderModel;
import com.onez.service.OrderService;

@WebServlet("/admin/orders")
public class OrderManagementController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try (OrderService orderService = new OrderService()) {
            List<OrderModel> orders = orderService.getAllOrders();
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/page/admin/orderManagement.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error fetching orders: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/page/admin/orderManagement.jsp").forward(request, response);
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if ("updateStatus".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String newStatus = request.getParameter("status");
                
                try (OrderService orderService = new OrderService()) {
                    boolean updated = orderService.updateOrderStatus(orderId, newStatus);
                    if (updated) {
                        request.getSession().setAttribute("message", 
                            "Order #" + orderId + " status updated to " + newStatus);
                    } else {
                        request.getSession().setAttribute("error", 
                            "Failed to update order #" + orderId + ". It may not exist.");
                    }
                } catch (SQLException e) {
                    request.getSession().setAttribute("error", 
                        "Database error updating order: " + e.getMessage());
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid order ID format");
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}