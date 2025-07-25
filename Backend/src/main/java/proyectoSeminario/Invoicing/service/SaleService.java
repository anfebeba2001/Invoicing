package proyectoSeminario.Invoicing.service;

import org.springframework.stereotype.Service;
import proyectoSeminario.Invoicing.dto.CreateSaleRequest;
import proyectoSeminario.Invoicing.model.Product;
import proyectoSeminario.Invoicing.model.Sale;
import proyectoSeminario.Invoicing.repository.ProductRepository;
import proyectoSeminario.Invoicing.repository.SaleRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public SaleService(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale createSale(CreateSaleRequest request) {
        List<Product> productsFound = productRepository.findAllById(request.getProductIds());
        if (productsFound.size() != request.getProductIds().size()) {
            throw new IllegalArgumentException("Uno o más productos no fueron encontrados.");
        }

        // Crea la nueva venta
        Sale newSale = Sale.builder()
                .invoiceId("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .customer(request.getCustomer())
                .date(LocalDate.now())
                .status(request.getStatus())
                .products(new HashSet<>(productsFound))
                .build();

        return saleRepository.save(newSale);
    }
}