package com.in28minutes.microservices.currencyexchangeservice;

import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/*
In the currency exchange, we have a predefined structure. When the currency exchange service is called, we want to return this structure back -

{
   "id":10001,
   "from":"USD",
   "to":"INR",
   "conversionMultiple":65.00,
   "environment":"8000 instance-id"
}

At last, we will return some details about the environment. For now, let's focus on only top four values.
 */
@Entity
public class CurrencyExchange {

    @Id
    private Long id;

    @Column(name = "currency_from")
    /*
    Above we are using from and from is a SQL keyword because we would use it in "select * from" right.
    So that's the reason why we can't have a column name with the name as from and that's the reason why there is an exception on running the application.
    To counter that, give it different column name using @Column annotation. So we'll call it from in our bean but in the database we'll give it a different name.
    */
    private String from;

    // To be consistent, we use similar name for to as well.
    @Column(name = "currency_to") // Whenever we get some error with JdbcSQLSyntaxErrorException where error says something like - expected "identifier"; SQL statement, then it suggests that we are using some keyword.
    private String to;

    private BigDecimal conversionMultiple;
    private String environment;
    /*
    Later, we see that we would have multiple instances of currency conversion microservice and the currency exchange microservice.
    And when I'm calling from currency conversion microservice, I need to know which instance of the currency exchange microservice is providing the response.
    Is this instance one or two or three? Is this currency exchange instance running on port 8000 or 8001 or 8002?

    That would help us identify whether our load balancers and the naming servers are working properly. So to be able to track the instance of currency exchange
    that is providing the response back, we add in a variable in here "environment". So in this environment, we send a few environment details back in the response of the REST API.
    I'll not add it to the constructor, but I would create a couple of getters and setters for it.
     */

    // When we would play with JPA, it would need the no argument constructor.
    public CurrencyExchange() {

    }

    public CurrencyExchange(Long id, String from, String to, BigDecimal conversionMultiple) {
        super();
        this.id = id;
        this.from = from;
        this.to = to;
        this.conversionMultiple = conversionMultiple;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getConversionMultiple() {
        return conversionMultiple;
    }

    public void setConversionMultiple(BigDecimal conversionMultiple) {
        this.conversionMultiple = conversionMultiple;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}