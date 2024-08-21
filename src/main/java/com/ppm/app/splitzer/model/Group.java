package com.ppm.app.splitzer.model;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
@Document(collection = "group")
@Data
@Getter
@Setter
public class Group {
    @Id
    String id;
    ArrayList<String> members;
    ArrayList<String> expenses;
}
