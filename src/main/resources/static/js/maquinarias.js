const API_MAQ = "/api/maquinarias";
let modalMaq = null;

function initMaquinarias() {
  const body = document.getElementById("body-maquinarias");

  // Si no estamos en maquinarias, ignoramos
  if (!body) return;

  const modalElement = document.getElementById("modalMaquinaria");
  modalMaq = modalElement ? new bootstrap.Modal(modalElement) : null;

  cargarMaquinarias();
}
document.addEventListener("vista-cargada", (e) => {
  if (e.detail.includes("maquinarias.html")) {
    initMaquinarias();
  }
});




if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initMaquinarias);
} else {
    initMaquinarias();
}

async function cargarMaquinarias() {
    try {
        const res = await fetch(API_MAQ);
        const data = await res.json();

        const body = document.getElementById("body-maquinarias");
        if (!body) return;

        body.innerHTML = "";

        data.forEach((m) => {
            // Lógica para asignar color al badge según el estado
            let badgeClass = "bg-warning"; // Por defecto: Mantenimiento
            if (m.estado === "Disponible") badgeClass = "bg-success"; // Verde para Disponible
            if (m.estado === "Ocupada") badgeClass = "bg-danger"; // Rojo para Ocupada (no disponible)

            body.innerHTML += `<tr>
<td><strong>#${m.maquinaId}</strong></td>
<td>${m.tipo}</td>
<td>${m.modelo}</td>
<td>${m.serialInterno}</td>
<td>${m.horasAcumuladas} h</td>
<td><span class="badge ${badgeClass} badge-estado">${m.estado}</span></td>
<td>
<button class="btn btn-sm btn-editar me-1" onclick="editarMaquinaria(${m.maquinaId})">✏️ Editar</button>
<button class="btn btn-sm btn-eliminar" onclick="eliminarMaquinaria(${m.maquinaId})">🗑️ Eliminar</button>
</td>
</tr>
`;
        });

    } catch (error) {
        console.error("Error cargando maquinarias:", error);
    }
}

function abrirModalNuevoMaquinaria() {
    if (!modalMaq) {
        const modalElement = document.getElementById("modalMaquinaria");
        if (modalElement) modalMaq = new bootstrap.Modal(modalElement);
    }

    document.getElementById("modalTituloMaquinaria").innerText = "Nueva Maquinaria";
    document.getElementById("formMaquinaria").reset();
    document.getElementById("maquinaId").value = "";

    modalMaq.show();
}

async function editarMaquinaria(id) {
    try {
        const res = await fetch(`${API_MAQ}/${id}`);
        const m = await res.json();

        if (!modalMaq) {
            const modalElement = document.getElementById("modalMaquinaria");
            if (modalElement) modalMaq = new bootstrap.Modal(modalElement);
        }

        document.getElementById("modalTituloMaquinaria").innerText = "Editar Maquinaria";

        document.getElementById("maquinaId").value = m.maquinaId;
        document.getElementById("tipo").value = m.tipo;
        document.getElementById("modelo").value = m.modelo;
        document.getElementById("serialInterno").value = m.serialInterno;
        document.getElementById("horasAcumuladas").value = m.horasAcumuladas;
        document.getElementById("estado").value = m.estado;

        modalMaq.show();
    } catch (error) {
        console.error("Error editando maquinaria:", error);
    }
}

async function guardarMaquinaria() {
    const id = document.getElementById("maquinaId").value;

    const maquinaria = {
        tipo: document.getElementById("tipo").value,
        modelo: document.getElementById("modelo").value,
        serialInterno: document.getElementById("serialInterno").value,
        horasAcumuladas: parseFloat(document.getElementById("horasAcumuladas").value),
        estado: document.getElementById("estado").value
    };

    const method = id ? "PUT" : "POST";
    const url = id ? `${API_MAQ}/${id}` : API_MAQ;

    const errorDiv = document.getElementById("errorMaquinaria");
    errorDiv.classList.add("d-none");
    errorDiv.innerText = "";

    try {
        const res = await fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(maquinaria)
        });

        if (!res.ok) {
            const errorMsg = await res.text();
            errorDiv.innerText = errorMsg;
            errorDiv.classList.remove("d-none");
            return;
        }

        modalMaq?.hide();
        cargarMaquinarias();

    } catch (error) {
        console.error("Error guardando maquinaria:", error);
        errorDiv.innerText = "Error inesperado al guardar la maquinaria.";
        errorDiv.classList.remove("d-none");
    }
}



async function eliminarMaquinaria(id) {
    if (!confirm("¿Seguro que deseas eliminar esta maquinaria?")) return;

    try {
        await fetch(`${API_MAQ}/${id}`, { method: "DELETE" });
        cargarMaquinarias();
    } catch (error) {
        console.error("Error eliminando maquinaria:", error);
    }
}

// Exponer funciones globales
window.abrirModalNuevoMaquinaria = abrirModalNuevoMaquinaria;
window.editarMaquinaria = editarMaquinaria;
window.guardarMaquinaria = guardarMaquinaria;
window.eliminarMaquinaria = eliminarMaquinaria;
