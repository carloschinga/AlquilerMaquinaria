const API_MAQ = "/api/maquinarias";
let modalMaq = null;

function initMaquinarias() {
    const modalElement = document.getElementById("modalMaquinaria");
    if (modalElement) {
        modalMaq = new bootstrap.Modal(modalElement);
        cargarMaquinarias();
    } else {
        console.log("Esperando a que se cargue el modal...");
        setTimeout(initMaquinarias, 100);
    }
}


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
            body.innerHTML += `
                <tr>
                    <td>${m.maquinaId}</td>
                    <td>${m.tipo}</td>
                    <td>${m.modelo}</td>
                    <td>${m.serialInterno}</td>
                    <td>${m.horasAcumuladas}</td>
                    <td>${m.estado}</td>
                    <td>
                        <button class="btn btn-sm btn-warning" onclick="editarMaquinaria(${m.maquinaId})">Editar</button>
                        <button class="btn btn-sm btn-danger" onclick="eliminarMaquinaria(${m.maquinaId})">Eliminar</button>
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

    try {
        await fetch(url, {
            method: method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(maquinaria)
        });

        modalMaq.hide();
        cargarMaquinarias();

    } catch (error) {
        console.error("Error guardando maquinaria:", error);
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
