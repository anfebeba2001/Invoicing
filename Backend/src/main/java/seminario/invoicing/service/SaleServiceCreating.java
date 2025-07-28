package seminario.invoicing.service;

import seminario.invoicing.ServiceImpl.NotFountProduct;
import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.exceptions.InsufficientStockException;

public interface SaleServiceCreating {
     void create(SaleDTORequest request) throws InsufficientStockException, NotFountProduct;
}
