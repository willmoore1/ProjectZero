package com.revature.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 13516124L;
	private int id;
	private String username;
	private String password;
	private List<Account> accounts;
	private Role role;
	
	public User() {}

	public User(int id, String username, String password, Role role, List<Account> accounts) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.accounts = accounts;
		this.role = role;
	}

	public User(String username, String password, List<Account> accounts) {
		super();
		this.username = username;
		this.password = password;
		this.accounts = accounts;
		this.role = Role.Customer;
		this.accounts = accounts;
	}
	

	public int getId() {
		return id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accounts, id, password, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(accounts, other.accounts) && id == other.id && Objects.equals(password, other.password)
				&& role == other.role && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", accounts=" + accounts
				+ ", role=" + role + "]";
	}
	
	
	
}
