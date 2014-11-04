package io.pivotal.poc.spring.ratings.controllers;

import io.pivotal.poc.spring.ratings.data.ObjectDatabase;
import io.pivotal.poc.spring.ratings.om.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by sgupta on 10/30/14.
 */
@Component
@EnableScheduling
public class BondDataGenerator implements ApplicationListener<ContextRefreshedEvent> {
  private static final Logger LOGGER = Logger.getLogger(BondDataGenerator.class.getName());

  @Autowired
  ObjectDatabase objectDatabase;

  private final Map<GroupType,String[]> groupIssuers;
  private final Random random = new Random();
  public static final long day = 1000l * 3600l * 24l;
  public static final Long month = day * 31l;

  public BondDataGenerator() {
    LOGGER.info(">> >> >> >> created bond data generator!");
    groupIssuers = new HashMap<>();
  }

  private void buildDatabase() {
    populateGroupIssuers();
    buildIssuers();
  }

  private void buildIssuers() {
    int i = 0;
    for (Map.Entry<GroupType, String[]> entry : groupIssuers.entrySet()) {
      for (String issuerName : entry.getValue()) {
        StateType stateType = StateType.values()[i % StateType.values().length];
        i++;
        BondIssuer issuer = new BondIssuer(generateCusip(), issuerName, entry.getKey(), stateType);
        objectDatabase.put(BondIssuer.class.getSimpleName(), issuer.getIssuerId(), issuer);
        buildBondsForIssuer(issuer);
      }
    }
  }

  private void buildBondsForIssuer(BondIssuer issuer) {
    for (int i = 0; i < 3; i++) {
      Bond bond = buildNewBondForIssuer(issuer);
      buildBondActionsForBond(bond);
    }
  }

  private Bond buildNewBondForIssuer(BondIssuer issuer) {
    Bond bond = new Bond(generateCusip(),
                         issuer.getGroupType().getDisplayName(),
                         issuer,
                         issuer.getStateType().name(),
                         DebtType.getRandomDebtType(),
                         3 + random.nextInt(25));
    objectDatabase.put(Bond.class.getSimpleName(), bond.getCusip(), bond);
    return bond;
  }



  private void buildBondActionsForBond(Bond bond) {
    int mult = random.nextInt(6);
    Long startTime = System.currentTimeMillis() - (mult * month);

    BondAction action = new BondAction(generateCusip(),
                                       bond.getCusip(),
                                       startTime,
                                       ActionType.New, null, RatingSymbol.NR);
    bond.getActions().add(action);
    objectDatabase.put(BondAction.class.getSimpleName(), action.getActionId(), action);

    for (int i = 1; i < mult; i++) {
      Long timestamp = startTime + (i * month);
      BondAction bondAction = new BondAction(generateCusip(),
                                             bond.getCusip(),
                                             timestamp,
                                             ActionType.Revised,
                                             null,
                                             RatingSymbol.getRevised());
      bond.getActions().add(bondAction);
      objectDatabase.put(BondAction.class.getSimpleName(), bondAction.getActionId(), bondAction);
    }
    objectDatabase.put(Bond.class.getSimpleName(), bond.getCusip(), bond);
  }

  private String generateCusip() {
    return Long.toString(System.currentTimeMillis(), 36).toUpperCase() + "." + Long.toString(Math.abs(random.nextLong()), 36).toUpperCase();
  }

  private void populateGroupIssuers() {
    groupIssuers.put(GroupType.chemicals, new String[] {"Bayer, Inc.","BASF, Inc.", "Dow Chemical", "Helmholtz Warwick"});
    groupIssuers.put(GroupType.assetmanagers, new String[] {"Citigroup, Inc.", "Blackrock", "Titanium Group", "AKD Mortgage", "Arrow Global Finance PLC", "TIAA Asset Management, LLC"});
    groupIssuers.put(GroupType.assetbackedsecurities, new String[] {"Agate Bay Mortgage Trust 2014-3", "Wells Mortgage 2014-5", "Pimco ABF 2020", "CitiFund XQ2014-07"});
    groupIssuers.put(GroupType.bondinsadmin, new String[] {"Richland County Sch Dist", "Pasco County Sch Dist", "Suffolk County Sch Dist"});
    groupIssuers.put(GroupType.diversifiedenergy, new String[] {"Southeast PowerGen", "Great Oklahoma Gas", "DeepDrill Advanced"});
    groupIssuers.put(GroupType.infra, new String[] {"Vantage Pipeline", "Santana Roads", "Sombrero PowerLines", "Pima Cnty Indl Dev Auth", "Apache Cnty Indl Dev Auth"});
    groupIssuers.put(GroupType.publicadmin, new String[] {"West Deptford Twp", "Small Hills Reg Twp", "Fayetteville", "Sugar Grove Pub Lib Dist"});
    groupIssuers.put(GroupType.loc, new String[] {"Union Cnty", "Miami Hlth Fac Auth", "Alexandria Indl Dev Auth", "Orange Cnty Indl Dev Auth", "Macon-Bibb Cnty Urban Dev Auth"});
    groupIssuers.put(GroupType.aerospace, new String[] {"Paccar Financial", "NetJets", "GATX Corp."});
    groupIssuers.put(GroupType.hitech, new String[] {"Regal Cinemas", "Microsoft", "Apple", "IBM"});
    groupIssuers.put(GroupType.consumerprod, new String[] {"Procter & Gamble Co.", "Unilever", "SCJohnson"});
    groupIssuers.put(GroupType.infotech, new String[] {"Asian Development Bank", "Pearson PLC", "Freddie Mac", "Barclays Bank PLC", "Ho-Ping Power Co."});
  }





  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    objectDatabase.registerType(BondDataInfo.class.getSimpleName(), BondDataInfo.class);
    objectDatabase.registerType(BondIssuer.class.getSimpleName(), BondIssuer.class);
    objectDatabase.registerType(Bond.class.getSimpleName(), Bond.class);
    objectDatabase.registerType(BondAction.class.getSimpleName(), BondAction.class);

    BondDataInfo dataInfo = objectDatabase.get(BondDataInfo.class.getSimpleName(), "databaseBuilt");
    if(dataInfo == null) {
      LOGGER.info(">>>>>>>>>>>>>>>>> database NOT PRESENT, will build");
      buildDatabase();
      dataInfo = new BondDataInfo("databaseBuilt");
      dataInfo.setInfo("true");
      objectDatabase.put(BondDataInfo.class.getSimpleName(), dataInfo.getId(), dataInfo);
    }
    else {
      LOGGER.info(">>>>>>>>>>>>>>>>> database already built, will not rebuild");
    }
  }


  @Scheduled(fixedRate = day, initialDelay = day)
  public void updateDatabase() {
    LOGGER.info("[----- ----- ----- will update database ----- ----- -----]");
    List<BondIssuer> issuers = objectDatabase.getAll(BondIssuer.class.getSimpleName(), BondIssuer.class);
    Set<Integer> selectedIssuers = new HashSet<>();
    while (selectedIssuers.size() < 4) {
      selectedIssuers.add(random.nextInt(issuers.size()));
    }
    for (Integer selectedIssuer : selectedIssuers) {
      BondIssuer issuer = issuers.get(selectedIssuer);
      Bond newBond = buildNewBondForIssuer(issuer);
      buildBondActionsForBond(newBond);
    }
  }
}
