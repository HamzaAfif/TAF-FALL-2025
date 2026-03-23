package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.payload.request.SignupRequest;
import ca.etsmtl.taf.payload.request.TestApiRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
  @GetMapping("/all")
  public String allAccess() {
    return "Bienvenue au TAF.";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public String userAccess() {
    return "User Content.";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminAccess() {
    return "Admin Board.";
  }

  public String normalizeMessage(String message) {
    if (message == null) {
      return "";
    }
    String normalized = message.trim();
    if (normalized.isEmpty()) {
      return "";
    }
    return normalized.replaceAll("\\s+", " ");
  }

  public boolean hasAdminAccess(String role) {
    if (role == null) {
      return false;
    }
    String normalizedRole = role.trim().toUpperCase();
    return "ADMIN".equals(normalizedRole) || "ROLE_ADMIN".equals(normalizedRole);
  }
}
