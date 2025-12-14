const API_UTILIZACION = "/api/contratos/utilizacion";

function initUtilizacion() {
  const body = document.getElementById("body-utilizacion");
  if (!body) return;
}

document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("utilizacion.html")) {
    initUtilizacion();
  }
});

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initUtilizacion);
} else {
  initUtilizacion();
}


async function buscarUtilizacion() {
  const inicio = document.getElementById("filtroInicio").value;
  const fin = document.getElementById("filtroFin").value;

  if (!inicio || !fin) {
    alert("Selecciona ambas fechas de inicio y fin.");
    return;
  }
  


  try {
    const res = await fetch(`${API_UTILIZACION}?inicio=${encodeURIComponent(inicio)}&fin=${encodeURIComponent(fin)}`);
    
    if (!res.ok) {
        throw new Error(`Error en la API: ${res.statusText}`);
    }
    
    const data = await res.json();

    const body = document.getElementById("body-utilizacion");
    body.innerHTML = "";

    if (data.length === 0) {
        body.innerHTML = `<tr><td colspan="4">No se encontraron datos de utilización para el período seleccionado.</td></tr>`;
        return;
    }

    data.forEach(u => {
     
      const horasFormateadas = `${(u.horasUsadas ?? 0).toFixed(2)} h`;

      body.innerHTML += `<tr>
<td><strong>#${u.maquinaId}</strong></td>
<td>${u.modelo ?? "N/A"}</td>
<td class="col-horas">${horasFormateadas}</td>
<td>${u.cantidadContratos ?? 0}</td>
</tr>
`;
    });

  } catch (error) {
    console.error("Error cargando utilización:", error);
    const body = document.getElementById("body-utilizacion");
    body.innerHTML = `<tr><td colspan="4" class="text-danger">Error al cargar los datos: ${error.message}</td></tr>`;
  }
}


window.buscarUtilizacion = buscarUtilizacion;