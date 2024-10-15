package Model.Entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "latitud", nullable = false)
    private double latitud;
    @Column(name = "longitud", nullable = false)
    private double longitud;
    public Ubicacion() {
    }
    public Ubicacion(int id, double latitud, double longitud) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    public int getId() {
        return id;
    }
    public double getLatitud() {
        return latitud;
    }
    public double getLongitud() {
        return longitud;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
