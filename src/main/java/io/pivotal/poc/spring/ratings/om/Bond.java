package io.pivotal.poc.spring.ratings.om;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgupta on 10/30/14.
 */
public class Bond implements Identifiable {
  private static final long serialVersionUID = 7387502003380580475L;

  private String cusip;
  private String group;
  private BondIssuer issuer;
  private String regionState;
  private DebtType debtType;
  private String lastActionId;
  private String department;
  private Integer amount;
  private List<BondAction> actions;

  public Bond() {}

  public Bond(String cusip, String group, BondIssuer issuer, String regionState, DebtType debtType, Integer amount) {
    this.cusip = cusip;
    this.group = group;
    this.issuer = issuer;
    this.regionState = regionState;
    this.debtType = debtType;
    this.amount = amount;
    this.department = issuer.getGroupType().getDepartment();
    this.actions = new ArrayList<>();
  }

  @Override
  @JsonIgnore
  public String getId() {
    return cusip;
  }

  public String getCusip() {
    return cusip;
  }

  public String getGroup() {
    return group;
  }

  public BondIssuer getIssuer() {
    return issuer;
  }

  public String getRegionState() {
    return regionState;
  }

  public DebtType getDebtType() {
    return debtType;
  }

  public String getLastActionId() {
    return lastActionId;
  }

  public void setLastActionId(String lastActionId) {
    this.lastActionId = lastActionId;
  }

  public String getDepartment() {
    return department;
  }

  public Integer getAmount() {
    return amount;
  }

  public List<BondAction> getActions() {
    return actions;
  }

  @Override
  public int hashCode() {
    return cusip.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && (obj == this || (obj instanceof Bond && ((Bond) obj).cusip.equals(cusip)));
  }
}
