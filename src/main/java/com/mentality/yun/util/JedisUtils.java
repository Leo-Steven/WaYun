package com.mentality.yun.util;/*
 * @author:一身都是月~
 * @date：2020/11/2 13:23
 * */

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.Properties;

public class JedisUtils {
    private static JedisPool jedisPool;

    static {
        Properties properties = new Properties();
        try {
            properties.load(JedisUtils.class.getClassLoader().getResourceAsStream("jedis.properties"));
            JedisPoolConfig jd = new JedisPoolConfig();
            jd.setMaxTotal(Integer.parseInt(properties.getProperty("maxTotal")));
            jd.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));
            jedisPool = new JedisPool(jd, properties.getProperty("host"), Integer.parseInt(properties.getProperty("port")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JedisPool getJedisPool() {
        return jedisPool;
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

}
