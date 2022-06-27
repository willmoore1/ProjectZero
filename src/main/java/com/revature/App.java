package com.revature;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.service.UserService;
import com.revature.util.GetInput;

public class App {

	static Scanner scan = GetInput.getScan();

	public static void main(String[] args) {
		System.out.println("Welcome to Chase Bank");
		System.out.print("Enter 1 to login, enter 2 to register: ");
		int input = scan.nextInt();
		UserService us = new UserService();
		User loggedInUser = null;

		switch (input) {
		case 1: {
			System.out.print("Username: ");
			String username = scan.next();
			System.out.print("Password: ");
			String password = scan.next();

			loggedInUser = us.login(username, password);
			System.out.println("Welcome " + username);
		}
		case 2: {
			boolean taken = true;
			String username = "";
			String password = "";
			while (taken) {
				System.out.print("Please enter a username: ");
				username = scan.next();
				if (us.checkUsername(username)) {
					System.out.println("Sorry, that username is taken. Please use another: ");
				} else {
					taken = false;
				}
			}
			taken = true;
			while (taken) {
				System.out.println("Please enter a password: ");
				System.out.println("Note: password must be at least 8 characters, contain at least one capital, and have one special character or number.");
				password = scan.next();
				if(us.checkPassword(password)) {
					taken = false;
				}
			}
			System.out.println("Welcome new user. You are now logged in.");
			loggedInUser = us.register(new User(username, password, null));
		}
		} 
		System.out.println(loggedInUser);
		if(loggedInUser == null) {GetInput.closeScan(); System.exit(0);}
		
		System.out.print("Enter 1 to create a new account, 2 to view existing accounts, 3 to transfer between accounts");
		input = scan.nextInt();
		switch(input) {
		case 1: {
			
		}
		case 2: {
			
		}
		case 3: {
			
		}
		}
		
		GetInput.closeScan();
	}
}
