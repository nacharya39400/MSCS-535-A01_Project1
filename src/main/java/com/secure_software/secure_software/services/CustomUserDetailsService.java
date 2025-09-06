package com.secure_software.secure_software.services;

import com.secure_software.secure_software.dao.UserDAO;
import com.secure_software.secure_software.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * CustomUserDetailsService
 */
@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User appUser = userDAO.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                appUser.getEmail(),
                appUser.getPassword(),
                getAuthority(appUser));
    }

    private Collection<? extends GrantedAuthority> getAuthority(User user) {
        List<GrantedAuthority> authority = new ArrayList<>();
        return authority;
    }

}
