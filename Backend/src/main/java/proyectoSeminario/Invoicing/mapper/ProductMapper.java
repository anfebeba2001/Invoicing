package proyectoSeminario.Invoicing.mapper;

import proyectoSeminario.Invoicing.dto.ProductDTOResponse;
import proyectoSeminario.Invoicing.model.Product;

public class ProductMapper {

    private ProductMapper() {}

    public static ProductDTOResponse entityToResponse (Product entity) {
        return ProductDTOResponse.builder()
                .name(entity.getName())
                .price(entity.getPrice())
                .manufacturer(entity.getManufacturer())
                .amountInStock(entity.getAmountInStock())
                .build();
    }
}
