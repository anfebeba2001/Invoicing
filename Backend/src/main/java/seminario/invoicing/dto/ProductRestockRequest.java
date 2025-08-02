package seminario.invoicing.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductRestockRequest {
    private Long productId;
    private int amount;


}
