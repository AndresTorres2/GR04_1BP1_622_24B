package Model.DAO;

import Model.Entity.Calle;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class CalleDAO extends GenericDAO {
    public List<Calle> obtenerCallesPorRutaId(int rutaId) {
        List<Calle> calles = new ArrayList<>();
        try {
            String sql = "SELECT c.id, c.nombre " +
                    "FROM rutas_calles rc " +
                    "JOIN calles c ON rc.calleId = c.id " +
                    "WHERE rc.rutaId = :rutaId";

            Query query = em.createNativeQuery(sql);
            query.setParameter("rutaId", rutaId);

            List<Object[]> resultList = query.getResultList();

            for (Object[] result : resultList) {
                Calle calle = new Calle();
                calle.setId((Integer) result[0]);
                calle.setNombre((String) result[1]);
                calles.add(calle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calles;
    }
    public List<Object[]> obtenerCallesYCoordenadasPorRutaId(int rutaId) {
        List<Object[]> callesConCoordenadas = new ArrayList<>();
        try {
            String sql = "SELECT c.id, c.nombre, u.latitud, u.longitud " +
                    "FROM rutas_calles rc " +
                    "JOIN calles c ON rc.calleId = c.id " +
                    "JOIN ubicaciones u ON c.ubicacionId = u.id " +
                    "WHERE rc.rutaId = :rutaId";

            Query query = em.createNativeQuery(sql);
            query.setParameter("rutaId", rutaId);

            List<Object[]> resultList = query.getResultList();
            callesConCoordenadas.addAll(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return callesConCoordenadas;
    }

}
