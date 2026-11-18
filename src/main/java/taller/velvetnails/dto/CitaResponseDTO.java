package taller.velvetnails.dto;

import lombok.Getter;
import lombok.Setter;
import taller.velvetnails.entity.EstadoCita;


@Getter
@Setter
public class CitaResponseDTO {

    private Long idCita;
    private int dia;
    private int mes;
    private int anio;
    private int horaCita;
    //private Image comprobanteAdelantoCita;
    private int tiempoCita;
    private boolean asistio;
    private EstadoCita estadoCita;
    private ClienteResponseDTO cliente;

/*    public CitaResponseDTO(Long idCita, int dia, int mes, int anio, int horaCita, Image comprobanteAdelantoCita, int tiempoCita, Long idCliente) {
        this.idCita = idCita;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.horaCita = horaCita;
        this.comprobanteAdelantoCita = comprobanteAdelantoCita;
        this.tiempoCita = tiempoCita;
        this.idCliente = idCliente;
        }
*/
        public CitaResponseDTO(Long idCita, int dia, int mes, int anio, int horaCita, int tiempoCita,
                               boolean asistio, EstadoCita estadoCita, ClienteResponseDTO cliente) {
            this.idCita = idCita;
            this.dia = dia;
            this.mes = mes;
            this.anio = anio;
            this.horaCita = horaCita;
            this.tiempoCita = tiempoCita;
            this.asistio = asistio;
            this.estadoCita = estadoCita;
            this.cliente = cliente;
        }
}
