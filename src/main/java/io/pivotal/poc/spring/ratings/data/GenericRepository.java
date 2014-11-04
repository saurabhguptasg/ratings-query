package io.pivotal.poc.spring.ratings.data;

import io.pivotal.poc.spring.ratings.om.Identifiable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by sgupta on 11/3/14.
 */
public class GenericRepository<T extends Identifiable> implements RedisObjectRepository<T> {
  private final HashOperations<String, String, T> hashOps;
  private final String PROPERTIES_KEY;

  public GenericRepository(RedisTemplate<String, T> redisTemplate, Class<T> tClass) {
    this.hashOps = redisTemplate.opsForHash();
    this.PROPERTIES_KEY = tClass.getName();
  }


  @Override
  public <S extends T> S save(S s) {
    hashOps.put(PROPERTIES_KEY, s.getId(), s);
    return s;
  }

  @Override
  public <S extends T> Iterable<S> save(Iterable<S> ses) {
    List<S> result = new ArrayList<S>();

    for (S entity : ses) {
      result.add(save(entity));
    }

    return result;
  }

  @Override
  public T findOne(String id) {
    return hashOps.get(PROPERTIES_KEY, id);
  }

  @Override
  public boolean exists(String id) {
    return hashOps.hasKey(PROPERTIES_KEY, id);
  }

  @Override
  public Iterable<T> findAll() {
    return hashOps.values(PROPERTIES_KEY);
  }

  @Override
  public Iterable<T> findAll(Iterable<String> ids) {
    return hashOps.multiGet(PROPERTIES_KEY, Util.convertIterableToList(ids));
  }

  @Override
  public long count() {
    return hashOps.keys(PROPERTIES_KEY).size();
  }

  @Override
  public void delete(String id) {
    hashOps.delete(PROPERTIES_KEY, id);
  }

  @Override
  public void delete(T t) {
    hashOps.delete(PROPERTIES_KEY, t.getId());
  }

  @Override
  public void delete(Iterable<? extends T> entities) {
    for (T object : entities) {
      delete(object);
    }
  }

  @Override
  public void deleteAll() {
    Set<String> ids = hashOps.keys(PROPERTIES_KEY);
    for (String id : ids) {
      delete(id);
    }
  }


}
