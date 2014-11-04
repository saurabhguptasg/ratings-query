package io.pivotal.poc.spring.ratings.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgupta on 11/3/14.
 */
public class Util {
  public static <V> List<V> convertIterableToList(Iterable<V> iterable) {
    List<V> list = new ArrayList<>();
    for (V object : iterable) {
      list.add(object);
    }
    return list;
  }
}
