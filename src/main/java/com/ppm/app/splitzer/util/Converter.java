package com.ppm.app.splitzer.util;

import com.ppm.app.splitzer.enums.ExpenseType;
import com.ppm.app.splitzer.model.Expense;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Converter {
    public static void convertGenericExpenseRequest(LinkedHashMap<String,Object> expenseRequest,
                                                    ExpenseType expenseType){
        switch (expenseType) {
            case EQUAL -> {
                Expense expense = new Expense();
                for (Map.Entry entry:expenseRequest.entrySet()) {
                    switch ((String) entry.getKey()){
                        case "payee" -> {

                        }
                    }
                }
            }
        }
    }
}
