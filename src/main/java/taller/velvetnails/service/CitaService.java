package taller.velvetnails.service;

import taller.velvetnails.dto.CitaRequestDTO;
import taller.velvetnails.dto.CitaResponseDTO;
import taller.velvetnails.entity.Cita;

import java.sql.Date;
import java.util.List;

public interface CitaService {

    CitaResponseDTO create(CitaRequestDTO request);

    List<CitaResponseDTO> findAll();

    CitaResponseDTO findById(Long id);

    CitaResponseDTO update(Long id, CitaRequestDTO request);

    CitaResponseDTO actualizarAsistencia(Long id, boolean asistio);

    List<CitaResponseDTO> findByFechaCita(Date fecha);

    List<CitaResponseDTO> findByMes(int mes);

    List<CitaResponseDTO> findByDia(int dia);

    List<CitaResponseDTO> findByAnio(int anio);

    List<CitaResponseDTO> findByIdCliente(Long id);

    void delete(Long id);
}
