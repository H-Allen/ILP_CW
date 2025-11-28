package uk.ac.ed.acp.cw2.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uk.ac.ed.acp.cw2.data.OrderHistory;
import uk.ac.ed.acp.cw2.data.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    /**
     * Handles user signup logic including validation and user creation
     */
    public Map<String, String> signupUser(String email, String password, String name) {
        // Check if email already exists
        if (userService.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // Create the new user
        userService.createUser(email, password, name);

        return Map.of("message", "User created successfully");
    }

    /**
     * Handles user login including credential verification and session setup
     */
    public Map<String, Object> loginUser(String email, String password, HttpSession session) {
        // Find user by email
        Optional<User> userOpt = userService.findByEmail(email);

        // Verify credentials
        if (userOpt.isEmpty() || !userService.verifyPassword(password, userOpt.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        User user = userOpt.get();

        // Setup session
        setupUserSession(session, user);

        // Build and return response
        return buildUserResponse(user);
    }

    /**
     * Sets up session attributes for authenticated user
     */
    private void setupUserSession(HttpSession session, User user) {
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("userEmail", user.getEmail());
    }

    /**
     * Handles user logout by invalidating the session
     */
    public Map<String, String> logoutUser(HttpSession session) {
        session.invalidate();
        return Map.of("message", "Logged out");
    }

    /**
     * Retrieves current session information
     */
    public Map<String, Object> getSessionInfo(HttpSession session) {
        Long userId = userService.getUserIdFromSession(session);

        if (userId == null) {
            return Map.of("authenticated", false);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("id", userId);
        response.put("email", session.getAttribute("userEmail"));
        response.put("name", session.getAttribute("userName"));

        return response;
    }

    /**
     * Retrieves order history for authenticated user
     */
    public List<OrderHistory> getUserOrders(HttpSession session) {
        Long userId = userService.getUserIdFromSession(session);

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        return userService.getUserOrders(userId);
    }

    /**
     * Builds a user response map with user details
     */
    private Map<String, Object> buildUserResponse(User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        return response;
    }
}
