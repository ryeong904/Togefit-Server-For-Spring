package Togefit.server.service;

import Togefit.server.domain.User;
import Togefit.server.repository.JpaUserRepository;
import Togefit.server.response.error.CustomException;
import Togefit.server.response.error.ErrorCode;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Optional;


@Service
@Transactional
public class UserService {
    @Value("${JWT_SECRET_KEY}")
    String JWT_SECRET_KEY;

    private final JpaUserRepository userRepository;

    public UserService(JpaUserRepository userRepository) {
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
                    throw new CustomException(new ErrorCode("이 아이디는 현재 사용중입니다. 다른 아이디를 입력해주세요."));
                });

    }

    public Optional<User> findOne(String userId){
        return userRepository.findByUserId(userId);
    }

    public String getUserToken(String userId, String password){
        Optional<User> findUser = this.findOne(userId);

        if(findUser.isEmpty()){
            throw new CustomException(new ErrorCode("해당 유저를 찾지 못했습니다."));
        }

        boolean isPasswordCorrect = BCrypt.checkpw(password, findUser.get().getPassword());

        if(!isPasswordCorrect){
            throw new CustomException(new ErrorCode("비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요."));
        }

        String nickname = findUser.get().getNickname();

        return makeJwtToken(userId, nickname);
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

    public void deleteUser(String userId, String password){
        Optional<User> findUser = this.findOne(userId);

        if(findUser.isEmpty()){
            throw new CustomException(new ErrorCode("해당 유저를 찾지 못했습니다."));
        }

        boolean isPasswordCorrect = BCrypt.checkpw(password, findUser.get().getPassword());

        if(!isPasswordCorrect){
            throw new InvalidParameterException("비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.");
        }

        userRepository.delete(findUser.get());
    }

    public void updateUser(User user, String currentPassword){

        // 1. 해당 유저가 있는지 찾기(userId)
        // 2. 현재입력한 비밀번호를 받아서 비교함 -> 다를 경우 일치하지 않는 경고문
        // 3. 비밀번호를 바꾸려는 경우에는 (password 조건문 통과시) 해쉬화해서 전달

        Optional<User> findUser = this.findOne(user.getUserId());

        if(findUser.isEmpty()){
            throw new CustomException(new ErrorCode("해당 유저를 찾지 못했습니다."));
        }

        boolean isPasswordCorrect = BCrypt.checkpw(currentPassword, findUser.get().getPassword());


        if(!isPasswordCorrect){
            throw new InvalidParameterException("비밀번호가 일치하지 않습니다. 다시 한 번 확인해주세요.");
        }

        User updateUser = updateUserInfo(findUser.get(), user);
        userRepository.save(updateUser);

    }

    private User updateUserInfo(User user, User updateUser){
        if(updateUser.getPassword() != null){
            String hashedPassword = BCrypt.hashpw(updateUser.getPassword(), BCrypt.gensalt());
            user.setPassword(hashedPassword);
        }
        if(updateUser.getName() != null) user.setName(updateUser.getName());
        if(updateUser.getNickname() != null) {
            user.setNickname(updateUser.getNickname());
        }

        return user;
    }

}
