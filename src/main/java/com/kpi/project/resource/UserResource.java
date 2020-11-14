package com.kpi.project.resource;

import com.kpi.project.model.dto.UserDto;
import com.kpi.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping("/user/registration")
    public ResponseEntity<UserDto> showRegistrationForm(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.saveUser(userDto));
    }
}
