package com.david.scaffold.dal.pubsub;

import java.io.Serializable;

/**
 * Redis消息传递
 * 
 * @author David.dai
 * 
 */
public interface RedisMessageDAO {
	
	void sendMessage(String channel, Serializable message);
}
