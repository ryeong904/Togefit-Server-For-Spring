package Togefit.server.controller;

import Togefit.server.domain.User;
import Togefit.server.model.UserInfo;
import Togefit.server.response.UserLoginResponse;
import Togefit.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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

    @PostMapping("/login")
    @ResponseBody
    public UserLoginResponse login(@RequestBody UserInfo userInfo, HttpServletResponse response){
        UserLoginResponse resp = new UserLoginResponse();
        String userId = userInfo.getUserId();
        String password = userInfo.getPassword();

        String userToken = userService.getUserToken(userId, password);

        Cookie cookie = new Cookie("token", userToken);
        cookie.setMaxAge(1000 * 60 * 60 * 24 * 7);
        cookie.setHttpOnly(true);
        resp.setUserId(userId);
        response.addCookie(cookie);
        return resp;
    }
}
