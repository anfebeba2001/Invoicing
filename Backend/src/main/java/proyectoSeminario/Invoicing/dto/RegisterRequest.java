package proyectoSeminario.Invoicing.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String password;
    private String email;
}