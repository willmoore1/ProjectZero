package com.revature;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.service.AccountService;
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
		AccountService as = new AccountService();

		switch (input) {
		case 1: {
			System.out.print("Username: ");
			String username = scan.next();
			System.out.print("Password: ");
			String password = scan.next();

			loggedInUser = us.login(username, password);
			System.out.println("Welcome " + username);
			break;
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
				System.out.println(
						"Note: password must be at least 8 characters, contain at least one capital, and have one special character or number.");
				password = scan.next();
				if (us.checkPassword(password)) {
					taken = false;
				}
			}
			System.out.println("Welcome new user. You are now logged in.");
			loggedInUser = us.register(new User(username, password, null));
			break;
		}
		}
		System.out.println(loggedInUser);
		if (loggedInUser == null) {
			GetInput.closeScan();
			System.exit(0);
		}
		boolean keepGoing = true;
		List<Account> accList = as.getAccounts(loggedInUser);
		while (keepGoing) {
			System.out.println(
					"Enter 1 to create a new account, 2 to view existing accounts, 3 to transfer funds between accounts, Other to quit");
			try {
				input = scan.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Thank you for using Chase bank.");
				System.exit(0);
			}
			switch (input) {
			case 1: {
				System.out.print("Please input an initial deposit: ");
				Account a = new Account(scan.nextInt());
				as.createAccount(a, loggedInUser);
				accList = as.getAccounts(loggedInUser);
				break;
			}
			case 2: {
				for (int i = 0; i < accList.size(); i++) {
					System.out.println("Account #" + accList.get(i).getId() + " " + accList.get(i).toString());
				}
				break;
			}
			case 3: {
				for (Account a : as.getAccounts(loggedInUser)) {
					System.out.println("Account ID: " + a.getId() + "    Balance: $" + a.getBalance());
				}
				boolean owner = false;
				int sendID = -1;
				while (!owner) {
					System.out.println("What is the ID number of the sending account?");
					try {
						sendID = scan.nextInt();
					} catch (InputMismatchException e) {
						break;
					}
					for (int i = 0; i < accList.size(); i++) {
						if (sendID == accList.get(i).getId()) {
							owner = true;
							break;
						}
					}
					if (!owner) {
						System.out.println("Please one of the following IDs:");
						accList.forEach(t -> System.out.println(t.getId()));
					}
				}
				if (!owner)
					break;
				System.out.println("Funds will be transfered out of account " + sendID);
				double balance = as.findAccount(sendID).getBalance();
				System.out.println("Current balance = " + DecimalFormat.getCurrencyInstance().format(balance));
				int recID = -1;
				owner = false;

				while (!owner) {
					System.out.println("What is the ID number of the receiving account?");
					try {
						recID = scan.nextInt();
					} catch (InputMismatchException e) {
						break;
					}
					if (sendID != recID)
						owner = true;
					else
						System.out.println("Cannot transfer funds to the sending account");

				}
				if (!owner)
					break;

				owner = false;
				double transAmt = 0;
				while (!owner) {
					System.out.println("What is the amount to be transfered?");
					try {
						transAmt = scan.nextDouble();
					} catch (InputMismatchException e) {
						break;
					}
					if (transAmt <= balance) {
						owner = true;
					} else {
						System.out.println("Cannot transfer more funds than are in the account.");
					}
				}
				if (!owner)
					break;
				as.transferFunds(transAmt, sendID, recID);
				accList = as.getAccounts(loggedInUser);
				break;
			}
			default: {
				keepGoing = false;
			}
			}
		}

		GetInput.closeScan();
	}
}
