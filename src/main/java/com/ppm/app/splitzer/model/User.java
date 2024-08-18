package com.ppm.app.splitzer.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "user")
@Data
@Getter
@Setter
public class User {
    @Id
    String id;
    @Indexed(unique = true)
    String username;
    String password;
    String firstName;
    String lastName;
}
