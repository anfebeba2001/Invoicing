package seminario.invoicing.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class SaleDTORequest {
    private String customer;
    private List<SaleProductDetailDTORequest> products;
}

