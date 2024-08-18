package com.ppm.app.splitzer.repositories;

import com.ppm.app.splitzer.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExpenseRepository extends MongoRepository<Expense,String> {
}
