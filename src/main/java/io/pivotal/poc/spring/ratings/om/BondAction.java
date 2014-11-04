package io.pivotal.poc.spring.ratings.om;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sgupta on 10/30/14.
 */
public class BondAction implements Identifiable {
  private static final long serialVersionUID = -265357045749039583L;
  private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");

  private String actionId;
  private String cusip;
  private Long actionDateTime; //
  private ActionType actionType;
  private String cwOutlook;
  private RatingSymbol ratingSymbol;
  private Long actionYearMonth;

  public BondAction(){}

  public BondAction(String actionId,
                    String cusip,
                    Long actionDateTime,
                    ActionType actionType,
                    String cwOutlook,
                    RatingSymbol ratingSymbol) {
    this.actionId = actionId;
    this.cusip = cusip;
    this.actionDateTime = actionDateTime;
    this.actionType = actionType;
    this.cwOutlook = cwOutlook;
    this.ratingSymbol = ratingSymbol;
    this.actionYearMonth = Long.parseLong(FORMAT.format(new Date(actionDateTime)));
  }

  @Override
  @JsonIgnore
  public String getId() {
    return actionId;
  }

  public String getActionId() {
    return actionId;
  }

  public String getCusip() {
    return cusip;
  }

  public Long getActionDateTime() {
    return actionDateTime;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public String getCwOutlook() {
    return cwOutlook;
  }

  public RatingSymbol getRatingSymbol() {
    return ratingSymbol;
  }

  public Long getActionYearMonth() {
    return actionYearMonth;
  }

  @Override
  public int hashCode() {
    return actionId.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null && (obj == this || (obj instanceof BondAction && ((BondAction) obj).actionId.equals(actionId)));
  }
}
