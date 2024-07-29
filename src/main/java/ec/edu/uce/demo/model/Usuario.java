package ec.edu.uce.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
    @Column(name = "nombreUsuario")
    private String nombreUsuario;
    @Column(name = "apellido")
    private String apellido;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "provincia")
    private String provincia;
    @Column(name = "localidad")
    private String localidad;
    @Column(name = "pais")
    private String pais;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "rol")
    @Enumerated(EnumType.STRING)
    private Roles rol;

    public Usuario(String nombre) {
        this.nombre = nombre;
    }
    public Usuario(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
    }
    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public Usuario(String nombre, String email, String password, String nombreUsuario, String apellido, String direccion, String provincia, String localidad, String pais, String telefono, Roles rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.nombreUsuario = nombreUsuario;
        this.apellido = apellido;
        this.direccion = direccion;
        this.provincia = provincia;
        this.localidad = localidad;
        this.pais = pais;
        this.telefono = telefono;
        this.rol = rol;
    }

    public Usuario(int id, String nombreUsuario, String apellido, String telefono, String direccion, String pais, String provincia, String localidad) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
    }
}
