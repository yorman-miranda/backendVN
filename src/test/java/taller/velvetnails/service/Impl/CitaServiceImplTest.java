package taller.velvetnails.service.Impl;

import org.junit.jupiter.api.Test;
import taller.velvetnails.entity.Cita;
import taller.velvetnails.exception.ModificacionCitaFueraDePlazoException;

import java.sql.Date;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CitaServiceImplTest {

    private final CitaServiceImpl service = new CitaServiceImpl(null, null, null);

    @Test
    void permitsModificationAtLeast24HoursBeforeAppointment() {
        LocalDateTime now = LocalDateTime.of(2026, 11, 17, 10, 0);
        Cita cita = citaPara(now.plusHours(24));

        assertDoesNotThrow(() -> service.validarAnticipacion(cita, now.plusHours(24), now));
    }

    @Test
    void rejectsModificationLessThan24HoursBeforeAppointment() {
        LocalDateTime now = LocalDateTime.of(2026, 11, 17, 10, 0);
        Cita cita = citaPara(now.plusHours(23).plusMinutes(59));

        assertThrows(
                ModificacionCitaFueraDePlazoException.class,
                () -> service.validarAnticipacion(cita, now.plusHours(48), now)
        );
    }

    @Test
    void rejectsModificationOfAnAppointmentThatHasAlreadyPassed() {
        LocalDateTime now = LocalDateTime.of(2026, 11, 17, 10, 0);
        Cita cita = citaPara(now.minusHours(1));

        assertThrows(
                ModificacionCitaFueraDePlazoException.class,
                () -> service.validarAnticipacion(cita, now.plusHours(48), now)
        );
    }

    @Test
    void rejectsMovingAppointmentToWithin24Hours() {
        LocalDateTime now = LocalDateTime.of(2026, 11, 17, 10, 0);
        Cita cita = citaPara(now.plusHours(48));

        assertThrows(
                ModificacionCitaFueraDePlazoException.class,
                () -> service.validarAnticipacion(cita, now.plusHours(23), now)
        );
    }

    private Cita citaPara(LocalDateTime inicio) {
        Cita cita = new Cita();
        cita.setFechaCita(Date.valueOf(inicio.toLocalDate()));
        cita.setHoraCita(inicio.getHour());
        return cita;
    }
}
