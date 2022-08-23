package Togefit.server.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{
    @Value("${JWT_SECRET_KEY}")
    String JWT_SECRET_KEY;

    private boolean checkUserHttpMethod(String method) {
        if(method.equals("PATCH") || method.equals("DELETE")){
            return true;
        }else{
            return false;
        }
    }

    @Override
    @ResponseBody
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        String uri = request.getRequestURI();

        String method = request.getMethod();
        if(uri.equals("/users/")){
            if(checkUserHttpMethod(method) == false){
                return true;
            }
        }

        String token = extractToken(request.getCookies()).trim();

        // 토큰이 없으면 false 리턴
        if(token.equals("")){
            setResonse(response);
            return false;
        }

        // 유효성 검사
        Claims claims = getClaims(response, token);
        if(claims == null){
            setResonse(response);
            return false;
        }

        String userId = (String) claims.get("userId");
        String nickname = (String) claims.get("nickname");
        request.setAttribute("userId", userId);
        request.setAttribute("nickname", nickname);
        return true;
    }

    private Claims getClaims(HttpServletResponse response, String token){
        try{
            return Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String extractToken(Cookie[] cookies){
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void setResonse(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
                .write("{\"result\":\"forbidden-approach\",\"reason\":\"로그인한 유저만 사용할 수 있는 서비스입니다.\"}");
    }
}