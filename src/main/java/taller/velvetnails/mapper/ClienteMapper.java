package taller.velvetnails.mapper;

import org.springframework.stereotype.Component;
import taller.velvetnails.dto.ClienteRequestDTO;
import taller.velvetnails.dto.ClienteResponseDTO;
import taller.velvetnails.entity.Cliente;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto){
        Cliente cliente = new Cliente();

        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDocumento(dto.getDocumento());
        cliente.setTelefono(dto.getTelefono());

        return cliente;
    }

    public ClienteResponseDTO toDTO(Cliente cliente){
        return new ClienteResponseDTO(
                cliente.getIdCliente(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getDocumento(),
                cliente.getTelefono()
        );
    }
}
