package taller.velvetnails.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taller.velvetnails.dto.CitaRequestDTO;
import taller.velvetnails.dto.CitaResponseDTO;
import taller.velvetnails.dto.EstadoCitaRequestDTO;
import taller.velvetnails.service.Impl.CitaServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cita")
@Tag(name = "citas")
public class CitaController {

    private final CitaServiceImpl citaService;

    public CitaController(CitaServiceImpl citaService){
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> findAll(){
        return ResponseEntity.ok(citaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> findById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(citaService.findById(id));
    }

    @GetMapping("/idCliente")
    public ResponseEntity<List<CitaResponseDTO>> findByIdCliente(
            @RequestParam Long id
    ){
        return ResponseEntity.ok(citaService.findByIdCliente(id));
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<CitaResponseDTO>> findByFechaCita(
            @RequestParam int dia,
            @RequestParam int mes,
            @RequestParam int anio){
        Date fecha = Date.valueOf(LocalDate.of(anio, mes, dia));
        return ResponseEntity.ok(citaService.findByFechaCita(fecha));
    }

//    @GetMapping("/dia")
//    public ResponseEntity<List<CitaResponseDTO>> fundByDia(
//            @RequestParam int dia
//    ){
//        return ResponseEntity.ok(citaService.findByDia(dia));
//    }

    @PostMapping("/CrearCita")
    public ResponseEntity<CitaResponseDTO> create(
             @Valid @RequestBody CitaRequestDTO citaRequestDTO)
    {
        return ResponseEntity.ok(citaService.create(citaRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CitaResponseDTO> update(
        @PathVariable Long id,
        @Valid @RequestBody CitaRequestDTO citaRequestDTO
    ){
        return ResponseEntity.ok(citaService.update(id,citaRequestDTO));
    }

    @PatchMapping("/{id}/asistencia")
    public ResponseEntity<CitaResponseDTO> actualizarAsistencia(
            @PathVariable Long id,
            @RequestParam boolean asistio
    ) {
        return ResponseEntity.ok(citaService.actualizarAsistencia(id, asistio));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CitaResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoCitaRequestDTO request
    ) {
        return ResponseEntity.ok(citaService.actualizarEstado(id, request.getEstadoCita()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ){
        citaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
