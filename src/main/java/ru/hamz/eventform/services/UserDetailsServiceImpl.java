package ru.hamz.eventform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hamz.eventform.models.ERole;
import ru.hamz.eventform.models.User;
import ru.hamz.eventform.repos.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.
                findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No such user with user email: " + username));
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new NoSuchElementException("No such user with email: " + email));
    }

    public User findByRole(ERole role) {
        Optional<User> userByRole = userRepository.findByRole(role);
        return userByRole.orElseThrow(() -> new NoSuchElementException("No user with role "  + role));
    }


    public boolean saveUser(User user) {
        if (userRepository.existsById(user.getId())) {
            return false;
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
    }
}
