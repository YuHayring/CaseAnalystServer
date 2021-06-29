package cn.hayring.detecttool.config;

import cn.hayring.detecttool.web.authentication.RestLoginFailureHandler;
import cn.hayring.detecttool.web.authentication.RestLoginSuccessHandler;
import cn.hayring.detecttool.web.authentication.RestLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()

                //静态资源
                .antMatchers("/js/**", "/layui/**", "/login.js","/*.png").permitAll()
                //登录
                .antMatchers( "/investigator/session", "/register").permitAll()


                .anyRequest().authenticated()
                .and()

                .formLogin().loginPage("/login.html")
                .loginProcessingUrl("/investigator/session")
                .successHandler(new RestLoginSuccessHandler())
                .failureHandler(new RestLoginFailureHandler())
                .permitAll()

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/investigator/session", "DELETE"))
                // 登出成功的处理器
                .logoutSuccessHandler(new RestLogoutSuccessHandler())




                .and()
                .csrf().disable();

        http.headers().frameOptions().disable();

    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(investigatorService)// 设置自定义的userDetailsService
                .passwordEncoder(bCryptPasswordEncoder);

    }

    @Autowired
    private UserDetailsService investigatorService;

    @Deprecated
    @Bean
    public PasswordEncoder NoOpPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }




}