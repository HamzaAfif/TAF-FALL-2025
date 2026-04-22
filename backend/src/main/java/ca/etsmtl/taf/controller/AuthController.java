package ca.etsmtl.taf.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.etsmtl.taf.entity.ERole;
import ca.etsmtl.taf.entity.Role;
import ca.etsmtl.taf.entity.User;
import ca.etsmtl.taf.payload.request.LoginRequest;
import ca.etsmtl.taf.payload.request.SignupRequest;
import ca.etsmtl.taf.payload.response.JwtResponse;
import ca.etsmtl.taf.payload.response.MessageResponse;
import ca.etsmtl.taf.repository.RoleRepository;
import ca.etsmtl.taf.repository.UserRepository;
import ca.etsmtl.taf.security.jwt.JwtUtils;
import ca.etsmtl.taf.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth/api")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();

    List<String> roles = user.getRoles().stream()
        .map(role -> role.getName().name())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
        userDetails.getId(),
        userDetails.getFullName(),
        userDetails.getUsername(),
        userDetails.getEmail(),
        roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(
        signUpRequest.getFullName(),
        signUpRequest.getUsername(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null || strRoles.isEmpty()) {
      roles.add(getOrCreateRole(ERole.ROLE_USER));
    } else {
      for (String role : strRoles) {
        if ("admin".equalsIgnoreCase(role) || "ROLE_ADMIN".equalsIgnoreCase(role)) {
          roles.add(getOrCreateRole(ERole.ROLE_ADMIN));
        } else {
          roles.add(getOrCreateRole(ERole.ROLE_USER));
        }
      }
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  private Role getOrCreateRole(ERole roleName) {
    return roleRepository.findByName(roleName)
        .orElseGet(() -> roleRepository.save(new Role(roleName)));
  }
}
