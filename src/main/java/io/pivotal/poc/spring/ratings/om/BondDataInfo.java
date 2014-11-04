package io.pivotal.poc.spring.ratings.om;

/**
 * Created by sgupta on 11/3/14.
 */
public class BondDataInfo implements Identifiable {
  private final String id;
  private String info;

  private BondDataInfo(){
    id = null;
  }

  public BondDataInfo(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }
}
