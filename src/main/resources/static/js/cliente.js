const API = "/api/clientes";
let modal = null;

function initClientes() {
  const modalElement = document.getElementById("modalCliente");
  if (modalElement) {
    modal = new bootstrap.Modal(modalElement);
    cargarClientes();
  } else {
    console.log("Esperando a que se cargue el modal...");
    setTimeout(initClientes, 100);
  }
}


document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("clientes.html")) {
    initClientes();
  }
});

async function cargarClientes() {
  try {
    const res = await fetch(API);
    const data = await res.json();

    const body = document.getElementById("body-clientes");
    if (!body) return;

    body.innerHTML = "";

    data.forEach((cli) => {
      body.innerHTML += `
          <tr>
          <td><strong>#${cli.clienteId}</strong></td>
          <td>${cli.nombre}</td>
          <td>${cli.rucDni}</td>
          <td>${cli.telefono ?? ""}</td>
          <td>${cli.direccion ?? ""}</td>
          <td>
          <button class="btn btn-sm btn-editar me-1" onclick="editar(${cli.clienteId})"> Editar</button>
          <button class="btn btn-sm btn-eliminar" onclick="eliminar(${cli.clienteId})"> Eliminar</button>
          </td>
          </tr>
          `;
    });
  } catch (error) {
    console.error("Error cargando clientes:", error);
  }
}

function abrirModalNuevoCliente() {
  if (!modal) {
    const modalElement = document.getElementById("modalCliente");
    if (modalElement) {
      modal = new bootstrap.Modal(modalElement);
    } else {
      console.error("Modal no encontrado en el DOM");
      return;
    }
  }

  document.getElementById("modalTitulo").innerText = "Nuevo Cliente";
  document.getElementById("formCliente").reset();
  document.getElementById("clienteId").value = "";
  modal.show();
}

async function editar(id) {
  try {
    const res = await fetch(`${API}/${id}`);
    const cli = await res.json();

    if (!modal) {
      const modalElement = document.getElementById("modalCliente");
      if (modalElement) modal = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTitulo").innerText = "Editar Cliente";
    document.getElementById("clienteId").value = cli.clienteId;
    document.getElementById("nombre").value = cli.nombre;
    document.getElementById("rucDni").value = cli.rucDni;
    document.getElementById("telefono").value = cli.telefono ?? "";
    document.getElementById("direccion").value = cli.direccion ?? "";

    modal.show();
  } catch (error) {
    console.error("Error editando cliente:", error);
  }
}

/* --------------------- VALIDACIÓN--------------------- */

function validarRucDni(valor) {
  return /^\d{8}$/.test(valor) || /^\d{11}$/.test(valor);
}

/* -------------------------------------------------------------- */

async function guardarCliente() {
  const id = document.getElementById("clienteId").value;

  const cliente = {
    nombre: document.getElementById("nombre").value,
    rucDni: document.getElementById("rucDni").value.trim(),
    telefono: document.getElementById("telefono").value,
    direccion: document.getElementById("direccion").value,
  };

  const errorDiv = document.getElementById("errorCliente");
  errorDiv.classList.add("d-none");
  errorDiv.innerText = "";

  if (!validarRucDni(cliente.rucDni)) {
    errorDiv.classList.remove("d-none");
    errorDiv.innerText = "El RUC/DNI debe tener exactamente 8 o 11 dígitos.";
    return;
  }

  let metodo = id ? "PUT" : "POST";
  let url = id ? `${API}/${id}` : API;

  try {
    const res = await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(cliente),
    });

    if (!res.ok) {
      const errorMsg = await res.text();
      errorDiv.innerText = errorMsg;
      errorDiv.classList.remove("d-none");
      return;
    }

    modal?.hide();
    cargarClientes();
  } catch (error) {
    console.error("Error guardando cliente:", error);
    errorDiv.innerText = "Error inesperado al guardar el cliente.";
    errorDiv.classList.remove("d-none");
  }
}

async function eliminar(id) {
  if (!confirm("¿Seguro que deseas eliminar este cliente?")) return;

  try {
    await fetch(`${API}/${id}`, { method: "DELETE" });
    cargarClientes();
  } catch (error) {
    console.error("Error eliminando cliente:", error);
  }
}

// Hacer las funciones disponibles globalmente
window.abrirModalNuevoCliente = abrirModalNuevoCliente;
window.editar = editar;
window.guardarCliente = guardarCliente;
window.eliminar = eliminar;
