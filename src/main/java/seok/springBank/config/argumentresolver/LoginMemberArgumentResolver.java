package seok.springBank.config.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import seok.springBank.domain.member.Member;
import seok.springBank.service.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


// @Login annotation을 처리하는 ArgumentResolver
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest req,
        WebDataBinderFactory binderFactory){
        HttpServletRequest request = (HttpServletRequest) req.getNativeRequest();
        HttpSession session = request.getSession(false);
        if(session==null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
