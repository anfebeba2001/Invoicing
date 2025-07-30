package seminario.invoicing.service;

import org.springframework.stereotype.Service;
import seminario.invoicing.dto.SaleDTOResponse;
import java.util.List;
import java.util.Optional;

@Service
public interface SaleServiceReading {
     List<SaleDTOResponse> findAll();
     SaleDTOResponse findById(Long id);
}
