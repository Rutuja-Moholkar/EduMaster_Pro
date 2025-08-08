package com.edumaster.security;

import com.edumaster.model.User;
import com.edumaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom UserDetailsService Implementation
 * 
 * This service loads user details from our database for Spring Security authentication.
 * It converts our User entity into Spring Security's UserDetails format.
 * 
 * Key Features:
 * - Loads user by email (username in our system)
 * - Converts user roles to Spring Security authorities
 * - Handles account status (active/inactive, verified/unverified)
 * 
 * @author EduMaster Team
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Load user details by username (email in our case)
     * 
     * @param username Email address of the user
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return UserPrincipal.create(user);
    }

    /**
     * Load user details by user ID
     * 
     * @param id User ID
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if user not found
     */
    @Transactional
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return UserPrincipal.create(user);
    }
}