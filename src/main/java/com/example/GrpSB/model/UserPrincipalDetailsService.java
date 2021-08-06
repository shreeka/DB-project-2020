package com.example.GrpSB.model;

import com.example.GrpSB.dao.UserPrincipal;
import com.example.GrpSB.repo.UserRepository;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        if (user != null) {
            DateTime dt = new DateTime();
            List<GrantedAuthority> authorities = new ArrayList<>();
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRoles());
            authorities.add(authority);
            UserPrincipal userPrincipal = new UserPrincipal(user.getUsername(), user.getPassword(), user.getAddress(), user.getLastLogin(), authorities);
            userRepository.updateLastLoggedIn(user.getId(), dt.toString());
            return userPrincipal;
        } else {
            throw new UsernameNotFoundException("user not found");
        }
    }
}
