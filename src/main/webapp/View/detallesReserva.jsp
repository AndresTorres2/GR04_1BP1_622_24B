<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Detalles de la Reserva</title>
    <style>
        body {
            background: #100f0f;
            color: #dcdcdc;
            font-family: Arial, sans-serif;
        }

        .container {
            width: 80%;
            margin: auto;
            padding: 20px;
            background: #1c1c1c;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5);
        }

        h1, h2 {
            text-align: center;
        }

        ul {
            list-style-type: none;
            padding: 0;
        }

        li {
            margin: 5px 0;
        }

        button {
            all: unset;
            cursor: pointer;
            background-color: #48578e;
            border-radius: 5px;
            padding: 0.75rem 1.5rem;
            margin: 0;
        }

        a {
            display: block;
            text-align: center;
            text-decoration: none;
            padding: 10px;
            background-color: #48578e;
            color: white;
            border-radius: 5px;
            margin: 20px 0;
        }

        a:hover {
            background-color: #71a8df;
        }

        .leaflet-top, .leaflet-bottom {
            display: none;
        }

        #map {
            height: 600px;
            width: 100%;
            margin: 20px 0;
        }

    </style>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.2.0/dist/leaflet.css"/>
    <link rel="stylesheet" href="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.css"/>
</head>
<body>
<div class="container">
    <h1>Detalles de la Reserva</h1>
    <h2>Bus #${reserva.viaje.bus.id}</h2>
    <h3>${reserva.viaje.ruta.origen} ➜ ${reserva.viaje.ruta.destino}</h3>
    <p><strong>Fecha de viaje:</strong> <fmt:formatDate value="${reserva.viaje.fecha}"
                                                        pattern="EEEE"/>, ${reserva.viaje.fecha}</p>
    <p><strong>Fecha de la reserva:</strong> <fmt:formatDate value="${reserva.fecha}" pattern="EEEE"/>, ${reserva.fecha}
    </p>
    <p><strong>Horario:</strong> ${reserva.viaje.horaDeSalida} (${reserva.viaje.jornada})</p>


    <p><strong>Recorrido:</strong>
        <c:forEach var="calle" items="${reserva.viaje.ruta.calles}" varStatus="status">
            ${calle.nombre}
            <c:if test="${!status.last}">, </c:if>
        </c:forEach>
    </p>

    <p><strong>Nombre del estudiante:</strong> ${reserva.estudiante.nombre} ${reserva.estudiante.apellido}</p>

    <div style="display: flex; justify-content: space-between; align-items: center; width: 100%">
        <button id="add-waypoint" style="align-items: center; display: block;">Establecer Parada</button>
        <div style="display: flex; gap: 1rem">
            <p>Notificar proximidad del bus</p>
            <input type="checkbox">
        </div>
    </div>

    <div id="map"></div>
    <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1.5rem">
        <a href="javascript:history.back();" class="tab">Regresar a Reservas</a>
        <a style="background: #501d1b"
           href="${pageContext.request.contextPath}/ReservarAsientoServlet?action=cancelarReserva&reservaId=${reserva.id}"
           onclick="return confirm('¿Está seguro de que desea cancelar la reserva?');">
            Cancelar Reserva
        </a>
    </div>
</div>
<script src="https://unpkg.com/leaflet@1.2.0/dist/leaflet.js"></script>
<script src="https://unpkg.com/leaflet-routing-machine@latest/dist/leaflet-routing-machine.js"></script>
<script>
    var map = L.map('map').setView([-0.210194, -78.489326], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 20,
    }).addTo(map);

    var waypoints = [];
    var waypointAvailable = false;

    function addMarker(latitud, longitud, popupText, icon = null) {
        const markerOptions = icon ? { icon: icon } : {};
        L.marker([latitud, longitud], markerOptions).addTo(map).bindPopup(popupText);
    }

    <c:forEach var="calle" items="${callesYCoordenadas}">
    waypoints.push(L.latLng(${calle[2]}, ${calle[3]}));
    addMarker(${calle[2]}, ${calle[3]}, '${calle[1]}');
    </c:forEach>

    var routingControl = L.Routing.control({
        waypoints: waypoints,
        routeWhileDragging: false,
        draggableWaypoints: false,
        autoRoute: false,
        editable: false,
    }).addTo(map);

    routingControl.getPlan().on('click', function (e) {
        e.preventDefault();
    });

    routingControl.route();

    var paradaIcon = L.icon({
        iconUrl: "${pageContext.request.contextPath}/assets/paradaIcon.png",
        iconSize: [25, 41],
    });

    var busIcon = L.icon({
        iconUrl: "${pageContext.request.contextPath}/assets/busIcon.png",
        iconSize: [40, 40],
    });

    addMarker(-0.332250, -78.553271, null, busIcon);

    map.on('click', function (e) {
        if (waypointAvailable) {
            addMarker(e.latlng.lat, e.latlng.lng, null, paradaIcon);
            waypointAvailable = false;
        }
    });

    document.getElementById('add-waypoint').addEventListener('click', function () {
        alert('Haz clic en el mapa para agregar un nuevo waypoint. Solo se podrá agregar uno.');
        waypointAvailable = true;
    });

</script>
</body>
</html>
