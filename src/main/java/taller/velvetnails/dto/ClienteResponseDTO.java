package taller.velvetnails.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponseDTO {

    private Long idCliente;
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;

    public ClienteResponseDTO(Long idCliente, String nombre, String apellido, String documento,String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
    }
}
