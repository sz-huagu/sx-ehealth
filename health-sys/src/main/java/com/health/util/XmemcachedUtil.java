package com.health.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.yanf4j.core.impl.StandardSocketOption;

/**
 * memcached操作工具类
 * 
 * @Description
 * @author RickJou
 * @date 2016年10月9日 下午1:58:34
 */
@Slf4j
@Component
public class XmemcachedUtil {
	@Autowired
	private static XMemcachedClientBuilder	xMemcachedClientBuilder;

	public void setxMemcachedClientBuilder(XMemcachedClientBuilder xMemcachedClientBuilder) {
		XmemcachedUtil.xMemcachedClientBuilder = xMemcachedClientBuilder;
	}

	private static MemcachedClient	client	= null;

	private XmemcachedUtil() {
	};

	/**
	 * 初始化bean时执行
	 * 
	 * @Description
	 * @author RickJou
	 */
	@PostConstruct
	public static void initMemcached() {
		try {
			log.info(StringUtils.center("Memcached 连接池开始建立连接", 80, "="));
			xMemcachedClientBuilder.setSocketOption(StandardSocketOption.SO_RCVBUF, 32 * 1024);// 设置接收缓存区为32K，默认16K
			xMemcachedClientBuilder.setSocketOption(StandardSocketOption.SO_SNDBUF, 16 * 1024); // 设置发送缓冲区为16K，默认为8K
			xMemcachedClientBuilder.setSocketOption(StandardSocketOption.TCP_NODELAY, false); // 启用nagle算法，提高吞吐量，默认关闭

			client = xMemcachedClientBuilder.build();
			/*
			 * 将连续的单个get合并成一个multi get批量操作获取，将连续的请求合并成socket发送缓冲区大小的buffer发送。
			 * 果对响应时间比较在意，那么可以将合并的因子减小，或者关闭合并buffer的优化：
			 */
			client.setMergeFactor(150);
			/*
			 * xmemcached的通讯层是基于非阻塞IO的，那么在请求发送给memcached之后，需要等待应答的到来，这个等待时间默认是1秒，
			 * 如果
			 * 超过1秒就抛出java.util.TimeoutExpcetion给用户。如果频繁抛出此异常，可以尝试将全局的等待时间设置长一些
			 * ，在压测中 设置为3秒：
			 */
			client.setOpTimeout(3000L);
			// 关闭合并buffer的优化
			// client.setOptimizeMergeBuffer(false);
			// 10秒后发起心跳检测,默认5秒
			xMemcachedClientBuilder.getConfiguration().setSessionIdleTimeout(10000);
			// 如果对心跳检测不在意，也可以关闭心跳检测，减小系统开销
			// client.setEnableHeartBeat(false);
			// 上面的仅仅是关闭了心跳的功能，客户端仍然会去统计连接是否空闲，禁止统计可以通过：
			// xMemcachedClientBuilder.getConfiguration().setStatisticsServer(false);
			log.info(StringUtils.center("Memcached 连接池建立连接完成", 80, "="));
		} catch (IOException e) {
			log.info(StringUtils.center("Memcached 连接池建立连接失败", 80, "="));
			e.printStackTrace();
		}
	}

	public synchronized static MemcachedClient getClient() {
		if (client == null) {// double check
			initMemcached();
		}
		return client;
	}

	/**
	 * @param key
	 * @param value
	 * @return 永久存储
	 */
	public static boolean add(String key, Object value) {
		try {
			return getClient().add(key, 0, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param key
	 * @param liveSecond
	 *            存储秒数
	 * @param value
	 * @return
	 * 
	 */
	public static boolean add(String key, int liveSecond, Object value) {
		try {
			return getClient().add(key, liveSecond, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param key
	 * @param liveSecond
	 *            存储秒数
	 * @param value
	 *            这个操作是迅速的,无需等待返回结果,但也是无法知道是否存储成功的
	 */
	public static void addWithNoReply(String key, int liveSecond, Object value) {
		try {
			getClient().addWithNoReply(key, liveSecond, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		try {
			return getClient().get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 批量get
	 * 
	 * @param keyList
	 *            key列表
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> get(List<String> keyList) {
		try {
			return getClient().get(keyList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param key
	 * @return Object[]{cas,value}; cas type is long,value type is Object
	 */
	public static Object[] gets(String key) {
		try {
			GetsResponse<Object> res = getClient().gets("testGets");
			return new Object[] { res.getCas(), res.getValue() };
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 永久存储
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(String key, Object value) {
		try {
			return set(key, 0, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param key
	 * @param liveSecond
	 *            存储秒数
	 * @param value
	 * @return
	 */
	public static boolean set(String key, int liveSecond, Object value) {
		try {
			return getClient().set(key, liveSecond, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean replace(String key, Object value) {
		try {
			return replace(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param key
	 * @param liveSecond
	 *            存储秒数
	 * @param value
	 * @return
	 */
	public static boolean replace(String key, int liveSecond, Object value) {
		try {
			return getClient().replace(key, liveSecond, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param key
	 * @param appendValue
	 * @return
	 */
	public static boolean append(String key, Object appendValue) {
		try {
			return getClient().append(key, appendValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param key
	 * @param appendValue
	 *            这个操作是迅速的,无需等待返回结果,但也是无法知道是否追加成功的
	 */
	public static void appendWithNoReply(String key, Object appendValue) {
		try {
			getClient().appendWithNoReply(key, appendValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param key
	 * @param prependValue
	 * @return
	 */
	public static boolean prepend(String key, Object prependValue) {
		try {
			return getClient().prepend(key, prependValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 这个操作是迅速的,无需等待返回结果,但也是无法知道是否追加成功的
	 * 
	 * @param key
	 * @param prependValue
	 */
	public static void prependWithNoReply(String key, Object prependValue) {
		try {
			getClient().prependWithNoReply(key, prependValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete key's data item from memcached.It it is not exists,return false.
	 * 
	 * @param key
	 * @return
	 */
	public static boolean delete(String key) {
		try {
			return getClient().delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 这个操作是迅速的,无需等待返回结果,但也是无法知道是否删除成的
	 * 
	 * @param key
	 */
	public static void deleteWithNoReply(String key) {
		try {
			getClient().deleteWithNoReply(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 会抹掉memcached中所有数据,谨慎使用
	 */
	public static void flushAll(boolean confirm) {
		if (confirm) {
			try {
				getClient().flushAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 第一个参数指定递增的key名称，第二个参数指定递增的幅度大小，第三个参数指定当key不存在的情况下的初始值。
	 * 
	 * @param key
	 * @param steps
	 * @param initVlaue
	 * @return
	 */
	public static long incr(String key, long steps, long initVlaue) {
		try {
			return getClient().incr(key, steps, initVlaue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 第一个参数指定递增的key名称，第二个参数指定递增的幅度大小，第当key不存在的情况下的初始值位0。
	 * 
	 * @param key
	 * @param steps
	 * @return
	 */
	public static long incr(String key, long steps) {
		try {
			return getClient().incr(key, steps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 第一个参数指定递增的key名称，第二个参数指定递增的幅度大小，第当key不存在的情况下的初始值位0。
	 * 
	 * @param key
	 * @param steps
	 * @param initVlaue
	 * @return
	 */
	public static long decr(String key, long steps, long initVlaue) {
		try {
			return getClient().decr(key, steps, initVlaue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 第一个参数指定递增的key名称，第二个参数指定递增的幅度大小，第当key不存在的情况下的初始值位0。
	 * 
	 * @param key
	 * @param steps
	 * @return
	 */
	public static long decr(String key, long steps) {
		try {
			return getClient().decr(key, steps);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 刷新存活时间
	 * 
	 * @param key
	 * @param liveSecond
	 * @return
	 */
	public static boolean touch(String key, int liveSecond) {
		try {
			return getClient().touch(key, liveSecond);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * connect shutdown
	 */
	public static void shutdown() {
		try {
			getClient().shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
