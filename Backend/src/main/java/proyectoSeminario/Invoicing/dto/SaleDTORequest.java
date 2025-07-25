package proyectoSeminario.Invoicing.dto;

import org.springframework.data.util.Pair;
import proyectoSeminario.Invoicing.model.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class SaleDTORequest {
    private String customer;
    private LocalDate date;
    private String status;
    private Pair<Set<Product>, List<Integer>> productsAndAmounts;
    private int totalValue;
}
