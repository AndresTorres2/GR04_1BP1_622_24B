package Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Model.DAO.CalleDAO;
import Model.DAO.ViajeDAO;
import Model.Entity.Calle;
import Model.Entity.Ruta;
import Model.Entity.Viaje;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ViajeServlet", value = "/ViajeServlet")
public class ViajeController extends HttpServlet {
    private CalleDAO calleDAO;
    private ViajeDAO viajeDAO;

    public void init() {
        viajeDAO = new ViajeDAO();
        calleDAO = new CalleDAO();
    }

    public void listarViajes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("viajes", viajeDAO.listarViajesPorJornada(req.getParameter("jornada")));

        RequestDispatcher dispatcher = req.getRequestDispatcher("View/listarViajes.jsp");
        dispatcher.forward(req, resp);
    }

    public void verDetallesViaje(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Integer> idViajesList = viajeDAO.convertirCadenaAListaDeIds(req.getParameter("ids"));
        Viaje viaje = viajeDAO.obtenerViajePorCodigo(idViajesList.get(0));
        List<Object[]> callesYCoordenadas = calleDAO.obtenerCallesYCoordenadasPorRutaId(viaje.getRuta().getId());

        req.setAttribute("idViajes", req.getParameter("ids"));
        req.setAttribute("viaje", viaje);
        req.setAttribute("callesYCoordenadas", callesYCoordenadas);

        if (!callesYCoordenadas.isEmpty()) {
            req.setAttribute("origen", callesYCoordenadas.get(0));
            req.setAttribute("destino", callesYCoordenadas.get(callesYCoordenadas.size() - 1));
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("View/detallesViaje.jsp");
        dispatcher.forward(req, resp);
    }


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    public void ruteador(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ruta = (req.getParameter("ruta") == null) ? "seleccionarJornada" : req.getParameter("ruta");
        switch (ruta) {
            case "seleccionarJornada":
                this.listarViajes(req, resp);
                break;
            case "verDetalles":
                this.verDetallesViaje(req, resp);
                break;
            default:
                break;

        }

    }
}
