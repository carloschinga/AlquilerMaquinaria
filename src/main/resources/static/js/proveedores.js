const API_PROV = "/api/proveedores";
let modalProveedor = null;

function initProveedores() {
  const modalElement = document.getElementById("modalProveedor");
  if (modalElement) {
    modalProveedor = new bootstrap.Modal(modalElement);
    cargarProveedores();
  } else {
    console.log("Esperando a que se cargue el modal de proveedores...");
    setTimeout(initProveedores, 100);
  }
}

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
      body.innerHTML += `
        <tr>
            <td>${prov.proveedorId}</td>
            <td>${prov.nombre}</td>
            <td>${prov.ruc}</td>
            <td>${prov.contacto ?? ""}</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editarProveedor(${prov.proveedorId})">Editar</button>
                <button class="btn btn-danger btn-sm" onclick="eliminarProveedor(${prov.proveedorId})">Eliminar</button>
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

    document.getElementById("modalTituloProveedor").innerText = "Editar Proveedor";

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

  try {
    await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(proveedor),
    });

    modalProveedor?.hide();
    cargarProveedores();
  } catch (error) {
    console.error("Error guardando proveedor:", error);
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
