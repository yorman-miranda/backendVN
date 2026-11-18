package taller.velvetnails.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taller.velvetnails.dto.ClienteRequestDTO;
import taller.velvetnails.dto.ClienteCitasResponseDTO;
import taller.velvetnails.dto.ClienteResponseDTO;
import taller.velvetnails.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@Tag(name = "clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll(){
        return ResponseEntity.ok(clienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> create(
            @Valid @RequestBody ClienteRequestDTO requestDTO
            ){
        ClienteResponseDTO clienteCreated = clienteService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO requestDTO
    ){
        return ResponseEntity.ok(clienteService.update(id,requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ){
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<ClienteResponseDTO> buscarPorDocumento(
            @RequestParam String documento
    ){
        return ResponseEntity.ok(clienteService.findByDocumento(documento));
    }

    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNombre(
            @RequestParam String nombre
    ){
        return ResponseEntity.ok(clienteService.findByNombre(nombre));
    }



    @GetMapping("/buscar/telefono")
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorTelefono(
            @RequestParam String telefono
    ){
        return ResponseEntity.ok(clienteService.findByTelefono(telefono));
    }

    @GetMapping("/buscar/citas")
    public ResponseEntity<List<ClienteCitasResponseDTO>> buscarCitas(
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String telefono
    ) {
        if ((documento == null) == (telefono == null)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(documento != null
                ? clienteService.buscarCitasPorDocumento(documento)
                : clienteService.buscarCitasPorTelefono(telefono));
    }


}
