package Togefit.server.service;

import Togefit.server.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void 회원가입() {
        // given
        User user = new User();
        user.setUserId("userId");
        user.setName("test");
        user.setNickname("nickname");
        user.setPassword("123");

        // when
        String userID = userService.join(user);

        // then
        User findUser = userService.findOne(user.getUserId()).get();
        assertThat(user.getUserId()).isEqualTo(findUser.getUserId());
    }

    @Test
    void 중복회원예외(){
        User user1 = new User();
        user1.setUserId("same");

        User user2 = new User();
        user2.setUserId("same");

        userService.join(user1);
        IllegalStateException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
                () -> userService.join(user2));

        assertThat(e.getMessage()).isEqualTo("이 아이디는 현재 사용중입니다. 다른 아이디를 입력해주세요.");
    }
}
