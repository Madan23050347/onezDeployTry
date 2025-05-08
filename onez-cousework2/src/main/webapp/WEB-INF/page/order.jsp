<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Checkout</title>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/order.css">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <main class="main-content">
        <div class="checkout-container">
            <h1>Checkout</h1>
            
            <c:if test="${not empty success}">
                <div class="message success-message">
                    ${success}
                    <c:if test="${not empty latestOrder}">
                        <p>Your order ID is: <strong>${latestOrder.orderId}</strong></p>
                    </c:if>
                </div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="message error-message">${error}</div>
            </c:if>
            
            <c:choose>
                <c:when test="${empty success}">
                    <!-- Check for empty cart -->
                    <c:choose>
                        <c:when test="${empty cart or empty cart.items}">
                            <div class="empty-cart">
                                <div class="empty-cart-icon">🛒</div>
                                <h3>Your cart is empty</h3>
                                <p>There are no items to checkout</p>
                                <a href="${contextPath}/home" class="btn btn-primary">Browse Products</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <!-- Show checkout form when cart has items -->
                            <div class="order-summary">
                                <h2>Order Summary</h2>
                                <div class="order-items">
                                    <c:forEach items="${cart.items}" var="item">
                                        <div class="order-item">
                                            <div class="item-details">
                                                <img src="${contextPath}/resources/product/${item.product.productImage}" 
                                                     alt="${item.product.productName}" class="item-image">
                                                <div>
                                                    <h4>${item.product.productName}</h4>
                                                    <p>Quantity: ${item.productQuantity}</p>
                                                    <p>Price: $<fmt:formatNumber value="${item.product.price}" 
                                                                               minFractionDigits="2" maxFractionDigits="2"/></p>
                                                </div>
                                            </div>
                                            <div class="item-price">
                                                $<fmt:formatNumber value="${item.product.price * item.productQuantity}" 
                                                                    minFractionDigits="2" maxFractionDigits="2"/>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="order-total">
                                    <p>Total Items: ${cart.totalItems}</p>
                                    <p>Total Price: $<fmt:formatNumber value="${cart.totalPrice}" 
                                                                      minFractionDigits="2" maxFractionDigits="2"/></p>
                                </div>
                            </div>
                            
                            <form action="${contextPath}/processOrder" method="post" class="payment-form">
                                <h2>Payment Method</h2>
                                
                                <div class="payment-options">
                                    <div class="payment-option">
                                        <input type="radio" id="esewa" name="paymentMethod" value="Esewa" required>
                                        <label for="esewa">Esewa</label>
                                    </div>
                                    
                                    <div class="payment-option">
                                        <input type="radio" id="khalti" name="paymentMethod" value="Khalti">
                                        <label for="khalti">Khalti</label>
                                    </div>
                                    
                                    <div class="payment-option">
                                        <input type="radio" id="imePay" name="paymentMethod" value="IME Pay">
                                        <label for="imePay">IME Pay</label>
                                    </div>
                                    
                                    <div class="payment-option">
                                        <input type="radio" id="cashOnDelivery" name="paymentMethod" value="Cash on Delivery">
                                        <label for="cashOnDelivery">Cash on Delivery</label>
                                    </div>
                                </div>
                                
                                <div class="form-actions">
                                    <button type="submit" class="btn btn-primary">Place Order</button>
                                    <a href="${contextPath}/cart" class="btn btn-secondary">Back to Cart</a>
                                </div>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <!-- Order Confirmation (shown after successful order) -->
                    <div class="order-details">
                        <h2>Order Confirmation</h2>
                        <c:if test="${not empty latestOrder}">
                            <div class="order-info">
                                <p><strong>Order ID:</strong> ${latestOrder.orderId}</p>
                                <p><strong>Date:</strong> ${latestOrder.orderDate}</p>
                                <p><strong>Status:</strong> ${latestOrder.orderStatus}</p>
                                <p><strong>Payment Method:</strong> ${latestOrder.paymentMethod}</p>
                            </div>
                            
                            <h3>Order Items</h3>
                            <div class="order-items">
                                <c:forEach items="${latestOrder.items}" var="item">
                                    <div class="order-item">
                                        <div class="item-details">
                                            <img src="${contextPath}/images/products/${item.product.productImage}" 
                                                 alt="${item.product.productName}" class="item-image">
                                            <div>
                                                <h4>${item.product.productName}</h4>
                                                <p>Quantity: ${item.quantity}</p>
                                            </div>
                                        </div>
                                        <div class="item-price">
                                            $<fmt:formatNumber value="${item.priceAtOrder * item.quantity}" 
                                                              minFractionDigits="2" maxFractionDigits="2"/>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            
                            <div class="order-total">
                                <p>Subtotal: $<fmt:formatNumber value="${latestOrder.items.stream().map(item -> item.priceAtOrder * item.quantity).sum()}" 
                                                                 minFractionDigits="2" maxFractionDigits="2"/></p>
                            </div>
                        </c:if>
                    </div>
                    
                    <div class="form-actions">
                        <a href="${contextPath}/order" class="btn btn-secondary">View All Orders</a>
                        <a href="${contextPath}/home" class="btn btn-primary">Continue Shopping</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <jsp:include page="footer.jsp"/>
</body>
</html>