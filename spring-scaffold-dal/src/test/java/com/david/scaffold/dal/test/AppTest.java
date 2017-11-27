package com.david.scaffold.dal.test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.david.scaffold.dal.dao.MemberInfoMapper;
import com.david.scaffold.dal.model.MemberInfo;
import com.david.scaffold.dal.model.MemberInfoExample;
import com.david.scaffold.dal.model.User;
import com.david.scaffold.util.JUnit4ClassRunner;

@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext-dal.xml"})
public class AppTest {

	private static final Logger log = LoggerFactory.getLogger(AppTest.class);

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, User> redisTemplate;

	@Autowired
	private MemberInfoMapper memberInfoMapper;

	@Test
	public void getMemberInfoTest() {
		String memberNo = "7JS2SMl0RNbOvd5Ott";
		MemberInfoExample example = new MemberInfoExample();
		MemberInfoExample.Criteria criteria = example.createCriteria();
		criteria.andMemberNoEqualTo(memberNo);
		List<MemberInfo> memberInfos = memberInfoMapper.selectByExample(example);
		MemberInfo memberInfo = memberInfos.size() > 0 ? memberInfos.get(0) : null;
		System.out.println(memberInfo.getUsername());
		List<MemberInfo> list = memberInfoMapper.selectAll();
		System.out.println(list);
	}

	@Test
	public void stringOpsGetAndSetObjTest() {
		String key = "user_key";
		User user = new User();
		user.setId(RandomUtils.nextInt(0, 99));
		user.setName(RandomStringUtils.randomAlphabetic(8));
		ValueOperations<String, User> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, user);
		User resultUser = valueOperations.get(key);
		System.out.println(resultUser);
		Assert.assertEquals(user.getId(), resultUser.getId());
	}

	@Test
	public void stringOpsGetAndSetStrTest() {
		String key = "member";
		stringRedisTemplate.opsForValue().set(key, "david");
		String value = stringRedisTemplate.opsForValue().get(key);
		Assert.assertEquals("david", value);
		System.out.println(value);
		stringRedisTemplate.delete(key);
		String value2 = stringRedisTemplate.opsForValue().get(key);
		Assert.assertNull(value2);
	}

	private MemberInfo getMemberInfo() {
		MemberInfo memberInfo = new MemberInfo();
		memberInfo.setAge(RandomUtils.nextInt(0, 50));
		memberInfo.setEmail(RandomStringUtils.randomNumeric(9) + "@qq.com");
		memberInfo.setId(RandomUtils.nextInt(1, 1000));
		memberInfo.setMemberNo(RandomStringUtils.randomAlphanumeric(32));
		memberInfo.setMobile("135" + RandomStringUtils.randomNumeric(8));
		memberInfo.setRealname("daivdreal_" + RandomStringUtils.randomNumeric(6));
		memberInfo.setUsername("daivduser_" + RandomStringUtils.randomNumeric(6));
		memberInfo.setStatus(1);
		memberInfo.setCreateTime(new Date());
		memberInfo.setModifyTime(new Date());
		return memberInfo;
	}

	@Test
	public void stringOpsGetAndSetObject() {
		log.info("redist str key start");
		String key = "member_info";
		MemberInfo memberInfo = getMemberInfo();
		stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(memberInfo));
		String value = stringRedisTemplate.opsForValue().get(key);
		MemberInfo resultMemberInfo = JSON.parseObject(value, MemberInfo.class);
		System.out.println(value);
		Assert.assertEquals(memberInfo.getMemberNo(), resultMemberInfo.getMemberNo());
		System.out.println("set value for ttl");
		String newkey = "member_info_new";
		stringRedisTemplate.opsForValue().set(newkey, JSON.toJSONString(memberInfo), 1, TimeUnit.SECONDS);
		String ttlValue = stringRedisTemplate.opsForValue().get(newkey);
		System.out.println("ttlValue=" + ttlValue);
		Assert.assertNotNull(ttlValue);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException ex) {
			log.error(ex.getMessage(), ex);
		}
		String ttlValueExpired = stringRedisTemplate.opsForValue().get(newkey);
		System.out.println("ttlValueExpired=" + ttlValueExpired);
		Assert.assertNull(ttlValueExpired);
		log.info("redist str key end");
	}

	@Test
	public void listOpsGetAndSet() {
		String listkey = "list_key";
		stringRedisTemplate.delete(listkey);
		stringRedisTemplate.opsForList().leftPush(listkey, RandomStringUtils.randomAlphabetic(6));
		stringRedisTemplate.opsForList().leftPush(listkey, RandomStringUtils.randomAlphabetic(6));
		List<String> list = stringRedisTemplate.opsForList().range(listkey, 0, 2);
		System.out.println(list);
		Assert.assertEquals(2, list.size());
	}

	/**
	 * 记录异常信息： support.SerializationFailedException: Failed to deserialize
	 * payload. Is the byte array a result of corresponding serialization for
	 * DefaultDeserializer?; nested exception is java.io.InvalidClassException
	 * 使用org.springframework.data.redis.core.RedisTemplate从Redis中读取数据时，报上面的错。
	 * 
	 * 原因，缓存是昨天，缓存中用到的Bean今天改过并重新部署，这样Redis中存放的bean和今天的项目中Bean已经不是同一个了。
	 * 解决方式删除这个key重新set
	 */
	@Test
	public void zSetOpsGetAndSet() {
		String keyset = "zset_key";
		stringRedisTemplate.delete(keyset);
		Set<String> hashSet = new HashSet<>(Arrays.asList(new String[]{"daviddai", "zhangsan", "abc"}));
		for (String item : hashSet) {
			double weight = RandomUtils.nextDouble(0, 100);
			System.out.println("item=" + item + ", weight=" + weight);
			stringRedisTemplate.opsForZSet().add(keyset, item, weight);
		}

		Set<String> rangeSets = stringRedisTemplate.opsForZSet().range(keyset, 0, hashSet.size());
		System.out.println(rangeSets);
		Set<String> rangeScoreSets = stringRedisTemplate.opsForZSet().rangeByScore(keyset, 0, 100);
		System.out.println(rangeScoreSets);
		Set<String> reverseRangeSets = stringRedisTemplate.opsForZSet().reverseRange(keyset, 0, hashSet.size());
		System.out.println(reverseRangeSets);

	}

	@Test
	public void hashmapOpsGetAndSet() {
		String key = "hash_member";
		MemberInfo memberInfo = getMemberInfo();
		String result = JSON.toJSONString(memberInfo);
		Map<Object, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<Object, Object>>() {
		});
		System.out.println(resultMap);
		System.out.println(resultMap.size());
		for (Entry<Object, Object> kv : resultMap.entrySet()) {
			stringRedisTemplate.opsForHash().put(key, kv.getKey(), String.valueOf(kv.getValue()));
		}
		String redisMemberNo = (String) stringRedisTemplate.opsForHash().get(key, "memberNo");
		String redisRealName = (String) stringRedisTemplate.opsForHash().get(key, "realname");
		Integer redisAge = NumberUtils.toInt((String) stringRedisTemplate.opsForHash().get(key, "age"), 0);
		System.out.println("redisMemberNo=" + redisMemberNo);
		System.out.println("redisRealName=" + redisRealName);
		System.out.println("redisAge=" + redisAge);
		Assert.assertEquals(redisMemberNo, memberInfo.getMemberNo());
		System.out.println("modify redis hash object value");
		String modifyValue = "12345678";
		stringRedisTemplate.opsForHash().put(key, "memberNo", modifyValue);
		String redisMemberNoRs = (String) stringRedisTemplate.opsForHash().get(key, "memberNo");
		System.out.println(redisMemberNoRs);
		Assert.assertEquals(modifyValue, redisMemberNoRs);

	}

}
