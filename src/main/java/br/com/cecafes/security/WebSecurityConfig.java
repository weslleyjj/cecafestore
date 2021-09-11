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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                .antMatchers("/").permitAll()
                .antMatchers("/busca-produto/**").permitAll()
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
                .antMatchers("/pedido/pedidos").hasAnyAuthority("FUNCIONARIO", "ADMIN", "COMPRADOR")
                .antMatchers("/pedido/pedidos/**").hasAnyAuthority("FUNCIONARIO", "ADMIN", "COMPRADOR")
                .antMatchers("/pedido/revisar/**").hasAnyAuthority("FUNCIONARIO", "ADMIN")
                .antMatchers("/pedido/pedidos-em-aberto").hasAnyAuthority("FUNCIONARIO", "ADMIN")
                .antMatchers("/pedido/*").hasAnyAuthority("FUNCIONARIO", "ADMIN", "COMPRADOR")
                .and()
                .formLogin().loginPage("/login")
                .successHandler(sucessoLogin())
                .permitAll()
                .and()
                .logout().invalidateHttpSession(true).logoutSuccessUrl("/").deleteCookies("JSESSIONID", "usuario")
                .and()
                .exceptionHandling().accessDeniedPage("/403");
    }

    private AuthenticationSuccessHandler sucessoLogin(){
        AuthenticationSuccessHandler atsh = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                Cookie c = new Cookie("usuario", authentication.getName());
                response.addCookie(c);
                response.sendRedirect("/");
            }
        };

        return atsh;
    }
}
