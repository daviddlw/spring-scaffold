package com.david.scaffold.service.impl;

import org.springframework.stereotype.Service;

import com.david.scaffold.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public String userList() {
		return "hello world, userlist";
	}

}
