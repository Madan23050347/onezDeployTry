<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
   
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>About Us - OneZ Gaming</title>
     	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
		<link rel="stylesheet" type="text/css"
		href="${pageContext.request.contextPath}/css/aboutus.css" />
  </head>
  <body>
  <jsp:include page="header.jsp" />
  <div clas="main-content">
    <div class="main">
      <div class="image1">
        <img src="${contextPath}/resources/aboutus/About.jpg" alt="aboutus" />
      </div>
      <div class="imagetext">
        <h1>About Us</h1>
        <p>
          At OneZ, we live and breathe gaming. Whether you're a casual player, a
          hardcore enthusiast, or a two-level competitor, our mission is simple:
          to elevate your gaming experience with top-tier peripherals and gear
          that deliver performance, style, and durability.
        </p>
      </div>
    </div>

    <div class="history">
      <div class="image2">
        <img src="${contextPath}/resources/aboutus/vision.jpg" alt="image2" />
      </div>
      <div class="history-text">
        <h2>OUR HISTORY</h2>

        <p>
          Founded in 2025, OneZ started with a vision to make premium gaming
          accessories accessible to every player. What began as a small passion
          project in a home office has now grown into a trusted name in the
          gaming community.
        <br><br>
          From our very first RGB keyboard to today's full range of peripherals,
          we've stayed committed to quality, innovation, and the ever-evolving
          needs of gamers worldwide.
        </p>
      </div>
    </div>

    <div class="goal">
      <div class="image3">
        <img src="${contextPath}/resources/aboutus/goal.jpg" alt="image3" />
      </div>
      <div class="vision-text">
        <h2>Our Goal and Vision</h2>
        <p>
          Our goal is to provide high-quality gaming gear that helps every gamer
          play at their best. Whether you're a beginner or a pro, we want to
          give you the right tools to enjoy your games and improve your
          performance.
        <br> <br>
          Our vision is to make top gaming equipment available to everyone and
          to become a trusted name in the gaming world by building a strong
          community of gamers who share our passion.
        </p>
      </div>
    </div>
    </div>
    <jsp:include page="footer.jsp" />
    
  </body>
</html>