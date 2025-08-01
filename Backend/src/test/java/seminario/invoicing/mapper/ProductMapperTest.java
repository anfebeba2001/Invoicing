package seminario.invoicing.mapper;

import org.junit.jupiter.api.Test;
import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.dto.ProductDTOResponse;
import seminario.invoicing.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    @Test
    void entityToResponse_shouldReturnResponse(){
        //Arrange
        Product entity = Product.builder()
                .price(BigDecimal.valueOf(1000))
                .entryDate(LocalDate.now())
                .amountInStock(10)
                .id(10L)
                .name("SampleProductName")
                .manufacturer("SampleManufacturer")
                .build();

        //Act
        ProductDTOResponse productDTOResponse = ProductMapper.entityToResponse(entity);

        //Assert
        assertNotNull(productDTOResponse);
        assertEquals(entity.getPrice(),productDTOResponse.getPrice());
        assertEquals(entity.getAmountInStock(),productDTOResponse.getAmountInStock());
        assertEquals(entity.getManufacturer(),productDTOResponse.getManufacturer());
        assertEquals(entity.getId(),productDTOResponse.getId());
        assertEquals(entity.getName(),productDTOResponse.getName());
    }

    @Test
    void requestToEntity_ShouldReturnEntity(){
        //Arrange
        ProductDTORequest request = ProductDTORequest.builder()
                .price(BigDecimal.valueOf(1000))
                .amountInStock(10)
                .name("SampleProductName")
                .manufacturer("SampleManufacturer")
                .build();

        //Act
        Product entity = ProductMapper.requestToEntity(request);

        //Assert
        assertNotNull(entity);
        assertEquals(request.getPrice(),entity.getPrice());
        assertEquals(request.getAmountInStock(),entity.getAmountInStock());
        assertEquals(request.getManufacturer(),entity.getManufacturer());
        assertEquals(request.getName(),entity.getName());
    }

}