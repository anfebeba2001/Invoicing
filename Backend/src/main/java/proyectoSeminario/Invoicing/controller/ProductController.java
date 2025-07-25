package proyectoSeminario.Invoicing.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyectoSeminario.Invoicing.dto.ProductDTOResponse;
import proyectoSeminario.Invoicing.model.Product;
import proyectoSeminario.Invoicing.repository.ProductRepository;
import proyectoSeminario.Invoicing.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTOResponse>> getAllProducts() {
        List<ProductDTOResponse> responseList = productService.findAll();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> getProductById(@PathVariable Long id){
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}