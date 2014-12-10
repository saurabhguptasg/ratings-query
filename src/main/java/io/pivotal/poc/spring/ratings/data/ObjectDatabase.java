package io.pivotal.poc.spring.ratings.data;

import io.pivotal.poc.spring.ratings.om.Identifiable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by sgupta on 10/30/14.
 */
@Component
public class ObjectDatabase {
  private static final Logger LOGGER = Logger.getLogger(ObjectDatabase.class.getName());

//  private final Map<String,Map<String,?>> tables;
  private final Map<String,GenericRepository<? extends Identifiable>> templates;

  @Autowired
  private RedisConnectionFactory redisConnectionFactory;

  public ObjectDatabase() {
    LOGGER.info(">>>>>>>> created new object database instance!");
//    tables = new HashMap<>();
    templates = new HashMap<>();
  }

  public <T extends Identifiable> void registerType(String type, Class<T> tClass) {
    GenericRepository<T> repository = getRepository(tClass);
    templates.put(type, repository);
  }

  private <T extends Identifiable> GenericRepository<T> getRepository(Class<T> tClass) {
    RedisTemplate<String, T> template = new RedisTemplate<String, T>();

    template.setConnectionFactory(redisConnectionFactory);

    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    RedisSerializer objectSerializer = new JdkSerializationRedisSerializer();

    template.setKeySerializer(stringSerializer);
    template.setValueSerializer(objectSerializer);
    template.setHashKeySerializer(stringSerializer);
    template.setHashValueSerializer(objectSerializer);

    template.afterPropertiesSet();

    return new GenericRepository<>(template, tClass);
  }

  public <T extends Identifiable> void put(String type, String key, T value) {
    GenericRepository<T> repository = (GenericRepository<T>) templates.get(type);
    repository.save(value);

/*
    Map<String,T> lookup = (Map<String, T>) tables.get(type);
    if(lookup == null) {
      lookup = new HashMap<>();
      tables.put(type, lookup);
    }
    lookup.put(key, value);
*/
  }

  public <T extends Identifiable> T get(String type, String key) {
    GenericRepository<T> repository = (GenericRepository<T>) templates.get(type);
    if(repository == null) {
      return null;
    }
    return repository.findOne(key);

/*
    Map<String,T> lookup = (Map<String, T>) tables.get(type);
    if(lookup == null) {
      return null;
    }
    else {
      return lookup.get(key);
    }
*/
  }

  public <T extends Identifiable> List<T> getAll(String type, Class<T> tClass) {
    GenericRepository<T> repository = (GenericRepository<T>) templates.get(type);
    return repository == null ?  new ArrayList<T>() : Util.convertIterableToList(repository.findAll());
/*
    Map<String,T> lookup = (Map<String, T>) tables.get(type);
    return lookup == null ? new ArrayList<T>() : new ArrayList<T>(lookup.values());
*/
  }

  public <T extends Identifiable> List<T> query(String type, Class<T> tClass, Comparator<T> comparator, QueryPredicate<T> ... predicates) {
    List<T> items = getAll(type, tClass);
    List<T> results = new ArrayList<>();

    for (T item : items) {
      boolean ok = true;
      if(predicates != null) {
        for (QueryPredicate<T> predicate : predicates) {
          ok = ok && predicate.isMatch(item);
        }
      }
      if (ok) {
        results.add(item);
      }
    }

    if(comparator != null) {
      Collections.sort(results, comparator);
    }
    return results;
  }

  public void flush() {
    redisConnectionFactory.getConnection().flushAll();
    redisConnectionFactory.getConnection().flushDb();
  }

}
