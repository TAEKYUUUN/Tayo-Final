package com.mysite.tayo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.mysite.tayo.repository.MemberRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 private final MemberRepository memberRepository;

	    public SecurityConfig(MemberRepository memberRepository) {
	        this.memberRepository = memberRepository;
	    }
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	    .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/member/**").permitAll()  // 로그인 없이 접근 가능한 URL 패턴
                .requestMatchers("/", "/mainpage").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()  // 그 외의 모든 URL은 로그인 필요
            )
	        .formLogin((formLogin) -> formLogin
	                .loginPage("/member/login")
	                .successHandler(new CustomAuthenticationSuccessHandler(memberRepository)) // Custom handler 등록
	                )
	        .logout((logout) -> logout
	                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
	                .logoutSuccessUrl("/mainpage")
	                .invalidateHttpSession(true))
	    ;
	    http.csrf(AbstractHttpConfigurer::disable);
	    return http.build();
	}

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(memberRepository);
    }

	
	 @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	
	  @Bean
	    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }
}
