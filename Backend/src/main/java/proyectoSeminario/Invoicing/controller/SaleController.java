package proyectoSeminario.Invoicing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyectoSeminario.Invoicing.dto.SaleDTOResponse;
import proyectoSeminario.Invoicing.service.SaleServiceReading;

import java.util.List;

@RestController
@RequestMapping("/v1/api/sales")
public class SaleController {

    private final SaleServiceReading saleServiceReading;
    private final SaleServiceCreating saleServiceCreating;

    public SaleController(SaleServiceReading saleServiceReading) {
        this.saleServiceReading = saleServiceReading;
    }

    @GetMapping
    public ResponseEntity<List<SaleDTOResponse>> getAllProducts() {
        List<SaleDTOResponse> responseList = saleServiceReading.findAll();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTOResponse> getProductById(@PathVariable Long id) {
        return saleServiceReading.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}