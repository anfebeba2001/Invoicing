package seminario.invoicing.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import seminario.invoicing.ServiceImpl.ProductServiceImpl;
import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.mapper.ProductMapper;
import seminario.invoicing.model.Product;
import seminario.invoicing.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;


    @Test
    void createProduct_shouldCreateProduct() {
        //Arrange
        ProductDTORequest productDTORequest = ProductDTORequest.builder()
                .price(BigDecimal.valueOf(10L))
                .manufacturer("Manufacturer")
                .name("Name")
                .amountInStock(50)
                .build();

        //Mock
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        productService.create(productDTORequest);

        //Verify
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();
        assertNotNull(savedProduct);
        assertEquals("Name", savedProduct.getName());
        assertEquals("Manufacturer", savedProduct.getManufacturer());
        assertEquals(0, BigDecimal.valueOf(10L).compareTo(savedProduct.getPrice()));
    }
}