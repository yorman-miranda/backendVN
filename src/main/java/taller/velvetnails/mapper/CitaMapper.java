package taller.velvetnails.mapper;

import org.springframework.stereotype.Component;
import taller.velvetnails.conversorImagen.ConvertirImagen;
import taller.velvetnails.dto.CitaRequestDTO;
import taller.velvetnails.dto.CitaResponseDTO;
import taller.velvetnails.entity.Cita;
import taller.velvetnails.entity.Cliente;

//import java.awt.image.BufferedImage;
import java.sql.Date;
import java.time.LocalDate;

@Component
public class CitaMapper {

    public Cita toEntity(CitaRequestDTO dto, Cliente cliente){
        Cita cita = new Cita();
        ConvertirImagen convertirImagen = new ConvertirImagen();

        cita.setFechaCita(Date.valueOf(LocalDate.of(dto.getAnio(), dto.getMes(), dto.getDia())));
        cita.setTiempoCita(dto.getTiempoCita());
        cita.setHoraCita(dto.getHoraCita());
        //cita.setComprobanteAdelantoCita(convertirImagen.convertToDatabaseColumn((BufferedImage) dto.getComprobantePago()));
        cita.setCliente(cliente);

        return cita;
    }

    public CitaResponseDTO toDTO(Cita cita){
        //ConvertirImagen convertirImagen = new ConvertirImagen();
        ClienteMapper clienteMapper = new ClienteMapper();
        LocalDate fecha = cita.getFechaCita().toLocalDate();
        return new CitaResponseDTO(
                cita.getIdCita(),
                fecha.getDayOfMonth(),
                fecha.getMonthValue(),
                fecha.getYear(),
                cita.getHoraCita(),
                //convertirImagen.convertToEntityAttribute(cita.getComprobanteAdelantoCita()),
                cita.getTiempoCita(),
                cita.isAsistio(),
                cita.getEstadoCita(),
                clienteMapper.toDTO(cita.getCliente())
                );
    }
}
