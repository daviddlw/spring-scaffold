package com.david.scaffold.facade.dto;

import java.io.Serializable;

/**
 * 会员信息
 * 
 * @author David.dai
 * 
 */
public class MemberDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String memberNo;

	private String username;

	private String realname;

	private int age;

	private String mobile;

	private String email;

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "MemberDTO [memberNo=" + memberNo + ", username=" + username + ", realname=" + realname + ", age=" + age
				+ ", mobile=" + mobile + ", email=" + email + "]";
	}

}
