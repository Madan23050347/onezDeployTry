package com.onez.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.onez.model.OrderModel;
import com.onez.model.UserModel;
import com.onez.service.OrderService;
import com.onez.util.RedirectionUtil;

@WebServlet(asyncSupported = true, urlPatterns = { "/orderHistory", "/orderHistory/delete" })
public class OrderHistoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UserModel user = (UserModel) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }
        
        // Use try-with-resources to ensure OrderService is properly closed
        try (OrderService orderService = new OrderService()) {
            // Get user's order history
            List<OrderModel> orders = orderService.getUserOrders(user.getId());
            
            // Set attributes for JSP
            request.setAttribute("orders", orders);
            request.setAttribute("user", user);
            
            // Forward to order history page
            request.getRequestDispatcher("/WEB-INF/page/orderHistory.jsp").forward(request, response);
            
        } catch (SQLException e) {
            // Log the error
            System.err.println("Database error loading order history for user " + user.getId() + ": " + e.getMessage());
            
            // Set error attributes
            request.setAttribute("error", "We couldn't load your order history due to a system error. Please try again later.");
            
            // Forward to error page or back to order history with error message
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        } catch (Exception e) {
            // Handle other unexpected errors
            System.err.println("Unexpected error in OrderHistoryController: " + e.getMessage());
            request.setAttribute("error", "An unexpected error occurred.");
            request.getRequestDispatcher("/WEB-INF/page/error.jsp").forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        UserModel user = (UserModel) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }
        
        String path = request.getServletPath();
        
        if (path.equals("/orderHistory/delete")) {
            try (OrderService orderService = new OrderService()) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                
                if (orderService.deleteOrder(orderId)) {
                    session.setAttribute("orderSuccess", "Order #" + orderId + " has been successfully deleted.");
                } else {
                    session.setAttribute("error", "Failed to delete order #" + orderId);
                }
                
                response.sendRedirect(request.getContextPath() + "/orderHistory");
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid order ID");
                response.sendRedirect(request.getContextPath() + "/orderHistory");
            } catch (SQLException e) {
                System.err.println("Database error deleting order: " + e.getMessage());
                session.setAttribute("error", "Failed to delete order due to a system error.");
                response.sendRedirect(request.getContextPath() + "/orderHistory");
            }
        }
    }
}