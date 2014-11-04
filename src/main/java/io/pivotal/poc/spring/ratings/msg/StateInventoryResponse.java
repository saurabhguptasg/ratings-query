package io.pivotal.poc.spring.ratings.msg;

import io.pivotal.poc.spring.ratings.om.Bond;
import io.pivotal.poc.spring.ratings.om.StateType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgupta on 10/31/14.
 */
public class StateInventoryResponse {
  private final StateType stateType;
  private Long totalAmount = 0L;
  private final List<Bond> bonds;

  public StateInventoryResponse(StateType stateType) {
    this.stateType = stateType;
    this.bonds = new ArrayList<>();
  }

  public StateType getStateType() {
    return stateType;
  }

  public List<Bond> getBonds() {
    return bonds;
  }

  public Long getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Long totalAmount) {
    this.totalAmount = totalAmount;
  }
}
