package com.david.scaffold.service.dto;

import javax.servlet.ServletException;

import org.springframework.web.servlet.HttpServletBean;

public class CustomHttpServlet extends HttpServletBean {

	private String initArg;

	public void setInitArg(String initArg) {
		this.initArg = initArg;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void initServletBean() throws ServletException {
		System.err.println("initServletBean start...");
		System.err.println("initArg: " + initArg);
		System.err.println("initServletBean end...");
	}

}
