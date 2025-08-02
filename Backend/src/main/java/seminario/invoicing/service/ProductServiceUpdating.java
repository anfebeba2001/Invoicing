package seminario.invoicing.service;

import seminario.invoicing.dto.ProductRestockRequest;

public interface ProductServiceUpdating {
    void restock(ProductRestockRequest productRestockRequest);
}
