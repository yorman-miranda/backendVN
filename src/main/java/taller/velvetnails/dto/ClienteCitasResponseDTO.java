package taller.velvetnails.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClienteCitasResponseDTO {

    private ClienteResponseDTO cliente;
    private List<CitaResponseDTO> citas;

    public ClienteCitasResponseDTO(ClienteResponseDTO cliente, List<CitaResponseDTO> citas) {
        this.cliente = cliente;
        this.citas = citas;
    }
}
