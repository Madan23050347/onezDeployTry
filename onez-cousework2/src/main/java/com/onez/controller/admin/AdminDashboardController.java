package com.onez.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import com.onez.service.AdminDashboardService;
import com.onez.service.OrderService;
import com.onez.util.RedirectionUtil;

/**
 * Servlet implementation for handling dashboard-related HTTP requests.
 * 
 * This servlet manages interactions with the DashboardService to fetch student
 * information, handle updates, and manage student data. It forwards requests to
 * appropriate JSP pages or handles POST actions based on the request
 * parameters.
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/adminDashboard" })
public class AdminDashboardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Instance of DashboardService for handling business logic
	private AdminDashboardService dashboardService;
	private OrderService orderService;
	
	/**
	 * Default constructor initializes the DashboardService and orderService instance.
	 */
	public AdminDashboardController() {
		
		try {
            this.orderService = new OrderService();
            this.dashboardService = new AdminDashboardService();
        } catch (SQLException e) {
            // Log the error and handle it appropriately
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize OrderService", e);
        }
	}

	/**
	 * Handles HTTP GET requests by retrieving student information and forwarding
	 * the request to the dashboard JSP page.
	 * 
	 * @param request  The HttpServletRequest object containing the request data.
	 * @param response The HttpServletResponse object used to return the response.
	 * @throws ServletException If an error occurs during request processing.
	 * @throws IOException      If an input or output error occurs.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Retrieve all student information from the DashboardService
		request.setAttribute("orderList", orderService.getRecentOrders());
		request.setAttribute("totalProduct", dashboardService.getTotalProducts());
		request.setAttribute("total", dashboardService.getTotalUsers());

		// Forward the request to the dashboard JSP for rendering
		request.getRequestDispatcher(RedirectionUtil.adminDashboardUrl).forward(request, response);
	}

}