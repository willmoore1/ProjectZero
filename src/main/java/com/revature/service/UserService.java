package com.revature.service;

import java.util.List;
import java.util.Scanner;

import com.revature.dao.IUserDao;
import com.revature.dao.UserDao;
import com.revature.exceptions.RegisterUserFailedException;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.util.GetInput;

public class UserService {
	// Dependency Injection
	public IUserDao udao = new UserDao();
	
	public boolean checkUsername(String username) {
		return(udao.findByUsername(username) != null);
	}
	
	public boolean checkPassword(String password) {
		if(password.length() < 8) {System.out.println("Password too short"); return false;}
	 	if(password.toLowerCase().equals(password)) {System.out.println("Password contains no capitals"); return false;}
	 	char[] chAr = password.toLowerCase().toCharArray();
	 	boolean specCar = false;
	 	for(char c : chAr) {
	 		if(c < 97 || c > 122) specCar = true;
	 	}
	 	if(!specCar) {System.out.println("Password lacks a non-letter character"); return false;} 
	 	return true;
	}

	public User register(User u) {
		System.out.println("Registering User...");
		if (u.getId() != 0) {
			throw new RegisterUserFailedException("User not valid because id was not 0");
		}
		int generatedID = udao.insert(u);
		if (generatedID != -1 && generatedID != u.getId()) {
			u.setId(generatedID);
		} else {
			throw new RegisterUserFailedException("User id either -1 or unchanged after insertion");
		}
		System.out.println("User registered with id: " + u.getId());
		return u;
	}

	public User login(String username, String password) {
		User returnedUser = udao.findByUsername(username);
		int counter = 0;
		while (counter++ < 5) {
			if (returnedUser.getPassword().equals(password)) {
				System.out.println("Login Successful");
				return (returnedUser);
			} else {
				System.out.println("Incorrect Password");
				Scanner scan = GetInput.getScan();
				System.out.println("Please try again: ");
				password = scan.next();
			}
		}
		System.out.println("Too many incorrect attempts, please try again");
		return null;
	}
	public List<User> findUsers(Account a) {
		return(udao.findByAcc(a.getId()));
	}
}
