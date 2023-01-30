package com.in28minutes.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
/*
In this step let's create a REST API in the currency exchange service.
We want to create a rest API which adheres to this specific URL - http://localhost:8000/currency-exchange/from/USD/to/INR
We would later also create another service called the CurrencyConversionService which would be having this URL - http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10
 */
@RestController // As we would want expose the REST API from here
public class CurrencyExchangeController
{
    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private CurrencyExchangeRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to)
    {
        logger.info("retrieveExchangeValue called with {} to {}",from, to);
    // CurrencyExchange currencyExchange = new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(50)); // initially just returning a hard-coded value of CurrencyExchange to the REST api.

    /*
    Above we are autowiring the CurrencyExchangeRepository and then I'd be able to get all the details from the db.

    repository.findAll() - For getting all the rows from the db. This would return all the currency exchange records which are present.
    repository.count()   - It'll return the count of number of rows.

    However, here we want not the count nor to return all the things back. We want to actually search the table by "from" and "to".
    So based on the path variables from and to, I would want to actually find something and return that back.

    And to be able to do that, you can actually extend the JpaRepository interface (we did in CurrencyExchangeRepository) and add a method there.
    So you can add findBy which column name you want to findBy - FromAndTo. So, we want to do a findByFromAndTo, the parameters which would be passed to method are - (String from, String to).
    It would return back a specific Currency Exchange value. So method signature is like - CurrencyExchange findByFromAndTo(String from, String to);
    When you create a method like this, the implementation would be provided by Spring Data JPA.

    Spring Data JPA would convert it into a SQL query where we would actually query the table by from and to. After adding above method in
    CurrencyExchangeRepository interface, we can try and get the details from the db. So instead of creating new CurrencyExchange and setting
    parameters thereafter, I would get it from the repository using repository.findByFromAndTo(from, to) i.e. we passed in the from and the to variable.

    So, let's go ahead and try our URL - http://localhost:8000/currency-exchange/from/USD/to/INR
    And you can see that I'm getting 65 back which we've set conversionMultiple for USD/INR in our data.sql
    We can try EUR and AUD as well in our URL, for EUR to INR, we'll get 75 and in AUD, it returns 25 back.

    In this step, we created a simple repository file CurrencyExchangeRepository extending the JpaRepository which helped us to talk to the db.
    We were able to query record by from and to and we updated our CurrencyExchangeController to be able to call the repository and get the data from the db.
    */
        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);

        if(currencyExchange == null) {
            throw new RuntimeException("Unable to Find data for " + from + " to " + to);
        }

        /*
        As part of this environment, for now I want to just set the port where the response is coming back from using setter method of environment.
        Spring offers a class called "Environment" to get the value of the port, so we'll create variable of type Environment and autowire it in.

        Import Environment from "org.springframework.core.env.Environment". Now from the environment, I can actually now get the value of the port.
        So "environment.getProperty", and the specific property that we need to get is "local.server.port", set this port in environment variable. let's run it.
        Now we see that it's returning a port of 8000 back.

        Now, I want to launch this application on port 8001 as well. One of the ways I can do that is by going to application.properties, and change port configuration to 8001.

        Let's say I would want to have two instances of this application running.
        One of the ways you can do that is by going to "Run Configurations" in eclipse or "Edit Configurations" in IntelliJ. So over there we can modify the configurations
        that are being used for currency exchange service application running on 8000, that's the default. I'll create a copy of instance. You can right-click and say
        "Duplicate". And the duplicate, I would want to run this on port 8001. To run it on port 8001, go over to the Arguments tab and write in VM arguments section, "-Dserver.port=8001".

        So whatever you are providing as an environment variable here would override whatever is configured in application.properties.
        So when we launch up an instance using this particular configuration, then this instance will be launched up on port 8001.
        So this would launch up a new instance. If you go to the console, you'd see that a new instance will be launched up and this would be launched up on port 8001.

        Let's look up in application startup log - "started on port(s) 8001", and to access that, use the URL "localhost:8001/currency-exchange/from/USD/to/INR".
        And you can see that the environment is now 8001.
        Right now we only have port in the environment. Later when we play with other tools, we'll add more information to the environment.

        So we enhanced our currency exchange microservice to be able to return the environment details. In this particular case, we are reading the port details.
        So when we call the currency exchange service from the currency conversion microservice, we will know which instance of the currency exchange microservice is responding back.



        Right now we have a hardcoded rest API.
        Let's now make it fetch some details from the db and we'll use an in-memory db called H2 and we'll use JPA to talk to the in-memory db.
        Therefore, we add a few more dependencies in pom.xml -

        1. spring-boot-starter-data-jpa
        2. h2 artifactId in com.h2database groupId - We want to connect to in-memory db using JPA

        On restarting this application, we can see that it has started up fine and by default you can see in startup logs that in-memory db is created for us at a specific location.
        We do not want to use a random db URL, so what we'll do is we'll configure one.

        Go to application.properties and configure a few things around our db.

        1) spring.jpa.show-sql=true - This property configuration is to make sure that I can see all the SQL statements that are generated.
        2) springboot.datasource.url = jdbc:h2:mem:testdb - This is the data source URL. I want to configure it to jdbc:h2 we want to use an in-memory db and I would want to call this testdb
            In the earlier versions of Spring Data JPA, this was the default and in the later versions they made it random.
            I like to configure a standard data source URL so that we can use this from here on.
        3) spring.h2.console.enabled = true - As I want to also enable h2 console to be able to see what are the details which are present in the db.

        On restarting the application, it should launch up fine and the url to h2 console should be - localhost:8000/h2-console
        If you see a different JDBC URL there then just copy value of springboot.datasource.url property in application.properties and paste in JDBC URL column and
        connect to h2 console where you can actually see all the tables and the data which is present in the tables. Initially there are no tables present in here.

So you want to create a table and you would want to add some data in it.
So to be able to create a table, let's create an entity. Once we define an entity, spring data JPA would automatically create a table for us.

So we'll make CurrencyExchange class as JPA entity with @Entity annotation and mark Long id as primary id of entity with @Id annotation,
I want to import in javax.persistence.entity and javax.persistence.id.

So what would happen in the background is a table would be created for us. Now I can see the table but there is no data in the table.

If you see the name of the table there then it is currency_exchange. However, the bean that we have created is currency exchange.
Similar is for even the column names as well where underscores have been added between words.
In Java, we use camel case but in databases typically underscores are used to separate words and string data JPA understands that, and that's the reason why tables and columns are created with underscores.

Now To insert some data into this specific table, we would be just go to src/main/resources and create a new file named data.sql where we write sql insert query for adding data into our table. Right beside application.properties, we need to save this data.sql file.
So whenever the application is restarted, the data from this file would be loaded into your db.

We want this to be loaded after the tables are created but by default with the latest version of spring boot, the load of data is done before the tables are created.
And that's why we need to make configuration to defer the execution of data.sql by configuring property spring.jpa.defer-datasource-initialization = true.

On going to /h2-console, I can see the data in the db. So we added the dependencies for spring data jpa and H2 in-memory db,
and we made the currency exchange an entity then we populated all the data that we want into data.sql. Next, let's connect our rest API to fetch data from this db.
         */

        String port = environment.getProperty("local.server.port");
        currencyExchange.setEnvironment(port);
        return currencyExchange;
    }
}
