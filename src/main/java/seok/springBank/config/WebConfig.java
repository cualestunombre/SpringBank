package seok.springBank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import seok.springBank.config.HandlerExceptionResolver.MyHandlerExceptionResolver;
import seok.springBank.config.argumentresolver.LoginMemberArgumentResolver;
import seok.springBank.interceptor.LogInterceptor;
import seok.springBank.interceptor.LoginCheckInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    @Autowired
    private LogInterceptor logInterceptor;

    //argumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(new LoginMemberArgumentResolver());

    }


    //정적파일 제공 로직
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(100);
            // cache-control:max-age=0;
            // 정적파일에 대해서
    }


    //인터셉터 추가 로직
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginCheckInterceptor)
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/","/auth/login","/auth/signup",
                        "/auth/logout","/css/**","/*.ico","/error","/test/**","/auth/created/**","/auth/member/**");
        registry.addInterceptor(logInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/*.ico");
    }
    // HandlerExceptionResolver 등록
    @Override public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers){
        resolvers.add(new MyHandlerExceptionResolver());
    }

}
