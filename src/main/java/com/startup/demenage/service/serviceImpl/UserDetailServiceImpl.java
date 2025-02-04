package com.startup.demenage.service.serviceImpl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.startup.demenage.domain.UserDomain;
import com.startup.demenage.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

  private UserRepository userRepo;

  public UserDetailServiceImpl(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (Strings.isBlank(username)) {
      throw new UsernameNotFoundException("Invalid user.");
    }
    final String uname = username.trim();
    Optional<UserDomain> oUserEntity = userRepo.findByUsername(uname);
    UserDomain userEntity = oUserEntity.orElseThrow(
        () -> new UsernameNotFoundException(String.format("Given user(%s) not found.", uname)));
    return User.builder()
        .username(userEntity.getUsername())
        .password(userEntity.getPassword())
        .authorities(userEntity.getRole().getAuthority())
        .build();
  }
}