package Togefit.server.controller;

import Togefit.server.domain.User;
import Togefit.server.model.UserInfo;
import Togefit.server.response.OperationResponse;
import Togefit.server.response.UserLoginResponse;
import Togefit.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        response.addCookie(cookie);
        resp.setUserId(userId);
        return resp;
    }

    @GetMapping("/logout")
    @ResponseBody
    public OperationResponse logout(HttpServletResponse response){
        OperationResponse resp = new OperationResponse();

        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        resp.setResult("로그아웃 되었습니다.");
        return resp;
    }

    @GetMapping("/info/{userId}")
    @ResponseBody
    public User getUserInfo(@PathVariable String userId){
        Optional<User> findUser = userService.findOne(userId);
        if(findUser.isEmpty()){
            throw new NoSuchElementException("해당 유저를 찾지 못했습니다.");
        }
        return findUser.get();
    }
}
