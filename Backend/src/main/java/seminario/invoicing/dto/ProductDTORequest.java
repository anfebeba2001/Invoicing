package seminario.invoicing.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Builder
@Data
public class ProductDTORequest {

    @NotBlank(message = "Name can not be empty or null.")
    private String name;
    @NotBlank(message = "Manufacturer can not be empty or null.")
    private String manufacturer;
    @NotNull(message = "Amount in stock can not be null.")
    @Min(value = 0, message = "Amount in stock can not be negative.")
    private int amountInStock;
    @NotNull(message = "Price can not be null.")
    @Min(value = 0, message = "Price can not be negative.")
    private BigDecimal price;
}
