package seminario.invoicing.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class SaleProductDetailDTORequest {
    private Long productId;
    private int quantity;
}
