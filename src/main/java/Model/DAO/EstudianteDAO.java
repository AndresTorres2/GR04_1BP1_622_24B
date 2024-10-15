package Model.DAO;

import Model.Entity.Estudiante;
import jakarta.persistence.Query;

public class EstudianteDAO extends GenericDAO {


    public Estudiante obtenerEstudiantePorId(String idEstudiante) {
            return em.find(Estudiante.class, idEstudiante);
    }



}
