package seminario.invoicing.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seminario.invoicing.dto.SaleDTORequest;
import seminario.invoicing.dto.SaleDTOResponse;
import seminario.invoicing.dto.SaleProductDetailDTORequest;
import seminario.invoicing.model.Product;
import seminario.invoicing.model.Sale;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SaleMapperTest {

    @Test
    void  entityToResponse_ShouldReturnResponse() {
        //Arrange
        Product product = Product.builder()
                .id(1L)
                .name("Test")
                .price(BigDecimal.valueOf(10))
                .amountInStock(10)
                .entryDate(LocalDate.now())
                .build();

        Sale sale = Sale.builder()
                .products(Set.of(product))
                .totalValue(10000)
                .status("COMPLETED")
                .invoiceId("SampleInvoiceId")
                .customer("SampleCustomer")
                .id(1L)
                .date(LocalDate.now())
                .build();

        //Act

        SaleDTOResponse saleDTOResponse = SaleMapper.entityToResponse(sale);

        //Assert
        assertNotNull(saleDTOResponse);
        assertEquals(sale.getProducts().size(),saleDTOResponse.getProducts().size());
        assertEquals(sale.getTotalValue(),saleDTOResponse.getTotalValue());
        assertEquals(sale.getDate(),saleDTOResponse.getDate());
        assertEquals(sale.getStatus(),saleDTOResponse.getStatus());
        assertEquals(sale.getCustomer(),saleDTOResponse.getCustomer());
        assertEquals(sale.getInvoiceId(),saleDTOResponse.getInvoiceId());
    }

    @Test
    void requestToEntity_ShouldReturnResponse() {
        //Arrange

        Product product1 =  Product.builder()
                .id(1L)
                .build();

        SaleProductDetailDTORequest saleProductDetailDTORequest = SaleProductDetailDTORequest.builder()
                .productId(product1.getId())
                .quantity(10)
                .build();

        SaleDTORequest request = SaleDTORequest.builder()
                .products(List.of(saleProductDetailDTORequest))
                .customer("SampleCustomer")
                .build();


        Set<Product> products=Set.of(product1);
        int totalValue = 1000;

        //Act
        Sale sale = SaleMapper.requestToEntity(request, products, totalValue);
        //Assert
        assertNotNull(sale);
        assertEquals(totalValue,sale.getTotalValue());
        assertEquals(request.getProducts().size(),sale.getProducts().size());
        assertEquals(request.getCustomer(),sale.getCustomer());
    }
}