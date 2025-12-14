const API_PAGOS_PROV = "/api/pagos-proveedores";
const API_PROVEEDORES = "/api/proveedores";

let modalPagoProveedor = null;

// Formateador de moneda (asumiendo moneda local, usando USD como genérico)
const formatterPagoPro = new Intl.NumberFormat('es-PE', {
    style: 'currency',
    currency: 'PEN',
    minimumFractionDigits: 2,
});

function initPagosProveedores() {
  const body = document.getElementById("body-pagos-proveedores");

  if (!body) return;

  const modalElement = document.getElementById("modalPagoProveedor");
  modalPagoProveedor = modalElement ? new bootstrap.Modal(modalElement) : null;

  cargarPagosProveedores();
  cargarProveedoresComboPagos();
}

document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("pagosProveedores.html")) {
    initPagosProveedores();
  }
});

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initPagosProveedores);
} else {
  initPagosProveedores();
}

// ---------------------------------------------------------
// LISTAR
// ---------------------------------------------------------
async function cargarPagosProveedores() {
  try {
    const res = await fetch(API_PAGOS_PROV);
    const data = await res.json();

    const body = document.getElementById("body-pagos-proveedores");
    if (!body) return;

    body.innerHTML = "";

    data.forEach((p) => {
        // Lógica para badge de estado
        let badgeClass = "bg-warning"; // Amarillo para Pendiente
        if (p.estado === "Pagado") badgeClass = "bg-success"; // Verde para Pagado
        if (p.estado === "Anulado") badgeClass = "bg-secondary"; // Gris para Anulado
        
        const montoFormateado = formatterPagoPro.format(p.montoPagado ?? 0);

      body.innerHTML += `<tr>
<td><strong>#${p.pagoProveedorId}</strong></td>
<td>${p.proveedor?.nombre ?? ""}</td>
<td>${formatearFechaTabla(p.fechaPago)}</td>
<td>${montoFormateado}</td>
<td>${p.metodoPago}</td>
<td>${p.descripcion ?? ""}</td>
<td><span class="badge ${badgeClass} badge-estado">${p.estado.toUpperCase() ?? "N/A"}</span></td>
<td>
<button class="btn btn-sm btn-editar me-1" onclick="editarPagoProveedor(${p.pagoProveedorId})"> Editar</button>
<button class="btn btn-sm btn-eliminar" onclick="eliminarPagoProveedor(${p.pagoProveedorId})"> Eliminar</button>
 </td>
</tr>
`;
    });

  } catch (error) {
    console.error("Error cargando pagos proveedores:", error);
  }
}

// ---------------------------------------------------------
// MODAL NUEVO
// ---------------------------------------------------------
function abrirModalNuevoPagoProveedor() {
  if (!modalPagoProveedor) {
    const modalElement = document.getElementById("modalPagoProveedor");
    if (modalElement) modalPagoProveedor = new bootstrap.Modal(modalElement);
  }

  document.getElementById("modalTituloPagoProveedor").innerText = "Nuevo Pago a Proveedor";
  document.getElementById("formPagoProveedor").reset();
  document.getElementById("pagoProveedorId").value = "";

  // Default estado a Pendiente
  document.getElementById("estado").value = "Pendiente";
  // Default de combo
  document.getElementById("proveedorId").selectedIndex = 0;


  modalPagoProveedor.show();
}

// ---------------------------------------------------------
// EDITAR
// ---------------------------------------------------------
async function editarPagoProveedor(id) {
  try {
    const res = await fetch(`${API_PAGOS_PROV}/${id}`);
    const p = await res.json();

    if (!modalPagoProveedor) {
      const modalElement = document.getElementById("modalPagoProveedor");
      if (modalElement) modalPagoProveedor = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTituloPagoProveedor").innerText =
      "Editar Pago a Proveedor";

    document.getElementById("pagoProveedorId").value = p.pagoProveedorId;
    // Asegurar que proveedorId existe antes de asignar
    document.getElementById("proveedorId").value = p.proveedorId ?? p.proveedor?.proveedorId ?? "";
    document.getElementById("fechaPago").value =
      convertirFechaInput(p.fechaPago);

    document.getElementById("montoPagado").value = p.montoPagado;
    document.getElementById("metodoPago").value = p.metodoPago;
    document.getElementById("descripcion").value = p.descripcion ?? "";
    document.getElementById("estado").value = p.estado ?? "Pendiente"; // Fallback

    modalPagoProveedor.show();

  } catch (error) {
    console.error("Error cargando pago proveedor:", error);
  }
}

// ---------------------------------------------------------
// GUARDAR (POST / PUT)
// ---------------------------------------------------------
async function guardarPagoProveedor() {
  const id = document.getElementById("pagoProveedorId").value;

   // Validación simple de campos requeridos
   const monto = parseFloat(document.getElementById("montoPagado").value);
   const proveedorId = document.getElementById("proveedorId").value;

   if (!proveedorId || monto <= 0) {
       alert("Debe seleccionar un proveedor y el monto debe ser mayor a cero.");
       return;
   }

  const pago = {
    proveedorId: parseInt(proveedorId),
    fechaPago: document.getElementById("fechaPago").value,
    montoPagado: monto,
    metodoPago: document.getElementById("metodoPago").value,
    descripcion: document.getElementById("descripcion").value,
    estado: document.getElementById("estado").value,
  };

  const metodo = id ? "PUT" : "POST";
  const url = id ? `${API_PAGOS_PROV}/${id}` : API_PAGOS_PROV;

  try {
    await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(pago),
    });

    modalPagoProveedor?.hide();
    cargarPagosProveedores();

  } catch (error) {
    console.error("Error guardando pago proveedor:", error);
  }
}

// ---------------------------------------------------------
// ELIMINAR
// ---------------------------------------------------------
async function eliminarPagoProveedor(id) {
  if (!confirm("¿Seguro que deseas eliminar este pago?")) return;

  try {
    await fetch(`${API_PAGOS_PROV}/${id}`, { method: "DELETE" });
    cargarPagosProveedores();
  } catch (error) {
    console.error("Error eliminando pago proveedor:", error);
  }
}

// ---------------------------------------------------------
// COMBOS
// ---------------------------------------------------------
async function cargarProveedoresComboPagos() {
  try {
    const res = await fetch(API_PROVEEDORES);
    const data = await res.json();

    const combo = document.getElementById("proveedorId");
    combo.innerHTML = '<option value="">Seleccione un proveedor</option>';

    data.forEach((p) => {
      combo.innerHTML += `<option value="${p.proveedorId}">${p.nombre}</option>`;
    });

  } catch (error) {
    console.error("Error cargando proveedores:", error);
  }
}

// ---------------------------------------------------------
// UTILIDADES DE FECHA
// ---------------------------------------------------------
function convertirFechaInput(fechaIso) {
  if (!fechaIso) return "";
  // Asegura que el formato es compatible con input[type="datetime-local"] (YYYY-MM-DDTHH:MM)
  return fechaIso.substring(0, 16).replace(" ", "T");
}

function formatearFechaTabla(fechaIso) {
  if (!fechaIso) return "N/A";
  // Formato más amigable para la tabla: DD/MM/YYYY HH:MM
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

// ---------------------------------------------------------
window.abrirModalNuevoPagoProveedor = abrirModalNuevoPagoProveedor;
window.editarPagoProveedor = editarPagoProveedor;
window.guardarPagoProveedor = guardarPagoProveedor;
window.eliminarPagoProveedor = eliminarPagoProveedor;