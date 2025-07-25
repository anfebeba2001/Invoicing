package proyectoSeminario.Invoicing.mapper;

import proyectoSeminario.Invoicing.dto.SaleDTOResponse;
import proyectoSeminario.Invoicing.model.Sale;

public class SaleMapper {

    private SaleMapper() {}

    public static SaleDTOResponse entityToResponse(Sale sale) {
        return  SaleDTOResponse.builder()
                .invoiceId(sale.getInvoiceId())
                .customer(sale.getCustomer())
                .date(sale.getDate())
                .status(sale.getStatus())
                .totalValue(sale.getTotalValue())
                .build();
    }
}
