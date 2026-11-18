package taller.velvetnails.service.Impl;


import org.springframework.stereotype.Service;
import taller.velvetnails.dto.CitaResponseDTO;
import taller.velvetnails.dto.ClienteCitasResponseDTO;
import taller.velvetnails.dto.ClienteRequestDTO;
import taller.velvetnails.dto.ClienteResponseDTO;
import taller.velvetnails.entity.Cliente;
import taller.velvetnails.exception.ExistClienteByDocument;
import taller.velvetnails.exception.ResourceNotFoundException;
import taller.velvetnails.mapper.ClienteMapper;
import taller.velvetnails.mapper.CitaMapper;
import taller.velvetnails.repository.ClienteRepository;
import taller.velvetnails.service.ClienteService;

import java.util.List;
import java.util.Objects;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteMapper clienteMapper;
    private final CitaMapper citaMapper;
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper, CitaMapper citaMapper){
        this.clienteMapper = clienteMapper;
        this.citaMapper = citaMapper;
        this.clienteRepository = clienteRepository;
    }


    @Override
    public ClienteResponseDTO create(ClienteRequestDTO request) {

        VerifyNotExistClientByDocumentoOrThrow(request.getDocumento());

        Cliente cliente = new Cliente();

        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setDocumento(request.getDocumento());
        cliente.setTelefono(request.getTelefono());

        return clienteMapper.toDTO(clienteRepository.save(cliente));
    }

    @Override
    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll().stream().map(clienteMapper::toDTO).toList();
    }

    @Override
    public ClienteResponseDTO findById(Long id) {

        return clienteMapper.toDTO(findByIdOrThrow(id));
    }

    @Override
    public List<ClienteResponseDTO> findByNombre(String nombre) {
        List<Cliente> clientes = clienteRepository.findByNombre(nombre);
        return clientes.stream().map(clienteMapper::toDTO).toList();
    }

    @Override
    public void delete(Long id) {
        Cliente cliente = findByIdOrThrow(id);
        clienteRepository.delete(cliente);
    }

    @Override
    public ClienteResponseDTO update(Long id, ClienteRequestDTO request) {

        Cliente cliente = findByIdOrThrow(id);

        if(!Objects.equals(request.getDocumento(), cliente.getDocumento())){
            VerifyNotExistClientByDocumentoOrThrow(request.getDocumento());
        }

        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setTelefono(request.getTelefono());
        cliente.setDocumento(request.getDocumento());
        Cliente clienteUpdate = clienteRepository.save(cliente);

        return clienteMapper.toDTO(clienteUpdate);
    }

    @Override
    public ClienteResponseDTO findByDocumento(String documento){
        Cliente cliente = clienteRepository.findByDocumento(documento);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente");
        }
        return clienteMapper.toDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> findByNombreOrApellido(String nombre, String apellido){

        return clienteRepository.findByNombreOrApellido( nombre,  apellido).stream().map(clienteMapper::toDTO).toList();
    }

    @Override
    public List<ClienteResponseDTO> findByTelefono(String telefono){
        return clienteRepository.findByTelefono(telefono).stream().map(clienteMapper::toDTO).toList();
    }

    public List<ClienteCitasResponseDTO> buscarCitasPorDocumento(String documento) {
        Cliente cliente = clienteRepository.findByDocumentoConCitas(documento);
        if (cliente == null) {
            throw new ResourceNotFoundException("Cliente");
        }
        return List.of(toClienteCitasDTO(cliente));
    }

    public List<ClienteCitasResponseDTO> buscarCitasPorTelefono(String telefono) {
        return clienteRepository.findByTelefonoConCitas(telefono).stream()
                .map(this::toClienteCitasDTO)
                .toList();
    }

    private ClienteCitasResponseDTO toClienteCitasDTO(Cliente cliente) {
        List<CitaResponseDTO> citas = cliente.getCita().stream()
                .map(citaMapper::toDTO)
                .toList();
        return new ClienteCitasResponseDTO(clienteMapper.toDTO(cliente), citas);
    }

    public Cliente findByIdOrThrow(Long id){
        return  clienteRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Cliente"));
    }

    public void VerifyNotExistClientByDocumentoOrThrow(String documento){
        Cliente cliente = clienteRepository.findByDocumento(documento);
        if (cliente != null){
            throw(new ExistClienteByDocument("Cliente"));
        }
    }
}
