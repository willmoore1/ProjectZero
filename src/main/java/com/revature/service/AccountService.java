package com.revature.service;

import java.util.List;

import com.revature.dao.AccountDao;
import com.revature.dao.IAccountDao;
import com.revature.models.Account;
import com.revature.models.User;

public class AccountService {
	private IAccountDao adao = new AccountDao();
	
	public void createAccount(Account a, User u) {
		adao.insert(a, u);
	}
	
	public List<Account> getAccounts(User u) {
		return(adao.findByOwner(u.getId()));
	}
	
	public Account findAccount(int id) {
		return(adao.findById(id));
	}
	
	public void transferFunds(double amt, int id1, int id2) {
		Account a1 = adao.findById(id1);
		Account a2 = adao.findById(id2);
		System.out.println("Withdrawing $" + amt + " from account " + a1.getId());
		a1.setBalance(a1.getBalance()-amt);
		System.out.println("Depositing $" + amt + " into account " + a2.getId());
		a2.setBalance(a2.getBalance()+amt);
		adao.update(a1);
		adao.update(a2);
		System.out.println("Transfer complete.");
	}
	
}
