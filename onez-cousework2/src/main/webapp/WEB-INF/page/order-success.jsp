<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Order Confirmation</title>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" href="${contextPath}/css/order.css">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <main class="main-content">
        <div class="checkout-container">
            <h1>Order Confirmation</h1>
            
            <c:if test="${not empty orderSuccess}">
                <div class="order-details">
                    <div class="success-message">
                        <h2>Thank you for your order!</h2>
                        <p>Your order #${orderSuccess.orderId} has been placed successfully.</p>
                    </div>
                    
                    <div class="order-info">
                        <p><strong>Order Date:</strong> ${orderSuccess.orderDate}</p>
                        <p><strong>Status:</strong> ${orderSuccess.orderStatus}</p>
                        <p><strong>Payment Method:</strong> ${orderSuccess.paymentMethod}</p>
                    </div>
                    
                    <h3>Order Items</h3>
                    <div class="order-items">
                        <c:forEach items="${orderSuccess.items}" var="item">
                            <div class="order-item">
                                <div class="item-details">
                                    <img src="${contextPath}/images/products/${item.product.productImage}" 
                                         alt="${item.product.productName}" class="item-image">
                                    <div>
                                        <h4>${item.product.productName}</h4>
                                        <p>Quantity: ${item.quantity}</p>
                                        <p>Price: $<fmt:formatNumber value="${item.priceAtOrder}" 
                                                                     minFractionDigits="2" maxFractionDigits="2"/></p>
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
                        <p><strong>Total:</strong> $<fmt:formatNumber value="${orderSuccess.items.stream().map(item -> item.priceAtOrder * item.quantity).sum()}" 
                                                                     minFractionDigits="2" maxFractionDigits="2"/></p>
                    </div>
                    
                    <div class="form-actions">
                        <a href="${contextPath}/order-history" class="btn btn-primary">View Order History</a>
                        <a href="${contextPath}/products" class="btn btn-secondary">Continue Shopping</a>
                    </div>
                </div>
            </c:if>
        </div>
    </main>

    <jsp:include page="footer.jsp"/>
</body>
</html>