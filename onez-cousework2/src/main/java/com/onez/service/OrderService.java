package com.onez.service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.onez.config.DbConfig;
import com.onez.model.OrderModel;
import com.onez.model.OrderItemModel;
import com.onez.model.CartModel;
import com.onez.model.CartItemModel;
import com.onez.model.ProductModel;
import com.onez.model.UserModel;
import com.onez.model.AddressModel;

public class OrderService implements AutoCloseable {
    // Constants
    private static final String ORDER_STATUS_PROCESSING = "Processing";
    private static final String ORDER_STATUS_PENDING = "Pending";
    
    private final Connection dbConn;
    private boolean isConnectionError = false;
    
    public OrderService() throws SQLException {
        try {
            this.dbConn = DbConfig.getDbConnection();
            this.dbConn.setAutoCommit(false); // Transactions disabled by default
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Database driver not found", ex);
        }
    }

    @Override
    public void close() {
        try {
            if (dbConn != null && !dbConn.isClosed()) {
                dbConn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    // Main order processing method
    public OrderModel processOrder(int userId, String paymentMethod) throws SQLException {
        validatePaymentMethod(paymentMethod);
        
        try {
            // 1. Get user's cart
            CartModel cart = getCartForUser(userId);
            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                return null;
            }

            // 2. Create order record
            OrderModel order = createOrderRecord(cart, paymentMethod);
            if (order == null) {
                dbConn.rollback();
                return null;
            }

            // 3. Save order items
            saveOrderItems(order.getOrderId(), cart.getItems());
            
            // 4. Update product quantities
            if (!updateProductQuantities(cart)) {
                dbConn.rollback();
                return null;
            }

            // 5. Clear the cart
            if (!clearUserCart(cart.getCartId())) {
                dbConn.rollback();
                return null;
            }

            dbConn.commit();
            return order;
        } catch (SQLException e) {
            dbConn.rollback();
            throw e;
        }
    }

    // Cart-related methods
    public CartModel getCartForUser(int userId) throws SQLException {
        String cartSql = "SELECT * FROM onez.cart WHERE user_id = ?";
        String itemsSql = "SELECT ci.*, p.* FROM onez.cartitem ci JOIN onez.product p ON ci.product_id = p.product_id WHERE ci.cart_id = ?";
        
        try (PreparedStatement cartStmt = dbConn.prepareStatement(cartSql)) {
            cartStmt.setInt(1, userId);
            
            try (ResultSet cartRs = cartStmt.executeQuery()) {
                if (cartRs.next()) {
                    CartModel cart = mapCart(cartRs, userId);
                    cart.setItems(getCartItems(cart.getCartId(), itemsSql));
                    return cart;
                }
            }
        }
        return null;
    }

    private List<CartItemModel> getCartItems(int cartId, String itemsSql) throws SQLException {
        List<CartItemModel> items = new ArrayList<>();
        
        try (PreparedStatement itemsStmt = dbConn.prepareStatement(itemsSql)) {
            itemsStmt.setInt(1, cartId);
            
            try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                while (itemsRs.next()) {
                    items.add(mapCartItem(itemsRs));
                }
            }
        }
        return items;
    }

    // Order-related methods
    public List<OrderModel> getUserOrders(int userId) throws SQLException {
        String orderSql = "SELECT * FROM onez.order_table WHERE user_id = ? ORDER BY order_date DESC";
        String itemsSql = "SELECT oi.*, p.* FROM onez.order_items oi JOIN onez.product p ON oi.product_id = p.product_id WHERE oi.order_id = ?";
        
        List<OrderModel> orders = new ArrayList<>();
        
        try (PreparedStatement orderStmt = dbConn.prepareStatement(orderSql)) {
            orderStmt.setInt(1, userId);
            
            try (ResultSet orderRs = orderStmt.executeQuery()) {
                while (orderRs.next()) {
                    OrderModel order = mapOrder(orderRs);
                    order.setItems(getOrderItems(order.getOrderId(), itemsSql));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    private List<OrderItemModel> getOrderItems(int orderId, String itemsSql) throws SQLException {
        List<OrderItemModel> items = new ArrayList<>();
        
        try (PreparedStatement itemsStmt = dbConn.prepareStatement(itemsSql)) {
            itemsStmt.setInt(1, orderId);
            
            try (ResultSet itemsRs = itemsStmt.executeQuery()) {
                while (itemsRs.next()) {
                    items.add(mapOrderItem(itemsRs));
                }
            }
        }
        return items;
    }

    // Database operations
    private OrderModel createOrderRecord(CartModel cart, String paymentMethod) throws SQLException {
        String sql = "INSERT INTO onez.order_table (user_id, cart_id, order_date, order_status, paymentMethod) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, cart.getUser().getId());
            stmt.setInt(2, cart.getCartId());
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setString(4, ORDER_STATUS_PROCESSING);
            stmt.setString(5, paymentMethod);
            
            if (stmt.executeUpdate() == 0) {
                return null;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    OrderModel order = new OrderModel();
                    order.setOrderId(generatedKeys.getInt(1));
                    order.setItems(convertCartItemsToOrderItems(cart.getItems()));
                    order.setUser(cart.getUser());
                    order.setOrderDate(LocalDate.now());
                    order.setOrderStatus(ORDER_STATUS_PENDING);
                    order.setPaymentMethod(paymentMethod);
                    return order;
                }
            }
        }
        return null;
    }

    private void saveOrderItems(int orderId, List<CartItemModel> cartItems) throws SQLException {
        String sql = "INSERT INTO onez.order_items (order_id, product_id, quantity, price_at_order) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            for (CartItemModel item : cartItems) {
                ProductModel product = item.getProduct();
                stmt.setInt(1, orderId);
                stmt.setInt(2, product.getProductId());
                stmt.setInt(3, item.getProductQuantity());
                stmt.setDouble(4, product.getPrice());
                stmt.addBatch();
            }
            
            if (!executeBatchSuccessfully(stmt)) {
                throw new SQLException("Failed to insert order items");
            }
        }
    }

    private boolean updateProductQuantities(CartModel cart) throws SQLException {
        String sql = "UPDATE onez.product SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            for (CartItemModel item : cart.getItems()) {
                stmt.setInt(1, item.getProductQuantity());
                stmt.setInt(2, item.getProduct().getProductId());
                stmt.setInt(3, item.getProductQuantity()); // Ensure sufficient quantity
                stmt.addBatch();
            }
            return executeBatchSuccessfully(stmt);
        }
    }

    private boolean clearUserCart(int cartId) throws SQLException {
        try {
            deleteCartItems(cartId);
            resetCartTotals(cartId);
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }

    // Helper methods
    private boolean executeBatchSuccessfully(PreparedStatement stmt) throws SQLException {
        int[] results = stmt.executeBatch();
        for (int result : results) {
            if (result != PreparedStatement.SUCCESS_NO_INFO && result <= 0) {
                return false;
            }
        }
        return true;
    }

    private void validatePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("Payment method cannot be null or empty");
        }
    }

    private List<OrderItemModel> convertCartItemsToOrderItems(List<CartItemModel> cartItems) {
        List<OrderItemModel> orderItems = new ArrayList<>();
        for (CartItemModel cartItem : cartItems) {
            ProductModel product = cartItem.getProduct();
            OrderItemModel orderItem = new OrderItemModel();
            orderItem.setProductId(product.getProductId());
            orderItem.setQuantity(cartItem.getProductQuantity());
            orderItem.setPriceAtOrder(product.getPrice());
            orderItem.setProduct(product);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    // Mapping methods
    private CartModel mapCart(ResultSet rs, int userId) throws SQLException {
        CartModel cart = new CartModel();
        cart.setCartId(rs.getInt("cart_id"));
        
        UserModel user = new UserModel();
        user.setId(userId);
        cart.setUser(user);
        
        cart.setTotalItems(rs.getInt("total_items"));
        cart.setTotalPrice(rs.getDouble("total_price"));
        cart.setCreatedAt(rs.getDate("createdAt").toLocalDate());
        return cart;
    }

    private CartItemModel mapCartItem(ResultSet rs) throws SQLException {
        CartItemModel item = new CartItemModel();
        item.setCartItemId(rs.getInt("cartitem_id"));
        item.setProductQuantity(rs.getInt("productQuantity"));
        item.setProduct(mapProduct(rs));
        return item;
    }

    private OrderModel mapOrder(ResultSet rs) throws SQLException {
        OrderModel order = new OrderModel();
        order.setOrderId(rs.getInt("order_id"));
        order.setOrderDate(rs.getDate("order_date").toLocalDate());
        order.setOrderStatus(rs.getString("order_status"));
        order.setPaymentMethod(rs.getString("paymentMethod"));
        return order;
    }

    private OrderItemModel mapOrderItem(ResultSet rs) throws SQLException {
        OrderItemModel item = new OrderItemModel();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPriceAtOrder(rs.getDouble("price_at_order"));
        item.setProduct(mapProduct(rs));
        return item;
    }

    private ProductModel mapProduct(ResultSet rs) throws SQLException {
        ProductModel product = new ProductModel();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("productName"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setCategory(rs.getString("category"));
        product.setProductImage(rs.getString("productImage"));
        return product;
    }

    private void deleteCartItems(int cartId) throws SQLException {
        String sql = "DELETE FROM onez.cartitem WHERE cart_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }

    private void resetCartTotals(int cartId) throws SQLException {
        String sql = "UPDATE onez.cart SET total_items = 0, total_price = 0 WHERE cart_id = ?";
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        }
    }
    
    public List<OrderModel> getAllOrders() throws SQLException {
        String orderSql = "SELECT o.*, u.first_name, u.last_name, a.name as address " +
                         "FROM onez.order_table o " +
                         "JOIN onez.user u ON o.user_id = u.user_id " +
                         "LEFT JOIN onez.address a ON u.address_id = a.address_id " +
                         "ORDER BY o.order_date DESC";
        
        String itemsSql = "SELECT oi.*, p.* FROM onez.order_items oi " +
                         "JOIN onez.product p ON oi.product_id = p.product_id " +
                         "WHERE oi.order_id = ?";
        
        List<OrderModel> orders = new ArrayList<>();
        
        try (PreparedStatement orderStmt = dbConn.prepareStatement(orderSql)) {
            try (ResultSet orderRs = orderStmt.executeQuery()) {
                while (orderRs.next()) {
                    OrderModel order = mapOrder(orderRs);
                    order.setUser(mapOrderUser(orderRs));
                    order.setItems(getOrderItems(order.getOrderId(), itemsSql));
                    
                    // Calculate total price
                    double totalPrice = order.getItems().stream()
                        .mapToDouble(item -> item.getPriceAtOrder() * item.getQuantity())
                        .sum();
                    order.setTotalPrice(totalPrice);
                    
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE onez.order_table SET order_status = ? WHERE order_id = ?";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            dbConn.commit();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            dbConn.rollback();
            System.err.println("Update failed: " + e.getMessage());
            throw e;
        }
    }
    
    private UserModel mapOrderUser(ResultSet rs) throws SQLException {
        UserModel user = new UserModel();
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        
        // Create and set AddressModel with just the name
        AddressModel address = new AddressModel();
        address.setName(rs.getString("address"));
        
        user.setAddress(address);
        return user;
    }
    
    public List<OrderModel> getRecentOrders() {
        if (isConnectionError) {
            System.out.println("Connection Error!");
            return null;
        }

        // SQL query to fetch recent orders with customer details and address
        String query = "SELECT o.order_id, o.order_status, u.first_name, u.last_name, u.address_id, a.name as name " +
                       "FROM onez.order_table o " +
                       "JOIN onez.user u ON o.user_id = u.user_id " +
                       "LEFT JOIN onez.address a ON u.address_id = a.address_id " +
                       "ORDER BY o.order_date DESC LIMIT 3";
        
        try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            List<OrderModel> orderList = new ArrayList<>();

            while (result.next()) {
                // Create AddressModel
                AddressModel addressModel = new AddressModel();
                addressModel.setAddressId(result.getInt("address_id"));
                addressModel.setName(result.getString("name"));

                // Create UserModel with basic info and address
                UserModel userModel = new UserModel();
                userModel.setFirstName(result.getString("first_name"));
                userModel.setLastName(result.getString("last_name"));
                userModel.setAddress(addressModel);

                // Create OrderModel
                OrderModel orderModel = new OrderModel();
                orderModel.setOrderId(result.getInt("order_id"));
                orderModel.setOrderStatus(result.getString("order_status"));
                orderModel.setUser(userModel);

                orderList.add(orderModel);
            }
            return orderList;
        } catch (SQLException e) {
            // Log and handle exceptions related to order query execution
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean deleteOrder(int orderId) throws SQLException {
        try {
            // First delete order items
            String deleteItemsSql = "DELETE FROM onez.order_items WHERE order_id = ?";
            try (PreparedStatement itemsStmt = dbConn.prepareStatement(deleteItemsSql)) {
                itemsStmt.setInt(1, orderId);
                itemsStmt.executeUpdate();
            }
            
            // Then delete the order
            String deleteOrderSql = "DELETE FROM onez.order_table WHERE order_id = ?";
            try (PreparedStatement orderStmt = dbConn.prepareStatement(deleteOrderSql)) {
                orderStmt.setInt(1, orderId);
                int affectedRows = orderStmt.executeUpdate();
                dbConn.commit();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            dbConn.rollback();
            throw e;
        }
    }
    
}