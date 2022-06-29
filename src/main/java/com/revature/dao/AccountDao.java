package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class AccountDao implements IAccountDao {

	public int insert(Account a, User u) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO accounts (balance, active) VALUES (?, ?);";
			PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setDouble(1, a.getBalance());
			stmt.setBoolean(2, a.isActive());
			stmt.execute();
			ResultSet rs = stmt.getGeneratedKeys();
			int acc_id = -1;
			if (rs.next()) {
				acc_id = (rs.getInt("id"));
			}
			sql = "INSERT INTO user_accounts_jt VALUES (?,?);";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, u.getId());
			stmt.setInt(2, acc_id);
			stmt.execute();
			if(u.getAccounts() != null)
				u.getAccounts().add(a);
			else  {
				List<Account> tempList = new ArrayList<Account>();
				tempList.add(a);
				u.setAccounts(tempList);
			}
			
			return acc_id;

		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL Exception");
			e.printStackTrace();
		}
		return -1;
	}

	public List<Account> findAll() {
		List<Account> accList = new ArrayList<Account>();
		try (Connection conn = ConnectionUtil.getConnection()) {
			Statement stmt = conn.createStatement();
			String sql = "Select * FROM accounts a LEFT JOIN user_accounts_jt u ON a.id = u.account;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				double balance = rs.getDouble("balance");
				boolean isActive = rs.getBoolean("Active");
				Account temp = new Account(id, balance, isActive);
				accList.add(temp);
			}

		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL Exception");
			e.printStackTrace();
		}

		return accList;
	}

	public Account findById(int id) {

		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "Select * FROM accounts WHERE id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				double balance = rs.getDouble("balance");
				boolean isActive = rs.getBoolean("Active");
				Account temp = new Account(id, balance, isActive);
				return (temp);
			}

		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL Exception");
			e.printStackTrace();
		}
		return null;

	}

	public List<Account> findByOwner(int id) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			List<Account> accList = new ArrayList<Account>();
			String sql = "Select * FROM accounts WHERE id IN (SELECT account FROM user_accounts_jt WHERE acc_owner = ?);";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			//System.out.println(rs);
			Account temp;
			double balance;
			boolean isActive;
			int acc_id;
			while (rs.next()) {
				acc_id = rs.getInt(1);
				balance = rs.getDouble("balance");
				isActive = rs.getBoolean("Active");
				temp = new Account(acc_id, balance, isActive);
				//System.out.println("hi");
				//System.out.println(temp);
				accList.add(temp);
			}
			return(accList);
		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL Exception");
			e.printStackTrace();
		}
		return null;
	}

	public boolean update(Account a) {
		try (Connection conn = ConnectionUtil.getConnection()) {
			String sql = "UPDATE accounts SET balance = ?, active = ? WHERE id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, a.getBalance());
			stmt.setBoolean(2, a.isActive());
			stmt.setInt(3, a.getId());
			stmt.execute();
			return true;
			//UPDATE stocks SET ticker = 'MSFT' WHERE ticker = 'MFST';
			//id balance acc_owner active user_role
		} catch (SQLException e) {
			System.out.println("Unable to retrieve account -- SQL Exception");
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(Account a) { // Add JT
		
	try (Connection conn = ConnectionUtil.getConnection()) {
		String sql = "DELETE FROM accounts WHERE id = ?;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, a.getId());
		stmt.executeQuery(sql);
		sql = "DELETE FROM user_accounts_jt WHERE account = ?;";
		stmt = conn.prepareStatement(sql);
		stmt.setInt(1, a.getId());
		stmt.executeQuery(sql);
		return(true);
	} catch(SQLException e) {
		System.out.println("Unable to retrieve account -- SQL Exception");
		e.printStackTrace();
	}return false;
}

}
