<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
HttpSession userSession = request.getSession(false);
String currentUser = (String) (userSession != null ? userSession.getAttribute("username") : null);
pageContext.setAttribute("currentUser", currentUser);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Your Wishlist - Onez</title>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" type="text/css" href="${contextPath}/css/wishlist.css">
    <script src="https://kit.fontawesome.com/91fb88d05c.js" crossorigin="anonymous"></script>
    <link href="https://cdn.lineicons.com/5.0/lineicons.css" rel="stylesheet" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <jsp:include page="header.jsp"/>
    
    <main class="main-content">
        <div class="wishlist-container">
            <h1><i class="fas fa-heart"></i> Your Wishlist</h1>

            <c:if test="${not empty error}">
                <div class="error-message">
                    <i class="fas fa-exclamation-circle"></i> ${error}
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="success-message">
                    <i class="fas fa-check-circle"></i> ${success}
                </div>
            </c:if>

            <c:choose>
                <c:when test="${not empty wishlist and not empty wishlist.products}">
                    <div class="wishlist-table-container">
                        <table class="wishlist-table">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Price</th>
                                    <th>Stock Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="product" items="${wishlist.products}">
                                    <tr>
                                        <td class="product-name">
                                                <div class="product-info">
                                                <a href="${contextPath}/viewProduct?productId=${product.productId}">
                                                    <img src="${contextPath}/resources/product/${product.productImage}" 
                                                         alt="${product.productName}" 
                                                         class="product-image"></a>
                                                    <div class="product-details">
                                                        <span class="product-title">${product.productName}</span>
                                                        <p class="product-desc">${product.description}</p>
                                                    </div>
                                                </div>
                                            
                                        </td>
                                        <td class="product-price">
                                            <fmt:formatNumber value="${product.price}" type="currency" />
                                        </td>
                                        <td class="stock-status">
                                            <c:choose>
                                                <c:when test="${product.quantity > 0}">
                                                    <span class="in-stock"><i class="fas fa-check-circle"></i> In Stock</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="out-of-stock"><i class="fas fa-times-circle"></i> Out of Stock</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="product-actions">
                                            <form action="${contextPath}/wishlist/remove" method="post" class="action-form">
                                                <input type="hidden" name="productId" value="${product.productId}" />
                                                <button type="submit" class="remove-btn">
                                                    <i class="fas fa-trash-alt"></i> Remove
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="wishlist-summary">
                        <div class="summary-details">
                            <h3>Wishlist Summary</h3>
                            <div class="summary-row">
                                <span>Total Items:</span>
                                <span>${wishlist.products.size()}</span>
                            </div>
                            <a href="${contextPath}/products" class="continue-shopping">
                                <i class="fas fa-arrow-left"></i> Continue Shopping
                            </a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-wishlist">
                        <i class="fas fa-heart empty-wishlist-icon"></i>
                        <h3>Your wishlist is empty</h3>
                        <p>You haven't added any items to your wishlist yet.</p>
                        <a href="${contextPath}/home" class="browse-btn">
                            <i class="fas fa-store"></i> Browse Products
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

    <jsp:include page="footer.jsp"/>

    <script>
        // Confirm before removing item
        document.querySelectorAll('.remove-btn').forEach(button => {
            button.addEventListener('click', (e) => {
                if (!confirm('Are you sure you want to remove this item from your wishlist?')) {
                    e.preventDefault();
                }
            });
        });
    </script>
</body>
</html>