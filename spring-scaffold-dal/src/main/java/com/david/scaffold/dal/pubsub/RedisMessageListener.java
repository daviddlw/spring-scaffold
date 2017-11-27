package com.david.scaffold.dal.pubsub;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

public class RedisMessageListener implements MessageListener {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
		Object obj = message.getChannel();
		System.out.println(obj.getClass());
		// Object channel = serializer.deserialize(message.getChannel());
		// System.out.println(String.valueOf(channel));
		Object body = serializer.deserialize(message.getBody());
		if (body.getClass() == String.class) {
			System.out.println(String.valueOf(body));
		} else if (body.getClass().isArray()) {
			System.out.println(Arrays.toString((Object[]) body));
		} else if (body instanceof List<?> || body instanceof Map<?, ?>) {
			System.out.println(body);
		} else {
			System.out.println(ToStringBuilder.reflectionToString(body));
		}

	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
