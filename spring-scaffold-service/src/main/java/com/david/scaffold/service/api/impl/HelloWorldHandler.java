package com.david.scaffold.service.api.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HelloWorldHandler implements InvocationHandler {

	/**
	 * 被代理对象
	 */
	private Object proxyObj;

	public HelloWorldHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HelloWorldHandler(Object proxyObj) {
		super();
		this.proxyObj = proxyObj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		doBefore();
		result = method.invoke(proxyObj, args);
		doAfter();
		return result;
	}

	private void doBefore() {
		System.out.println("do before");
	}

	private void doAfter() {
		System.out.println("do after...");
	}

}
