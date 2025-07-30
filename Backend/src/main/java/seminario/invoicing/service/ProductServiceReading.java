package seminario.invoicing.service;

import seminario.invoicing.dto.ProductDTOResponse;

import java.util.List;
import java.util.Optional;


public interface ProductServiceReading {
     List<ProductDTOResponse> findAll();
     ProductDTOResponse findById(Long id);
}
