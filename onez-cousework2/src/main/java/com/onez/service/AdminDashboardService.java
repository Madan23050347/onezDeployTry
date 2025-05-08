package com.onez.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.onez.config.DbConfig;
import com.onez.model.AddressModel;
import com.onez.model.UserModel;

/**
 * Service class for interacting with the database to retrieve dashboard-related
 * data. This class handles database connections and performs queries to fetch
 * user information.
 */
public class AdminDashboardService {

	private Connection dbConn;
	private boolean isConnectionError = false;

	/**
	 * Constructor that initializes the database connection. Sets the connection
	 * error flag if the connection fails.
	 */
	public AdminDashboardService() {
		try {
			dbConn = DbConfig.getDbConnection();
		} catch (SQLException | ClassNotFoundException ex) {
			// Log and handle exceptions related to database connection
			ex.printStackTrace();
			isConnectionError = true;
		}
	}

	/**
	 * Retrieves all user information from the database.
	 * 
	 * @return A list of UserModel objects containing user data. Returns null
	 *         if there is a connection error or if an exception occurs during query
	 *         execution.
	 */
	public List<UserModel> getAllUsersInfo() {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		// SQL query to fetch user details
		String query = "SELECT user_id, first_name, last_name, address_id, email, number FROM user WHERE userRole = 'customer'";
		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			ResultSet result = stmt.executeQuery();
			List<UserModel> userList = new ArrayList<>();

			while (result.next()) {
				// SQL query to fetch address name based on address_id
				String addressQuery = "SELECT address_id, name FROM address WHERE address_id = ?";
				try (PreparedStatement addressStmt = dbConn.prepareStatement(addressQuery)) {
					addressStmt.setInt(1, result.getInt("address_id"));
					ResultSet addressResult = addressStmt.executeQuery();

					AddressModel addressModel = new AddressModel();
					if (addressResult.next()) {
						// Set address name in the AddressModel
						addressModel.setName(addressResult.getString("name"));
						addressModel.setAddressId(addressResult.getInt("address_id"));
					}

					// Create and add UserModel to the list
					userList.add(new UserModel(result.getInt("user_id"), // User ID
							result.getString("first_name"), // First Name
							result.getString("last_name"), // Last Name
							addressModel, // Associated Address
							result.getString("email"), // Email
							result.getString("number") // Phone Number
					));

					addressResult.close(); // Close ResultSet to avoid resource leaks
				} catch (SQLException e) {
					// Log and handle exceptions related to address query execution
					e.printStackTrace();
					// Continue to process other users or handle this error appropriately
				}
			}
			return userList;
		} catch (SQLException e) {
			// Log and handle exceptions related to user query execution
			e.printStackTrace();
			return null;
		}
	}

	public UserModel getSpecificUserInfo(int userId) {
		if (isConnectionError) {
			System.out.println("Connection Error!");
			return null;
		}

		// SQL query to join user and address tables
		String query = "SELECT s.user_id, s.first_name, s.last_name, s.username, s.dob, s.gender, "
				+ "s.email, s.number, s.address_id, s.profilePic, "
				+ "p.name AS name" + "FROM user s "
				+ "JOIN address p ON s.address_id = p.address_id " + "WHERE s.user_id = ?";

		try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
			stmt.setInt(1, userId);
			ResultSet result = stmt.executeQuery();
			UserModel user = null;

			if (result.next()) {
				// Extract data from the result set
				int id = result.getInt("user_id");
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String userName = result.getString("username");
				LocalDate dob = result.getDate("dob").toLocalDate(); // Assuming dob is of type DATE in SQL
				String gender = result.getString("gender");
				String email = result.getString("email");
				String number = result.getString("number");
				String imageUrl = result.getString("profilePic");

				// Create addresAModel instance
				AddressModel address = new AddressModel();
				address.setAddressId(result.getInt("address_id"));
				address.setName(result.getString("name"));

				// Create UserModel instance
				user = new UserModel(id, firstName, lastName, userName, dob, gender, email, number, null, address,
						imageUrl);

				// Add the user to the list
			}
			return user;
		} catch (SQLException e) {
			// Log and handle exceptions
			e.printStackTrace();
			return null;
		}
	}

	public String getTotalUsers() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS total FROM user where userRole= 'customer';";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {

			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("total");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getTotalProducts() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS totalProduct FROM product;";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {

			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("totalProduct");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getKathmanduUsers() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS total FROM user WHERE address_id = 1;";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("total");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getLalitpurUsers() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS total FROM user WHERE address_id = 2;";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("total");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getBhaktapurUsers() {
		if (isConnectionError) {
			return null;
		}

		String countQuery = "SELECT COUNT(*) AS total FROM user WHERE address_id = 3;";
		try (PreparedStatement stmt = dbConn.prepareStatement(countQuery)) {
			ResultSet result = stmt.executeQuery();
			if (result.next()) {
				return result.getString("total");
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}