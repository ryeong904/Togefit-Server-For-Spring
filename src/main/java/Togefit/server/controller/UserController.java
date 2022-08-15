package Togefit.server.controller;

import Togefit.server.domain.User;
import Togefit.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseBody
    public String create(@RequestBody User newUser){
        return userService.join(newUser);
    }
}
