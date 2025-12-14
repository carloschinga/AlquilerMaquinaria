const API_PAGOS_PEND = "/api/reportes/choferes/pagos-pendientes";


const formatterRepoDEe = new Intl.NumberFormat('es-PE', {
    style: 'currency',
    currency: 'PEN', 
    minimumFractionDigits: 2,
});


function initPagosPendientes() {
  const body = document.getElementById("bodyPagosPendientes");
  if (!body) return;

  // Cargar automáticamente cuando la vista se abra
  cargarPagosPendientes();
}

// Detecta cuando SPA carga esta vista
document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("reporteDeudaChoferes.html")) {
    initPagosPendientes();
  }
});

// Carga inicial por si no es SPA
if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initPagosPendientes);
} else {
  initPagosPendientes();
}

// ------------------------------------------------------
// FUNCIÓN PRINCIPAL: Cargar Pagos Pendientes
// ------------------------------------------------------
async function cargarPagosPendientes() {
  try {
    const res = await fetch(API_PAGOS_PEND);
    
    if (!res.ok) {
        throw new Error(`Error en la API: ${res.statusText}`);
    }
    
    const data = await res.json();

    const body = document.getElementById("bodyPagosPendientes");
    body.innerHTML = "";

    if (!data || data.length === 0) {
      body.innerHTML = `<tr>
<td colspan="4" class="text-center text-muted p-3">
<i class="fas fa-check-circle me-2"></i> ¡Excelente! No hay pagos pendientes registrados.
</td>
</tr>
`;
      return;
    }
    
    // Calcular el monto total pendiente para todo el reporte
    let totalPendiente = 0;

    data.forEach(row => {
        const monto = Number(row.montoPendientePago) || 0;
        totalPendiente += monto;
        const montoFormateado = formatterRepoDEe.format(monto);
        
      body.innerHTML += `<tr>
<td>#${row.choferId}</td>
<td class="text-start">${row.nombreChofer}</td>
<td class="monto-pendiente-col">${montoFormateado}</td>
<td class="turnos-pendientes-col">${row.numeroTurnosPendientes ?? 0}</td>
</tr>
`;
    });
    
    // Agregar fila de totales
    body.innerHTML += `<tr class="table-warning">
<td colspan="2" class="text-end"><strong>TOTAL PENDIENTE:</strong></td>
<td class="monto-pendiente-col"><strong>${formatterRepoDEe.format(totalPendiente)}</strong></td>
<td></td>
</tr>`;

  } catch (err) {
    console.error("Error cargando pagos pendientes:", err);
    const body = document.getElementById("bodyPagosPendientes");
    body.innerHTML = `<tr><td colspan="4" class="text-danger p-3">Error al cargar los datos. Intente de nuevo.</td></tr>`;
  }
}