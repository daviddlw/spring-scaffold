package com.david.scaffold.webapp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/")
public class HomeController {

	@ResponseBody
	@RequestMapping(value = "/helloworld.json", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> helloWorldJson() {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("name", "JsonSuffix");
		hashMap.put("age", 30);
		return hashMap;
	}

	@ResponseBody
	@RequestMapping(value = "/helloworld.form", method = { RequestMethod.POST, RequestMethod.GET })
	public String helloWorldForm() {
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("name", "formSuffix");
		hashMap.put("age", 88);
		return JSON.toJSONString(hashMap);
	}

	@ResponseBody
	@RequestMapping(value = "/healthCheck", method = { RequestMethod.POST, RequestMethod.GET })
	public String healthCheck() {
		return "healthCheck";
	}
	
}
