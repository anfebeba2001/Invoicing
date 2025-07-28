package seminario.invoicing.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ProductDTOResponse {
    private Long id;
    private String name;
    private String manufacturer;
    private BigDecimal price;
    private int amountInStock;
}
