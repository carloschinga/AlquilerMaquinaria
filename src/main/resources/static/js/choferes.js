const API_CHOFER = "/api/choferes";
let modalChofer = null;

function initChoferes() {
  const body = document.getElementById("body-choferes");

  // Si la vista no es choferes, no hacer nada
  if (!body) return;

  const modalElement = document.getElementById("modalChofer");
  modalChofer = modalElement ? new bootstrap.Modal(modalElement) : null;

  cargarChoferes();
}

// Detectar cuando se carga la vista
document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("choferes.html")) {
    initChoferes();
  }
});

// Si el DOM ya cargó
if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initChoferes);
} else {
  initChoferes();
}

// LISTAR CHOFERES

async function cargarChoferes() {
  try {
    const res = await fetch(API_CHOFER);
    const data = await res.json();

    const body = document.getElementById("body-choferes");
    if (!body) return;

    body.innerHTML = "";

    data.forEach((ch) => {
        // Lógica para asignar color al badge según el estado
        let badgeClass = "bg-warning"; // Por defecto: Inactivo o color neutro
        if (ch.estado === "Activo") badgeClass = "bg-success"; // Verde para Activo
        if (ch.estado === "Ocupado") badgeClass = "bg-danger"; // Rojo para Ocupado

      body.innerHTML += `<tr>
<td><strong>#${ch.choferId}</strong></td>
<td>${ch.nombre}</td>
<td>${ch.identificacion}</td>
<td>S/. ${ch.tarifaHora.toFixed(2)}</td>
<td><span class="badge ${badgeClass} badge-estado">${ch.estado.toUpperCase()}</span></td>
<td>
<button class="btn btn-sm btn-editar me-1" onclick="editarChofer(${ch.choferId})"> Editar</button>
<button class="btn btn-sm btn-eliminar" onclick="eliminarChofer(${ch.choferId})"> Eliminar</button>
</td>
</tr>
`;
    });
  } catch (error) {
    console.error("Error cargando choferes:", error);
  }
}
//VALIDAR
function validarDNI(dni) {
  const dniRegex = /^[0-9]{8}$/; 
  return dniRegex.test(dni);
}

// ABRIR MODAL NUEVO


function abrirModalNuevoChofer() {
  if (!modalChofer) {
    const modalElement = document.getElementById("modalChofer");
    if (modalElement) modalChofer = new bootstrap.Modal(modalElement);
  }

  document.getElementById("modalTituloChofer").innerText = "Nuevo Chofer";
  document.getElementById("formChofer").reset();
  document.getElementById("choferId").value = "";

  modalChofer.show();
}


// EDITAR

async function editarChofer(id) {
  try {
    const res = await fetch(`${API_CHOFER}/${id}`);
    const ch = await res.json();

    if (!modalChofer) {
      const modalElement = document.getElementById("modalChofer");
      if (modalElement) modalChofer = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTituloChofer").innerText = "Editar Chofer";

    document.getElementById("choferId").value = ch.choferId;
    document.getElementById("nombreChofer").value = ch.nombre;
    document.getElementById("identificacionChofer").value = ch.identificacion;
    document.getElementById("tarifaChofer").value = ch.tarifaHora;
    document.getElementById("estadoChofer").value = ch.estado;

    modalChofer.show();

  } catch (error) {
    console.error("Error cargando chofer:", error);
  }
}


// GUARDAR

async function guardarChofer() {
  const id = document.getElementById("choferId").value;

  const chofer = {
    nombre: document.getElementById("nombreChofer").value,
    identificacion: document.getElementById("identificacionChofer").value,
    tarifaHora: parseFloat(document.getElementById("tarifaChofer").value),
    estado: document.getElementById("estadoChofer").value,
  };

  const errorDiv = document.getElementById("errorChofer");
  errorDiv.classList.add("d-none");
  errorDiv.innerText = "";


  if (!validarDNI(chofer.identificacion)) {
    errorDiv.innerText = "El DNI debe tener exactamente 8 dígitos numéricos.";
    errorDiv.classList.remove("d-none");
    return;
  }

  const metodo = id ? "PUT" : "POST";
  const url = id ? `${API_CHOFER}/${id}` : API_CHOFER;

  try {
    const res = await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(chofer),
    });

    if (!res.ok) {
      const errorMsg = await res.text();
      errorDiv.innerText = errorMsg;
      errorDiv.classList.remove("d-none");
      return;
    }

    modalChofer?.hide();
    cargarChoferes();

  } catch (error) {
    console.error("Error guardando chofer:", error);
    errorDiv.innerText = "Error inesperado al guardar el chofer.";
    errorDiv.classList.remove("d-none");
  }
}



// ELIMINAR

async function eliminarChofer(id) {
  if (!confirm("¿Seguro que deseas eliminar este chofer?")) return;

  try {
    await fetch(`${API_CHOFER}/${id}`, { method: "DELETE" });
    cargarChoferes();
  } catch (error) {
    console.error("Error eliminando chofer:", error);
  }
}


//funciones globales

window.abrirModalNuevoChofer = abrirModalNuevoChofer;
window.editarChofer = editarChofer;
window.guardarChofer = guardarChofer;
window.eliminarChofer = eliminarChofer;
