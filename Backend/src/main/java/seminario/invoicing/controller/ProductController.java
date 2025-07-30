package seminario.invoicing.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.dto.ProductDTOResponse;
import seminario.invoicing.service.ProductServiceCreating;
import seminario.invoicing.service.ProductServiceReading;

import java.util.List;

@RestController
@RequestMapping("/v1/api/products")
public class ProductController {

    private final ProductServiceReading productServiceReading;
    private final ProductServiceCreating productServiceCreating;

    public ProductController(ProductServiceReading productServiceReading, ProductServiceCreating productServiceCreating) {
        this.productServiceReading = productServiceReading;
        this.productServiceCreating = productServiceCreating;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTOResponse>> getAllProducts() {
        List<ProductDTOResponse> responseList = productServiceReading.findAll();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTOResponse> getProductById(@PathVariable Long id) {
        ProductDTOResponse product = productServiceReading.findById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductDTORequest productDTORequest) {
        productServiceCreating.create(productDTORequest);
        return new ResponseEntity<>("Product Successfully Created", HttpStatus.CREATED);
    }
}