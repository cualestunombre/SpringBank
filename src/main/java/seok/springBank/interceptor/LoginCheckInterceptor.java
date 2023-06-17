package seok.springBank.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import seok.springBank.service.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception{
        HttpSession session = req.getSession(false);
        String requestURI = req.getRequestURI();
        if(session==null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            res.sendRedirect("/auth/login?redirectURL="+requestURI);
            return false;
        }
        return true;
    }

}
