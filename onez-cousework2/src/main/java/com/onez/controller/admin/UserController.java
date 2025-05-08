package com.onez.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.onez.service.AdminDashboardService;

/**
 * Servlet implementation class userController
 */
@WebServlet(asyncSupported = true, urlPatterns = { "/modifyUsers" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50) // 50MB
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Instance of DashboardService for handling business logic
	private AdminDashboardService dashboardService;

	/**
	 * Default constructor initializes the DashboardService instance.
	 */
	public UserController() {
		this.dashboardService = new AdminDashboardService();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Retrieve all user information from the DashboardService
		request.setAttribute("userList", dashboardService.getAllUsersInfo());

		request.setAttribute("total", dashboardService.getTotalUsers());
		request.setAttribute("Kathmandu", dashboardService.getKathmanduUsers());
		request.setAttribute("Lalitpur", dashboardService.getLalitpurUsers());
		request.setAttribute("Bhaktapur", dashboardService.getBhaktapurUsers());
		// Forward the request to the Users JSP for rendering
		request.getRequestDispatcher("/WEB-INF/page/admin/users.jsp").forward(request, response);
	}
}