package com.david.scaffold.webapp;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.david.scaffold.facade.dto.MemberDTO;
import com.david.scaffold.facade.dto.request.AddMemberRequest;
import com.david.scaffold.facade.dto.request.MemberRequest;
import com.david.scaffold.facade.dto.response.AddMemberResponse;
import com.david.scaffold.facade.dto.response.ListMemberResponse;
import com.david.scaffold.facade.dto.response.MemberResponse;
import com.david.scaffold.service.MemberInfoService;

@Controller
@RequestMapping("member")
public class MemberController {

	private static final String SUCCESS_CODE = "1000";
	@Autowired
	private MemberInfoService memberInfoService;

	@ResponseBody
	@RequestMapping(value = "/op_get_member_info.json", method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public MemberResponse getMemberInfo(@RequestBody MemberRequest request) {
		System.out.println(JSON.toJSONString(request, true));
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setMemberNo(RandomStringUtils.randomAlphanumeric(18));
		memberDTO.setRealname(RandomStringUtils.randomAlphabetic(6));
		memberDTO.setUsername(RandomStringUtils.randomAlphabetic(6));
		memberDTO.setMobile("131" + RandomStringUtils.randomNumeric(8));
		memberDTO.setEmail(RandomStringUtils.randomNumeric(9) + "@qq.com");
		memberDTO.setAge(RandomUtils.nextInt(1, 30));
		MemberResponse response = new MemberResponse();
		response.setMemberDTO(memberDTO);
		response.setRespCode(SUCCESS_CODE);
		response.setMsg(StringUtils.EMPTY);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/op_add_member_info.json", method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public AddMemberResponse getMemberInfo(@RequestBody AddMemberRequest request) {
		memberInfoService.insertMemberInfo(request.getMemberDTO());
		AddMemberResponse response = new AddMemberResponse();
		response.setRespCode(SUCCESS_CODE);
		response.setMsg(StringUtils.EMPTY);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/op_get_member_info_v2.json", method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public MemberResponse getMemberInfoV2(@RequestBody MemberRequest request) {
		System.out.println(JSON.toJSONString(request, true));
		MemberDTO memberDTO = memberInfoService.getMemberInfo(request.getMemberNo());
		MemberResponse response = new MemberResponse();
		response.setMemberDTO(memberDTO);
		response.setRespCode(SUCCESS_CODE);
		response.setMsg(StringUtils.EMPTY);
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/op_get_member_infos.json", method = { RequestMethod.POST }, consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ListMemberResponse getMemberInfos() {
		List<MemberDTO> list = memberInfoService.getMemberInfos(new MemberDTO());
		ListMemberResponse response = new ListMemberResponse();
		response.setMemberDTOs(list);
		response.setRespCode(SUCCESS_CODE);
		response.setMsg(StringUtils.EMPTY);
		return response;
	}

}
