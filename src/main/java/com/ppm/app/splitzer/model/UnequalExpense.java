package com.ppm.app.splitzer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

public class UnequalExpense extends Expense {
    HashMap<String,Float> share = new HashMap<>();

    public UnequalExpense(Expense expense) {
    }
}
