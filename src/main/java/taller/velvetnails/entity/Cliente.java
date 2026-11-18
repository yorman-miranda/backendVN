package taller.velvetnails.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "cliente", indexes = {
        @Index(name = "idx_cliente_telefono", columnList = "telefono")
})
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "nombre", length = 50 ,nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "documento", length = 15, nullable = false, unique = true)
    private String documento;

    @Column(name = "telefono", length = 15, nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cita> cita = new HashSet<>();

    public Cliente(){}

    public Cliente(String nombre, String apellido, String documento, String telefono){
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
    }

}
