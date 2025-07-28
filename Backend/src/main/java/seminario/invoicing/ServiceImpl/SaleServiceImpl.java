package seminario.invoicing.ServiceImpl;

import org.springframework.stereotype.Service;
import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.dto.SaleDTOResponse;
import seminario.invoicing.dto.SaleProductDetailRequest;
import seminario.invoicing.exceptions.InsufficientStockException;
import seminario.invoicing.mapper.SaleMapper;
import seminario.invoicing.model.Product;
import seminario.invoicing.repository.ProductRepository;
import seminario.invoicing.repository.SaleRepository;
import seminario.invoicing.service.SaleServiceCreating;
import seminario.invoicing.service.SaleServiceReading;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SaleServiceImpl implements SaleServiceReading, SaleServiceCreating {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleServiceImpl(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<SaleDTOResponse> findAll() {
        return saleRepository
                .findAll()
                .stream()
                .map(SaleMapper::entityToResponse)
                .toList();
    }

    @Override
    public Optional<SaleDTOResponse> findById(Long id) {
        return saleRepository
                .findById(id)
                .map(SaleMapper::entityToResponse);
    }

    @Override
    public void create(SaleDTORequest saleDtoRequest) throws InsufficientStockException, NotFountProduct {
        Set<Product> saleProducts = new HashSet<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (SaleProductDetailRequest detail : saleDtoRequest.getProducts()) {
            Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new NotFountProduct("not found product with ID:  " + detail.getProductId()));

            if (product.getAmountInStock() < detail.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            product.setAmountInStock(product.getAmountInStock() - detail.getQuantity());
            totalValue = totalValue.add(product.getPrice().multiply(new BigDecimal(detail.getQuantity())));
            saleProducts.add(product);
        }


        saleRepository.save(
                SaleMapper.requestToEntity(saleDtoRequest, saleProducts, totalValue.intValue())
        );
    }
}
