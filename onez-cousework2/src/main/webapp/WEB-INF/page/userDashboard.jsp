<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Edit Profile</title>

  <!-- Set contextPath variable -->
  <c:set var="contextPath" value="${pageContext.request.contextPath}" />

  <!-- Link to external CSS -->
  <link rel="stylesheet" type="text/css" href="${contextPath}/css/userDashboard.css" />
</head>
<body>

<!-- Sidebar Navigation -->
<nav class="sidebar">
  <div>
    <a href="${contextPath}/home"><div><img src="${contextPath}/resources/logo/logoWhite.png" alt="ONEZ Logo" class="logo"/></div></a>
  </div>
  <a href="${contextPath}/userDashboard" class="no-style"><div><p>Account details</p></div></a>
  <a href="${contextPath}/orderHistory" class="no-style"><div><p>Orders</p></div></a>
  <a href="${contextPath}/wishlist" class="no-style"><div><p>Wishlist</p></div></a>
  <a href="${contextPath}/cart" class="no-style"><div><p>Cart</p></div></a>

  <!-- Logout Button -->
  <form action="${contextPath}/logout" method="post" class="logout-form">
    <button type="submit" class="sidebar-button">Logout</button>
  </form>
</nav>


<!-- Main Content -->
<div class="main-content">
  <div class="content-wrapper">
    <div class="top-section">
      <h3>Manage My Account</h3>
    </div>

    <c:if test="${not empty successMessage}">
      <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger">${errorMessage}</div>
    </c:if>

    <div class="account-details">
      <form action="${pageContext.request.contextPath}/userDashboard" method="post">
        <div class="form-group">
          <div class="profile-icon">
            <img src="${contextPath}/resources/user/${user.imageUrl}" width="100" height="100"
                 onerror="this.src='${contextPath}/resources/logo/onez.svg'" />
          </div>
        </div>

        <div class="form-group">
          <label for="firstName">First Name</label>
          <input type="text" id="firstName" name="firstName" value="${user.firstName}" required />
        </div>

        <div class="form-group">
          <label for="lastName">Last Name</label>
          <input type="text" id="lastName" name="lastName" value="${user.lastName}" required />
        </div>

        <div class="form-group">
          <label for="dob">Date of Birth</label>
          <input type="date" id="dob" name="dob" value="${user.dob}" />
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" name="email" value="${user.email}" required />
        </div>

        <div class="form-group">
          <label for="number">Phone Number</label>
          <input type="tel" id="number" name="number" value="${user.number}" />
        </div>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary">Update Profile</button>
        </div>
      </form>
    </div>
  </div>
</div>



</body>
</html>
