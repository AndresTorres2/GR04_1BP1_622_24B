package Model.DAO;

import Model.Entity.Estudiante;
import jakarta.persistence.Query;

public class EstudianteDAO extends GenericDAO {


    public Estudiante obtenerEstudiantePorId(String idEstudiante) {
        Estudiante estudiante = null;
        try {
            estudiante = em.find(Estudiante.class, idEstudiante);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return estudiante;
    }



}
