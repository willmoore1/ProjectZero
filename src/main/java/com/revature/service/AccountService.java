package com.revature.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.dao.AccountDao;
import com.revature.dao.IAccountDao;
import com.revature.models.Account;
import com.revature.models.Role;
import com.revature.models.User;

public class AccountService {
	private IAccountDao adao = new AccountDao();
	private Logger logger = Logger.getLogger(AccountService.class);
	public void createAccount(Account a, User u) {
		adao.insert(a, u);
	}
	
	public List<Account> getAccounts(User u) {
		return(adao.findByOwner(u.getId()));
	}
	
	public Account findAccount(int id) {
		return(adao.findById(id));
	}
	
	public boolean isOpen(int id) {
		return(adao.findById(id).isActive());
	}
	
	public void activateAccount(int id) {
		Account temp = adao.findById(id);
		temp.setActive(true);
		adao.update(temp);
	}
	
	public void transferFunds(double amt, int id1, int id2) {
		Account a1 = adao.findById(id1);
		Account a2 = adao.findById(id2);
		System.out.println("Withdrawing " + DecimalFormat.getCurrencyInstance().format(amt) + " from account " + a1.getId());
		a1.setBalance(a1.getBalance()-amt);
		System.out.println("Depositing " + DecimalFormat.getCurrencyInstance().format(amt) + " into account " + a2.getId());
		a2.setBalance(a2.getBalance()+amt);
		adao.update(a1);
		adao.update(a2);
		System.out.println("Transfer complete.");
		logger.info(DecimalFormat.getCurrencyInstance().format(amt) + " transfered from account " + id1 + " to " + id2);
	}
	
	public List<Account> viewClosedAccounts(Role role, int userID) {
		//Lets call on the DAO to get the accounts
		logger.info("An " + role.toString() + " is fetching the closed account list, ID: " + userID);
		System.out.println("Fetching accounts, please hold...");
		List<Account> accList = adao.findAll();
		List<Account> retList = new ArrayList<Account>();
		for(Account a : accList) {
			if(!a.isActive()) {
				retList.add(a);
			}
		}
		return(retList);
	}
	
}
