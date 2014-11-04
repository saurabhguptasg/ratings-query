package io.pivotal.poc.spring.ratings.data;

/**
* Created by sgupta on 10/30/14.
*/
public interface QueryPredicate<T> {
  public boolean isMatch(T item);
}
