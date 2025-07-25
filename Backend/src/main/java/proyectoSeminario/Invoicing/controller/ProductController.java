package proyectoSeminario.Invoicing.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyectoSeminario.Invoicing.model.Product;
import proyectoSeminario.Invoicing.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}