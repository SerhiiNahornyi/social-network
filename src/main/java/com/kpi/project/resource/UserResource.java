package com.kpi.project.resource;

import com.kpi.project.model.dto.UserDto;
import com.kpi.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/registration")
    public ResponseEntity<UserDto> showRegistrationForm(@RequestBody UserDto userDto) {

        return ResponseEntity.ok(userService.saveUser(userDto));
    }

    @PutMapping("/user/update/roles")
    public ResponseEntity<UserDto> updateUsersRole(@RequestBody UserDto userDto) {
        final UserDto user = userService.updateUserRoles(userDto);

        return ResponseEntity.ok(user);
    }
}
