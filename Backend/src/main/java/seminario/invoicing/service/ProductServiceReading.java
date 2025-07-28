package seminario.invoicing.service;

import seminario.invoicing.dto.ProductDTOResponse;

import java.util.List;
import java.util.Optional;


public interface ProductServiceReading {
     List<ProductDTOResponse> findAll();
     Optional<ProductDTOResponse> findById(Long id);
}
