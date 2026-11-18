package taller.velvetnails.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import taller.velvetnails.dto.ErrorResponseDTO;

import java.time.DateTimeException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRequestURI("/cita");
        request = mockRequest;
    }

    @Test
    void mapsMissingResourcesToNotFound() {
        ResponseEntity<ErrorResponseDTO> response =
                handler.handleNotFound(new ResourceNotFoundException("Cita"), request);

        assertError(response, HttpStatus.NOT_FOUND, "Cita no existe");
    }

    @Test
    void mapsDuplicateDocumentsToConflict() {
        ResponseEntity<ErrorResponseDTO> response =
                handler.handleExistClienteByDocument(new ExistClienteByDocument("Cliente"), request);

        assertError(response, HttpStatus.CONFLICT, "El documento ya existe; no se puede repetir.");
    }

    @Test
    void mapsInvalidBusinessDatesToBadRequest() {
        ResponseEntity<ErrorResponseDTO> response =
                handler.handleInvalidDate(new DateTimeException("Invalid date"), request);

        assertError(response, HttpStatus.BAD_REQUEST, "La fecha indicada no es valida.");
    }

    @Test
    void mapsPastAppointmentDatesToBadRequest() {
        ResponseEntity<ErrorResponseDTO> response =
                handler.handleInvalidDate(new fechaIvalida("Fecha invalida"), request);

        assertError(response, HttpStatus.BAD_REQUEST, "La fecha indicada no es valida.");
    }

    @Test
    void mapsInvalidAppointmentTimesToBadRequest() {
        ResponseEntity<ErrorResponseDTO> response =
                handler.handleInvalidAppointmentTime(new ExistFechaAndHora("Horario ocupado"), request);

        assertError(response, HttpStatus.BAD_REQUEST, "Horario ocupado");
    }

    @Test
    void mapsDatabaseConstraintConflictsToConflict() {
        ResponseEntity<ErrorResponseDTO> response =
                handler.handleDataConflict(new DataIntegrityViolationException("duplicate"), request);

        assertError(response, HttpStatus.CONFLICT, "La operacion entra en conflicto con datos existentes.");
    }

    private void assertError(
            ResponseEntity<ErrorResponseDTO> response,
            HttpStatus expectedStatus,
            String expectedMessage
    ) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals(expectedStatus.value(), response.getBody().getStatus());
        assertEquals(expectedMessage, response.getBody().getMessage());
        assertEquals("/cita", response.getBody().getPath());
    }
}
