package seminario.invoicing.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class SaleDTOResponse {
    private String invoiceId;
    private String customer;
    private LocalDate date;
    private String status;
    private int totalValue;
    private List<ProductInfoDTO> products;
}
