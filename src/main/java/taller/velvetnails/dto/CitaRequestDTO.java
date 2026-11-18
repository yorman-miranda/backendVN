package taller.velvetnails.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class CitaRequestDTO {

    @Min(1)
    @Max(31)
    @Positive
    private int dia;

    @Min(1)
    @Max(12)
    @Positive
    private int mes;

    @Min(1)
    @Max(9999)
    @Positive
    private int anio;

    @Positive
    @Min(8) @Max(15)
    private int horaCita;
    //@NotBlank
    //private Image comprobantePago;

    @Positive
    @Min(2) @Max(3)
    private int tiempoCita;

    @NotNull
    @Positive
    private Long idCliente;

    //public CitaRequestDTO(int dia, int mes, int anio, float horaCita, Image comprobantePago, int tiempoCita, Long idCliente) {
    public CitaRequestDTO(int dia, int mes, int anio, int horaCita, int tiempoCita, Long idCliente) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.horaCita = horaCita;
        //this.comprobantePago = comprobantePago;
        this.tiempoCita = tiempoCita;
        this.idCliente = idCliente;
    }
}
