package io.pivotal.poc.spring.ratings.om;

/**
 * Created by sgupta on 10/30/14.
 */
public class BondIssuer implements Identifiable {
  private String issuerId;
  private String name;
  private GroupType groupType;
  private String groupName;
  private StateType stateType;
  private String groupDepartment;

  public BondIssuer(){}

  public BondIssuer(String issuerId, String name, GroupType groupType, StateType stateType) {
    this.issuerId = issuerId;
    this.name = name;
    this.groupType = groupType;
    this.stateType = stateType;
    this.groupName = groupType.getDisplayName();
    this.groupDepartment = groupType.getDepartment();
  }

  @Override
  public String getId() {
    return issuerId;
  }

  public String getIssuerId() {
    return issuerId;
  }

  public String getName() {
    return name;
  }

  public GroupType getGroupType() {
    return groupType;
  }

  public StateType getStateType() {
    return stateType;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getGroupDepartment() {
    return groupDepartment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BondIssuer that = (BondIssuer) o;

    if (!issuerId.equals(that.issuerId)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return issuerId.hashCode();
  }

}
