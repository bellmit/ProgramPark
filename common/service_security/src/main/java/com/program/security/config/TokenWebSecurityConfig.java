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
//        http.formLogin() // ????????????
//                .loginPage("/on.html")
//                .loginProcessingUrl("/user/login")   //??????????????????
//                .successForwardUrl("/success.html").permitAll() // ????????????????????????????????? url
//                .failureUrl("/fail.html")// ????????????????????????????????? url
        http.authorizeRequests() // ????????????
                .anyRequest() // ????????????
                .authenticated()// ?????????????????????
         .and().csrf().disable();  //??????csrf??????
        http.logout().logoutUrl("/ProgramPark/user/logout")
                .addLogoutHandler(new TokenLogoutHandler(tokenManager,redisTemplate)).and()
        .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))
                .addFilter(new TokenAuthenticationFilter(authenticationManager(), tokenManager, redisTemplate)).httpBasic();

    }

    /**
     * ????????????
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);

    }

    /**
     * ???????????????????????????
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

    //?????????????????????????????????  ?????? /msg/list   //msg/list
    @Bean
    public HttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }

}
