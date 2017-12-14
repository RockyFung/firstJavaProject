package rocky.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtils {
	private static JedisPool pool = null;
	static{
		JedisPoolConfig config = new JedisPoolConfig();
		// 加载配置资源
		InputStream in = JedisPoolUtils.class.getClassLoader().getResourceAsStream("redis.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 最大最小闲置数
		config.setMaxIdle(Integer.parseInt(pro.get("redis.maxIdle").toString()));
		config.setMinIdle(Integer.parseInt(pro.get("redis.minIdle").toString()));
		// 最大最小连接数
		config.setMaxTotal(Integer.parseInt(pro.get("redis.maxTotal").toString()));
		
		pool = new JedisPool(config,pro.get("redis.url").toString(),Integer.parseInt(pro.get("redis.port").toString()));
	}
	
	// 获得jedis资源
	public static Jedis getJedis() {
		return pool.getResource();
	}
	
	
	
	
	
	
	
}
