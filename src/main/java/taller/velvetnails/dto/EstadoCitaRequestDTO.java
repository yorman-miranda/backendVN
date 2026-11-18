package taller.velvetnails.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import taller.velvetnails.entity.EstadoCita;

@Getter
@Setter
public class EstadoCitaRequestDTO {

    @NotNull
    private EstadoCita estadoCita;
}
