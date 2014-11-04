package io.pivotal.poc.spring.ratings.data;

import io.pivotal.poc.spring.ratings.om.Identifiable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by sgupta on 11/3/14.
 */
public interface RedisObjectRepository<T extends Identifiable> extends CrudRepository<T,String> {
}
