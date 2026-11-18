package taller.velvetnails.service;

import taller.velvetnails.dto.ClienteRequestDTO;
import taller.velvetnails.dto.ClienteCitasResponseDTO;
import taller.velvetnails.dto.ClienteResponseDTO;
import taller.velvetnails.entity.Cliente;

import java.util.List;

public interface ClienteService {

    ClienteResponseDTO create(ClienteRequestDTO request);

    List<ClienteResponseDTO> findAll();

    ClienteResponseDTO findById(Long id);

    List<ClienteResponseDTO> findByNombre(String nombre);

    void delete(Long id);

    ClienteResponseDTO update(Long id, ClienteRequestDTO request);

    ClienteResponseDTO findByDocumento(String documento);

    List<ClienteResponseDTO> findByNombreOrApellido(String nombre, String apellido);

    List<ClienteResponseDTO> findByTelefono(String telefono);

    List<ClienteCitasResponseDTO> buscarCitasPorDocumento(String documento);

    List<ClienteCitasResponseDTO> buscarCitasPorTelefono(String telefono);
}
