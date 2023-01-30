package com.in28minutes.microservices.currencyexchangeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
Following dependencies are added while creating this project from Spring Initializer -

1. Spring Boot DevTools
2. Spring Boot Actuator - It lets us monitor and manage our application.
3. Spring Web - As we would want to build a REST Api.
4. Config Client (Spring Cloud Config) - As it is useful for us to talk to a Cloud Config Server

When we first launch this CurrencyExchangeService application, it would say that Port 8080 is already in use because it's been used by the other services.
For the Currency Exchange Microservice, we would want to use ports 8000, 8001, 8002 and so on. So we'll start with using port 8000. So we've configured
port number 8000 in our application.properties for the currency exchange service. We also configure the name of this application as "currency-exchange".

When launching a lot of components so the names and the ports become very important.

I'm able to launch this up properly and in the next steps we would focus on the currency exchange and the currency conversion services.
So I would actually go and terminate the applications for the other two - limits-service and the spring cloud conflict server from our workspace.
So the only application that I have running right now is currency exchange service. So let's create our REST API in currency exchange service.
 */
@SpringBootApplication
public class CurrencyExchangeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeServiceApplication.class, args);
	}

}
