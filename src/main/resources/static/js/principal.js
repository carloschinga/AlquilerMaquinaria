// --------------------------------------
// Mapear roles a secciones que puede ver
const permisos = {
    ADMINISTRADOR:    { dashboard:true, contratos:true, maestros:true, operativa:true, reportes:true },
    GERENTE:          { dashboard:true, contratos:false, maestros:false, operativa:false, reportes:true },
    OPERADOR_ALQUILER: { dashboard:true, contratos:true, maestros:true, operativa:false, reportes:false },
    OPERADOR_PATIO:    { dashboard:true, contratos:false, maestros:true, operativa:true, reportes:false },
    CONTABILIDAD:      { dashboard:true, contratos:false, maestros:false, operativa:true, reportes:true }
};

// --------------------------------------
// Función para ocultar o mostrar secciones según rol
function aplicarPermisos(rolUsuario) {
    const secciones = permisos[rolUsuario];
    if (!secciones) return;

    Object.keys(secciones).forEach(seccion => {
        if (!secciones[seccion]) {
            document.querySelectorAll(".sidebar .section-title").forEach(title => {
                if (title.textContent.trim().toLowerCase() === seccion) {
                    title.parentElement.style.display = "none";
                }
            });
        }
    });
}

// Ejecutar al cargar la página
document.addEventListener("DOMContentLoaded", async () => {
    try {
        const res = await fetch("/api/users/me", {
            method: "GET",
            credentials: "include"  
        });
        if (!res.ok) throw new Error("No autenticado");

        const data = await res.json();
        const rolUsuario = data.rol;
        console.log("Rol del usuario:", rolUsuario);
        document.getElementById('nombre-usuario').innerHTML = `
      ${data.username} <span class="badge bg-info">${data.rol}</span>
    `;
        // Aplicar permisos
        aplicarPermisos(rolUsuario);

    } catch (err) {
        console.error(err);
        // Redirigir a login si no está autenticado
        window.location.href = "/index.html";
    }
});
document.getElementById("logout-btn").addEventListener("click", async () => {
    try {
        const res = await fetch("/api/users/logout", {
            method: "POST",
            credentials: "include"
        });

        if (res.ok) {
        
            window.location.href = "/index.html";
        } else {
            alert("No se pudo cerrar sesión. Intenta de nuevo.");
        }
    } catch (err) {
        console.error(err);
        alert("Error al cerrar sesión.");
    }
});

