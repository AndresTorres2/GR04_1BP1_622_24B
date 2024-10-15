package Controller;

import Model.DAO.*;
import Model.Entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "ReservarAsientoServlet", value = "/ReservarAsientoServlet")
public class ReservaController extends HttpServlet {
    private ReservaDAO reservaDAO;

    private EstudianteDAO estudianteDAO;
    private ViajeDAO viajeDAO;
    private CalleDAO calleDAO;

    public void init() {
        reservaDAO = new ReservaDAO();

        estudianteDAO = new EstudianteDAO();
        viajeDAO = new ViajeDAO();
        calleDAO = new CalleDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.ruteador(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.ruteador(request, response);
    }

    private void ruteador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (request.getParameter("action") == null) ? "listar" : request.getParameter("action");
        switch (action) {
            case "guardarReserva":
                guardarReserva(request, response);
                break;
            case "formularioReserva":
                mostrarFormularioReserva(request, response);
                break;
            case "consultarReservas":
                consultarReservas(request, response);
                break;
            case "verReservasDia":
                verReservasDia(request, response);
                break;
            case "detalleReserva":
                mostrarReserva(request, response);
                break;
            case "cancelarReserva":
                cancelarReserva(request, response);
                break;
            default:
                break;
        }
    }

    private void mostrarFormularioReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setAttribute("viajesList", viajeDAO.obtenerViajesPorIds(request.getParameter("idsViaje")));
        request.getRequestDispatcher("/View/reservarAsiento.jsp").forward(request, response);
    }

    private void guardarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] viajesIdsSeleccionados = request.getParameterValues("idsViajesSeleccionados");
        Estudiante estudiante = estudianteDAO.obtenerEstudiantePorId("202110777");
        List<Viaje> listaViajes = viajeDAO.obtenerListaDeViajes(viajesIdsSeleccionados);
        reservaDAO.guardarVariasReservas(listaViajes, estudiante);

        response.sendRedirect(request.getContextPath() + "/View/listarViajes.jsp");


    }

    private void verReservasDia(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        request.setAttribute("reservas", reservaDAO.obtenerReservasPorDia(Integer.parseInt(request.getParameter("dia"))));
        request.getRequestDispatcher("/View/consultarReservas.jsp").forward(request, response);
    }

    private void consultarReservas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/View/consultarReservas.jsp").forward(request, response);

    }

    private void mostrarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Reserva reservaSeleccionada = reservaDAO.obtenerReservaPorId( Integer.parseInt(request.getParameter("reservaId")));
        List<Object[]> callesYCoordenadas = calleDAO.obtenerCallesYCoordenadasPorRutaId(reservaSeleccionada.getViaje().getRuta().getId());
        request.setAttribute("callesYCoordenadas", callesYCoordenadas);
        request.setAttribute("reserva", reservaSeleccionada);

        if (!callesYCoordenadas.isEmpty()) {
            request.setAttribute("origen", callesYCoordenadas.get(0)); // Primera calle
            request.setAttribute("destino", callesYCoordenadas.get(callesYCoordenadas.size() - 1)); // Última calle
        }
        request.getRequestDispatcher("/View/detallesReserva.jsp").forward(request, response);

    }

    private void cancelarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        reservaDAO.cancelarReserva(Integer.parseInt(request.getParameter("reservaId")),
                reservaDAO.obtenerReservaPorId(Integer.parseInt(request.getParameter("reservaId"))).getViaje());
        response.sendRedirect(request.getContextPath() + "/ReservarAsientoServlet?action=consultarReservas");

    }
}