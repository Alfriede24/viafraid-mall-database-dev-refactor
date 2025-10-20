package com.wechatmall.api.security.config;


import com.wechatmall.api.security.filter.JwtAuthenticaitonTokenFilter;
import com.wechatmall.api.security.handler.RestFulAccessDenieHandler;
import com.wechatmall.api.security.handler.RestFulAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author zhupengcai
 * @version 1.0
 * @description: 商城后端API鉴权配置
 * @webSite: <a href="https://www.zpcnet.top">MyBlog</a>
 * @copyright ©, 2024-2025, PengCai Zhu<br>
 * This program is protected by copyright laws. <br>
 * @programName: wechat-mall-api <br>
 * @date: 2025/9/22 23:29
 */
@Configuration
public class MallSecurityConfig{
      @Autowired
      private JwtAuthenticaitonTokenFilter jwtAuthenticaitonTokenFilter;

      @Bean
      public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
      }

      @Bean
      public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
          return config.getAuthenticationManager();
      }

      @Bean
      public RestFulAccessDenieHandler restFulAccessDenieHandler(){
          return new RestFulAccessDenieHandler();
      }
      @Bean
      public RestFulAuthenticationEntryPoint  restFulAuthenticationEntryPoint(){
          return new RestFulAuthenticationEntryPoint();
      }

      @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http.csrf(AbstractHttpConfigurer::disable);
          http.authorizeHttpRequests(auth->{
             auth.requestMatchers("/auth/**",
                     "/doc.html",
                     "/swagger-ui.html",
                     "webjars/**",
                     "favicon.ico",
                     "/swagger-resources/**",
                     "/v3/**").permitAll().anyRequest().authenticated();
          });
          http.csrf(AbstractHttpConfigurer::disable);
          http.sessionManagement((session)->{
              session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
          });
          http.exceptionHandling(exception->{
              exception.accessDeniedHandler(restFulAccessDenieHandler())
                      .authenticationEntryPoint(restFulAuthenticationEntryPoint());
          });

          // 过滤配置
          http.addFilterBefore(jwtAuthenticaitonTokenFilter, UsernamePasswordAuthenticationFilter.class);
          return http.build();
      }

}
