package Togefit.server.controller;

import Togefit.server.domain.User.User;
import Togefit.server.model.UserInfo;
import Togefit.server.response.OperationResponse;
import Togefit.server.response.UserLoginResponse;
import Togefit.server.response.error.CustomException;
import Togefit.server.response.error.Error;
import Togefit.server.service.AwsS3Service;
import Togefit.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequestMapping("/users")
@RestController
public class UserController {

    final private UserService userService;
    final private AwsS3Service awsS3Service;

    @Autowired
    public UserController(UserService userService, AwsS3Service awsS3Service){
        this.userService = userService;
        this.awsS3Service = awsS3Service;
    }

    @PostMapping("/")
    public String create(@RequestBody User newUser){
        return userService.join(newUser);
    }

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserInfo userInfo, HttpServletResponse response){
        UserLoginResponse resp = new UserLoginResponse();
        String userId = userInfo.getId();
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
    public OperationResponse logout(HttpServletResponse response){
        OperationResponse resp = new OperationResponse();
        deleteCookie(response);
        resp.setResult("로그아웃 되었습니다.");
        return resp;
    }

    @GetMapping("/{userId}")
    public User getUserInfo(@PathVariable String userId){
        Optional<User> findUser = userService.findOne(userId);
        if(findUser.isEmpty()){
            throw new CustomException(new Error("해당 유저를 찾지 못했습니다."));
        }
        return findUser.get();
    }

    @DeleteMapping("/")
    public OperationResponse userUnregister(@RequestBody UserInfo userInfo, HttpServletRequest request, HttpServletResponse response){
        OperationResponse resp = new OperationResponse();

        String userId = (String) request.getAttribute("userId");
        String password = userInfo.getPassword();

        userService.deleteUser(userId, password);
        deleteCookie(response);
        //ToDo 1. 회원 탈퇴시 liked 테이블에서도 삭제

        resp.setResult("정상적으로 회원 탈퇴 되었습니다.");
        return resp;
    }

    @PatchMapping("/")
    public OperationResponse userUpdate(
            @ModelAttribute User user,
            @RequestParam String currentPassword,
            @RequestPart(value = "profile")MultipartFile multipartFile,
            HttpServletRequest request) throws IOException {
        OperationResponse resp = new OperationResponse();
        String userId = (String) request.getAttribute("userId");

        if(!multipartFile.isEmpty()){
            user.setProfile_image(awsS3Service.uploadFile(multipartFile));
        }

        user.setUserId(userId);
        userService.updateUser(user, currentPassword);
        resp.setResult("회원 정보가 정상적으로 수정되었습니다.");
        return resp;
    }

    private void deleteCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
