package com.david.scaffold.dal.pubsub;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisTemplate;

public class RedisMessageImplDAO implements RedisMessageDAO {

	private RedisTemplate<String, Object> redisTemplate;

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void sendMessage(String channel, Serializable message) {
		redisTemplate.convertAndSend(channel, message);
	}

}
