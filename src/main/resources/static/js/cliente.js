

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

// Inicializar cuando se carga el script
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initClientes);
} else {
  // DOM ya está listo
  initClientes();
}

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
                <td>${cli.clienteId}</td>
                <td>${cli.nombre}</td>
                <td>${cli.rucDni}</td>
                <td>${cli.telefono ?? ""}</td>
                <td>${cli.direccion ?? ""}</td>
                <td>
                    <button class="btn btn-sm btn-warning" onclick="editar(${
                      cli.clienteId
                    })">Editar</button>
                    <button class="btn btn-sm btn-danger" onclick="eliminar(${
                      cli.clienteId
                    })">Eliminar</button>
                </td>
            </tr>
        `;
    });
  } catch (error) {
    console.error("Error cargando clientes:", error);
  }
}

function abrirModalNuevoCliente() {
  // Verificar si el modal está inicializado
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
      if (modalElement) {
        modal = new bootstrap.Modal(modalElement);
      }
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

async function guardarCliente() {
  const id = document.getElementById("clienteId").value;


  const cliente = {
    nombre: document.getElementById("nombre").value,
    rucDni: document.getElementById("rucDni").value,
    telefono: document.getElementById("telefono").value,
    direccion: document.getElementById("direccion").value,
  };

  let metodo = id ? "PUT" : "POST";
  let url = id ? `${API}/${id}` : API;

  try {
    await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(cliente),
    });

    if (modal) modal.hide();
    cargarClientes();
  } catch (error) {
    console.error("Error guardando cliente:", error);
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