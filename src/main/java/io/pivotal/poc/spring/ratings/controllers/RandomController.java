package io.pivotal.poc.spring.ratings.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sgupta on 11/7/14.
 */
@Controller
@RequestMapping(value = "/rand/**", produces = "application/json")
public class RandomController {

  @RequestMapping(value = "/double")
  @ResponseBody
  public Map<String,String> getRandomDouble() {
    Map<String,String> result = new HashMap<>();
    result.put("random", "" + Math.random());
    return result;
  }
}
