package com.ppm.app.splitzer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "expense")
@Data
@Getter
@Setter
public class Expense {
    @Id
    String id;
    ArrayList<String> payerList;
    ArrayList<String> payeeList;
    float amount;

    Expense(Expense expense){
        this.payeeList=expense.getPayeeList();
        this.payerList=expense.getPayerList();
        this.amount=expense.getAmount();
    }
}
