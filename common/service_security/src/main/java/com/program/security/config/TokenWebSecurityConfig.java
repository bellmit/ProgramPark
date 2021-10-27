package com.program.security.config;


import com.program.security.filter.TokenAuthenticationFilter;
import com.program.security.filter.TokenLoginFilter;
import com.program.security.security.DefaultPasswordEncoder;
import com.program.security.security.TokenLogoutHandler;
import com.program.security.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    private UserDetailsService userDetailsService;
    private DefaultPasswordEncoder defaultPasswordEncoder;




    @Autowired
    public TokenWebSecurityConfig(TokenManager tokenManager, RedisTemplate redisTemplate
    , UserDetailsService userDetailsService ,DefaultPasswordEncoder defaultPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.defaultPasswordEncoder = defaultPasswordEncoder;
//        providerManager.setEraseCredentialsAfterAuthentication(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin() // 表单登录
//                .loginPage("/on.html")
//                .loginProcessingUrl("/user/login")   //登录访问路径
//                .successForwardUrl("/success.html").permitAll() // 登录成功之后跳转到哪个 url
//                .failureUrl("/fail.html")// 登录失败之后跳转到哪个 url
        http.authorizeRequests() // 认证配置
                .anyRequest() // 任何请求
                .authenticated()// 都需要身份验证
         .and().csrf().disable();  //关闭csrf防护
        http.logout().logoutUrl("/ProgramPark/user/logout")
                .addLogoutHandler(new TokenLogoutHandler(tokenManager,redisTemplate)).and()
        .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))
                .addFilter(new TokenAuthenticationFilter(authenticationManager(), tokenManager, redisTemplate)).httpBasic();

    }

    /**
     * 密码处理
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);

    }

    /**
     * 配置哪些请求不拦截
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","/on.html"
                ,"/fail.html","/success.html","/**/article/**","/index.html","/ProgramPark/oss/getUrlsById/**"
                ,"/ProgramPark/user/getUserById/**"
//                ,"/**"
               );

//        web.ignoring().antMatchers("/*/**"
//        );
    }

    //允许多请求地址多加斜杠  比如 /msg/list   //msg/list
    @Bean
    public HttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }

}
