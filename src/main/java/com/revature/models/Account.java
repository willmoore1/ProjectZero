package com.revature.models;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Objects;

public class Account implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 61234531L;
	
	private int id;
	private double balance;
	private boolean active;
	
	
	public Account() {
		super();
	}
	
	// for creating account in DB
	public Account(double balance) { 
		super();
		this.balance = balance;
		this.active = false;
	}
	public Account(int id, double balance, boolean active) {
		super();
		this.id = id;
		this.balance = balance;
		this.active = active;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}


	@Override
	public int hashCode() {
		return Objects.hash(active, balance, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return active == other.active && Double.doubleToLongBits(balance) == Double.doubleToLongBits(other.balance)
				&& id == other.id;
	}

	@Override
	public String toString() {
		return "[balance = " + DecimalFormat.getCurrencyInstance().format(balance) + "]";
	}

	public String toString(Role role) {
		if(role == Role.Administrator)
			return "Account [id=" + id + ", balance=" + balance + ", active=" + active + "]";
		else return null;
	}
	
	

}
