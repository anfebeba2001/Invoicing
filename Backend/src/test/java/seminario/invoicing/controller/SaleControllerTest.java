package seminario.invoicing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import seminario.invoicing.dto.ProductInfoDTO;
import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.dto.SaleDTOResponse;
import seminario.invoicing.dto.SaleProductDetailDTORequest;
import seminario.invoicing.exceptions.InsufficientStockException;
import seminario.invoicing.exceptions.ResourceNotFoundException;
import seminario.invoicing.service.SaleServiceCreating;
import seminario.invoicing.service.SaleServiceReading;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SaleController.class)
class SaleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SaleServiceReading saleServiceReading;

    @Autowired
    private SaleServiceCreating saleServiceCreating;

    @TestConfiguration
    static class ControllerTestConfig {

        @Bean
        public SaleServiceReading saleServiceReading() {
            return mock(SaleServiceReading.class);
        }

        @Bean
        public SaleServiceCreating saleServiceCreating() {
            return mock(SaleServiceCreating.class);
        }
    }

    @Test
    void getAllSales_ShouldReturnAllSales() throws Exception {

        //Arrange
        ProductInfoDTO expectedProduct = ProductInfoDTO.builder()
                .name("Sample Product")
                .build();

        SaleDTOResponse expectedSale = SaleDTOResponse.builder()
                .customer("Sample customer")
                .status("Completed")
                .totalValue(1000)
                .date(LocalDate.now())
                .invoiceId("Sample-Invoice.Id")
                .products(List.of(expectedProduct))
                .build();

        List<SaleDTOResponse> expectedList  = List.of(expectedSale);
        String expectedJson = objectMapper.writeValueAsString(expectedList);

        //Mocking
        doReturn(expectedList).when(saleServiceReading).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/v1/api/sales"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getAllSales_ShouldReturnEmpty() throws Exception {
        //Arrange
        String expectedJson = objectMapper.writeValueAsString(List.of());

        //Mocking
        doReturn(List.of()).when(saleServiceReading).findAll();

        //Act & Assert
        this.mockMvc.perform(get("/v1/api/sales"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getAllSales_WhenWrongPath_ShouldReturn404() throws Exception {
        //Act & Assert
        this.mockMvc.perform(get("/v1/api/sales/WrongPath"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getOneSale_ShouldReturnSale() throws Exception {
        //Arrange
        Long productId = 1L;

        ProductInfoDTO expectedProduct = ProductInfoDTO.builder()
                .name("Sample Product")
                .build();

        SaleDTOResponse expectedSale = SaleDTOResponse.builder()
                .products(List.of(expectedProduct))
                .customer("Sample customer")
                .status("Completed")
                .totalValue(1000)
                .date(LocalDate.now())
                .invoiceId("Sample-Invoice.Id")
                .build();

        String expectedJson = objectMapper.writeValueAsString(expectedSale);

        //Mocking
        doReturn(expectedSale).when(saleServiceReading).findById(productId);

        //Act & Assert
        this.mockMvc.perform(get("/v1/api/sales/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getOneProduct_whenWrongParameterType_shouldReturnWrongParameterType() throws Exception {
        // Arrange

        String productId = "Wrong-parameter-type";
        String expectedMessage = "Parameter 'id' must be type numeric (Long).";

        // Act & Assert
        mockMvc.perform(get("/v1/api/sales/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Invalid parameter type")))
                .andExpect(jsonPath("$.message", is(expectedMessage)));
    }
    @Test
    void getOneProduct_whenNotFound_shouldReturnNotFoundStatusAndErrorMessage() throws Exception {
        // Arrange

        Long productId = 99L;
        String expectedMessage = "Not found sale with ID: " + productId;

        // Mock:
        when(saleServiceReading.findById(productId))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        // Act & Assert
        mockMvc.perform(get("/v1/api/sales/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Resource not found")))
                .andExpect(jsonPath("$.message", is(expectedMessage)));
    }

    @Test
    void createSale_ShouldReturnCreatedStatus() throws Exception {
       //Arrange

        SaleProductDetailDTORequest validSaleProductDetailDTORequest = SaleProductDetailDTORequest.builder()
                .productId(1L)
                .quantity(2)
                .build();

        SaleProductDetailDTORequest validSaleProductDetailDTORequest2 = SaleProductDetailDTORequest.builder()
                .productId(12L)
                .quantity(22)
                .build();

        SaleDTORequest validSale = SaleDTORequest.builder()
               .customer("Sample customer")
               .products(List.of(validSaleProductDetailDTORequest,validSaleProductDetailDTORequest2))
               .build();

        // Mocking

        doNothing().when(saleServiceCreating).create(validSale);

        //Act & Assert
        mockMvc.perform(post("/v1/api/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSale)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Sale Successfully Created"));
    }

    @Test
    void createSale_WhenNotValidSale_ShouldReturnBadRequestStatus() throws Exception {
        //Arrange
        SaleDTORequest invalidSale = SaleDTORequest.builder()
                .customer("Sample customer")
                .build();

        //Act & Assert
        mockMvc.perform(post("/v1/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSale)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.products", is("List of products can not be empty or null")));
    }

    @Test
    void createSale_WhenNoJSONBody_ShouldReturnBadRequestStatus() throws Exception {
        //Act & Assert
        mockMvc.perform(post("/v1/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
    @Test
    void  createSale_WhenInsufficientStock_ShouldReturnBadRequestStatus() throws Exception {
        //Arrange

        SaleProductDetailDTORequest validSaleProductDetailDTORequest = SaleProductDetailDTORequest.builder()
                .productId(1L)
                .quantity(2)
                .build();

        SaleProductDetailDTORequest validSaleProductDetailDTORequest2 = SaleProductDetailDTORequest.builder()
                .productId(12L)
                .quantity(22)
                .build();

        SaleDTORequest validSale = SaleDTORequest.builder()
                .customer("Sample customer")
                .products(List.of(validSaleProductDetailDTORequest,validSaleProductDetailDTORequest2))
                .build();

        String errorMessage = "Insufficient Stock for product";
        // Mocking

        doThrow(new InsufficientStockException(errorMessage)).when(saleServiceCreating).create(validSale);

        //Act & Assert
        mockMvc.perform(post("/v1/api/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSale)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("InsufficientStockException")))
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

}