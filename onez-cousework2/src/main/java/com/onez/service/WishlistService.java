package com.onez.service;

import com.onez.config.DbConfig;
import com.onez.model.ProductModel;
import com.onez.model.UserModel;
import com.onez.model.WishlistModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WishlistService {
    private final Connection connection;

    public WishlistService() throws SQLException {
        try {
            this.connection = DbConfig.getDbConnection();
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Database driver not found", ex);
        }
    }

    public WishlistModel getWishlistByUser(UserModel user) throws SQLException {
        String sql = "SELECT w.* FROM wishlist w WHERE w.user_id = ?";
        WishlistModel wishlist = null;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                wishlist = new WishlistModel();
                wishlist.setWishlistId(rs.getInt("wishlist_id"));
                wishlist.setUser(user);
                wishlist.setWishlistName(rs.getString("wishlist_name"));
                wishlist.setAddedAt(rs.getTimestamp("addedAt").toLocalDateTime());
                wishlist.setProducts(getWishlistProducts(wishlist.getWishlistId()));
            } else {
                // Create wishlist if it doesn't exist
                wishlist = createWishlist(user);
            }
        }
        return wishlist;
    }

    private List<ProductModel> getWishlistProducts(int wishlistId) throws SQLException {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT p.*, i.quantity as quantity FROM wishlist_product wp " +
                     "JOIN product p ON wp.product_id = p.product_id " +
                     "LEFT JOIN product i ON p.product_id = i.product_id " +
                     "WHERE wp.wishlist_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.setProductId(rs.getInt("product_id"));
                product.setProductName(rs.getString("productName"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setProductImage(rs.getString("productImage"));
                product.setQuantity(rs.getInt("quantity")); // For stock status display
                products.add(product);
            }
        }
        return products;
    }

    public boolean addToWishlist(UserModel user, int productId) throws SQLException {
        WishlistModel wishlist = getWishlistByUser(user);
        if (wishlist == null) {
            throw new SQLException("Failed to create or retrieve wishlist");
        }
        
        // Check if product already exists in wishlist
        if (isProductInWishlist(wishlist.getWishlistId(), productId)) {
            return true; // Already exists, consider it a success
        }
        
        String sql = "INSERT INTO wishlist_product (wishlist_id, product_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wishlist.getWishlistId());
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    private boolean isProductInWishlist(int wishlistId, int productId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist_product WHERE wishlist_id = ? AND product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wishlistId);
            stmt.setInt(2, productId);
            return stmt.executeQuery().next();
        }
    }

    private WishlistModel createWishlist(UserModel user) throws SQLException {
        String sql = "INSERT INTO wishlist (user_id, wishlist_name, addedAt) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, user.getId());
            stmt.setString(2, "My Wishlist");
            stmt.setObject(3, LocalDateTime.now());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        WishlistModel wishlist = new WishlistModel();
                        wishlist.setWishlistId(generatedKeys.getInt(1));
                        wishlist.setUser(user);
                        wishlist.setWishlistName("My Wishlist");
                        wishlist.setAddedAt(LocalDateTime.now());
                        return wishlist;
                    }
                }
            }
            throw new SQLException("Creating wishlist failed, no ID obtained.");
        }
    }

    public boolean removeFromWishlist(UserModel user, int productId) throws SQLException {
        WishlistModel wishlist = getWishlistByUser(user);
        if (wishlist == null) return false;
        
        String sql = "DELETE FROM wishlist_product WHERE wishlist_id = ? AND product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, wishlist.getWishlistId());
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }
}