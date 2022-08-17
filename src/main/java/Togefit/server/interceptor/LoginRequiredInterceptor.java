package Togefit.server.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{
    @Value("${JWT_SECRET_KEY}")
    String JWT_SECRET_KEY;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {

        request.getCookies();

        String token = extractToken(request.getCookies());

        // 토큰이 없거나 유효하지 않으면 false 리턴
        if(token == null || token == ""){
            System.out.println("token is null");
            return false;
        }

        // 유효성 검사 코드 추가해야함
        Claims claims = getClaims(token);
        String userId = (String) claims.get("userId");
        String nickname = (String) claims.get("nickname");
        request.setAttribute("userId", userId);
        request.setAttribute("nickname", nickname);
        return true;
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractToken(Cookie[] cookies){
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null)
                .trim();
    }
}