// package com.fastbin.app.Config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// // import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.fastbin.app.security.JwtAuthenticationEntryPoint;

// @Configuration
// @EnableWebSecurity
// public class WebSecurityConfig {


//     @Autowired
//     private JwtAuthenticationEntryPoint point;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//         http.csrf(csrf -> csrf.disable())
//         .cors(cors -> cors.disable())
//         .authorizeHttpRequests(auth -> 
//                 auth
//                 .requestMatchers("/api/auth/**").authenticated()
                
//                 .anyRequest().permitAll()
//                 // .requestMatchers("/**").authenticated()
                
//                 // .requestMatchers("/auth/login","/auth/register").permitAll()

//                 // .anyRequest().authenticated()
                
//         )
//         .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
//         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//         return http.build();
//     }


// }