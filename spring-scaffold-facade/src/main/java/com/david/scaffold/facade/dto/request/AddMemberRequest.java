package com.david.scaffold.facade.dto.request;

import com.david.scaffold.facade.dto.MemberDTO;

public class AddMemberRequest {
	private MemberDTO memberDTO;

	public MemberDTO getMemberDTO() {
		return memberDTO;
	}

	public void setMemberDTO(MemberDTO memberDTO) {
		this.memberDTO = memberDTO;
	}

	@Override
	public String toString() {
		return "AddMemberRequest [memberDTO=" + memberDTO + "]";
	}

}
