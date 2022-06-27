package com.revature.dao;

import java.util.List;

import com.revature.models.User;

public interface IUserDao {
	//CRUD
	//Create
	int insert(User u); // returns primary key of new user
	//Read
	List<User> findByAcc(int id); // returns all users linked to an account
	User findByUsername(String username); // returns user with username
	List<User> findAll(); // returns all users
	//Update
	boolean update(User u);
	//Delete
	boolean delete(int username);
}
