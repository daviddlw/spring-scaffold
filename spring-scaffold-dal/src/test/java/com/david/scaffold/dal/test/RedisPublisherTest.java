package com.david.scaffold.dal.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.david.scaffold.dal.model.User;
import com.david.scaffold.dal.pubsub.RedisMessageDAO;
import com.david.scaffold.util.JUnit4ClassRunner;

@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext-dal.xml" })
public class RedisPublisherTest {

	@Autowired
	private RedisMessageDAO redisMessageDAO;

	private static final String TOPIC = "userTopic";

	@Test
	public void redisPublishMessageTest() {
		// common javabean
		User user = new User(RandomUtils.nextInt(1, 100), RandomStringUtils.randomAlphabetic(6));
		redisMessageDAO.sendMessage(TOPIC, user);

		// common string
		String message = "Hello, world_" + System.currentTimeMillis();
		redisMessageDAO.sendMessage(TOPIC, message);

	}

}
