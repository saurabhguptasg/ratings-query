package io.pivotal.poc.spring.ratings.controllers;

import io.pivotal.poc.spring.ratings.data.ObjectDatabase;
import io.pivotal.poc.spring.ratings.msg.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by sgupta on 11/3/14.
 */
@Controller
@RequestMapping(value = "/admin/**", produces = "application/json")
public class AdminController {

  @Autowired
  ObjectDatabase objectDatabase;

  @RequestMapping(value = "/flush", method = RequestMethod.GET)
  @ResponseBody
  public MessageResponse flushDatabase() {
    objectDatabase.flush();
    return MessageResponse.okResponse("database flushed");
  }

  @RequestMapping(value = "/kill", method = RequestMethod.GET)
  @ResponseBody
  public MessageResponse kill() {
    System.exit(-1);
    return MessageResponse.errorResponse("system killed");
  }


}
