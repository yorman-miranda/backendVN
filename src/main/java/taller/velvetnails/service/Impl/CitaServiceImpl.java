package taller.velvetnails.service.Impl;

import org.springframework.stereotype.Service;
import taller.velvetnails.dto.CitaRequestDTO;
import taller.velvetnails.dto.CitaResponseDTO;
import taller.velvetnails.entity.Cita;
import taller.velvetnails.entity.Cliente;
import taller.velvetnails.entity.EstadoCita;
import taller.velvetnails.exception.ExistFechaAndHora;
import taller.velvetnails.exception.ModificacionCitaFueraDePlazoException;
import taller.velvetnails.exception.ResourceNotFoundException;
import taller.velvetnails.exception.fechaIvalida;
import taller.velvetnails.mapper.CitaMapper;
import taller.velvetnails.repository.CitaRepository;
import taller.velvetnails.service.CitaService;
//import taller.velvetnails.conversorImagen.ConvertirImagen;

//import java.awt.image.BufferedImage;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitaServiceImpl implements CitaService {

    private final CitaMapper citaMapper;
    private final CitaRepository citaRepository;
    private final ClienteServiceImpl clienteService;
    //private final ConvertirImagen convertirImagen;

    //ConvertirImagen convertirImagen
    public CitaServiceImpl(CitaMapper citaMapper, CitaRepository citaRepository, ClienteServiceImpl clienteService){
        this.citaMapper = citaMapper;
        this.citaRepository = citaRepository;
        this.clienteService = clienteService;
        //this.convertirImagen = convertirImagen;
    }

    @Override
    public CitaResponseDTO create(CitaRequestDTO request) {
        Cita cita = new Cita();
        Date fecha = Date.valueOf(LocalDate.of(request.getAnio(), request.getMes(), request.getDia()));

        verificarFechaCita(request.getAnio(), request.getMes(), request.getDia());
        validarCupo(fecha, request.getHoraCita(), request.getTiempoCita(), null);
        cita.setHoraCita(request.getHoraCita());
        cita.setFechaCita(fecha);
        //cita.setComprobanteAdelantoCita(convertirImagen.convertToDatabaseColumn((BufferedImage) request.getComprobantePago()));
        cita.setTiempoCita(request.getTiempoCita());
        cita.setEstadoCita(EstadoCita.PENDIENTE_PAGO);
        cita.setCliente(returnCliente(request.getIdCliente()));

        return citaMapper.toDTO(citaRepository.save(cita));
    }

    @Override
    public CitaResponseDTO findById(Long id){
        Cita cita = findByIdOrThrow(id);

        return citaMapper.toDTO(cita);
    }

    @Override
    public List<CitaResponseDTO> findAll() {
        return citaRepository.findAll().stream().map(citaMapper::toDTO).toList();
    }

    @Override
    public CitaResponseDTO update(Long id, CitaRequestDTO request) {
        Cita cita = findByIdOrThrow(id);
        LocalDateTime ahora = LocalDateTime.now();
        Date fecha = Date.valueOf(LocalDate.of(request.getAnio(), request.getMes(), request.getDia()));
        validarAnticipacion(
                cita,
                fecha.toLocalDate().atTime(request.getHoraCita(), 0),
                ahora
        );

        verificarFechaCita(request.getAnio(), request.getMes(), request.getDia());
        validarCupo(fecha, request.getHoraCita(), request.getTiempoCita(), id);
        cita.setFechaCita(fecha);
        cita.setHoraCita(request.getHoraCita());
        //cita.setComprobanteAdelantoCita(cita.getComprobanteAdelantoCita());
        cita.setTiempoCita(request.getTiempoCita());
        cita.setCliente(returnCliente(request.getIdCliente()));

        Cita citaUpdate = citaRepository.save(cita);
        return citaMapper.toDTO(citaUpdate);
    }

    void validarAnticipacion(Cita cita, LocalDateTime nuevoInicio, LocalDateTime ahora) {
        LocalDateTime inicioCita = cita.getFechaCita().toLocalDate().atTime(cita.getHoraCita(), 0);
        LocalDateTime limite = ahora.plusHours(24);
        if (inicioCita.isBefore(limite) || nuevoInicio.isBefore(limite)) {
            throw new ModificacionCitaFueraDePlazoException(
                    "La cita actual y el nuevo horario deben estar al menos a 24 horas de distancia."
            );
        }
    }

    @Override
    public CitaResponseDTO actualizarAsistencia(Long id, boolean asistio) {
        Cita cita = findByIdOrThrow(id);
        cita.setAsistio(asistio);
        return citaMapper.toDTO(citaRepository.save(cita));
    }

    public CitaResponseDTO actualizarEstado(Long id, EstadoCita estadoCita) {
        Cita cita = findByIdOrThrow(id);
        cita.setEstadoCita(estadoCita);
        return citaMapper.toDTO(citaRepository.save(cita));
    }

    @Override
    public List<CitaResponseDTO> findByFechaCita(Date fecha) {
        return citaRepository.findByFechaCita(fecha).stream().map(citaMapper::toDTO).toList();
    }

    @Override
    public List<CitaResponseDTO> findByIdCliente(Long id) {
        return citaRepository.findByCliente(returnCliente(id)).stream().map(citaMapper::toDTO).toList();
    }

    @Override
    public List<CitaResponseDTO> findByMes(int mes){
        return citaRepository.findByMes(mes).stream().map(citaMapper::toDTO).toList();
    }

    @Override
    public List<CitaResponseDTO> findByDia(int dia){
        return citaRepository.findByDia(dia).stream().map(citaMapper::toDTO).toList();
    }

    @Override
    public List<CitaResponseDTO> findByAnio(int anio){
        return citaRepository.findByAnio(anio).stream().map(citaMapper::toDTO).toList();
    }


    @Override
    public void delete(Long id){
        Cita cita = findByIdOrThrow(id);
        citaRepository.delete(cita);
    }


    public Cita findByIdOrThrow(Long id){
        return citaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cita"));
    }

    public Cliente returnCliente(Long id){
        return clienteService.findByIdOrThrow(id);
    }

    public void verificarFechaCita(int anio, int mes, int dia ){

        LocalDate fechaCita = LocalDate.of(anio, mes,dia);
        LocalDate actual = LocalDate.now();

        if( fechaCita.isBefore(actual)){
            throw(new fechaIvalida("Fecha Invalida"));
        }
    }

    private void validarCupo(Date fecha, int hora, int duracion, Long idExcluir) {
        if ((duracion != 2 && duracion != 3) || hora < 8 || hora + duracion > 17) {
            throw new ExistFechaAndHora("La cita debe durar 2 o 3 horas y finalizar antes de las 17:00");
        }
        if (duracion == 3 && hora != 8 && hora + duracion != 17) {
            throw new ExistFechaAndHora("La cita de 3 horas debe ser la primera o la última del día");
        }
        LocalDate diaCita = fecha.toLocalDate();
        if (diaCita.equals(LocalDate.now()) && hora <= LocalTime.now().getHour()) {
            throw new ExistFechaAndHora("No se puede agendar una hora que ya pasó");
        }

        List<Cita> citasDelDia = citaRepository.findByFechaCita(fecha);
        for (Cita citaExistente : citasDelDia) {
            if (citaExistente.getEstadoCita() == EstadoCita.RECHAZADA) {
                continue;
            }
            if (idExcluir != null && idExcluir.equals(citaExistente.getIdCita())) {
                continue;
            }

            boolean seCruza = hora < citaExistente.getHoraCita() + citaExistente.getTiempoCita()
                    && citaExistente.getHoraCita() < hora + duracion;
            if (seCruza) {
                throw new ExistFechaAndHora("Ese horario se cruza con otra cita");
            }
            if (duracion == 3 && citaExistente.getTiempoCita() == 3) {
                throw new ExistFechaAndHora("Solo se permite una cita de 3 horas por día");
            }
        }
    }
}
