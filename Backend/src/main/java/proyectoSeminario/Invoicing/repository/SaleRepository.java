package proyectoSeminario.Invoicing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyectoSeminario.Invoicing.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}