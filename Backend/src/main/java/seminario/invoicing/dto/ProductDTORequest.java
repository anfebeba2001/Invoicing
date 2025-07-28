package seminario.invoicing.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Builder
@Data
public class ProductDTORequest {

    private String name;
    private String manufacturer;
    private int amountInStock;
    private BigDecimal price;
}
