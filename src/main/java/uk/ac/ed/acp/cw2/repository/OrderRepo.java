package uk.ac.ed.acp.cw2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ed.acp.cw2.data.OrderHistory;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
