package seminario.invoicing.ServiceImpl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.dto.SaleDTOResponse;
import seminario.invoicing.dto.SaleProductDetailDTORequest;
import seminario.invoicing.exceptions.InsufficientStockException;
import seminario.invoicing.exceptions.ResourceNotFoundException;
import seminario.invoicing.model.Product;
import seminario.invoicing.model.Sale;
import seminario.invoicing.repository.ProductRepository;
import seminario.invoicing.repository.SaleRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @InjectMocks
    private SaleServiceImpl saleService;
    @Mock
    private SaleRepository saleRepository;
    @Mock
    private ProductRepository  productRepository;

    @Test
    void createSell_ShouldCreateSell() {
        //Arrange
        SaleProductDetailDTORequest sampleProduct = SaleProductDetailDTORequest.builder()
                .quantity(10)
                .productId(1L)
                .build();

        SaleProductDetailDTORequest sampleProduct2 = SaleProductDetailDTORequest.builder()
                .quantity(20)
                .productId(2L)
                .build();

        SaleDTORequest productDtoRequest = SaleDTORequest.builder()
                .customer("customer")
                .products(List.of(sampleProduct, sampleProduct2))
                .build();

        //Mocking
        Product mockedProduct = Product.builder()
                .id(1L)
                .amountInStock(1000)
                .price(BigDecimal.valueOf(10))
                .build();

        Product mockedProduct2 = Product.builder()
                .id(2L)
                .amountInStock(2000)
                .price(BigDecimal.valueOf(10))
                .build();


        doReturn(new Sale()).when(saleRepository).save(any(Sale.class));
        doReturn(Optional.of(mockedProduct)).when(productRepository).findById(sampleProduct.getProductId());
        doReturn(Optional.of(mockedProduct2)).when(productRepository).findById(sampleProduct2.getProductId());

        //Act
        saleService.create(productDtoRequest);

        //Verify
        verify(productRepository, times(2)).findById(anyLong());
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void createSale_WhenProductNotFound_ShouldThrowException() {
        //Arrange
        SaleProductDetailDTORequest sampleProduct = SaleProductDetailDTORequest.builder()
                .quantity(10)
                .productId(1L)
                .build();

        SaleProductDetailDTORequest sampleProduct2 = SaleProductDetailDTORequest.builder()
                .quantity(20)
                .productId(2L)
                .build();

        SaleDTORequest productDtoRequest = SaleDTORequest.builder()
                .customer("customer")
                .products(List.of(sampleProduct, sampleProduct2))
                .build();

        //Mocking

        doReturn(Optional.empty()).when(productRepository).findById(sampleProduct.getProductId());

        //Act
        ResourceNotFoundException exception = assertThrows( ResourceNotFoundException.class,
                () -> saleService.create(productDtoRequest));

        //Verify
        assertNotNull(exception);
        assertEquals("Could not find Product with id:  " + sampleProduct.getProductId(), exception.getMessage());
    }

    @Test
    void createSale_WhenProductAmountInStockIsNotEnough_ShouldThrowException() {
        //Arrange
        SaleProductDetailDTORequest sampleProduct = SaleProductDetailDTORequest.builder()
                .quantity(10)
                .productId(1L)
                .build();

        SaleProductDetailDTORequest sampleProduct2 = SaleProductDetailDTORequest.builder()
                .quantity(20)
                .productId(2L)
                .build();

        SaleDTORequest productDtoRequest = SaleDTORequest.builder()
                .customer("customer")
                .products(List.of(sampleProduct, sampleProduct2))
                .build();

        //Mocking
        Product mockedProduct = Product.builder()
                .id(1L)
                .name("SampleProductName1")
                .amountInStock(1000)
                .price(BigDecimal.valueOf(10))
                .build();

        Product mockedProduct2 = Product.builder()
                .id(2L)
                .name("SampleProductName2")
                .amountInStock(0)
                .price(BigDecimal.valueOf(10))
                .build();

        doReturn(Optional.of(mockedProduct)).when(productRepository).findById(sampleProduct.getProductId());
        doReturn(Optional.of(mockedProduct2)).when(productRepository).findById(sampleProduct2.getProductId());

        //Act
        InsufficientStockException exception = assertThrows( InsufficientStockException.class,
                () -> saleService.create(productDtoRequest));

        //Verify
        assertNotNull(exception);
        assertEquals("Insufficient stock for product: " + mockedProduct2.getName(), exception.getMessage());
    }

    @Test
    void findProductById_ShouldReturnProduct(){
        //Arrange
        Long productId = 1L;

        //Mocking
        Product productForSale = Product.builder()
                .id(productId)
                .name("SampleProductName1")
                .amountInStock(1000)
                .price(BigDecimal.valueOf(10))
                .build();

        Product productForSale2 = Product.builder()
                .id(productId)
                .name("SampleProductName2")
                .amountInStock(1000)
                .price(BigDecimal.valueOf(20))
                .build();

        Sale mockedSale = Sale.builder()
                .customer("sampleName")
                .products(Set.of(productForSale, productForSale2))
                .build();

        doReturn(Optional.of(mockedSale)).when(saleRepository).findById(productId);

        //Act
        SaleDTOResponse actual = saleService.findById(productId);

        //Verify
        assertNotNull(actual);
        assertEquals(actual.getCustomer(), mockedSale.getCustomer());
        assertEquals(productForSale2.getName(),actual.getProducts().get(0).getName());
    }

    @Test
    void findProductById_WhenProductNotFound_ShouldThrowException() {
        //Arrange
        Long productId = 1L;

        //Mocking
        doReturn(Optional.empty()).when(saleRepository).findById(productId);

        //Act
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> saleService.findById(productId));

        //Verify
        assertNotNull(exception);
        assertEquals("Could not find Sale with id: " + productId, exception.getMessage());
    }

    @Test
    void findAllProducts_ShouldReturnAllProducts(){
        //Arrange
        Product productForSale1 = Product.builder().build();
        //Mocking

        Sale mockedSale1 = Sale.builder()
                .products(Set.of(productForSale1))
                .build();

        Sale mockedSale2 =  Sale.builder()
                .products(Set.of(productForSale1))
                .build();

        doReturn(List.of(mockedSale1,mockedSale2)).when(saleRepository).findAll();
        //Act
        List<SaleDTOResponse> received = saleService.findAll();
        //Verify
        assertNotNull(received);
        assertEquals(2,received.size());
    }
    @Test
    void findAllProducts_WhenNoSales_ShouldReturnEmpty(){
        //Mocking
        doReturn(List.of()).when(saleRepository).findAll();
        //Act
        List<SaleDTOResponse> received = saleService.findAll();
        //Verify
        assertNotNull(received);
        assertEquals(0,received.size());
    }
}