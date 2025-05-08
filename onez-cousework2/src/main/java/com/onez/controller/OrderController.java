package com.onez.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.onez.model.CartModel;
import com.onez.model.OrderModel;
import com.onez.service.OrderService;
import com.onez.util.RedirectionUtil;

@WebServlet(asyncSupported = true, urlPatterns = { 
    "/order-history",  // Shows past orders
    "/order",          // Shows checkout page
    "/processOrder"    // Processes new orders
})
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // No longer holding service instances as fields
    // They'll be created per request to ensure proper connection handling

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        if ("/order-history".equals(path)) {
            showOrderHistory(request, response);
        } else if ("/order".equals(path)) {
            showCheckoutPage(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("/processOrder".equals(request.getServletPath())) {
            processNewOrder(request, response);
        }
    }

    private void showOrderHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        try (OrderService orderService = new OrderService()) {
            List<OrderModel> orders = orderService.getUserOrders(userId);
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/page/orderHistory.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error loading order history: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/page/orderHistory.jsp").forward(request, response);
        }
    }

    private void showCheckoutPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        try (OrderService orderService = new OrderService()) {
            CartModel cart = orderService.getCartForUser(userId);
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                request.setAttribute("error", "Your cart is empty");
            }
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/WEB-INF/page/order.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Error loading cart: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/page/order.jsp").forward(request, response);
        }
    }

    private void processNewOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("id");
        String paymentMethod = request.getParameter("paymentMethod");
        
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        try (OrderService orderService = new OrderService()) {
            OrderModel order = orderService.processOrder(userId, paymentMethod);
            
            if (order == null) {
                request.setAttribute("error", "Failed to process your order");
                showCheckoutPage(request, response);
            } else {
                // Clear the cart from session after successful order
                request.getSession().removeAttribute("cart");
                
                // Set success message with order details
                request.getSession().setAttribute("orderSuccess", order);
                
                // Redirect to order confirmation
                response.sendRedirect(request.getContextPath() + "/order-success");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Order processing failed: " + e.getMessage());
            showCheckoutPage(request, response);
        }
    }
}