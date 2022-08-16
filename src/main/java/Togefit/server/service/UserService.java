package Togefit.server.service;

import Togefit.server.domain.User;
import Togefit.server.repository.UserRepository;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Value("${JWT_SECRET_KEY}")
    String JWT_SECRET_KEY;

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

    public String getUserToken(String userId, String password){
        Optional<User> findUser = this.findOne(userId);

        if(findUser.isEmpty()){
            throw new NoSuchElementException("해당 유저를 찾지 못했습니다.");
        }

        boolean isPasswordCorrect = BCrypt.checkpw(password, findUser.get().getPassword());

        if(isPasswordCorrect == false){
            throw new InvalidParameterException("비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.");
        }

        String nickname = findUser.get().getNickname();
        String accessToken = makeJwtToken(userId, nickname);

        return accessToken;
    }

    private String makeJwtToken(String userId, String nickname) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuedAt(now)
                .claim("userId", userId)
                .claim("nickname", nickname)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }
}
