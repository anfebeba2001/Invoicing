package seminario.invoicing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class SaleDTORequest {

    @NotBlank(message = "Customer name can not be empty or null")
    private String customer;
    @NotEmpty(message = "List of products can not be empty or null")
    @NotNull(message = "List of products can not be empty or null")
    private List<SaleProductDetailDTORequest> products;
}

