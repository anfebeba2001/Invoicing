package seminario.invoicing.ServiceImpl;

import org.springframework.stereotype.Service;
import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.dto.ProductDTOResponse;
import seminario.invoicing.exceptions.RepeatedDataRequestException;
import seminario.invoicing.exceptions.ResourceNotFoundException;
import seminario.invoicing.mapper.ProductMapper;
import seminario.invoicing.repository.ProductRepository;
import seminario.invoicing.service.ProductServiceCreating;
import seminario.invoicing.service.ProductServiceReading;

import java.util.List;
import java.util.Optional;
@Service
public class ProductServiceImpl implements ProductServiceReading, ProductServiceCreating {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTOResponse> findAll() {
        return productRepository
                .findAll()
                .stream()
                .map(ProductMapper::entityToResponse)
                .toList();
    }

    @Override
    public ProductDTOResponse findById(Long id) {
        return productRepository.findById(id)
                .map(ProductMapper::entityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product for id: " + id));
    }


    @Override
    public void create(ProductDTORequest productDTORequest) {

        if (productRepository.existsByName(productDTORequest.getName()))
            throw new RepeatedDataRequestException("Product name is already exists");

        productRepository.save(
            ProductMapper.requestToEntity(
                    productDTORequest
            ));
    }
}
