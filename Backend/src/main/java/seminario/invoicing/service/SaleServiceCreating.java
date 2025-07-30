package seminario.invoicing.service;

import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.exceptions.InsufficientStockException;
import seminario.invoicing.exceptions.NotFountProductException;

public interface SaleServiceCreating {
     void create(SaleDTORequest request) throws InsufficientStockException, NotFountProductException;
}
