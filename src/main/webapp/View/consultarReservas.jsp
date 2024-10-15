<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>Consultar Reservas</title>
  <style>
    body {
      background: #100f0f;
      color: #dcdcdc;
      font-family: Arial, sans-serif;
    }

    .container {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .tabs {
      display: flex;
      justify-content: center;
      margin-bottom: 20px;
    }

    .tab {
      padding: 15px 30px;
      margin: 0 5px;
      background-color: #1c1c1c;
      border: 1px solid #dcdcdc;
      color: #dcdcdc;
      cursor: pointer;
      text-align: center;
      text-decoration: none;
    }

    .tab:hover {
      background-color: #48578e;
    }

    h2 {
      margin-bottom: 20px;
    }

    .reserva {
      background-color: #1c1c1c;
      padding: 20px;
      border: 1px solid #dcdcdc;
      margin-bottom: 15px;
      width: 80%;
    }
    a{
      text-decoration: none;
      color: #dcdcdc;
    }
  </style>
</head>
<body>
<div class="container">
  <h1>Consultar Reservas</h1>

  <div class="tabs">
    <a class="tab" href="${pageContext.request.contextPath}/ReservarAsientoServlet?action=verReservasDia&dia=1">Lunes</a>
    <a class="tab" href="${pageContext.request.contextPath}/ReservarAsientoServlet?action=verReservasDia&dia=2">Martes</a>
    <a class="tab" href="${pageContext.request.contextPath}/ReservarAsientoServlet?action=verReservasDia&dia=3">Miércoles</a>
    <a class="tab" href="${pageContext.request.contextPath}/ReservarAsientoServlet?action=verReservasDia&dia=4">Jueves</a>
    <a class="tab" href="${pageContext.request.contextPath}/ReservarAsientoServlet?action=verReservasDia&dia=5">Viernes</a>
  </div>
  <c:set var="fechaAnterior" value="" />
  <c:forEach var="reserva" items="${reservas}">
    <c:if test="${reserva.fecha != fechaAnterior}">
      <h2>Reservas para el día: <fmt:formatDate value="${reserva.viaje.fecha}" pattern="EEEE"/></h2>
      <c:set var="fechaAnterior" value="${reserva.fecha}" />
    </c:if>
    <div class="reserva">
      <a  href="${pageContext.request.contextPath}/ReservarAsientoServlet?action=detalleReserva&reservaId=${reserva.id}">
        <p><strong>Viaje #${reserva.viaje.id}</strong></p>
        <p>Fecha de la reserva: <fmt:formatDate value="${reserva.fecha}" pattern="yyyy-MM-dd"/></p>
        <p>Horario: ${reserva.viaje.horaDeSalida} (${reserva.viaje.jornada})</p>
        <p>Ruta: Desde ${reserva.viaje.ruta.origen} hasta ${reserva.viaje.ruta.destino}</p>
        <p>Nombre del estudiante: ${reserva.estudiante.nombre} ${reserva.estudiante.apellido}</p>
      </a>
    </div>
  </c:forEach>

</div>
</body>
</html>
