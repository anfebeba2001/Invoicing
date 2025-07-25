package proyectoSeminario.Invoicing.service;

import org.springframework.stereotype.Service;
import proyectoSeminario.Invoicing.dto.SaleDTOResponse;
import java.util.List;
import java.util.Optional;

@Service
public interface SaleServiceReading {
    public List<SaleDTOResponse> findAll();
    public Optional<SaleDTOResponse> findById(Long id);
}
