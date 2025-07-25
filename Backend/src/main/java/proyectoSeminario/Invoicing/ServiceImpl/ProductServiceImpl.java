package proyectoSeminario.Invoicing.ServiceImpl;

import proyectoSeminario.Invoicing.dto.ProductDTOResponse;
import proyectoSeminario.Invoicing.mapper.ProductMapper;
import proyectoSeminario.Invoicing.model.Product;
import proyectoSeminario.Invoicing.repository.ProductRepository;
import proyectoSeminario.Invoicing.service.ProductService;

import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

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
    public Optional<ProductDTOResponse> findById(Long id) {
        return productRepository
                .findById(id)
                .map(ProductMapper::entityToResponse);
    }
}
