package seok.springBank.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res,Object handler) {
        String ip = req.getRemoteAddr();
        String uri = req.getRequestURI();
        log.info("ip={},{} request",ip,uri);
        req.setAttribute("ip",ip);
        req.setAttribute("uri",uri);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex){
        String ip = (String)req.getAttribute("ip");
        String uri = (String)req.getAttribute("uri");
        log.info("ip={} {} response",ip,uri);

    }
}
