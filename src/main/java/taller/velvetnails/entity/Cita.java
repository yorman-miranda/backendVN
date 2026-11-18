package taller.velvetnails.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;

@Entity
@Setter
@Getter
@Table(name = "cita", indexes = {
        @Index(name = "idx_cita_cliente_id", columnList = "cliente_id")
})
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long idCita;

    @Column(name = "fecha_cita", nullable = false)
    private Date fechaCita;

    @Column(name = "hora_cita", nullable = false)
    private int horaCita;

/*    @Lob
    @Column(name = "comprobante-adelanto-cita", columnDefinition = "VARBINARY(MAX)", nullable = false)
   private byte[] comprobanteAdelantoCita;
*/

    @Column(name = "tiempo_cita", nullable = false)
    private int tiempoCita;

    @Column(name = "asistio", nullable = false)
    @ColumnDefault("false")
    private boolean asistio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cita", nullable = false, length = 20)
    @ColumnDefault("'PENDIENTE_PAGO'")
    private EstadoCita estadoCita = EstadoCita.PENDIENTE_PAGO;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Cita(){
    }

    /*public Cita(Date fechaCita, int horaCita, int tiempoCita, byte[] comprobanteAdelantoCita){
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.tiempoCita = tiempoCita;
        this.comprobanteAdelantoCita = comprobanteAdelantoCita;
    }*/

    public Cita(Date fechaCita, int horaCita, int tiempoCita){
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.tiempoCita = tiempoCita;
    }
    public int getDia(){
        return fechaCita.getDay();
    }

    public int getMes(){
        return fechaCita.getMonth();
    }

    public int getAnio(){
        return fechaCita.getYear();
    }
}
