package seminario.invoicing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private int amountInStock;

    @Column(nullable = false)
    private BigDecimal price;

    public void modifyStock(int amount) {
        if (amount == 0)
            throw new IllegalArgumentException("Amount to buy/restock must be different than zero");

        if (amount < 0 && amountInStock < amount)
            throw new IllegalArgumentException("Amount to buy is greater than the amount on stock");

        if (amount > 1000)
            throw new IllegalArgumentException("Amount to restock is way to big, max is 1000");

        this.amountInStock += amount;
    }



}