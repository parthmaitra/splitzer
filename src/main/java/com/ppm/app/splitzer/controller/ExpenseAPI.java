package com.ppm.app.splitzer.controller;

import com.ppm.app.splitzer.enums.ExpenseType;
import com.ppm.app.splitzer.model.Expense;
import com.ppm.app.splitzer.repositories.ExpenseRepository;
import com.ppm.app.splitzer.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping(value="/expense")
public class ExpenseAPI {
    @Autowired
    ExpenseRepository expenseRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Expense> getExpense(){
        return expenseRepository.findAll();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Object addExpense(@RequestParam(name = "expenseType") ExpenseType expenseType,@RequestBody LinkedHashMap<String,Object> expenseRequest){
        System.out.println(expenseType);
        Converter.convertGenericExpenseRequest(expenseRequest,expenseType);
        return expenseRequest;
    }
}
