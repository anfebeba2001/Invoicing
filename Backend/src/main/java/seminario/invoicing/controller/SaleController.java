package seminario.invoicing.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.dto.SaleDTOResponse;
import seminario.invoicing.service.SaleServiceCreating;
import seminario.invoicing.service.SaleServiceReading;

import java.util.List;

@RestController
@RequestMapping("/v1/api/sales")
public class SaleController {

    private final SaleServiceReading saleServiceReading;
    private final SaleServiceCreating saleServiceCreating;

    public SaleController(SaleServiceReading saleServiceReading, SaleServiceCreating saleServiceCreating) {
        this.saleServiceReading = saleServiceReading;
        this.saleServiceCreating = saleServiceCreating;
    }

    @GetMapping
    public ResponseEntity<List<SaleDTOResponse>> getAllSales() {
        List<SaleDTOResponse> responseList = saleServiceReading.findAll();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTOResponse> getSaleById(@PathVariable Long id) {
        SaleDTOResponse sale =  saleServiceReading.findById(id);
        return ResponseEntity.ok(sale);
    }

    @PostMapping
    public ResponseEntity<String> createSale(@Valid @RequestBody SaleDTORequest saleDTORequest) {
        saleServiceCreating.create(saleDTORequest);
        return new ResponseEntity<>("Sale Successfully Created", HttpStatus.CREATED);
    }
}