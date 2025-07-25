package proyectoSeminario.Invoicing.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import proyectoSeminario.Invoicing.dto.CreateSaleRequest;
import proyectoSeminario.Invoicing.model.Sale;
import proyectoSeminario.Invoicing.service.SaleService;

import java.util.List;

@RestController
@RequestMapping("/v1/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public List<Sale> getAllSales() {
        return saleService.getAllSales();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sale createSale(@RequestBody CreateSaleRequest request) {
        return saleService.createSale(request);
    }
}