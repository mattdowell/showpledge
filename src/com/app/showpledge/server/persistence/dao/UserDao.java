package com.app.showpledge.server.persistence.dao;

import java.util.List;
import java.util.Random;

import com.app.showpledge.server.persistence.Obj;
import com.app.showpledge.shared.entities.User;
import com.googlecode.objectify.Objectify;

public class UserDao extends ObjectifyGenericDao<User> {


	public User getUser(String inUsername, String inPassword) {
		Objectify obj = Obj.begin();
		return  obj.query(User.class).filter("email", inUsername.trim()).filter("password", inPassword.trim()).get();
	}
	
	public List<User> getAllUsers() {
		Objectify obj = Obj.begin();
		return obj.query(User.class).list();
	}
	
	
	public boolean doesEmailAlreadyExist(String inEmail) {
		User u = getUserByEmail(inEmail);
		if (u == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public User getUserByEmail(String inEmail) {
		Objectify obj = Obj.begin();
		return obj.query(User.class).filter("email", inEmail).get();
	}	
	

	public List<User> getUsers(int inOffset, int inLimit) {
		Objectify obj = Obj.begin();
		return obj.query(User.class).order("id").offset(inOffset).limit(inLimit).list();
	}

	public int getTotalUserCount() {
		Objectify obj = Obj.begin();
		return obj.query(User.class).count();
	}	
	
	/**
	 * Generate and set a unique number from 1-50
	 */
	public int getCaptchaNumber(int inCeiling) {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(inCeiling);
		return randomInt;
	}


	public void suspendUser(User u) {
		u.setLocked(true);
		put(u);
	}


	public void activateUser(User u) {
		u.setLocked(false);
		put(u);
	}	
	
	public void delete(User inUser) {
		Obj.begin().delete(inUser);
	}
	
	public User getById(Long inId) {
		return Obj.begin().get(User.class, inId);
	}
}
