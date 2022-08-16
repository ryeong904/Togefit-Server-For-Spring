package Togefit.server.service;

import Togefit.server.domain.User;
import Togefit.server.model.UserToken;
import Togefit.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String join(User user){
        validateUser(user);
        // password 해쉬화
        // user의 패스워드 변경해주기
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return user.getUserId();
    }

    private void validateUser(User user){
        userRepository.findByUserId(user.getUserId())
                .ifPresent(u -> {
                    throw new IllegalStateException("이 아이디는 현재 사용중입니다. 다른 아이디를 입력해주세요.");
                });

    }

    public Optional<User> findOne(String userId){
        return userRepository.findByUserId(userId);
    }

//    public UserToken getUserToken(String userId, String password){
//        if(findOne(userId) == null){
//            throw new IllegalStateException("해당 유저를 찾지 못했습니다.");
//        }
//
//
//    }
}
