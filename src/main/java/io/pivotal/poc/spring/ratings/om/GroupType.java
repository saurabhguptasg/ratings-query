package io.pivotal.poc.spring.ratings.om;

/**
* Created by sgupta on 10/30/14.
*/
public enum GroupType {
  chemicals("CHEMICALS",                            "Corporate Ratings"),
  assetmanagers("ASSET MANAGERS",                   "Financial Institution Ratings"),
  assetbackedsecurities("ASSET-BACKED SECURITIES",  "Structured Finance Ratings"),
  bondinsadmin("BOND INSURANCE ADMIN",              "Public Finance Ratings"),
  diversifiedenergy("DIVERSIFIED ENERGY",           "Corporate Ratings"),
  infra("INFRASTRUCTURE",                           "Corporate Ratings"),
  publicadmin("PUBLIC ADMINISTRATION",              "Public Finance Ratings"),
  loc("LOC",                                        "Structured Finance Ratings"),
  sovereign("SOVEREIGN",                            "Sovereigns And International Public Finance"),
  aerospace("AEROSPACE/DEFENSE",                    "Corporate Ratings"),
  genobs("GENERAL OBLIGATION",                      "Public Finance Ratings"),
  commercialmortgage("COMMERCIAL MORTGAGE RATINGS", "Structured Finance Ratings"),
  structdebt("STRUCTURED CREDIT",                   "Structured Finance Ratings"),
  hitech("HIGH TECHNOLOGY",                         "Corporate Ratings"),
  consumerprod("CONSUMER PRODUCTS",                 "Corporate Ratings"),
  bondinsurers("BOND INSURERS",                     "Insurance Ratings"),
  cmopay("CMO/PAY/PASS-THROUGH",                    "Structured Finance Ratings"),
  newassets("NEW ASSETS",                           "Structured Finance Ratings"),
  mutualfunds("MUTUAL FUNDS",                       "Fund Ratings & Evaluations"),
  infotech("INFORMATION TECHNOLOGY",                "Information Technology"),
  servicerevals("SERVICER EVALUATIONS",             "Structured Finance Ratings"),
  consumerassets("CONSUMER ASSETS",                 "Structured Finance Ratings"),
  commercialassets("COMMERCIAL ASSETS",             "Structured Finance Ratings"),
  ;
  private final String displayName;
  private final String department;

  GroupType(String displayName, String department) {
    this.displayName = displayName;
    this.department = department;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getDepartment() {
    return department;
  }


  @Override
  public String toString() {
    return displayName;
  }
}
