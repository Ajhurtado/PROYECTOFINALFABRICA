package ec.edu.uce.demo.model;

import ec.edu.uce.demo.repository.Observer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    Usuario usuario;
    @Column(name = "fecha")
    private Timestamp fecha;
    @Column(name = "estado_pedido")
    @Enumerated(EnumType.STRING)
    private Estado estado_pedido;
    @Column(name = "num_factura")
    private String num_factura;
    @Column(name = "total")
    private double total;

    public Pedido(Estado estado_pedido, int id) {
        this.estado_pedido = estado_pedido;
        this.id = id;
    }

    public Pedido(Usuario usuario, Timestamp fecha, Estado estado_pedido, String num_factura, double total) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.estado_pedido = estado_pedido;
        this.num_factura = num_factura;
        this.total = total;
    }
}
