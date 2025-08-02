package seminario.invoicing.ServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.support.Resource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.dto.ProductDTOResponse;
import seminario.invoicing.dto.ProductRestockRequest;
import seminario.invoicing.exceptions.RepeatedDataRequestException;
import seminario.invoicing.exceptions.ResourceNotFoundException;
import seminario.invoicing.model.Product;
import seminario.invoicing.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
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
        doAnswer(invocation -> invocation.getArgument(0)).when(productRepository).save(any(Product.class));

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
    @Test
    void createProduct_ShouldThrowRepeatedRequestException() {
        //Arrange
        ProductDTORequest productDTORequest = ProductDTORequest.builder()
                .price(BigDecimal.valueOf(10L))
                .manufacturer("Manufacturer")
                .name("Name")
                .amountInStock(50)
                .build();

        //Mock
        doReturn(true).when(productRepository).existsByName(productDTORequest.getName());

        //Act
        RepeatedDataRequestException thrownException = assertThrows(
                RepeatedDataRequestException.class,
                () -> productService.create(productDTORequest)
        );

        assertEquals("Product name is already exists", thrownException.getMessage());
    }
    @Test
    void findOneProduct_shouldReturnProduct() {
        //Arrange
        Product mockedSimulatedProduct = Product.builder()
                .price(BigDecimal.valueOf(10L))
                .manufacturer("Manufacturer")
                .name("Name")
                .id(1L)
                .entryDate(LocalDate.now())
                .amountInStock(50)
                .build();

        ProductDTOResponse expectedProductResponse = ProductDTOResponse.builder()
                .price(mockedSimulatedProduct.getPrice())
                .amountInStock(mockedSimulatedProduct.getAmountInStock())
                .manufacturer(mockedSimulatedProduct.getManufacturer())
                .name(mockedSimulatedProduct.getName())
                .id(mockedSimulatedProduct.getId())
                .price(mockedSimulatedProduct.getPrice())
                .build();

        //Mocking
        doReturn(Optional.of(mockedSimulatedProduct)).when(productRepository).findById(mockedSimulatedProduct.getId());

        //Act
        ProductDTOResponse actual = productService.findById(mockedSimulatedProduct.getId());

        //Assert
        assertNotNull(actual);
        assertEquals(expectedProductResponse.getId(), actual.getId());
        assertEquals(expectedProductResponse.getPrice(), actual.getPrice());
        assertEquals(expectedProductResponse.getAmountInStock(), actual.getAmountInStock());
        assertEquals(expectedProductResponse.getManufacturer(), actual.getManufacturer());
        assertEquals(expectedProductResponse.getName(), actual.getName());
    }

    @Test
    void findOneProduct_whenObjectNotFound_shouldThrowException() {
        //Arrange

        long id = 10L;
        Product mockedSimulatedProduct = Product.builder()
                .price(BigDecimal.valueOf(10))
                .manufacturer("Manufacturer")
                .name("Name")
                .id(id)
                .entryDate(LocalDate.now())
                .amountInStock(50)
                .build();

        //Mocking
        doReturn(Optional.empty()).when(productRepository).findById(mockedSimulatedProduct.getId());

        //Act
        ResourceNotFoundException exceptionMessage = assertThrows(ResourceNotFoundException.class,() ->
                productService.findById(id));

        //Assert
        assertEquals( "Not found Product for id: " + mockedSimulatedProduct.getId(),exceptionMessage.getMessage());
    }

    @Test
    void findAllProducts_shouldReturnAllProducts() {
        //Arrange
        Product mockedSimulatedProduct = Product.builder()
                .price(BigDecimal.valueOf(10))
                .manufacturer("Manufacturer")
                .name("Name")
                .id(10L)
                .entryDate(LocalDate.now())
                .amountInStock(50)
                .build();

        Product mockedSimulatedProduct2 = Product.builder()
                .price(BigDecimal.valueOf(20))
                .manufacturer("Manufacturer2")
                .name("Name2")
                .id(20L)
                .entryDate(LocalDate.now())
                .amountInStock(20)
                .build();

        //Mocking
        doReturn(List.of(mockedSimulatedProduct, mockedSimulatedProduct2)).when(productRepository).findAll();

        //Act
        List<ProductDTOResponse> actual = productService.findAll();

        //Assert
        assertEquals(2, actual.size());
        assertEquals(mockedSimulatedProduct.getId(), actual.get(0).getId());
        assertEquals(mockedSimulatedProduct2.getId(), actual.get(1).getId());
        assertEquals(mockedSimulatedProduct.getName(), actual.get(0).getName());
        assertEquals(mockedSimulatedProduct2.getName(), actual.get(1).getName());
        assertEquals(mockedSimulatedProduct.getPrice(), actual.get(0).getPrice());
        assertEquals(mockedSimulatedProduct.getAmountInStock(), actual.get(0).getAmountInStock());
        assertEquals(mockedSimulatedProduct.getManufacturer(), actual.get(0).getManufacturer());
    }

    @Test
    void findAllProducts_shouldReturnEmptyList() {
        //Mocking
        when(productRepository.findAll()).thenReturn(List.of());

        //Act
        List<ProductDTOResponse> actual = productService.findAll();

        //Assert
        assertEquals(0, actual.size());
    }

    @Test
    void restock_RestockProduct() {
        //Arrange
        ProductRestockRequest productRestockRequest = ProductRestockRequest.builder()
                .amount(10)
                .productId(100L)
                .build();
        //Mocking
        Product product = new Product();

        doReturn(Optional.of(product)).when(productRepository).findById(productRestockRequest.getProductId());
        doReturn(product).when(productRepository).save(any(Product.class));
        //Act
        productService.restock(productRestockRequest);
        //Verify
        verify(productRepository, times(1)).findById(productRestockRequest.getProductId());
        verify(productRepository, times(1)).save(any(Product.class));
    }


    @Test
    void restockShould_ShouldThrowException() {
        //Arrange
        ProductRestockRequest productRestockRequest = ProductRestockRequest.builder()
                .amount(10)
                .productId(100L)
                .build();
        //Mocking
        doReturn(Optional.empty()).when(productRepository).findById(productRestockRequest.getProductId());
        //Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> productService.restock(productRestockRequest));
        //Verify
       assertNotNull(exception.getMessage());
       assertEquals("Not found Product for id: " + productRestockRequest.getProductId(), exception.getMessage());
    }

}