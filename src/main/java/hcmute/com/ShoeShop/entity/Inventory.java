package hcmute.com.ShoeShop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Inventory {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;
        private int quantity;
        private LocalDateTime createdAt;
}
