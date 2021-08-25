package br.com.cecafes.security;

import br.com.cecafes.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Method;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${auth-api.username}")
    private String username;
    @Value("${auth-api.password}")
    private String password;

    // Configurações para roles e autenticação
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        http.authorizeRequests()
//                .antMatchers("/").hasAnyAuthority("PRODUTOR", "COMPRADOR", "FUNCIONARIO", "ADMIN")
                .antMatchers("/").permitAll()
                .antMatchers("/cadastro-sistema").permitAll()
                .antMatchers("/loja").permitAll()
                .antMatchers("/produtor/form-produtor").permitAll()
                .antMatchers("/produtor/cadastrar").permitAll()
                .antMatchers("/produtor/**").hasAnyAuthority("PRODUTOR", "ADMIN")
                .antMatchers("/produto/**").hasAnyAuthority("PRODUTOR", "ADMIN")
                .antMatchers("/comprador/form-comprador").permitAll()
                .antMatchers("/comprador/cadastrar").permitAll()
                .antMatchers("/comprador/**").hasAnyAuthority("COMPRADOR", "ADMIN")
                .antMatchers("/funcionario/**").hasAnyAuthority("FUNCIONARIO", "ADMIN")
                .antMatchers(HttpMethod.POST, "/pedido").hasAnyAuthority("COMPRADOR")
                .antMatchers("/pedido/gerencia-pedidos").hasAnyAuthority("FUNCIONARIO", "ADMIN", "COMPRADOR")
//                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().invalidateHttpSession(true).logoutSuccessUrl("/").deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }
}
