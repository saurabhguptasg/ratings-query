package io.pivotal.poc.spring.ratings.controllers;

import io.pivotal.poc.spring.ratings.data.ObjectDatabase;
import io.pivotal.poc.spring.ratings.data.QueryPredicate;
import io.pivotal.poc.spring.ratings.msg.DatesResponse;
import io.pivotal.poc.spring.ratings.msg.StateInventoryResponse;
import io.pivotal.poc.spring.ratings.om.ActionType;
import io.pivotal.poc.spring.ratings.om.Bond;
import io.pivotal.poc.spring.ratings.om.BondAction;
import io.pivotal.poc.spring.ratings.om.StateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sgupta on 10/30/14.
 */
@Controller
@RequestMapping(value = "/query/**", produces = "application/json")
public class RatingsQueryController {
  private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMM");

  @Autowired
  ObjectDatabase objectDatabase;

  @RequestMapping(value = "/dates", method = RequestMethod.GET)
  @ResponseBody
  public List<DatesResponse> datesResponses() {
    List<DatesResponse> responses = new LinkedList<>();
    Long now = System.currentTimeMillis();
    for (int i = 0; i < 6; i++) {
      Long time = now - (i * BondDataGenerator.month);
      Date date = new Date(time);
      String formattedDate = FORMAT.format(date);
      DatesResponse response = new DatesResponse(formattedDate, formattedDate.substring(0, 4), formattedDate.substring(4));
      responses.add(0,response);
    }
    return responses;
  }

  @RequestMapping(value = "/base", method = RequestMethod.GET)
  @ResponseBody
  public String base(@RequestParam(value = "num", required = false)String num) {
    return new BigInteger(num==null?""+System.currentTimeMillis():num).toString(36);
  }

  @RequestMapping(value = "/bonds", method = RequestMethod.GET)
  @ResponseBody
  public List<Bond> getBonds(@RequestParam(value = "month", required = false) final Long actionYearMonth,
                             @RequestParam(value = "action", required = false, defaultValue = "New")final ActionType actionType) {
    ArrayList<QueryPredicate<Bond>> predicates = new ArrayList<>();

    if(actionYearMonth != null || actionType != null) {
      predicates.add(new QueryPredicate<Bond>() {
        @Override
        public boolean isMatch(Bond item) {
          switch (actionType) {
            case New:
              BondAction newAction = item.getActions().get(0);
              return newAction.getActionYearMonth()/100l == actionYearMonth;

            case Revised:
              for (BondAction bondAction : item.getActions()) {
                if(bondAction.getActionYearMonth()/100l == actionYearMonth &&
                    bondAction.getActionType() == actionType) {
                  return true;
                }
              }
              return false;
          }
          return false;
        }
      });
    }


    return actionYearMonth == null ?
           objectDatabase.getAll(Bond.class.getSimpleName(), Bond.class) :
           objectDatabase.query(Bond.class.getSimpleName(),
                                Bond.class,
                                null,
                             predicates.toArray(new QueryPredicate[predicates.size()])
                             );
  }

  @RequestMapping(value = "/stateInventory", method = RequestMethod.GET)
  @ResponseBody
  public Collection<StateInventoryResponse> getStateInventory() {
    Map<StateType,StateInventoryResponse> inventoryResponseMap = new HashMap<>();

    return inventoryResponseMap.values();
  }


  @RequestMapping(value = "/info", method = RequestMethod.GET)
  @ResponseBody
  public Map<String,String> getInfo(HttpServletRequest request) {
    Map<String,String> info = new HashMap<>();
    info.put("ip", request.getLocalAddr());
    return info;
  }

}
