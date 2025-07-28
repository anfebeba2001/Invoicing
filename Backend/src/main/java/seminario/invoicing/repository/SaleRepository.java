package seminario.invoicing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seminario.invoicing.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}