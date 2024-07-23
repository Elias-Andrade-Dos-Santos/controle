package stoque.controle.infrastructure.configuration;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import stoque.controle.domain.entitys.userEntity.User;
import stoque.controle.domain.repositories.UserRepository;
import stoque.controle.infrastructure.TokenJWT;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenJWT tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token != null){
            var email = tokenService.validateToken(token);
            Optional<User> user = userRepository.findByEmail(email);
            if(!user.isEmpty()){
            var authentication = new UsernamePasswordAuthenticationToken(user, null,user.get().getAuthorities() );
            SecurityContextHolder.getContext().setAuthentication(authentication);}
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}