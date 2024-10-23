package com.kass.backend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kass.backend.models.UserModel;
import com.kass.backend.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static com.kass.backend.security.TokenJwtConfig.*;

//CREA EL TOKEN
public class JwtAutheticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private final UserService userService;


    public JwtAutheticationFilter(AuthenticationManager authenticationManager,UserService userService ) {
        this.authenticationManager = authenticationManager;
        this.userService=userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserModel user = null;
        String email = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), UserModel.class);
            email = user.getEmail();
            password = user.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(authRequest);

    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String email = user.getUsername();

        Optional<UserModel> optionalUserModel = userService.findByEmail(email);

        if (optionalUserModel.isPresent()) {
            UserModel userModel = optionalUserModel.get();
            int userId = userModel.getId();
            Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
            Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(roles)).build();

            String token = Jwts.builder().subject(email)
                    .signWith(SECRET_KEY)
                    .claims(claims)
                    .expiration(new Date(System.currentTimeMillis() + 3600000))
                    .issuedAt(new Date())
                    .compact();

            response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

            Map<String, String> body = new HashMap<>();
            body.put("token", token);
            body.put("email", email);
            body.put("userId", String.valueOf(userId)); // Agregar el ID del usuario
            body.put("message", String.format("Hola %s ! , has iniciado sesión con éxito!", email));

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(200);
        } else {
            // Manejo del caso en que no se encuentra el usuario
            Map<String, String> body = new HashMap<>();
            body.put("message", "Usuario no encontrado.");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setContentType(CONTENT_TYPE);
            response.setStatus(404); // O cualquier otro estado adecuado
        }
    }





    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();

        body.put("message", "Error en la autenticacion, email o password incorrectos.");
        body.put("error",failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);

    }
}
