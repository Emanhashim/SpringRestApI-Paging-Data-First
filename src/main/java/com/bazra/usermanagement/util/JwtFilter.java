package com.bazra.usermanagement.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bazra.usermanagement.model.UserInfo;
import com.bazra.usermanagement.model.UserInfoService;
import com.bazra.usermanagement.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserInfoService userInfoService;
    
    @Autowired
    UserRepository userRepository;
    
    @Value("${jwt.jwtsecret}")
    private String SECRET_KEY;
    
    @Autowired
    private JwtUtil jwtUtil;
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            // parse the token.
            String user;
            try {
                user = Jwts.parser()
                        .setSigningKey(SECRET_KEY.getBytes())
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody()
                        .getSubject();
            } catch (SignatureException e) {
                return null;
            }


            // Set it
            if (user != null) return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

            return null;
        }
        return null;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.getUserNameFromJwtToken(jwt);
           
            UserDetails userDetails = userInfoService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            
            
            
        }
    }catch(Exception e)
    {
            logger.error("Cannot set user authentication: {}", e);
        }
        chain.doFilter(request, response);
    }
       
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

}
