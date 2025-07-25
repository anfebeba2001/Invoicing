package proyectoSeminario.Invoicing.service;

import org.springframework.stereotype.Service;
import proyectoSeminario.Invoicing.dto.ProductDTOResponse;
import proyectoSeminario.Invoicing.model.Product;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    public List<ProductDTOResponse> findAll();
    public Optional<ProductDTOResponse> findById(Long id);
}
