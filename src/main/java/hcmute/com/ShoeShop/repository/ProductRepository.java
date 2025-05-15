package hcmute.com.ShoeShop.repository;

import hcmute.com.ShoeShop.entity.Product;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@SpringBootApplication
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
    // list 20 san pham co avg rating cao nhat
    @Query("SELECT p FROM Product p JOIN p.ratings r GROUP BY p.id ORDER BY AVG(r.star) DESC")
    List<Product> findTop20ByAvgRating();
}
