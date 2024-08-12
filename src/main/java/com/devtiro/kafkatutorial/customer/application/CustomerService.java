package com.devtiro.kafkatutorial.customer.application;

import com.devtiro.kafkatutorial.customer.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerEventsService customerEventsService;

    public Customer save(Customer customer) {
        System.out.println("Received " + customer);
        //this.customerEventsService.publish(customer);
        return customer;

    }

}
