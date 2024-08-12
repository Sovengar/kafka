package com.devtiro.kafkatutorial.customer.events;

import com.devtiro.kafkatutorial.customer.domain.Customer;
import com.devtiro.kafkatutorial.kafka.events.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerCreatedEvent extends Event<Customer> {

}
