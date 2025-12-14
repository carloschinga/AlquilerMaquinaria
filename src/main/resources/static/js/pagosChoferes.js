const API_PAGO_CHOFER = "/api/pagos-choferes";
const API_CHOFERES = "/api/choferes";

let modalPagoChofer = null;

// Formateador de moneda (usando USD como genérico)
const formatterChofer = new Intl.NumberFormat('es-PE', {
    style: 'currency',
    currency: 'PEN',
    minimumFractionDigits: 2,
});

function initPagosChofer() {
  const body = document.getElementById("body-pagos-chofer");

  if (!body) return;

  const modalElement = document.getElementById("modalPagoChofer");
  modalPagoChofer = modalElement ? new bootstrap.Modal(modalElement) : null;

  cargarPagosChofer();
  cargarChoferesCombo();
}

/* -------------------------------
   Carga por "vista-cargada"
------------------------------- */
document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("pagosChoferes.html")) {
    initPagosChofer();
  }
});

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initPagosChofer);
} else {
  initPagosChofer();
}

/* -------------------------------------
   LISTAR (SOPORTE PARA FILTROS)
------------------------------------- */
async function cargarPagosChofer() {
    // 1. Obtener valores de los filtros
    const choferId = document.getElementById("filtroChofer").value;
    const desde = document.getElementById("filtroDesde").value;
    const hasta = document.getElementById("filtroHasta").value;

    let url = API_PAGO_CHOFER;
    const params = [];

    if (choferId) params.push(`choferId=${choferId}`);
    if (desde) params.push(`desde=${desde}`);
    if (hasta) params.push(`hasta=${hasta}`);

    if (params.length > 0) {
        // Preparar la URL para enviar los filtros si la API los soporta
        url += `?${params.join('&')}`;
    }

  try {
    const res = await fetch(url);
    const data = await res.json();

    const body = document.getElementById("body-pagos-chofer");
    if (!body) return;

    body.innerHTML = "";

    data.forEach((p) => {
        // Lógica para badge de estado
        let badgeClass = "bg-success"; // Verde para Pagado
        if (p.estado === "Pendiente") badgeClass = "bg-warning"; // Amarillo para Pendiente
        if (p.estado === "Observado") badgeClass = "bg-danger"; // Rojo para Observado
        
        const montoFormateado = formatterChofer.format(p.montoPagado ?? 0);

      body.innerHTML += `<tr>
<td><strong>#${p.pagoChoferId}</strong></td>
<td>${p.chofer?.nombre ?? "N/A"}</td>
<td>${formatearFechaTabla(p.fechaPago)}</td>
<td>${montoFormateado}</td>
<td>${p.metodoPago.toUpperCase()}</td>
<td><span class="badge ${badgeClass} badge-estado">${p.estado.toUpperCase()}</span></td>
<td>
<button class="btn btn-sm btn-editar me-1" onclick="editarPagoChofer(${p.pagoChoferId})"> Editar</button>
<button class="btn btn-sm btn-eliminar" onclick="eliminarPagoChofer(${p.pagoChoferId})"> Eliminar</button>
</td>
</tr>
`;
    });
  } catch (error) {
    console.error("Error cargando pagos de chofer:", error);
  }
}

/* -------------------------------------
   MODAL NUEVO
------------------------------------- */
function abrirModalPagoChofer() {
  if (!modalPagoChofer) {
    const modalElement = document.getElementById("modalPagoChofer");
    if (modalElement) modalPagoChofer = new bootstrap.Modal(modalElement);
  }

  document.getElementById("modalTituloPagoChofer").innerText =
    "Nuevo Pago a Chofer";

  document.getElementById("formPagoChofer").reset();
  document.getElementById("pagoChoferId").value = "";

  // Valores por defecto
  document.getElementById("estado").value = "Pendiente";
  document.getElementById("metodoPago").value = "transferencia";
  document.getElementById("choferId").selectedIndex = 0; // Seleccionar el primer chofer o la opción "Seleccione"


  modalPagoChofer.show();
}

/* -------------------------------------
   EDITAR
------------------------------------- */
async function editarPagoChofer(id) {
  try {
    const res = await fetch(`${API_PAGO_CHOFER}/${id}`);
    const pago = await res.json();

    if (!modalPagoChofer) {
      const modalElement = document.getElementById("modalPagoChofer");
      if (modalElement) modalPagoChofer = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTituloPagoChofer").innerText =
      "Editar Pago a Chofer";

    document.getElementById("pagoChoferId").value = pago.pagoChoferId;
    // Asegurar compatibilidad (choferId o chofer.choferId)
    document.getElementById("choferId").value = pago.choferId ?? pago.chofer?.choferId ?? "";

    document.getElementById("fechaPago").value =
      convertirFechaInput(pago.fechaPago);

    document.getElementById("montoPagado").value = pago.montoPagado;
    document.getElementById("metodoPago").value = pago.metodoPago;
    document.getElementById("descripcion").value = pago.descripcion ?? "";
    document.getElementById("estado").value = pago.estado;

    modalPagoChofer.show();
  } catch (error) {
    console.error("Error cargando pago chofer:", error);
  }
}

/* -------------------------------------
   GUARDAR (POST / PUT)
------------------------------------- */
async function guardarPagoChofer() {
  const id = document.getElementById("pagoChoferId").value;

   // Validación
   const monto = parseFloat(document.getElementById("montoPagado").value);
   const choferId = document.getElementById("choferId").value;

   if (!choferId || monto <= 0) {
       alert("Debe seleccionar un chofer y el monto debe ser mayor a cero.");
       return;
   }

  const pago = {
    choferId: parseInt(choferId),
    fechaPago: document.getElementById("fechaPago").value,
    montoPagado: monto,
    metodoPago: document.getElementById("metodoPago").value,
    descripcion: document.getElementById("descripcion").value,
    estado: document.getElementById("estado").value,
  };

  const metodo = id ? "PUT" : "POST";
  const url = id ? `${API_PAGO_CHOFER}/${id}` : API_PAGO_CHOFER;

  try {
    await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(pago),
    });

    modalPagoChofer?.hide();
    cargarPagosChofer();
  } catch (error) {
    console.error("Error guardando pago chofer:", error);
  }
}

/* -------------------------------------
   ELIMINAR
------------------------------------- */
async function eliminarPagoChofer(id) {
  if (!confirm("¿Seguro que deseas eliminar este pago?")) return;

  try {
    await fetch(`${API_PAGO_CHOFER}/${id}`, { method: "DELETE" });
    cargarPagosChofer();
  } catch (error) {
    console.error("Error eliminando pago chofer:", error);
  }
}

/* -------------------------------------
   COMBO CHOFERES (COMBO Y FILTRO)
------------------------------------- */
async function cargarChoferesCombo() {
  try {
    const res = await fetch(API_CHOFERES);
    const data = await res.json();

    // 1. Combo del modal (con opción de selección)
    const combo = document.getElementById("choferId");
    combo.innerHTML = '<option value="">Seleccione un chofer</option>';

    data.forEach((c) => {
      combo.innerHTML += `<option value="${c.choferId}">${c.nombre}</option>`;
    });

    // 2. Llenar el filtro superior (filtroChofer)
    const filtro = document.getElementById("filtroChofer");
    if (filtro) {
      filtro.innerHTML = `<option value="">Todos los Choferes</option>`;
      data.forEach((c) => {
        filtro.innerHTML += `<option value="${c.choferId}">${c.nombre}</option>`;
      });
    }

  } catch (error) {
    console.error("Error cargando choferes:", error);
  }
}


/* -------------------------------------
   UTILIDADES FECHA
------------------------------------- */
function convertirFechaInput(fechaIso) {
  if (!fechaIso) return "";
  // Asegura que el formato es YYYY-MM-DDTHH:MM para input[datetime-local]
  return fechaIso.substring(0, 16).replace(" ", "T");
}

function formatearFechaTabla(fechaIso) {
  if (!fechaIso) return "N/A";
  // Formato local amigable: DD/MM/YYYY HH:MM
   const date = new Date(fechaIso);
   return date.toLocaleString('es-CL', { 
       year: 'numeric', 
       month: '2-digit', 
       day: '2-digit', 
       hour: '2-digit', 
       minute: '2-digit',
       hour12: false
   });
}

/* -------------------------------------
   EXPORT GLOBAL
------------------------------------- */
window.cargarPagosChofer = cargarPagosChofer; // Exportar para el botón de búsqueda
window.abrirModalPagoChofer = abrirModalPagoChofer;
window.editarPagoChofer = editarPagoChofer;
window.guardarPagoChofer = guardarPagoChofer;
window.eliminarPagoChofer = eliminarPagoChofer;