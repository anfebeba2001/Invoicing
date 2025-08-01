package seminario.invoicing.mapper;

import seminario.invoicing.dto.ProductInfoDTO;
import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.dto.SaleDTOResponse;
import seminario.invoicing.model.Product;
import seminario.invoicing.model.Sale;

import java.time.LocalDate;
import java.util.Set;

public class SaleMapper {

    private SaleMapper() {}

    public static SaleDTOResponse entityToResponse(Sale sale) {
        return  SaleDTOResponse.builder()
                .invoiceId(sale.getInvoiceId())
                .customer(sale.getCustomer())
                .date(sale.getDate())
                .status(sale.getStatus())
                .totalValue(sale.getTotalValue())
                .products(
                        sale.getProducts().stream()
                                .map(product -> ProductInfoDTO.builder().name(product.getName()).build())
                                .toList()
                )
                .build();
    }

    public static Sale requestToEntity(SaleDTORequest saleDtoRequest, Set<Product> saleProducts, int totalValue) {
        return Sale.builder()
                .customer(saleDtoRequest.getCustomer())
                .date(LocalDate.now())
                .status("COMPLETED")
                .invoiceId("INV-" + System.currentTimeMillis())
                .products(saleProducts)
                .totalValue(totalValue)
                .build();
    }
}
