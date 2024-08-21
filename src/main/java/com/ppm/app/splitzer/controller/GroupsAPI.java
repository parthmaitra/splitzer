package com.ppm.app.splitzer.controller;

import com.ppm.app.splitzer.model.Group;
import com.ppm.app.splitzer.model.User;
import com.ppm.app.splitzer.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/groups")
public class GroupsAPI {
    @Autowired
    GroupRepository groupRepository;

    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    public List<Group> getGroupsById(@PathVariable String id) {
        return groupRepository.findAll();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Group createGroup(@RequestBody Group group) {
        return groupRepository.save(group);
    }
}
