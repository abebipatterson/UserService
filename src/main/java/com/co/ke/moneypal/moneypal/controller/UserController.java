package com.co.ke.moneypal.moneypal.controller;

import com.co.ke.moneypal.moneypal.model.User;
import com.co.ke.moneypal.moneypal.service.UserService;
import com.co.ke.moneypal.moneypal.wrapper.GeneralResponseWrapper;
import com.co.ke.moneypal.moneypal.wrapper.LoginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<GeneralResponseWrapper> createUser(@RequestBody User user){
        GeneralResponseWrapper generalResponseWrapper=userService.createUser(user);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

    @GetMapping("/login")
    public ResponseEntity<GeneralResponseWrapper> login(@RequestBody LoginWrapper loginWrapper){
        GeneralResponseWrapper generalResponseWrapper=userService.userLogin(loginWrapper);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

    @GetMapping("/validateUserName/{userName}")
    public ResponseEntity<GeneralResponseWrapper> checkUserName(@PathVariable String userName){
        GeneralResponseWrapper generalResponseWrapper=userService.checkUserUserName(userName);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }

    @GetMapping("/getUser/{emailAddress}")
    public ResponseEntity<GeneralResponseWrapper> getUserByEmail(@PathVariable String emailAddress){
        GeneralResponseWrapper generalResponseWrapper=userService.userByEmail(emailAddress);
        return ResponseEntity.ok().body(generalResponseWrapper);
    }
}
