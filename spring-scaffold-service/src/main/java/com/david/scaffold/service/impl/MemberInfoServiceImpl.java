package com.david.scaffold.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.david.scaffold.dal.dao.MemberInfoMapper;
import com.david.scaffold.dal.model.MemberInfo;
import com.david.scaffold.dal.model.MemberInfoExample;
import com.david.scaffold.facade.dto.MemberDTO;
import com.david.scaffold.service.MemberInfoService;

@Service
public class MemberInfoServiceImpl implements MemberInfoService {

	@Autowired
	private MemberInfoMapper memberInfoMapper;

	@Override
	public int countMemberInfo(MemberDTO memberInfo) {
		MemberInfoExample example = new MemberInfoExample();
		example.setDistinct(true);
		return memberInfoMapper.selectCountByExample(example);
	}

	@Override
	public int deleteMemberInfo(MemberDTO memberInfo) {
		MemberInfoExample example = new MemberInfoExample();
		MemberInfoExample.Criteria criteria = example.createCriteria();
		criteria.andMemberNoEqualTo(memberInfo.getMemberNo());
		return memberInfoMapper.deleteByExample(example);
	}

	@Override
	public List<MemberDTO> getMemberInfos(MemberDTO member) {
		MemberInfoExample example = new MemberInfoExample();
		List<MemberInfo> list = memberInfoMapper.selectByExample(example);
		List<MemberDTO> memberDTOs = new ArrayList<>();
		for (MemberInfo memberInfo : list) {
			memberDTOs.add(transferInfo(memberInfo));
		}
		return memberDTOs;
	}

	private MemberDTO transferInfo(MemberInfo memberInfo) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setMemberNo(memberInfo.getMemberNo());
		memberDTO.setUsername(memberInfo.getUsername());
		memberDTO.setRealname(memberInfo.getRealname());
		memberDTO.setMobile(memberInfo.getMobile());
		memberDTO.setAge(memberInfo.getAge());
		memberDTO.setEmail(memberInfo.getEmail());
		return memberDTO;
	}

	@Override
	public int updateMemberInfo(MemberDTO memberInfo) {
		MemberInfoExample example = new MemberInfoExample();
		MemberInfoExample.Criteria criteria = example.createCriteria();
		criteria.andMemberNoEqualTo(memberInfo.getMemberNo());
		MemberInfo record = transferDTO(memberInfo);
		return memberInfoMapper.updateByExampleSelective(record, example);
	}

	@Override
	public int insertMemberInfo(MemberDTO memberInfo) {
		MemberInfo record = transferDTO(memberInfo);
		record.setCreateTime(new Date());
		record.setModifyTime(new Date());
		record.setStatus(1);
		return memberInfoMapper.insertSelective(record);
	}

	private MemberInfo transferDTO(MemberDTO memberInfo) {
		MemberInfo record = new MemberInfo();
		record.setMemberNo(memberInfo.getMemberNo());
		record.setRealname(memberInfo.getRealname());
		record.setUsername(memberInfo.getUsername());
		record.setRealname(memberInfo.getRealname());
		record.setMobile(memberInfo.getMobile());
		record.setAge(memberInfo.getAge());
		record.setEmail(memberInfo.getEmail());
		return record;
	}

	@Override
	public MemberDTO getMemberInfo(MemberDTO memberInfo) {
		MemberInfoExample example = new MemberInfoExample();
		MemberInfoExample.Criteria criteria = example.createCriteria();
		criteria.andMemberNoEqualTo(memberInfo.getMemberNo());
		List<MemberInfo> list = memberInfoMapper.selectByExample(example);
		MemberInfo record = new MemberInfo();
		if (list.size() > 0) {
			record = list.get(0);
		}
		MemberDTO memberDTO = list.size() == 0 ? new MemberDTO() : transferInfo(record);
		return memberDTO;
	}

	@Override
	public MemberDTO getMemberInfo(String memberNo) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setMemberNo(memberNo);
		return getMemberInfo(memberDTO);
	}

}
