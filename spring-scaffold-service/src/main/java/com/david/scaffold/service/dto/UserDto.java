package com.david.scaffold.service.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	private long userId;

	private String name;

	private String phone;

	private Date birth;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDto(long userId, String name, String phone, Date birth) {
		super();
		this.userId = userId;
		this.name = name;
		this.phone = phone;
		this.birth = birth;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", phone=" + phone + ", birth=" + sdf.format(birth) + "]";
	}

}
