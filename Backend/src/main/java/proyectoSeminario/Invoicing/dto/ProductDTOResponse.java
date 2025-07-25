package proyectoSeminario.Invoicing.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class ProductDTOResponse {
    private String name;
    private String manufacturer;
    private BigDecimal price;
    private int amountInStock;
}
