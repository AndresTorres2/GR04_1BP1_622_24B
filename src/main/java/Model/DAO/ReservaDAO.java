package Model.DAO;

import Model.Entity.Reserva;
import Model.Entity.Estudiante;
import Model.Entity.Viaje;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO extends GenericDAO {

    public ReservaDAO() {
        super();
    }

    public void guardarReserva(Reserva reserva, Viaje viaje) {
        try {
            beginTransaction();

            em.persist(reserva);

            viaje.setAsientosOcupados(viaje.getAsientosOcupados() + 1);
            em.merge(viaje);

            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
        }
    }

    public List<Reserva> obtenerTodasLasReservas() {
        List<Reserva> reservas = null;
        try {
            beginTransaction();
            TypedQuery<Reserva> query = em.createQuery("SELECT r FROM Reserva r", Reserva.class);
            reservas = query.getResultList();
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
        }
        return reservas;
    }
    public Reserva obtenerReservaPorId(int reservaId) {
        Reserva reserva = null;
        try {
            beginTransaction();  // Iniciar la transacción

            // Crear una consulta parametrizada para obtener la reserva por su ID
            TypedQuery<Reserva> query = em.createQuery("SELECT r FROM Reserva r WHERE r.id = :reservaId", Reserva.class);
            query.setParameter("reservaId", reservaId);  // Pasar el parámetro

            reserva = query.getSingleResult();
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();  // Revertir en caso de error
            e.printStackTrace();
        }
        return reserva;
    }
    public void cancelarReserva(int reservaId, Viaje viaje) {
        try {
            beginTransaction();
            Reserva reserva = em.find(Reserva.class, reservaId);
            em.remove(reserva);
            viaje.setAsientosOcupados(viaje.getAsientosOcupados() - 1);
            em.merge(viaje);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            e.printStackTrace();
        }

    }
    public List<Reserva> obtenerReservasPorDia(int diaSeleccionado) {
        List<Reserva> reservasFiltradas = new ArrayList<>();
        for (Reserva  reserva : obtenerTodasLasReservas())
        {
            int diaReserva = reserva.getViaje().getFecha().getDay();
            if(diaReserva == diaSeleccionado){
                reservasFiltradas.add(reserva);
            }
        }

        return reservasFiltradas;
    }

    public void guardarVariasReservas(List<Viaje> listaViajes, Estudiante estudiante) {
        for (Viaje viaje : listaViajes) {
            Reserva reserva = new Reserva(0, viaje, estudiante, new Date(System.currentTimeMillis()));
            guardarReserva(reserva, viaje);
        }
    }







}
