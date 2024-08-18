package com.ppm.app.splitzer.controller;

import com.ppm.app.splitzer.model.User;
import com.ppm.app.splitzer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/user")
public class UserAPI {
    @Autowired
    private UserRepository userRepository;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> getUsers() {
        List<User> userList = null;
        return userRepository.findAll();
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public User addUser(@RequestBody User user){
        return userRepository.save(user);
    }
}
