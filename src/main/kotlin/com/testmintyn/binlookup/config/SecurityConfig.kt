package com.testmintyn.binlookup.config

import com.testmintyn.binlookup.service.impl.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var userService: UserServiceImpl
    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(UserDetailsService { email: String? ->
            userService.loadUserByUsername(email ?: "")
        }).passwordEncoder(passwordEncoder)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .cors().disable()
            .authorizeRequests()
            .antMatchers("/api/v1/users/register", "/error")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .headers().frameOptions().disable() // Only if using H2 console for development
            .and()
            .exceptionHandling().authenticationEntryPoint { request, response, authException ->
                authException.printStackTrace()
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
            }
    }

    @Throws(java.lang.Exception::class)
    override fun configure(web: WebSecurity) {
        web
            .ignoring()
            .antMatchers(HttpMethod.POST, "/api/v1/users/register")
            .antMatchers(HttpMethod.GET, "/error")
    }

}