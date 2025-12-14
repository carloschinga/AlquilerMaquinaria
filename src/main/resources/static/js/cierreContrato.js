const API_CONTRATOSCIERRE = "/api/contratos";
var horI;
let modalCierreContrato = null;

function initCierreContrato() {
  const body = document.getElementById("body-cierre-contratos");
  if (!body) return;

  const modalElement = document.getElementById("modalCierreContrato");
  modalCierreContrato = modalElement ? new bootstrap.Modal(modalElement) : null;

  cargarContratosCierre();
}

document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("cierreContrato.html")) {
    initCierreContrato();
  }
});

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initCierreContrato);
} else {
  initCierreContrato();
}

// ---------------------------------------------------------
// LISTAR CONTRATOS PARA CIERRE
// ---------------------------------------------------------
async function cargarContratosCierre() {
  try {
    const res = await fetch(API_CONTRATOSCIERRE);
    const data = await res.json();

    const body = document.getElementById("body-cierre-contratos");
    if (!body) return;

    body.innerHTML = "";

    data.forEach((c) => {
      // Determinar color del badge según estado de pago
      let badgeClass = "bg-warning";
      if (c.estadoPago === "Pagado") badgeClass = "bg-success";
      if (c.estadoPago === "Pendiente") badgeClass = "bg-danger";

      body.innerHTML += `
        <tr>
            <td><strong>#${c.contratoId}</strong></td>
            <td>${c.clienteNombre ?? ""}</td>
            <td><strong>${c.maquinaModelo ?? ""}</strong></td>
            <td>${formatearFechaTabla(c.fechaInicio)}</td>
            <td>${formatearFechaTabla(c.fechaFinEstimada)}</td>
            <td>${formatearFechaTabla(c.fechaFinReal)}</td>
            <td>${c.horometroInicial ?? ""}</td>
            <td>${c.horometroFinal ?? ""}</td>
            <td><strong>${c.tiempoUsoHrs ?? ""}</strong></td>
            <td>S/ ${c.tarifaAplicada ?? ""}</td>
            <td><span class="monto-total">S/ ${c.montoTotalCobrar?.toFixed(2) ?? "0.00"}</span></td>
            <td><span class="badge ${badgeClass} badge-estado-cierre">${c.estadoPago ?? ""}</span></td>
            <td>${c.condicionEntrega ?? ""}</td>
            <td>${c.condicionDevolucion ?? ""}</td>
            <td>
                <button class="btn btn-cerrar-contrato btn-sm" onclick="editarCierreContrato(${c.contratoId})">🔒 Cerrar</button>
            </td>
        </tr>
      `;
    });

  } catch (error) {
    console.error("Error cargando contratos para cierre:", error);
  }
}

// ---------------------------------------------------------
// MODAL NUEVO CIERRE
// ---------------------------------------------------------
function abrirModalCierreContrato() {
  if (!modalContrato) {
    const modalElement = document.getElementById("modalContrato");
    if (modalElement) modalContrato = new bootstrap.Modal(modalElement);
  }

  document.getElementById("formContrato").reset();
  document.getElementById("contratoId").value = "";
  document.getElementById("modalTituloContrato").innerText = "Nuevo Contrato";


  cargarClientesComboContrato();
  cargarMaquinariasComboContrato(); 
  cargarChoferesComboContrato();

  modalContrato.show();
}


// ---------------------------------------------------------
// EDITAR CIERRE CONTRATO
// ---------------------------------------------------------
async function editarCierreContrato(id) {
  try {
    const res = await fetch(`${API_CONTRATOSCIERRE}/${id}`);
    const c = await res.json();

    if (!modalCierreContrato) {
      const modalElement = document.getElementById("modalCierreContrato");
      if (modalElement) modalCierreContrato = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTituloCierreContrato").innerText = "🔒 Cerrar Contrato";

    document.getElementById("contratoIdCierre").value = c.contratoId;
    document.getElementById("fechaFinReal").value = convertirFechaInput(c.fechaFinReal);
    document.getElementById("horometroFinal").value = c.horometroFinal ?? "";
    document.getElementById("tiempoUsoHrs").value = c.tiempoUsoHrs ?? "";
    document.getElementById("montoTotalCobrar").value = c.montoTotalCobrar ?? "";
    document.getElementById("estadoPagoCierre").value = c.estadoPago ?? "";
    document.getElementById("condicionDevolucion").value = c.condicionDevolucion ?? "";
    horI = c.horometroInicial;
    
    // campos ocultos necesarios para cálculo
    document.getElementById("fechaInicioHidden").value = c.fechaInicio;
    document.getElementById("tarifaAplicadaHidden").value = c.tarifaAplicada ?? 0;

    modalCierreContrato.show();

  } catch (error) {
    console.error("Error cargando contrato para cierre:", error);
  }
}

// ---------------------------------------------------------
// CALCULAR TIEMPO DE USO Y MONTO TOTAL EN TIEMPO REAL
// ---------------------------------------------------------
const fechaFinRealInput = document.getElementById("fechaFinReal");
const horometroFinalInput = document.getElementById("horometroFinal");
const tiempoUsoInput = document.getElementById("tiempoUsoHrs");
const montoTotalInput = document.getElementById("montoTotalCobrar");

if (fechaFinRealInput) fechaFinRealInput.addEventListener("change", calcularCierre);
if (horometroFinalInput) horometroFinalInput.addEventListener("input", calcularCierre);
if (tiempoUsoInput) tiempoUsoInput.addEventListener("input", calcularCierrePorTiempo);

function calcularCierre() {
  const fechaInicioStr = document.getElementById("fechaInicioHidden").value;
  const fechaFinRealStr = document.getElementById("fechaFinReal").value;

  if (!fechaInicioStr || !fechaFinRealStr) return;
  
  const horometroFinal = parseFloat(document.getElementById("horometroFinal").value);
  
  if (horometroFinal < horI) {
    alert("⚠️ Error: El horómetro final no puede ser menor al inicial.");
    return;
  }

  const fechaInicio = new Date(fechaInicioStr);
  const fechaFinReal = new Date(fechaFinRealStr);

  if (isNaN(fechaInicio) || isNaN(fechaFinReal)) return;

  const tarifa = parseFloat(document.getElementById("tarifaAplicadaHidden").value || 0);

  let tiempoUso = (fechaFinReal - fechaInicio) / (1000 * 60 * 60); // horas
  if (tiempoUso < 0) tiempoUso = 0;

  document.getElementById("tiempoUsoHrs").value = tiempoUso.toFixed(2);
  document.getElementById("montoTotalCobrar").value = (tiempoUso * tarifa).toFixed(2);
}


function calcularCierrePorTiempo() {
  // Esta función permite que si editas tiempo de uso, se recalculen el monto total
  const tarifa = parseFloat(document.getElementById("tarifaAplicadaHidden").value || 0);
  const tiempoUso = parseFloat(tiempoUsoInput.value) || 0;

  montoTotalInput.value = (tiempoUso * tarifa).toFixed(2);
}

// ---------------------------------------------------------
// GUARDAR CIERRE CONTRATO
// ---------------------------------------------------------
async function guardarCierreContrato() {
  // forzamos el cálculo
  calcularCierre();

  const id = document.getElementById("contratoIdCierre").value;

  const contrato = {
    fechaFinReal: document.getElementById("fechaFinReal").value,
    horometroFinal: parseFloat(document.getElementById("horometroFinal").value),
    tiempoUsoHrs: parseFloat(document.getElementById("tiempoUsoHrs").value) || 0,
    montoTotalCobrar: parseFloat(document.getElementById("montoTotalCobrar").value) || 0,
    estadoPago: document.getElementById("estadoPagoCierre").value,
    condicionDevolucion: document.getElementById("condicionDevolucion").value
  };

  try {
    await fetch(`${API_CONTRATOSCIERRE}/cerrar/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(contrato),
    });

    modalCierreContrato?.hide();
    cargarContratosCierre();

  } catch (error) {
    console.error("Error guardando cierre de contrato:", error);
  }
}


// ---------------------------------------------------------
// UTILIDADES FECHA
// ---------------------------------------------------------
function convertirFechaInput(fechaIso) {
  if (!fechaIso) return "";
  return fechaIso.replace(" ", "T");
}

function formatearFechaTabla(fechaIso) {
  if (!fechaIso) return "";
  return fechaIso.replace("T", " ");
}

// ---------------------------------------------------------
window.abrirModalCierreContrato = abrirModalCierreContrato;
window.editarCierreContrato = editarCierreContrato;
window.guardarCierreContrato = guardarCierreContrato;