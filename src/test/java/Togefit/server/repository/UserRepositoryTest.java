package Togefit.server.repository;

import Togefit.server.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest // 스프링 컨테이너와 함께 실행
@Transactional // 테스트 후에 항상 롤백
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void save(){
        User user = new User();
        user.setUserId("testId");
        user.setPassword("123");
        user.setNickname("test");
        user.setName("user");

        userRepository.save(user);
        User result = userRepository.findByUserId(user.getUserId()).get();
        assertThat(user).isEqualTo(result);
    }
}
