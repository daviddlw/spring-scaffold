package com.david.scaffold.dal.pubsub;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SubscriberRun {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/applicationContext-dal.xml");
		System.out.println(ctx);
		System.out.println("start...");
		while (true) { // 这里是一个死循环,目的就是让进程不退出,用于接收发布的消息
			try {
//				System.out.println("current time: " + new Date());
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
