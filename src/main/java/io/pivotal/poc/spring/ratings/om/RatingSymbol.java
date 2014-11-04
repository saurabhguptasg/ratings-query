package io.pivotal.poc.spring.ratings.om;

import java.util.Random;

/**
 * Created by sgupta on 10/30/14.
 */
public enum RatingSymbol {
  NR("NR"),
  AAA("AAA"),
  AA_PLUS("AA+"),
  AA("AA"),
  AA_MINUS("AA-"),
  A_PLUS("A+"),
  A("A"),
  A_MINUS("A-"),
  BBB_PLUS("BBB+"),
  BBB("BBB"),
  BBB_MINUS("BBB-"),
  BB_PLUS("BB+"),
  BB("BB"),
  BB_MINUS("BB-"),
  B_PLUS("B+"),
  B("B"),
  B_MINUS("B-"),
  CCC_PLUS("CCC+"),
  CCC("CCC"),
  CCC_MINUS("CCC-"),
  CC("CC"),
  C("C"),
  D("D"),
  ;
  private final String symbol;

  private static final Random random = new Random();

  RatingSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getSymbol() {
    return symbol;
  }

  public static RatingSymbol getRevised() {
    return RatingSymbol.values()[1+random.nextInt(RatingSymbol.values().length - 1)];
  }
}
