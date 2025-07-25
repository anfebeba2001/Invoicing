package proyectoSeminario.Invoicing.service;

import proyectoSeminario.Invoicing.dto.SaleDTORequest;
import proyectoSeminario.Invoicing.dto.SaleDTOResponse;

public interface SaleServiceCreating {
    public SaleDTOResponse create(SaleDTORequest request);
}
