package com.in28minutes.microservices.currencyexchangeservice;

import org.springframework.data.jpa.repository.JpaRepository;
/*
Let's now update our rest API to connect to the database. And to be able to do that we need to create a spring data class called repository.
So we created a repository to manage the specific entity - currency exchange.
So we have created a public interface CurrencyExchangeRepository and extend a specific interface called JpaRepository.

And to this I would need to define two things in generics - the entity to be managed and he primary key ?
So the Entity we want to manage is CurrencyExchange and add Long as the type of primary key defined in our Entity class.

Just creating this interface is more than sufficient for us to be able to talk to the database. Now, I would want to use this from our CurrencyExchangeController.
And over there autowire an instance of this like "private CurrencyExchangeRepository repository".
 */
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
        CurrencyExchange findByFromAndTo(String from, String to);
}