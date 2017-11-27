package com.david.scaffold.service;

import java.util.List;

import com.david.scaffold.facade.dto.MemberDTO;

/**
 * 可直接暴露给第三方的service接口层
 * 
 * @author David.dai
 * 
 */
public interface MemberInfoService {

	int countMemberInfo(MemberDTO memberInfo);

	int deleteMemberInfo(MemberDTO memberInfo);

	List<MemberDTO> getMemberInfos(MemberDTO memberInfo);

	MemberDTO getMemberInfo(MemberDTO memberInfo);

	MemberDTO getMemberInfo(String memberNo);

	int updateMemberInfo(MemberDTO memberInfo);

	int insertMemberInfo(MemberDTO memberInfo);
}
