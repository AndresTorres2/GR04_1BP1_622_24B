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
        String viajesIds = request.getParameter("idsViaje");
        List<Integer> viajesIdList = new ArrayList<>();
        if (viajesIds != null && !viajesIds.isEmpty()) {

            String[] idArray = viajesIds.split(",");


            for (String id : idArray) {
                viajesIdList.add(Integer.parseInt(id.trim()));

            }
        }
        List<Viaje> viajesList = new ArrayList<>();
        // Iterar sobre la lista de IDs de viajes
        for (Integer idViaje : viajesIdList) {
            // Obtener el objeto Viaje a partir del ID
            Viaje viaje = viajeDAO.obtenerViajePorCodigo(idViaje);
            if (viaje != null) {
                viajesList.add(viaje);
            } else {
                System.out.println("No se encontró el viaje con ID: " + idViaje);
            }
        }
        for (Viaje viaje : viajesList) {
            System.out.println("ID Viaje: " + viaje.getId() + ", Asientos Ocupados: " + viaje.getAsientosOcupados());
        }
        request.setAttribute("viajesList", viajesList);
        request.getRequestDispatcher("/View/reservarAsiento.jsp").forward(request, response);
    }

    private void guardarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String[] viajesIdsSeleccionados = request.getParameterValues("idsViajesSeleccionados");
        List<Viaje> listaViajes = new ArrayList<>();
        Estudiante estudiante = estudianteDAO.obtenerEstudiantePorId("202110777");
        if (viajesIdsSeleccionados != null) {
            int[] idsViajesEnteros = Arrays.stream(viajesIdsSeleccionados)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            ViajeDAO viajeDAO = new ViajeDAO();  // Asegúrate de que el DAO está correctamente inicializado

            for (int id : idsViajesEnteros) {
                Viaje viaje = viajeDAO.obtenerViajePorCodigo(id); // Método para obtener el viaje por ID
                if (viaje != null) {
                    listaViajes.add(viaje);
                }
            }

        }
        for (Viaje viaje : listaViajes) {

            Reserva reserva = new Reserva(0, viaje, estudiante, new Date(System.currentTimeMillis()));
            reservaDAO.guardarReserva(reserva, viaje);
        }
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        response.sendRedirect(request.getContextPath() + "/View/listarViajes.jsp");


    }

    private void verReservasDia(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String diaSeleccionado = request.getParameter("dia");
        List<Reserva> reservas = reservaDAO.obtenerTodasLasReservas();
        List<Reserva> reservasFiltradas = new ArrayList<>();
        for (Reserva reserva : reservas) {

            String diaReserva = String.valueOf(reserva.getViaje().getFecha().getDay());

            if (diaReserva.equals(diaSeleccionado)) {
                reservasFiltradas.add(reserva);
            }
        }

        request.setAttribute("reservas", reservasFiltradas);

        request.getRequestDispatcher("/View/consultarReservas.jsp").forward(request, response);
    }

    private void consultarReservas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/View/consultarReservas.jsp").forward(request, response);

    }

    private void mostrarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservaId = Integer.parseInt(request.getParameter("reservaId"));

        Reserva reservaSeleccionada = reservaDAO.obtenerReservaPorId(reservaId);

        Viaje viaje = reservaSeleccionada.getViaje();
        Ruta ruta = viaje.getRuta();
        int idRuta = ruta.getId();

        List<Object[]> callesYCoordenadas = calleDAO.obtenerCallesYCoordenadasPorRutaId(idRuta);
        request.setAttribute("callesYCoordenadas", callesYCoordenadas);
        request.setAttribute("reserva", reservaSeleccionada);

        if (!callesYCoordenadas.isEmpty()) {
            request.setAttribute("origen", callesYCoordenadas.get(0)); // Primera calle
            request.setAttribute("destino", callesYCoordenadas.get(callesYCoordenadas.size() - 1)); // Última calle
        }

        request.getRequestDispatcher("/View/detallesReserva.jsp").forward(request, response);

    }

    private void cancelarReserva(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int reservaId = Integer.parseInt(request.getParameter("reservaId"));
        Reserva reserva = reservaDAO.obtenerReservaPorId(reservaId);
        Viaje viaje = viajeDAO.obtenerViajePorCodigo(reserva.getViaje().getId());


        reservaDAO.cancelarReserva(reservaId, viaje);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.sendRedirect(request.getContextPath() + "/ReservarAsientoServlet?action=consultarReservas");

    }
}