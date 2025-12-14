const API_PROV = "/api/proveedores";
let modalProveedor = null;

function initProveedores() {
  const body = document.getElementById("body-proveedores"); 

  if (!body) return;

  const modalElement = document.getElementById("modalProveedor");
  modalProveedor = modalElement ? new bootstrap.Modal(modalElement) : null;

  cargarProveedores();
}

document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("proveedores.html")) {
    initProveedores();
  }
});

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initProveedores);
} else {
  initProveedores();
}

async function cargarProveedores() {
  try {
    const res = await fetch(API_PROV);
    const data = await res.json();

    const body = document.getElementById("body-proveedores");
    if (!body) return;

    body.innerHTML = "";

    data.forEach((prov) => {
      body.innerHTML += `<tr>
<td><strong>#${prov.proveedorId}</strong></td>
<td>${prov.nombre}</td>
<td>${prov.ruc}</td>
<td>${prov.contacto ?? ""}</td>
<td>
<button class="btn btn-sm btn-editar me-1" onclick="editarProveedor(${
        prov.proveedorId
      })"> Editar</button>
<button class="btn btn-sm btn-eliminar" onclick="eliminarProveedor(${
        prov.proveedorId
      })"> Eliminar</button>
</td>
</tr>
`;
    });
  } catch (error) {
    console.error("Error cargando proveedores:", error);
  }
}

function abrirModalNuevoProveedor() {
  if (!modalProveedor) {
    const modalElement = document.getElementById("modalProveedor");
    if (modalElement) modalProveedor = new bootstrap.Modal(modalElement);
  }

  document.getElementById("modalTituloProveedor").innerText = "Nuevo Proveedor";
  document.getElementById("formProveedor").reset();
  document.getElementById("proveedorId").value = "";

  modalProveedor.show();
}

async function editarProveedor(id) {
  try {
    const res = await fetch(`${API_PROV}/${id}`);
    const prov = await res.json();

    if (!modalProveedor) {
      const modalElement = document.getElementById("modalProveedor");
      if (modalElement) modalProveedor = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTituloProveedor").innerText =
      "Editar Proveedor";

    document.getElementById("proveedorId").value = prov.proveedorId;
    document.getElementById("nombreProveedor").value = prov.nombre;
    document.getElementById("rucProveedor").value = prov.ruc;
    document.getElementById("contactoProveedor").value = prov.contacto ?? "";

    modalProveedor.show();
  } catch (error) {
    console.error("Error cargando proveedor:", error);
  }
}

async function guardarProveedor() {
  const id = document.getElementById("proveedorId").value;

  const proveedor = {
    nombre: document.getElementById("nombreProveedor").value,
    ruc: document.getElementById("rucProveedor").value,
    contacto: document.getElementById("contactoProveedor").value,
  };

  const metodo = id ? "PUT" : "POST";
  const url = id ? `${API_PROV}/${id}` : API_PROV;

  const errorDiv = document.getElementById("errorProveedor");
  errorDiv.classList.add("d-none");
  errorDiv.innerText = "";

  try {
    const res = await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(proveedor),
    });

    if (!res.ok) {
      const errorMsg = await res.text();
      errorDiv.innerText = errorMsg;
      errorDiv.classList.remove("d-none");
      return;
    }

    modalProveedor?.hide();
    cargarProveedores();
  } catch (error) {
    console.error("Error guardando proveedor:", error);
    errorDiv.innerText = "Error inesperado al guardar el proveedor.";
    errorDiv.classList.remove("d-none");
  }
}

async function eliminarProveedor(id) {
  if (!confirm("¿Seguro que deseas eliminar este proveedor?")) return;

  try {
    await fetch(`${API_PROV}/${id}`, { method: "DELETE" });
    cargarProveedores();
  } catch (error) {
    console.error("Error eliminando proveedor:", error);
  }
}

// Exponer
window.abrirModalNuevoProveedor = abrirModalNuevoProveedor;
window.editarProveedor = editarProveedor;
window.guardarProveedor = guardarProveedor;
window.eliminarProveedor = eliminarProveedor;
