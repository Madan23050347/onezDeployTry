<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>${product.productName}</title>
    <!-- Set contextPath variable -->
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
	<link rel="stylesheet" type="text/css"
		href="${pageContext.request.contextPath}/css/header.css" />
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/viewProduct.css" />
          <link rel="stylesheet" type="text/css"
		href="${pageContext.request.contextPath}/css/footer.css" />
    <script src="https://kit.fontawesome.com/91fb88d05c.js" crossorigin="anonymous"></script>
    <link href="https://cdn.lineicons.com/5.0/lineicons.css" rel="stylesheet" />
    <style>
    
       .product-container {
  width: 20rem;
  height: auto;
  overflow: hidden;
  border-radius: 1rem;
  /* box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.25); */
  /* border: 1px solid black; */
  /* transition: box-shadow 0.25s; */
}

.product-container:hover {
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.25);
}

.product-image img {
  width: 100%;
  height: 100%;
 
  object-fit: contain;
  /* mix-blend-mode: color-burn; */
}

.product-image {
  width: 100%;
  height: 18rem;
  overflow: hidden;
   padding: 10px 15px;
}

.product-info {
  display: flex;
  flex-direction: column;
  justify-content: center;
  /* align-items: center; */
  padding-left: 20px;
  gap: 0.75rem;
  overflow: hidden;
  padding-bottom: 10px;
}

.product-info h4 {
  font-size: x-large;
}

.product-info p {
  color: #808080;
  font-size: larger;
  font-weight: bold;
}

.product-info h5 {
  color: #e65100;
  font-size: large;
}

.product-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin: 1rem 0px;
  gap: 1rem;
}

        .others h2 {
            font-size: 2rem;
            padding-left: 2rem;
            padding-top: 1rem;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<br><br><br>
<!--  
<main class="main-content">
	<div class="view-container">
	    <div class="view-img">
	        <img src="${contextPath}/resources/product/${product.productImage}" 
	             alt="${product.productName}" />
	    </div>
	    <div class="view-details">
	        <h2>${product.productName}</h2>
	        <div class="price">Rs.${product.price}</div>
	        <div class="desc">${product.description}</div>
	        <p><strong>Category:</strong> ${product.category}</p>
	        <p><strong>Available Quantity:</strong> ${product.quantity}</p>
	
	        <!-- Quantity and Buttons 
	        <form action="${contextPath}/cart/add" method="post" class="product-actions">
	            <input type="hidden" name="productId" value="${product.productId}" />
	
	            <div class="quantity-control">
	                <label for="quantity">Quantity:</label>
	                <button type="button" onclick="decreaseQty()">-</button>
	                <input type="number" id="quantity" name="quantity" value="1" min="1" max="${product.quantity}" />
	                <button type="button" onclick="increaseQty()">+</button>
	            </div>
	
	            <div class="product-actions">
	                <button type="submit" class="add-to-cart">Add to Cart</button>
	                <button type="submit" class="buy-now" formaction="${contextPath}/order">Buy Now</button>
	            </div>
	        </form>
	        <form action="${pageContext.request.contextPath}/wishlist/add" method="post">
			  <input type="hidden" name="productId" value="${product.productId}">
			  <button type="submit" class="wishlist-btn">Add to Wishlist</button>
			</form>
	    </div>
	</div>

</main>
-->

<section class="product-details">
        <div class="container">
            <div class="image-container">
                <img src="${contextPath}/resources/product/${product.productImage}" 
	             alt="${product.productName}" />
            </div>
            
            <div class="details-container">
                <h1 class="product-name">${product.productName}</h1>
                <div class="price-container">
                    <p class="price">Rs.${product.price}</p>
                   <div class="stock ${product.quantity <= 0 ? 'out-of-stock' : 'in-stock'}">
    ${product.quantity <= 0 ? 'Out of Stock' : 'In Stock'}
</div>
                </div>

                <div class="colors">
                    <span class="option-label">Colors:</span>
                    <div class="color-options">
                        <button class="btn-color white active" aria-label="White color"></button>
                        <button class="btn-color black" aria-label="Black color"></button>
                    </div>
                </div>

            <form action="${contextPath}/cart/add" method="post" class="product-actions">
	            <input type="hidden" name="productId" value="${product.productId}" />
		<p><strong>Available Quantity:</strong> ${product.quantity}</p>
	            <div class="quantity-control">
	                <label for="quantity">Quantity:</label>
	                <button type="button" onclick="decreaseQty()">-</button>
	                <input type="number" id="quantity" name="quantity" value="0" min="1" max="${product.quantity}" />
	                <button type="button" onclick="increaseQty()">+</button>
	            </div>
	
	            <div class="product-actions">
	                <button type="submit" class="add-to-cart" > Add to Cart
    </button>
	                <button type="submit" class="buy-now" formaction="${contextPath}/order">Buy Now</button>
	            </div>
	            
	        </form>
	        
	        <form action="${pageContext.request.contextPath}/wishlist/add" method="post">
			  <input type="hidden" name="productId" value="${product.productId}">
			  <button type="submit" class="wishlist-btn">Add to Wishlist</button>
			</form>
			  
            </div>
        </div>
    </section>
    
    
    
<section class="descripton">
        <div class="descripton-wrapper">
            <h2>About this Product</h2>
            <p>The Lamzu Maya X is a high-performance wireless gaming mouse designed for competitive gamers who value
                precision, speed, and comfort. Its lightweight design supports all grip styles—palm, claw, and
                fingertip—making it adaptable to various play preferences. With included customization software, users
                can fine-tune settings to match their gameplay needs. The package also includes an 8K adapter and mouse
                grip forenhanced control and feel. Sensor: PixArt 3950 Optical Sensor
                Polling Rate: Up to 8000 Hz (8K) for ultra-fast responsiveness
                DPI: Up to 30,000 DPI for highly accurate tracking
                Speed & Acceleration: 750 IPS tracking speed and 50G maximum acceleration
                Switches: Omron Optical Switches to prevent double-clicking
                Encoder: Custom Lamzu Encoder
                Included Accessories: 8K Adapter and Mouse Grip
                MCU: Nordic 52840 for consistent wireless performance
                Connection Options: Wireless 2.4GHz and USB</p>
        </div>
    </section>

    <section class="products">
    <h1 style="font-size:30; margin-left:40%">Other Products</h1>
    <div class="product-box">
        <c:forEach var="product" items="${recentProducts}">
            <div class="product-container">
                <div class="product-image">
                    <a href="${contextPath}/viewProduct?productId=${product.productId}">
                        <img src="${contextPath}/resources/product/${product.productImage}" alt="${product.productName}">
                    </a>
                </div>
                <div class="product-info">
                    <h4>${product.productName}</h4>
                    <p>Category: ${product.category}</p>
                    <h5>Rs. ${product.price}</h5>
                </div>
            </div>
        </c:forEach>
    </div>
</section>


<jsp:include page="footer.jsp" />

<script>
    function increaseQty() {
        const input = document.getElementById("quantity");
        const max = parseInt(input.max);
        let current = parseInt(input.value);
        if (current < max) input.value = current + 1;
    }

    function decreaseQty() {
        const input = document.getElementById("quantity");
        let current = parseInt(input.value);
        if (current > 1) input.value = current - 1;
    }
</script>

</body>
</html>
