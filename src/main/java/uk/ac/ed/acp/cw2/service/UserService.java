package uk.ac.ed.acp.cw2.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import uk.ac.ed.acp.cw2.data.OrderHistory;
import uk.ac.ed.acp.cw2.data.User;
import uk.ac.ed.acp.cw2.dto.MedDispatchRec;
import uk.ac.ed.acp.cw2.dto.response.DeliveryPathResponse;
import uk.ac.ed.acp.cw2.repository.OrderRepo;
import uk.ac.ed.acp.cw2.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private OrderRepo orderRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // User authentication methods
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(String email, String password, String name) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(email, encodedPassword, name);
        return userRepository.save(user);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Long getUserIdFromSession(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    // Order history methods
    public OrderHistory saveOrderHistory(OrderHistory order) {
        return orderRepository.save(order);
    }

    public List<OrderHistory> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Creates and saves an order history record from a delivery path calculation.
     * This method encapsulates all the business logic for order creation.
     */
    public void createOrderHistoryFromDelivery(
            HttpSession session,
            List<MedDispatchRec> queries,
            DeliveryPathResponse deliveryResponse) {

        // Early return if prerequisites aren't met
        if (queries == null || queries.isEmpty() || deliveryResponse == null) {
            return;
        }

        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return;
        }

        User user = findById(userId).orElse(null);
        if (user == null || deliveryResponse.getDronePaths().isEmpty()) {
            return;
        }

        // Build order history from delivery data
        MedDispatchRec rec = queries.get(0);
        OrderHistory order = new OrderHistory();
        order.setUser(user);
        order.setLat(rec.getDelivery().getLat());
        order.setLng(rec.getDelivery().getLng());
        order.setCapacity(rec.getRequirements().getCapacity());
        order.setCooling(rec.getRequirements().isCooling());
        order.setHeating(rec.getRequirements().isHeating());
        order.setTotalCost(deliveryResponse.getTotalCost());
        order.setTotalMoves(deliveryResponse.getTotalMoves());
        order.setDroneId(deliveryResponse.getDronePaths().get(0).getDroneId());

        saveOrderHistory(order);
    }
}
