package seminario.invoicing.dto;

import lombok.Data;

@Data
public class SaleProductDetailRequest {
    private Long productId;
    private int quantity;
}
