package com.program.oss.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig2 {

    //为远程调用构造的请求添加拦截器
    @Bean("requestInterceptor2")
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                System.out.println("feign远程调用之前执行拦截器的 apply方法。。。");
                //最开始访问toTrade方法的请求 然后调用cartFeignClient 远程调用构造请求要执行本拦截器的方法
                //上面那个过程都是在同一个线程里的  所以可以用  threadLocal
                //spring提供了一个RequestContextHolder  可以获得当前请求（一开始访问的请求）的信息
                //RequestContextHolder 里也就是使用  threadLocal
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = requestAttributes.getRequest();///******这里就可以拿到一开始的请求了（老请求）
                //也可以在OrderWebController  的toTrade方法 里直接加一个 HttpServetRequest参数 远程调用的时候直接传这个request参数
                //然后在这里设置新构造请求的请求头  但是这样就麻烦嘛   多整一个参数传过来传过去的
                //直接用  RequestContextHolder 就获取了

                //为远程调用构造的新请求同步老请求的 请求头数据  （登录校验）
                // Cookie[] cookies = request.getCookies();
                String token = request.getHeader("token");
                requestTemplate.header("token",token);

            }
        };
    }
}

