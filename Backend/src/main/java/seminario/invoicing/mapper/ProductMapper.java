package seminario.invoicing.mapper;

import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.dto.ProductDTOResponse;
import seminario.invoicing.model.Product;

import java.time.LocalDate;

public class ProductMapper {

    private ProductMapper() {}

    public static ProductDTOResponse entityToResponse (Product entity) {
        return ProductDTOResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .manufacturer(entity.getManufacturer())
                .amountInStock(entity.getAmountInStock())
                .build();
    }

    public static Product requestToEntity(ProductDTORequest productDTORequest) {
        return Product.builder()
                .amountInStock(productDTORequest.getAmountInStock())
                .manufacturer(productDTORequest.getManufacturer())
                .entryDate(LocalDate.now())
                .name(productDTORequest.getName())
                .price(productDTORequest.getPrice())
                .build();
    }
}
