package seok.springBank.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Service
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String queryString = request.getQueryString();
        if (StringUtils.hasText(queryString)) {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap.containsKey("redirectURL")) {
                response.sendRedirect(request.getRequestURI()+"?redirectURL="
                        +parameterMap.get("redirectURL")[0]+"&error=true");
            }

            else{
                response.sendRedirect(request.getRequestURL().toString()+"?error=true");
            }
        }
        else{
            response.sendRedirect(request.getRequestURL().toString()+"?error=true");
        }

    }
}
