package Model.DAO;

import Model.Entity.Viaje;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

public class ViajeDAO extends GenericDAO{
    public ViajeDAO() {
        super();
    }

    public List<Object[]> listarViajesPorJornada(String jornada) {
        List<Object[]> resultList = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT v.horaDeSalida, r.origen, r.destino, (SELECT GROUP_CONCAT(v2.id)  " +
                    "FROM Viajes v2 WHERE v2.rutaId = v.rutaId AND v2.jornada = :jornada ) AS idViajes " +
                    "FROM Viajes v " +
                    "JOIN Rutas r ON v.rutaId = r.id " +
                    "WHERE v.jornada = :jornada " +
                    "AND v.rutaId IN ( " +
                    "    SELECT DISTINCT rutaId " +
                    "    FROM Viajes " +
                    "    WHERE jornada = :jornada " +
                    ") " +
                    "ORDER BY r.origen, r.destino;";

            Query query = em.createNativeQuery(sql);
            query.setParameter("jornada", jornada);

            resultList = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
    public Viaje obtenerViajePorCodigo(int codigo) {
        try {
            Viaje viaje = em.find(Viaje.class, codigo);
            if (viaje != null) {
                em.refresh(viaje);
            }
            return viaje;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
