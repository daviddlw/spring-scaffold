package com.david.scaffold.facade.dto.request;

import java.io.Serializable;

public class MemberRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String memberNo;

	private String mobile;

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
	public String toString() {
		return "MemberRequest [memberNo=" + memberNo + ", mobile=" + mobile + "]";
	}

}
