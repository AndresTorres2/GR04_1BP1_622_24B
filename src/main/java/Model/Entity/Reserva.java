package Model.Entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "reserva")
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "viajeId", nullable = false)
    private Viaje viaje;
    @ManyToOne
    @JoinColumn(name = "estudianteId", nullable = false)
    private Estudiante estudiante;
    @Column(name = "fecha", nullable = false)
    private Date fecha;
    public Reserva() {
    }

    public Reserva(int id, Viaje viaje, Estudiante estudiante, Date fecha) {
        this.id = id;
        this.viaje = viaje;
        this.estudiante = estudiante;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int idReserva) {
        this.id = idReserva;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fechaReserva) {
        this.fecha = fechaReserva;
    }

}
