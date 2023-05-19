package seok.springBank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import seok.springBank.config.argumentresolver.LoginMemberArgumentResolver;
import seok.springBank.interceptor.LoginCheckInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
        resolvers.add(new LoginMemberArgumentResolver());

    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(0);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/","/auth/login","/auth/signup",
                        "/auth/logout","/css/**","/*.ico","/error");
    }
}
