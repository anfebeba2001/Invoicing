package seminario.invoicing.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductRestockRequest {
    private Long productId;
    @Min(value = 1, message = "Amount to Restock must be positive")
    private int amount;


}
