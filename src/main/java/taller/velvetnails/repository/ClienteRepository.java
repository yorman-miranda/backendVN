package taller.velvetnails.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import taller.velvetnails.entity.Cliente;

import java.util.Date;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAll();

    List<Cliente> findByTelefono(String telefono);

    List<Cliente> findByNombre(String nombre);

    Cliente findByDocumento(String documento);

    @Query("select distinct cliente from Cliente cliente left join fetch cliente.cita where cliente.documento = :documento")
    Cliente findByDocumentoConCitas(@Param("documento") String documento);

    @Query("select distinct cliente from Cliente cliente left join fetch cliente.cita where cliente.telefono = :telefono")
    List<Cliente> findByTelefonoConCitas(@Param("telefono") String telefono);

    List<Cliente> findByNombreOrApellido(String nombre, String apellido);

}
