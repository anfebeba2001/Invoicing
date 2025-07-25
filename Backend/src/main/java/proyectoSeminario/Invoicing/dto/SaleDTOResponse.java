package proyectoSeminario.Invoicing.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class SaleDTOResponse {
    private String invoiceId;
    private String customer;
    private LocalDate date;
    private String status;
    private int totalValue;
}
