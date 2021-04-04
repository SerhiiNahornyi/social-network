package com.kpi.project.resource;

import com.kpi.project.model.dto.UserDto;
import com.kpi.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PutMapping("/user/add/friend")
    public ResponseEntity<UserDto> addUserFriends(@RequestBody UserDto userDto) {
        final SecurityContext context = SecurityContextHolder.getContext();
        final Authentication authentication = context.getAuthentication();
        final String username = authentication.getName();

        final UserDto user = userService.addUserFriend(username, userDto.getFriend());

        return ResponseEntity.ok(user);
    }

    @PutMapping("/user/change/password")
    public ResponseEntity<UserDto> changeUsersPassword(@RequestBody UserDto userDto) {
        final UserDto user = userService.changeUserPassword(userDto);

        return ResponseEntity.ok(user);
    }
}
