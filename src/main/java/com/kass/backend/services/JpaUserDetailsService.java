package com.kass.backend.services;

import com.kass.backend.models.UserModel;
import com.kass.backend.repositories.IUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final IUser iuser;

    public JpaUserDetailsService(IUser iuser) {
        this.iuser = iuser;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserModel> userModelOptional = iuser.findByEmail(email);
        if (userModelOptional.isEmpty()) {
            throw new UsernameNotFoundException(String.format("El usuario con el email %s no fue encontrado.", email));
        }

        UserModel userModel = userModelOptional.orElseThrow();

        List<GrantedAuthority> grantedAuthorities = userModel.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toUnmodifiableList());


        boolean isEnabled = Boolean.TRUE.equals(userModel.getEnabled());

        return new User(userModel.getEmail(),
                userModel.getPassword(),
                isEnabled,
                true,
                true,
                true,
                grantedAuthorities);
    }




}
