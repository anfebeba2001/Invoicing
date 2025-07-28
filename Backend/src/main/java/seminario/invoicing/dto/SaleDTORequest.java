package seminario.invoicing.dto;

import lombok.Data;

import java.util.List;


@Data
public class SaleDTORequest {
    private String customer;
    private List<SaleProductDetailRequest> products;
}

