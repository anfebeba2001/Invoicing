package proyectoSeminario.Invoicing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyectoSeminario.Invoicing.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}