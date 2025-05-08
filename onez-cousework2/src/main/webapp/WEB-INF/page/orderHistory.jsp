<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.util.Date" %>

<c:set var="orderDate" value="${order.orderDate}" />
<%
// Get the LocalDate from EL and convert to Date
LocalDate localDate = (LocalDate) pageContext.getAttribute("orderDate");
Date date = localDate != null ? 
    Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()) : 
    null;
pageContext.setAttribute("formattedDate", date);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Order History - Onez</title>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" href="${contextPath}/css/orderHistory.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <main class="main-content">
        <div class="order-history-container">
            <h1><i class="fas fa-history"></i> Order History</h1>
            
            <c:if test="${not empty error}">
                <div class="error-message">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            
            <c:choose>
                <c:when test="${empty orders}">
                    <div class="empty-orders">
                        <i class="fas fa-box-open"></i>
                        <p>You haven't placed any orders yet.</p>
                        <a href="${contextPath}/home" class="btn-shop">Start Shopping</a>
                    </div>
                </c:when>
                
                <c:otherwise>
                	<!-- Show success message if present -->
					<c:if test="${not empty orderSuccess}">
					    <div class="success">${orderSuccess}</div>
					    <c:remove var="orderSuccess" scope="session"/>
					</c:if>
                    <div class="orders-list">
                        <c:forEach var="order" items="${orders}">
						    <div class="order-card">
						        <div class="order-header">
						            <div class="order-id">Order #${order.orderId}</div>
						            <div class="order-date-status">
						                <span class="order-date">
										    <fmt:formatDate value="${formattedDate}" pattern="MMMM dd, yyyy"/>
										</span>

						                <span class="order-status ${order.orderStatus.toLowerCase()}">
						                    ${order.orderStatus}
						                </span>
						            </div>
						            <form action="${contextPath}/orderHistory/delete" method="post" class="delete-form">
								        <input type="hidden" name="orderId" value="${order.orderId}">
								        <button type="submit" class="delete-btn" 
								                onclick="return confirm('Are you sure you want to delete this order? This action cannot be undone.');">
								            <i class="fas fa-trash-alt"></i> Delete
								        </button>
								    </form>
						        </div>
						        
						        <div class="order-details">
						            <div class="order-items">
						                <c:forEach var="item" items="${order.items}">
						                    <div class="order-item">
						                        <a href="${contextPath}/viewProduct?productId=${item.product.productId}"><img src="${contextPath}/resources/product/${item.product.productImage}" 
						                             alt="${item.product.productName}" 
						                             class="item-image"></a>
						                        <div class="item-info">
						                            <h4>${item.product.productName}</h4>
						                            <p>Quantity: ${item.quantity}</p>
						                            <p class="item-price">
						                                <fmt:formatNumber value="${item.priceAtOrder}" type="currency"/> each
						                                <c:if test="${item.product.price != item.priceAtOrder}">
						                                    <span class="price-note">(current price: 
						                                        <fmt:formatNumber value="${item.product.price}" type="currency"/>)
						                                    </span>
						                                </c:if>
						                            </p>
						                        </div>
						                    </div>
						                </c:forEach>
						            </div>
						            
						            <div class="order-summary">
						                <div class="summary-row">
						                    <span>Items:</span>
						                    <span>${order.items.size()}</span> 
						                </div>
						                <div class="summary-row total">
						                    <span>Total:</span>
						                    <span>
						                        <fmt:formatNumber value="${order.items.stream().map(item -> item.priceAtOrder * item.quantity).sum()}" 
						                                         type="currency"/>
						                    </span>
						                </div>
						                <div class="payment-method">
										    <strong>Payment:</strong> ${order.paymentMethod}
										</div>
						            </div>
						        </div>
						    </div>
						</c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <jsp:include page="footer.jsp"/>
</body>
</html>