package io.pivotal.poc.spring.ratings;

import io.pivotal.poc.spring.ratings.data.ObjectDatabase;
import io.pivotal.poc.spring.ratings.om.Identifiable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by sgupta on 10/30/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DatabaseTests {

  @Autowired
  ObjectDatabase objectDatabase;

  @Test
  public void testDatabasePutGet() {
//    ObjectDatabase objectDatabase = ObjectDatabase.getInstance();
    TestObject testObject = new TestObject("123", "test 123");
    objectDatabase.put(TestObject.class.getSimpleName(), testObject.getId(), testObject);

    TestObject testObject2 = objectDatabase.get(TestObject.class.getSimpleName(), "123");
    assert testObject2 != null;
    assert testObject2.getId().equals("123");
  }


  public static final class TestObject implements Identifiable {
    private final String id;
    private final String name;

    public TestObject(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public String getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    @Override
    public int hashCode() {
      return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      TestObject that = (TestObject) o;

      if (!id.equals(that.id)) {
        return false;
      }

      return true;
    }
  }
}
