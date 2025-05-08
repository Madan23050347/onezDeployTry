<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products</title>
    <!-- Set contextPath variable -->
	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/viewCategory.css">
    <link href="https://cdn.lineicons.com/5.0/lineicons.css" rel="stylesheet" />
    <script src="https://kit.fontawesome.com/91fb88d05c.js" crossorigin="anonymous"></script>
</head>

<body>
    <jsp:include page="header.jsp" />

    <div class="main-content">
        <aside class="left-panel">
            <div class="category-title">
                <h3>Category:</h3>
            </div>
           <!--<ul class="category-list">
                <li class="category-items">
                    <input type="checkbox" id="all" name="category" checked>
                    <label for="all">ALL</label>
                </li>
                <li class="category-items">
                    <input type="checkbox" id="consoles" name="category">
                    <label for="consoles">Consoles</label> <!--for="consoles" is used so when press on 
                    text inside label it will activate checkbox with id="consoles"
                </li>
                <li class="category-items">
                    <input type="checkbox" id="keyboards" name="category">
                    <label for="keyboards">Keyboards</label>
                </li>
                <li class="category-items">
                    <input type="checkbox" id="mouse" name="category">
                    <label for="mouse">Mouse</label>
                </li>
                <li class="category-items">
                    <input type="checkbox" id="headset" name="category">
                    <label for="headset">Headset</label>
                </li>
                <li class="category-items">
                    <input type="checkbox" id="monitor" name="category">
                    <label for="monitor">Monitor</label>
                </li>
                <li class="category-items">
                    <input type="checkbox" id="cpu-gpu" name="category">
                    <label for="cpu-gpu">CPU/GPU</label>
                </li>
                <li class="category-items">
                    <input type="checkbox" id="storage" name="category">
                    <label for="storage"></label>Storage</label>
                </li>
                <li class="category-items">
                    <input type="checkbox" id="accessories" name="category">
                    <label for="accessories">Accessories</label>
                </li>
            </ul>  --> 
            <ul class="category-list">
			    <li class="category-items">
			        <a href="${contextPath}/viewCategory?category=All" class="${empty param.category || param.category == 'All' ? 'active' : ''}">
			            ALL
			        </a>
			    </li>
			    <li class="category-items">
			        <a href="${contextPath}/viewCategory?category=Consoles" class="${param.category == 'Consoles' ? 'active' : ''}">
			            Consoles
			        </a>
			    </li>
			    <li class="category-items">
			        <a href="${contextPath}/viewCategory?category=Keyboard" class="${param.category == 'Keyboard' ? 'active' : ''}">
			            Keyboard
			        </a>
			    </li>
				<li class="category-items">
			        <a href="${contextPath}/viewCategory?category=Mouse" class="${param.category == 'Mouse' ? 'active' : ''}">
			            Mouse
			        </a>
			    </li>
				<li class="category-items">
			        <a href="${contextPath}/viewCategory?category=Headphone" class="${param.category == 'Headset' ? 'active' : ''}">
			            Headset
			        </a>
			    </li>
				<li class="category-items">
			        <a href="${contextPath}/viewCategory?category=Monitor" class="${param.category == 'Monitor' ? 'active' : ''}">
			            Monitor
			        </a>
			    </li>
				<li class="category-items">
			        <a href="${contextPath}/viewCategory?category=CPU" class="${param.category == 'CPU' ? 'active' : ''}">
			            CPU/GPU
			        </a>
			    </li>
				<li class="category-items">
			        <a href="${contextPath}/viewCategory?category=Storage" class="${param.category == 'Storage' ? 'active' : ''}">
			            Storage
			        </a>
			    </li>
			    <li class="category-items">
			        <a href="${contextPath}/viewCategory?category=Mousepad" class="${param.category == 'Mousepad' ? 'active' : ''}">
			            Mousepad
			        </a>
			    </li>
			</ul>
        </aside>

        <!-- right panel -->
        <section class="right-panel">
            <div class="select-price">
                <div class="text">
				    <h3>${empty param.category || param.category == 'All' ? 'All' : param.category}</h3>
				</div>
                <div class="selection">
                    <label for="sort-by">Sort By:</label>
                    <select name="sort-by" id="sort-by">
                        <option value="default">Default</option>
                        <option value="priceHigh">High-Low</option>
                        <option value="priceLow">Low-High</option>
                    </select>
                </div>
                <div class="category">
				    <div class="category-selection">
				        <form id="categoryForm" action="${contextPath}/viewCategory" method="get">
				            <label for="category">Category:</label>
				            <select name="category" id="category" onchange="this.form.submit()">
				                <option value="All">All</option>
				                <option value="Consoles" ${param.category == 'Consoles' ? 'selected' : ''}>Consoles</option>
				                <option value="Keyboards" ${param.category == 'Keyboards' ? 'selected' : ''}>Keyboards</option>
				                <option value="Mouse" ${param.category == 'Mouse' ? 'selected' : ''}>Mouse</option>
				                <option value="Headset" ${param.category == 'Headset' ? 'selected' : ''}>Headset</option>
				                <option value="Monitor" ${param.category == 'Monitor' ? 'selected' : ''}>Monitor</option>
				                <option value="CPU/GPU" ${param.category == 'CPU/GPU' ? 'selected' : ''}>CPU/GPU</option>
				                <option value="Storage" ${param.category == 'Storage' ? 'selected' : ''}>Storage</option>
				                <option value="Accessories" ${param.category == 'Accessories' ? 'selected' : ''}>Accessories</option>
				            </select>
				        </form>
				    </div>
				</div>
            </div>



            <section>
                <div class="product-box">
			        <c:forEach var="product" items="${products}">
			            <div class="product-container">
			                <div class="product-image">
			                    <a href="${contextPath}/viewProduct?productId=${product.productId}">
			                        <img src="${contextPath}/resources/product/${product.productImage}" alt="${product.productName}">
			                    </a>
			                </div>
			                <div class="product-info">
			                    <h4>${product.productName}</h4>
			                    <p>Category: ${product.category}</p>
			                    <h5>Rs.${product.price}</h5>
			                 
			                </div>
			            </div>
			        </c:forEach>
			    </div>
            </section>
        </section>
    </div>

            <jsp:include page="footer.jsp" />
</body>

</html>