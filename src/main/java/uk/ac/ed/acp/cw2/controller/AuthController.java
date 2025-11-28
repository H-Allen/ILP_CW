package uk.ac.ed.acp.cw2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ed.acp.cw2.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        Map<String, String> response = authService.signupUser(
                body.get("email"),
                body.get("password"),
                body.get("name")
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
        Map<String, Object> response = authService.loginUser(
                body.get("email"),
                body.get("password"),
                session
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        Map<String, String> response = authService.logoutUser(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session")
    public ResponseEntity<?> getSession(HttpSession session) {
        Map<String, Object> response = authService.getSessionInfo(session);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getUserOrders(HttpSession session) {
        return ResponseEntity.ok(authService.getUserOrders(session));
    }
}
