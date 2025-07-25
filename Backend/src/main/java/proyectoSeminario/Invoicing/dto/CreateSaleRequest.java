package proyectoSeminario.Invoicing.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateSaleRequest {
    private String customer;
    private String status;
    private List<Long> productIds;
}