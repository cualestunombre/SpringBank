package seok.springBank.config.HandlerExceptionResolver;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//HandlerExceptionResolver 예외를 잡아서 500번대를 400번대로 바꿔 줌 -> 렌더링만
//JSON의 경우 ControllAdvice가 우선순의를 가짐
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse response,
                                          Object handler, Exception ex){
        try{
            ex.printStackTrace();
            if(ex instanceof IllegalArgumentException) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
            }

        }catch(IOException e){

        }

        return null;
    }
}
