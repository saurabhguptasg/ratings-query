package io.pivotal.poc.spring.ratings;

import io.pivotal.poc.spring.ratings.controllers.ResponseInterceptor;
import io.pivotal.poc.spring.ratings.data.ObjectDatabase;
import io.pivotal.poc.spring.ratings.data.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
    }

/*
  @Bean
  public RedisTemplate<String,BondIssuer> getBondIssuerTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String,BondIssuer> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    return template;
  }
*/

  @Bean
  public ObjectDatabase getObjectDatabase() {
    System.out.println("*******>>>>>> get object database called");
    return new ObjectDatabase();
//    return ObjectDatabase.getInstance();
  }


  @Bean
  public WebMvcConfigurerAdapter configurerAdapter() {
    return new WebMvcConfigurerAdapter() {
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ResponseInterceptor());
      }
    };
  }

}
