const API_CONTRATOS = "/api/contratos";
const API_CLIENTES = "/api/clientes";
const API_MAQUINARIAS = "/api/maquinarias";
const API_CHOFERES2 = "/api/choferes";

let modalContrato = null;

function initContratos() {
  const body = document.getElementById("body-contratos");
  if (!body) return;

  const modalElement = document.getElementById("modalContrato");
  modalContrato = modalElement ? new bootstrap.Modal(modalElement) : null;

  cargarContratos();
  cargarClientesComboContrato();
  cargarMaquinariasComboContrato();
  cargarChoferesComboContrato();
}

document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("nuevoContrato.html")) {
    initContratos();
  }
});

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initContratos);
} else {
  initContratos();
}

// ---------------------------------------------------------
// LISTAR CONTRATOS
// ---------------------------------------------------------
async function cargarContratos() {
  try {
    const res = await fetch(API_CONTRATOS);
    const data = await res.json();

    const body = document.getElementById("body-contratos");
    if (!body) return;

    body.innerHTML = "";

    data.forEach((c) => {
      
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
            <td><span class="badge ${badgeClass} badge-estado-pago">${c.estadoPago ?? ""}</span></td>
            <td><strong style="color: #27ae60;">S/ ${c.montoTotalCobrar?.toFixed(2) ?? "0.00"}</strong></td>
            <td>
                <button class="btn btn-editar-contrato btn-sm me-1" onclick="editarContrato(${c.contratoId})">✏️ Editar</button>
                <button class="btn btn-eliminar-contrato btn-sm" onclick="eliminarContrato(${c.contratoId})">🗑️ Eliminar</button>
            </td>
        </tr>
      `;
    });

  } catch (error) {
    console.error("Error cargando contratos:", error);
  }
}


// ---------------------------------------------------------
// MODAL NUEVO CONTRATO
// ---------------------------------------------------------
function abrirModalNuevoContrato() {
  if (!modalContrato) {
    const modalElement = document.getElementById("modalContrato");
    if (modalElement) modalContrato = new bootstrap.Modal(modalElement);
  }

  document.getElementById("formContrato").reset();
  document.getElementById("contratoId").value = "";
  document.getElementById("modalTituloContrato").innerText = "📝 Nuevo Contrato";

  modalContrato.show();
}

// ---------------------------------------------------------
// EDITAR CONTRATO
// ---------------------------------------------------------
async function editarContrato(id) {
  try {
    const res = await fetch(`${API_CONTRATOS}/${id}`);
    const c = await res.json();

    if (!modalContrato) {
      const modalElement = document.getElementById("modalContrato");
      if (modalElement) modalContrato = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTituloContrato").innerText = "✏️ Editar Contrato";

    document.getElementById("contratoId").value = c.contratoId;
    document.getElementById("clienteIdcontrato").value = c.clienteId;

    cargarMaquinariasComboContrato(c.maquinaId);

    document.getElementById("fechaInicio").value = convertirFechaInput(c.fechaInicio);
    document.getElementById("fechaFinEstimada").value = convertirFechaInput(c.fechaFinEstimada);

    document.getElementById("horometroInicial").value = c.horometroInicial ?? "";
    document.getElementById("tarifaAplicada").value = c.tarifaAplicada ?? "";
    document.getElementById("estadoPago").value = c.estadoPago ?? "";
    document.getElementById("condicionEntrega").value = c.condicionEntrega ?? "";

    cargarChoferesComboContrato(c.choferId ?? 0); 

    document.getElementById("horasIniciales").value = c.horasIniciales ?? 0;
    document.getElementById("fechaTurnoInicial").value = convertirFechaInput(c.fechaTurnoInicial);

    modalContrato.show();

  } catch (error) {
    console.error("Error cargando contrato:", error);
  }
}



// ---------------------------------------------------------
// GUARDAR CONTRATO
// ---------------------------------------------------------
async function guardarContrato() {

  const id = document.getElementById("contratoId").value;
  const choferSeleccionado = parseInt(document.getElementById("choferId").value);

  const contrato = {
    clienteId: parseInt(document.getElementById("clienteIdcontrato").value),
    maquinaId: parseInt(document.getElementById("maquinaIdcontrato").value),
    fechaInicio: document.getElementById("fechaInicio").value,
    fechaFinEstimada: document.getElementById("fechaFinEstimada").value,
    horometroInicial: parseFloat(document.getElementById("horometroInicial").value) || 0,
    tarifaAplicada: parseFloat(document.getElementById("tarifaAplicada").value) || 0,
    estadoPago: document.getElementById("estadoPago").value,
    condicionEntrega: document.getElementById("condicionEntrega").value,
    horasIniciales: parseFloat(document.getElementById("horasIniciales").value) || 0,
    fechaTurnoInicial: document.getElementById("fechaTurnoInicial").value.split("T")[0] || null,

    choferId: choferSeleccionado === 0 ? null : choferSeleccionado  
  };

  let metodo = "POST";
  let url = `${API_CONTRATOS}/crear`;

  if (id && id !== "") {
    contrato.contratoId = parseInt(id);
    metodo = "PUT";
    url = `${API_CONTRATOS}/${id}`;
  } 

  try {
    await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(contrato),
    });

    modalContrato?.hide();
    cargarContratos();

  } catch (error) {
    console.error("Error guardando contrato:", error);
  }
}




// ---------------------------------------------------------
// ELIMINAR CONTRATO
// ---------------------------------------------------------
async function eliminarContrato(id) {
  if (!confirm("⚠️ ¿Seguro que deseas eliminar este contrato?")) return;

  try {
    await fetch(`${API_CONTRATOS}/${id}`, { method: "DELETE" });
    cargarContratos();
  } catch (error) {
    console.error("Error eliminando contrato:", error);
  }
}

// ---------------------------------------------------------
// CARGAR COMBOS
// ---------------------------------------------------------
async function cargarClientesComboContrato() {
  const combo = document.getElementById("clienteIdcontrato");
  if (!combo) return;

  try {
    const res = await fetch(API_CLIENTES);
    const data = await res.json();

    combo.innerHTML = "";
    data.forEach((c) => {
      combo.innerHTML += `<option value="${c.clienteId}">${c.nombre}</option>`;
    });

  } catch (error) {
    console.error("Error cargando clientes:", error);
  }
}

async function cargarMaquinariasComboContrato(maquinaActualId = null) {
  const combo = document.getElementById("maquinaIdcontrato");
  if (!combo) return;

  try {
    const res = await fetch(API_MAQUINARIAS);
    const data = await res.json();

    combo.innerHTML = "";

    data.forEach((m) => {
      if (m.estado === "Disponible" || m.maquinaId === maquinaActualId) {

        const selected = m.maquinaId === maquinaActualId ? "selected" : "";

        combo.innerHTML += `
          <option value="${m.maquinaId}" ${selected}>
            ${m.modelo} - ${m.tipo}
          </option>`;
      }
    });

    if (combo.innerHTML === "") {
      combo.innerHTML = `<option value="">No hay máquinas disponibles</option>`;
    }

  } catch (error) {
    console.error("Error cargando maquinarias:", error);
  }
}


// ---------------------------------------------------------
// CARGAR COMBO CHOFERES
// ---------------------------------------------------------
async function cargarChoferesComboContrato(choferActualId = null) {
  const combo = document.getElementById("choferId");
  if (!combo) return;

  try {
    const res = await fetch(API_CHOFERES2);
    const data = await res.json();

    combo.innerHTML = `<option value="0">Sin chofer</option>`;

    data.forEach((ch) => {
      if (ch.estado === "Activo" || ch.choferId === choferActualId) {

        const selected = (ch.choferId === choferActualId) ? "selected" : "";

        combo.innerHTML += `
          <option value="${ch.choferId}" ${selected}>
            ${ch.nombre}
          </option>`;
      }
    });

  } catch (error) {
    console.error("Error cargando choferes:", error);
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
window.abrirModalNuevoContrato = abrirModalNuevoContrato;
window.editarContrato = editarContrato;
window.guardarContrato = guardarContrato;
window.eliminarContrato = eliminarContrato;