const API_CARGA = "/api/cargas-combustible";
let modalCarga = null;

// Formateador de moneda para USD/moneda genérica
const formatter = new Intl.NumberFormat('es-PE', {
    style: 'currency',
    currency: 'PEN',
    minimumFractionDigits: 2,
});

function initCargasCombustible() {
  const body = document.getElementById("body-cargas-combustible");
  if (!body) return;

  const modalElement = document.getElementById("modalCarga");
  modalCarga = modalElement ? new bootstrap.Modal(modalElement) : null;

  cargarCargas();
  cargarMaquinariasComboCombustible();
  cargarProveedoresComboCombustible();

  // Calcular costo total automáticamente
  const litrosInput = document.getElementById("litrosCargados");
  const costoUnitarioInput = document.getElementById("costoUnitario");
  const costoTotalInput = document.getElementById("costoTotal");

  litrosInput.addEventListener("input", calcularCostoTotal);
  costoUnitarioInput.addEventListener("input", calcularCostoTotal);

  function calcularCostoTotal() {
    const litros = parseFloat(litrosInput.value) || 0;
    const costoUnit = parseFloat(costoUnitarioInput.value) || 0;
    // Se usa Math.round para evitar problemas de precisión de coma flotante, luego se fija a 2 decimales
    costoTotalInput.value = (Math.round((litros * costoUnit) * 100) / 100).toFixed(2);
  }
}

document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("cargasCombustible.html")) {
    initCargasCombustible();
  }
});

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initCargasCombustible);
} else {
  initCargasCombustible();
}

// ---------------------------------------------------------
// LISTAR
// ---------------------------------------------------------
async function cargarCargas() {
  try {
    const res = await fetch(API_CARGA);
    const data = await res.json();

    const body = document.getElementById("body-cargas-combustible");
    if (!body) return;

    body.innerHTML = "";

    data.forEach((c) => {
        // Lógica para asignar color al badge según el estado de pago
        let badgeClass = "bg-danger"; // Rojo para Pendiente
        if (c.estadoPago === "Pagado") badgeClass = "bg-success"; // Verde para Pagado
        
        const costoUnitFormateado = formatter.format(c.costoUnitario);
        const costoTotalFormateado = formatter.format(c.costoTotal);


      body.innerHTML += `<tr>
<td><strong>#${c.cargaId}</strong></td>
<td>${c.maquinaModelo ?? ""} - ${c.maquinaTipo ?? ""}</td>
<td>${c.proveedorNombre ?? ""}</td>
<td>${formatearFechaTabla(c.fechaCarga)}</td>
<td>${c.litrosCargados} L</td>
<td>${costoUnitFormateado}</td>
<td>${costoTotalFormateado}</td>
<td>${c.lecturaHorometro} h</td>
<td>${c.facturaNum ?? "N/A"}</td>
<td><span class="badge ${badgeClass} badge-estado">${c.estadoPago.toUpperCase() ?? "N/A"}</span></td>
<td>
<button class="btn btn-sm btn-editar me-1" onclick="editarCarga(${c.cargaId})"> Editar</button>
<button class="btn btn-sm btn-eliminar" onclick="eliminarCarga(${c.cargaId})"> Eliminar</button>
</td>
</tr>
`;
    });
  } catch (error) {
    console.error("Error cargando cargas:", error);
  }
}

// ---------------------------------------------------------
// MODAL NUEVO
// ---------------------------------------------------------
function abrirModalNuevaCarga() {
  if (!modalCarga) {
    const modalElement = document.getElementById("modalCarga");
    if (modalElement) modalCarga = new bootstrap.Modal(modalElement);
  }

  document.getElementById("modalTituloCarga").innerText = "Nueva Carga de Combustible";
  document.getElementById("formCarga").reset();
  document.getElementById("cargaId").value = "";

  // Default estadoPago a Pendiente
  document.getElementById("estadoPago").value = "Pendiente";
   // Seleccionar la primera opción de combo por defecto (si la hay)
   document.getElementById("maquinariaId").selectedIndex = 0;
   document.getElementById("proveedorId").selectedIndex = 0;

  modalCarga.show();
}

// ---------------------------------------------------------
// EDITAR
// ---------------------------------------------------------
async function editarCarga(id) {
  try {
    const res = await fetch(`${API_CARGA}/${id}`);
    const carga = await res.json();

    if (!modalCarga) {
      const modalElement = document.getElementById("modalCarga");
      if (modalElement) modalCarga = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTituloCarga").innerText = "Editar Carga de Combustible";

    document.getElementById("cargaId").value = carga.cargaId;
    document.getElementById("maquinariaId").value = carga.maquinariaId;
    document.getElementById("proveedorId").value = carga.proveedorId;

    document.getElementById("fechaCarga").value = convertirFechaInput(carga.fechaCarga);
    document.getElementById("litrosCargados").value = carga.litrosCargados;
    document.getElementById("costoTotal").value = carga.costoTotal;
    document.getElementById("costoUnitario").value = carga.costoUnitario;
    document.getElementById("lecturaHorometro").value = carga.lecturaHorometro;

    document.getElementById("facturaNum").value = carga.facturaNum ?? "";
    document.getElementById("estadoPago").value = carga.estadoPago ?? "Pendiente";

    modalCarga.show();

  } catch (error) {
    console.error("Error al cargar la carga:", error);
  }
}

// ---------------------------------------------------------
// GUARDAR (POST / PUT)
// ---------------------------------------------------------
async function guardarCarga() {
  const id = document.getElementById("cargaId").value;

  // Validaciones
  const litros = parseFloat(document.getElementById("litrosCargados").value);
  const costoUnitario = parseFloat(document.getElementById("costoUnitario").value);
  const lecturaHorometro = parseFloat(document.getElementById("lecturaHorometro").value);


  if (litros < 0 || costoUnitario < 0 || lecturaHorometro < 0) {
    alert("Litros, costo unitario y lectura de horómetro deben ser mayores o iguales a 0.");
    return;
  }

  const maquinariaIdValue = document.getElementById("maquinariaId").value;
  const proveedorIdValue = document.getElementById("proveedorId").value;

  if (!maquinariaIdValue || !proveedorIdValue) {
    alert("Debe seleccionar una Maquinaria y un Proveedor.");
    return;
  }


  const carga = {
    maquinariaId: parseInt(maquinariaIdValue),
    proveedorId: parseInt(proveedorIdValue),
    fechaCarga: document.getElementById("fechaCarga").value,
    litrosCargados: litros,
    costoUnitario: costoUnitario,
    // Asegurar que costoTotal usa el valor calculado/actual
    costoTotal: parseFloat(document.getElementById("costoTotal").value),
    lecturaHorometro: lecturaHorometro,
    facturaNum: document.getElementById("facturaNum").value,
    estadoPago: document.getElementById("estadoPago").value,
  };

  const metodo = id ? "PUT" : "POST";
  const url = id ? `${API_CARGA}/${id}` : API_CARGA;

  try {
    // Aquí debería haber manejo de errores de la API (res.ok) como en otros módulos, 
    // pero para mantener la consistencia con el código original, lo dejamos simplificado.
    await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(carga),
    });

    modalCarga?.hide();
    cargarCargas();

  } catch (error) {
    console.error("Error guardando carga:", error);
  }
}

// ---------------------------------------------------------
// ELIMINAR
// ---------------------------------------------------------
async function eliminarCarga(id) {
  if (!confirm("¿Seguro que deseas eliminar esta carga?")) return;

  try {
    await fetch(`${API_CARGA}/${id}`, { method: "DELETE" });
    cargarCargas();
  } catch (error) {
    console.error("Error eliminando carga:", error);
  }
}

// ---------------------------------------------------------
// COMBOS
// ---------------------------------------------------------
// Asume que la Maquinaria API devuelve { maquinaId, modelo, tipo }
async function cargarMaquinariasComboCombustible() {
  try {
    const res = await fetch("/api/maquinarias");
    const data = await res.json();

    const combo = document.getElementById("maquinariaId");
    combo.innerHTML = '<option value="">Seleccione una máquina</option>'; // Opción por defecto

    data.forEach((m) => {
      combo.innerHTML += `<option value="${m.maquinaId}">${m.modelo} - ${m.tipo}</option>`;
    });

  } catch (error) {
    console.error("Error cargando maquinarias:", error);
  }
}

// Asume que la Proveedores API devuelve { proveedorId, nombre }
async function cargarProveedoresComboCombustible() {
  try {
    const res = await fetch("/api/proveedores");
    const data = await res.json();

    const combo = document.getElementById("proveedorId");
    combo.innerHTML = '<option value="">Seleccione un proveedor</option>'; // Opción por defecto

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
window.abrirModalNuevaCarga = abrirModalNuevaCarga;
window.editarCarga = editarCarga;
window.guardarCarga = guardarCarga;
window.eliminarCarga = eliminarCarga;