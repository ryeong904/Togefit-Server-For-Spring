package Togefit.server.service;

import Togefit.server.domain.User;
import Togefit.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String join(User user){
        validateUser(user);
        userRepository.save(user);
        return user.getUserId();
    }

    public String test(){
        return "test";
    }

    private void validateUser(User user){
        userRepository.findByUserId(user.getUserId())
                .ifPresent(u -> {
                    throw new IllegalStateException("이 아이디는 현재 사용중입니다. 다른 아이디를 입력해주세요.");
                });

    }
}
