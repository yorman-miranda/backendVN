package taller.velvetnails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taller.velvetnails.entity.Cita;
import taller.velvetnails.entity.Cliente;

import java.sql.Date;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findAll();

    List<Cita> findByFechaCita(Date fecha);

    Cita findByFechaCitaAndHoraCita(Date fecha, int HoraCita);

    List<Cita> findByAnio(int anio);

    List<Cita> findByMes(int mes);

    List<Cita> findByDia(int dia);

    List<Cita> findByCliente(Cliente cliente);



}
