package io.pivotal.poc.spring.ratings.om;

import java.util.Random;

/**
 * Created by sgupta on 10/30/14.
 */
public enum DebtType {
  SRUNSEC("Senior Unsecured"),
  SRSEC("Senior Secured"),
  MUNIDEBT("Municipal Debt"),
  ICR("ICR"),
  FSR("FSR"),
  JRSUBOR("Junior Subordinated"),
  SHTRMDEBT("Short Term Debt"),
  SUBOR("Subordinated"),
  ;
  private final String displayName;
  private static Random random = new Random();

  DebtType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public static DebtType getRandomDebtType() {
    return DebtType.values()[random.nextInt(DebtType.values().length)];
  }
}
