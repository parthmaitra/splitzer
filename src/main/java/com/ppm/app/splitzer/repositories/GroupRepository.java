package com.ppm.app.splitzer.repositories;

import com.ppm.app.splitzer.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GroupRepository extends MongoRepository<Group,String> {
    List<Group> findByMembers(String id);
}
