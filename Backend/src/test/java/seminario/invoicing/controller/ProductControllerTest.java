package seminario.invoicing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import seminario.invoicing.dto.ProductDTORequest;
import seminario.invoicing.dto.ProductDTOResponse;
import seminario.invoicing.exceptions.RepeatedDataRequestException;
import seminario.invoicing.exceptions.ResourceNotFoundException;
import seminario.invoicing.service.ProductServiceCreating;
import seminario.invoicing.service.ProductServiceReading;

import java.math.BigDecimal;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductServiceReading productServiceReading;

    @Autowired
    private ProductServiceCreating productServiceCreating;

    @TestConfiguration
    static class ControllerTestConfig {

        @Bean
        public ProductServiceReading productServiceReading() {
            return mock(ProductServiceReading.class);
        }

        @Bean
        public ProductServiceCreating productServiceCreating() {
            return mock(ProductServiceCreating.class);
        }
    }

    @Test
    void getAllProducts_shouldReturnProductList() throws Exception {
        // Arrange
        ProductDTOResponse product = ProductDTOResponse.builder()
                .id(1L)
                .name("Product 1")
                .manufacturer("Manufacturer")
                .price(BigDecimal.valueOf(10))
                .amountInStock(30)
                .build();
        List<ProductDTOResponse> allProducts = List.of(product);
        String expectedJson = objectMapper.writeValueAsString(allProducts);

        // Mock
        doReturn(allProducts).when(productServiceReading).findAll();

        // Act & Assert
        this.mockMvc.perform(get("/v1/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getAllProducts_shouldReturnNothing() throws Exception {
        // Arrange
        String expectedJson = objectMapper.writeValueAsString(List.of());

        // Mock
        doReturn(List.of()).when(productServiceReading).findAll();

        // Act & Assert
        this.mockMvc.perform(get("/v1/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getOneProducts_shouldReturnProduct() throws Exception {
        // Arrange

        ProductDTOResponse product = ProductDTOResponse.builder()
                .id(1L)
                .name("Product 1")
                .manufacturer("Manufacturer")
                .price(BigDecimal.valueOf(10))
                .amountInStock(30)
                .build();

        String expectedJson = objectMapper.writeValueAsString(product);

        // Mock
        doReturn(product).when(productServiceReading).findById(product.getId());

        // Act & Assert
        this.mockMvc.perform(get("/v1/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }
    @Test
    void getOneProduct_whenNotFound_shouldReturnNotFoundStatusAndErrorMessage() throws Exception {
        // Arrange

        Long productId = 99L;
        String expectedMessage = "Not found product with ID: " + productId;

        // Mock:
        doThrow(new ResourceNotFoundException(expectedMessage)).when(productServiceReading).findById(productId);

        // Act & Assert
        mockMvc.perform(get("/v1/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Resource not found")))
                .andExpect(jsonPath("$.message", is(expectedMessage)));
    }

    @Test
    void getOneProduct_whenWrongParameterType_shouldReturnWrongParameterType() throws Exception {
        // Arrange

        String productId = "Wrong-parameter-type";
        String expectedMessage = "Parameter 'id' must be type numeric (Long).";

        // Act & Assert
        mockMvc.perform(get("/v1/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Invalid parameter type")))
                .andExpect(jsonPath("$.message", is(expectedMessage)));
    }

    @Test
    void createProduct_whenValidRequest_shouldReturnCreated() throws Exception {
        // Arrange
        ProductDTORequest validRequest = ProductDTORequest.builder()
                .name("Product 1")
                .manufacturer("Manufacturer")
                .price(BigDecimal.valueOf(10))
                .amountInStock(30)
                .build();

        // Mock:
        doNothing().when(productServiceCreating).create(any(ProductDTORequest.class));

        // Act & Assert
        mockMvc.perform(post("/v1/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Product Successfully Created"));
    }

    @Test
    void createProduct_whenNameIsNull_shouldReturnBadRequest() throws Exception {
        // Arrange:
        ProductDTORequest invalidRequest = ProductDTORequest.builder()
                .manufacturer("Manufacturer")
                .price(BigDecimal.TEN)
                .amountInStock(10)
                .build();

        // Act & Assert:
        mockMvc.perform(post("/v1/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name", is("Name can not be empty or null.")));
    }

    @Test
    void createProduct_WhenNoJSONBody_ShouldReturnBadRequestStatus() throws Exception {
        //Act & Assert
        mockMvc.perform(post("/v1/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_whenRepeatedProduct_shouldReturnRepeatedRequestException() throws Exception {
        //Arrange
        ProductDTORequest validRequest = ProductDTORequest.builder()
                .name("Product 1")
                .manufacturer("Manufacturer")
                .price(BigDecimal.valueOf(10))
                .amountInStock(30)
                .build();

        //Mocking
        doThrow(new RepeatedDataRequestException("Product name is already exists")).when(productServiceCreating).create(validRequest);

        // Act & Assert:
        mockMvc.perform(post("/v1/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("RepeatedDataRequestException")))
                .andExpect(jsonPath("$.message", is("This request can not be processed since is repeated in the dataBase ... Product name is already exists")));
    }
}