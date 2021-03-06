package com.revature.dao;

import java.util.List;

import com.revature.models.Account;
import com.revature.models.User;

public interface IAccountDao {
	//CRUD
	//Create
	int insert(Account a, User u);
	
	//Read
	List<Account> findAll();
	Account findById(int id);
	List<Account> findByOwner(int id);
	//Update
	boolean update(Account a);
	
	//Delete
	boolean delete(Account a);
	
	
}
