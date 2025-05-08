package com.onez.controller;

import com.onez.model.CartModel;
import com.onez.model.CartItemModel;
import com.onez.model.ProductModel;
import com.onez.model.UserModel;
import com.onez.service.CartService;
import com.onez.service.ProductService;
import com.onez.util.RedirectionUtil;
import com.onez.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(asyncSupported = true, urlPatterns = { "/cart", "/cart/add", "/cart/remove", "/cart/update" })
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final CartService cartService = new CartService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserModel user = (UserModel) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        try {
            CartModel cart = cartService.getCartByUserId(user.getId());
            req.setAttribute("cart", cart);
            req.getRequestDispatcher(RedirectionUtil.cartUrl).forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading your cart: " + e.getMessage());
            req.getRequestDispatcher(RedirectionUtil.cartUrl).forward(req, resp);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserModel user = (UserModel) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + RedirectionUtil.loginUrl);
            return;
        }

        String servletPath = req.getServletPath();
        try {
            switch (servletPath) {
                case "/cart/add":
                    handleAddToCart(req, resp, user.getId());
                    break;
                case "/cart/remove":
                    handleRemoveFromCart(req, resp, user.getId());
                    break;
                case "/cart/update":
                    handleUpdateCart(req, resp, user.getId());
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Error processing your request: " + e.getMessage());
            req.getRequestDispatcher(RedirectionUtil.cartUrl).forward(req, resp);
            e.printStackTrace();
        }
    }

    private void handleAddToCart(HttpServletRequest req, HttpServletResponse resp, int userId) 
            throws ServletException, IOException {
        String productIdStr = req.getParameter("productId");
        String quantityStr = req.getParameter("quantity");

        if (!ValidationUtil.isValidId(productIdStr)) {
            sendError(req, resp, "Invalid product ID");
            return;
        }

        if (!ValidationUtil.isValidQuantity(quantityStr)) {
            sendError(req, resp, "Invalid quantity");
            return;
        }

        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(quantityStr);

        ProductModel product = productService.getProductById(productId);
        if (product == null) {
            sendError(req, resp, "Product not found");
            return;
        }

        if (quantity > product.getQuantity()) {
            sendError(req, resp, "Requested quantity exceeds available stock");
            return;
        }

        CartModel cart = cartService.addProductToCart(userId, productId, quantity);
        req.setAttribute("cart", cart);
        req.setAttribute("success", "Product added to cart successfully");
        req.getRequestDispatcher(RedirectionUtil.cartUrl).forward(req, resp);
    }

    private void handleRemoveFromCart(HttpServletRequest req, HttpServletResponse resp, int userId) 
            throws ServletException, IOException {
        String productIdStr = req.getParameter("productId");

        if (!ValidationUtil.isValidId(productIdStr)) {
            sendError(req, resp, "Invalid product ID");
            return;
        }

        int productId = Integer.parseInt(productIdStr);
        CartModel cart = cartService.removeProductFromCart(userId, productId, 1); // Remove 1 by default
        
        if (cart == null) {
            sendError(req, resp, "Product not found in cart");
            return;
        }

        req.setAttribute("cart", cart);
        req.setAttribute("success", "Product removed from cart");
        req.getRequestDispatcher(RedirectionUtil.cartUrl).forward(req, resp);
    }

    private void handleUpdateCart(HttpServletRequest req, HttpServletResponse resp, int userId) 
            throws ServletException, IOException {
        String productIdStr = req.getParameter("productId");
        String quantityStr = req.getParameter("quantity");

        if (!ValidationUtil.isValidId(productIdStr)) {
            sendError(req, resp, "Invalid product ID");
            return;
        }

        if (!ValidationUtil.isValidQuantity(quantityStr)) {
            sendError(req, resp, "Invalid quantity");
            return;
        }

        int productId = Integer.parseInt(productIdStr);
        int quantity = Integer.parseInt(quantityStr);

        ProductModel product = productService.getProductById(productId);
        if (product == null) {
            sendError(req, resp, "Product not found");
            return;
        }

        if (quantity > product.getQuantity()) {
            sendError(req, resp, "Requested quantity exceeds available stock");
            return;
        }

        // Get current cart to check existing quantity
        CartModel currentCart = cartService.getCartByUserId(userId);
        CartItemModel existingItem = currentCart.getItems().stream()
                .filter(item -> item.getProduct().getProductId() == productId)
                .findFirst()
                .orElse(null);

        if (existingItem == null) {
            sendError(req, resp, "Product not found in cart");
            return;
        }

        // Calculate difference
        int quantityDifference = quantity - existingItem.getProductQuantity();
        CartModel updatedCart;

        if (quantityDifference > 0) {
            updatedCart = cartService.addProductToCart(userId, productId, quantityDifference);
        } else if (quantityDifference < 0) {
            updatedCart = cartService.removeProductFromCart(userId, productId, -quantityDifference);
        } else {
            updatedCart = currentCart; // No change
        }

        req.setAttribute("cart", updatedCart);
        req.setAttribute("success", "Cart updated successfully");
        req.getRequestDispatcher(RedirectionUtil.cartUrl).forward(req, resp);
    }

    private void sendError(HttpServletRequest req, HttpServletResponse resp, String message) 
            throws ServletException, IOException {
        req.setAttribute("error", message);
        req.getRequestDispatcher(RedirectionUtil.cartUrl).forward(req, resp);
    }
}