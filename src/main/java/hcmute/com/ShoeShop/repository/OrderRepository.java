package hcmute.com.ShoeShop.repository;

import hcmute.com.ShoeShop.entity.Order;
import hcmute.com.ShoeShop.utlis.ShipmentStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
//        @Query("select o, s.status from Order o join Shipment s on s.order.id = o.id")
//        public List<Object> findALlWithStatus();

        public Order findOrderById(int orderId);

        long countByStatus(ShipmentStatus status);

        public Page<Order> findOrderByStatus(ShipmentStatus status, Pageable pageable);

        public Page<Order> findOrderByUser_Id(int userId, Pageable pageable);
}
