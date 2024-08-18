package com.ppm.app.splitzer.repositories;

import com.ppm.app.splitzer.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
