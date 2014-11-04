package io.pivotal.poc.spring.ratings.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Protocol;

import java.io.IOException;

/**
 * Created by sgupta on 11/3/14.
 */
@Configuration
public class RedisConfig {
  private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class.getName());

  @Bean
  public RedisConnectionFactory redisConnection() {
    try {
      String vcap_services = System.getenv("VCAP_SERVICES");
      if (vcap_services != null && vcap_services.length() > 0) {
        // parsing rediscloud credentials
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(vcap_services);
        JsonNode rediscloudNode = root.get("rediscloud");
        JsonNode credentials = rediscloudNode.get(0).get("credentials");

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(credentials.get("hostname").asText());
        factory.setPort(Integer.parseInt(credentials.get("port").asText()));
        factory.setTimeout(Protocol.DEFAULT_TIMEOUT);
        factory.setPassword(credentials.get("password").asText());

        logger.debug("vcap services is: " + vcap_services);
        logger.debug("factory is: " + factory);
        return factory;
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return new JedisConnectionFactory();
  }
}
