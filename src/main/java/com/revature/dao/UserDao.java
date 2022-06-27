package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class UserDao implements IUserDao {

	@Override
	public int insert(User u) {

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO users (username, pwd, user_role) VALUES (?, ?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, u.getUsername());
			stmt.setString(2, u.getPassword());
			stmt.setObject(3, u.getRole(), Types.OTHER);
			//System.out.println(stmt);
			stmt.execute();
			sql = "SELECT * FROM users WHERE username = '" + u.getUsername() + "';";
			Statement stmt2 = conn.createStatement();
			ResultSet rs = stmt2.executeQuery(sql);
			if (rs.next()) {
				return (rs.getInt("id"));
			}

		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL Exception");
			e.printStackTrace();
		}
		return -1;

	}

	@Override
	public List<User> findByAcc(int id) { // find users linked to account

		List<User> userList = new ArrayList<User>();
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM users WHERE id IN (SELECT acc_owner FROM user_accounts_jt WHERE account = ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int idt = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("pwd");
				Role role = Role.valueOf(rs.getString("user_role"));
				
				List<Account> accList = new ArrayList<Account>();
				String sql2 = "SELECT * FROM accounts WHERE id IN (SELECT account FROM user_accounts_jt WHERE acc_owner = ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, id);
				ResultSet rs2 = pstmt.executeQuery();
				while(rs2.next()) {
					int acc_id = rs2.getInt("id");
					double balance = rs2.getDouble("balance");
					boolean isActive = rs2.getBoolean("active");
					Account temp2 = new Account(acc_id,balance,isActive);
					accList.add(temp2);
				}
				User temp = new User(idt, username, password, role, null);
				userList.add(temp);
			}

		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL Exception");
			e.printStackTrace();
		}

		return userList;
	}

	@Override
	public User findByUsername(String username) {
		try (Connection conn = ConnectionUtil.getConnection()) {

			String sql = "SELECT * FROM users WHERE username = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int idt = rs.getInt("id");
				String password = rs.getString("pwd");
				Role role = Role.valueOf(rs.getString("user_role"));
				List<Account> accList = new ArrayList<Account>();
				String sql2 = "SELECT * FROM accounts WHERE id IN (SELECT account FROM user_accounts_jt WHERE acc_owner = ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, idt);
				ResultSet rs2 = pstmt.executeQuery();
				while(rs2.next()) {
					int acc_id = rs2.getInt("id");
					double balance = rs2.getDouble("balance");
					boolean isActive = rs2.getBoolean("active");
					Account temp2 = new Account(acc_id,balance,isActive);
					accList.add(temp2);
				}
				
				User temp = new User(idt, username, password, role, accList);
				return (temp);
			}

		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL Exception");
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<User> findAll() {
		// Instantiate Arraylist to store retrieved objects
		List<User> userList = new ArrayList<User>();

		// obtain a connection using try with resources
		try (Connection conn = ConnectionUtil.getConnection()) {
			// Create statement to execute against the DB
			Statement stmt = conn.createStatement();

			// Create sql querry
			String sql = "SELECT * FROM users";

			// return all data qweried
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("pwd");
				Role role = Role.valueOf(rs.getString("user_role"));
				
				List<Account> accList = new ArrayList<Account>();
				String sql2 = "SELECT * FROM accounts WHERE id IN (SELECT account FROM user_accounts_jt WHERE acc_owner = ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, id);
				ResultSet rs2 = pstmt.executeQuery();
				while(rs2.next()) {
					int acc_id = rs2.getInt("id");
					double balance = rs2.getDouble("balance");
					boolean isActive = rs2.getBoolean("active");
					Account temp2 = new Account(acc_id,balance,isActive);
					accList.add(temp2);
				}
				
				User temp = new User(id,username,password,role,accList);
				userList.add(temp);
			}

		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL exception");
			e.printStackTrace();
		}

		return userList;
	}

	@Override
	public boolean update(User u) {
		
		try(Connection conn = ConnectionUtil.getConnection()) {
			String sql = "UPDATE users SET VALUES (?,?,?,?) WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, u.getId());
			stmt.setString(2, u.getUsername());
			stmt.setString(3,u.getPassword());
			stmt.setString(4,u.getRole().toString());
			stmt.setInt(5,u.getId());
			stmt.execute();
			
			sql = "DELETE FROM user_accounts_jt WHERE acc_owner = " + u.getId() + ";";
			Statement stmt2 = conn.createStatement();
			stmt2.execute(sql);
			
			for(int i = 0; i < u.getAccounts().size(); i++) {
				sql = "INSERT INTO user_accounts_jt VALUES (?,?);";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, u.getId());
				stmt.setInt(2, u.getAccounts().get(i).getId());
				stmt.execute();
			}

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} return false;
		
		
		
	}

	@Override
	public boolean delete(int username) {
		try(Connection conn = ConnectionUtil.getConnection()) {
			String sql = "DELETE FROM user_accounts_jt WHERE acc_owner = " + username + ";";
			Statement stmt2 = conn.createStatement();
			stmt2.execute(sql);
			sql = "DELETE FROM users WHERE id = " + username + ";";
			stmt2.execute(sql);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} return false;
	}

}
