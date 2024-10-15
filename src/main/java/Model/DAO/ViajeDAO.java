package Model.DAO;

import Model.Entity.Viaje;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int[] convertirIdsAEnteros(String[] idsViajesSeleccionados) {
        if (idsViajesSeleccionados != null) {
            return Arrays.stream(idsViajesSeleccionados)
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }
        return new int[0];
    }
    public List<Viaje> obtenerListaDeViajes(String[] idsViajes) {
        List<Viaje> listaViajes = new ArrayList<>();
        for (int id : convertirIdsAEnteros(idsViajes)) {
                listaViajes.add(obtenerViajePorCodigo(id));
        }
        return listaViajes;
    }


    public List<Integer> convertirCadenaAListaDeIds(String idsViajes) {
        List<Integer> viajesIdList = new ArrayList<>();

        if (idsViajes != null && !idsViajes.isEmpty()) {
            String[] idArray = idsViajes.split(",");
            for (String id : idArray) {
                try {
                    viajesIdList.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("ID inválido: " + id);
                }
            }
        }
        return viajesIdList;
    }

    public List<Viaje> obtenerViajesPorIds(String idsViajes) {
        List<Viaje> viajesList = new ArrayList<>();
        for (Integer idViaje : convertirCadenaAListaDeIds(idsViajes)) {
                viajesList.add(obtenerViajePorCodigo(idViaje));
        }

        return viajesList;
    }



}
