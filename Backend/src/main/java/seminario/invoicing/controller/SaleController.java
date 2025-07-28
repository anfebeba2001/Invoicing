package seminario.invoicing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seminario.invoicing.ServiceImpl.NotFountProduct;
import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.dto.SaleDTOResponse;
import seminario.invoicing.exceptions.InsufficientStockException;
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
        return saleServiceReading.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody SaleDTORequest saleDTORequest) {
        try{
            saleServiceCreating.create(saleDTORequest);
            return  ResponseEntity.ok().body("OK");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (InsufficientStockException e) {
            return ResponseEntity.badRequest().body("not  enough stock for the products in sale");
        }
    }
}