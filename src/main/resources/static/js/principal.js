// CONFIGURACION DE NOTIFICACIONES (TOAST)
// Definimos el estilo de la alerta pequeña flotante
const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 2000,
    timerProgressBar: true,
    didOpen: (toast) => {
        toast.addEventListener('mouseenter', Swal.stopTimer)
        toast.addEventListener('mouseleave', Swal.resumeTimer)
    }
});

// Lo hacemos accesible para otros scripts (como usuarios.js)
window.Toast = Toast;

// Mapear roles a secciones que puede ver
const permisos = {
    ADMINISTRADOR:       { dashboard:true, contratos:true, maestros:true, operativa:true, reportes:true, seguridad:true },
    SUPER_ADMINISTRADOR: { dashboard:true, contratos:true, maestros:true, operativa:true, reportes:true, seguridad:true },
    GERENTE:             { dashboard:true, contratos:false, maestros:false, operativa:false, reportes:true, seguridad:false },
    OPERADOR_ALQUILER:   { dashboard:true, contratos:true, maestros:true, operativa:false, reportes:false, seguridad:false },
    OPERADOR_PATIO:      { dashboard:true, contratos:false, maestros:true, operativa:true, reportes:false, seguridad:false },
    CONTABILIDAD:        { dashboard:true, contratos:false, maestros:false, operativa:true, reportes:true, seguridad:false }
};

// Función para ocultar o mostrar secciones segun rol
function aplicarPermisos(rolUsuario) {
    const configPermisos = permisos[rolUsuario];
    if (!configPermisos) return;

    // 1. Ocultar secciones estándar
    document.querySelectorAll(".sidebar .section-title").forEach(title => {
        const seccionNombre = title.textContent.trim().toLowerCase();
        if (configPermisos.hasOwnProperty(seccionNombre) && configPermisos[seccionNombre] === false) {
            title.parentElement.style.display = "none";
        }
    });

    // 2. Ocultar sección de Seguridad (Administración)
    const menuSeguridad = document.getElementById('menu-seguridad');

    if (menuSeguridad) {
        menuSeguridad.style.display = 'none'; // Por defecto oculto
        if (configPermisos.seguridad === true) {
            menuSeguridad.style.display = 'block';
        }
    }
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

        const userLabel = document.getElementById('nombre-usuario');
        if (userLabel) {
            userLabel.innerHTML = `${data.username} <span class="badge bg-info">${data.rol}</span>`;
        }

        // Aplicar permisos
        aplicarPermisos(rolUsuario);

    } catch (err) {
        console.error(err);
        window.location.href = "/index.html";
    }
});

// LOGOUT CON NOTIFICACION
const btnLogout = document.getElementById("logout-btn");
if (btnLogout) {
    btnLogout.addEventListener("click", async () => {
        try {
            const res = await fetch("/api/users/logout", {
                method: "POST",
                credentials: "include"
            });

            if (res.ok) {
                // Notificacion pequeña al salir
                Toast.fire({
                    icon: 'success',
                    title: 'Sesión cerrada correctamente'
                });

                // Pequeña espera para ver el mensaje
                setTimeout(() => {
                    window.location.href = "/index.html";
                }, 1200);
            } else {
                alert("No se pudo cerrar sesión. Intenta de nuevo.");
            }
        } catch (err) {
            console.error(err);
            alert("Error al cerrar sesión.");
        }
    });
}

// Funcion global para cargar vistas
window.loadView = function(view) {
    fetch(view)
      .then((res) => {
          if(!res.ok) throw new Error("Error cargando vista");
          return res.text();
      })
      .then((html) => {
        document.getElementById("content-area").innerHTML = html;

        // Reactivar Tooltips
        var tooltipTriggerList = [].slice.call(
          document.querySelectorAll('[data-bs-toggle="tooltip"]')
        );
        tooltipTriggerList.map((el) => new bootstrap.Tooltip(el));

        document.dispatchEvent(
          new CustomEvent("vista-cargada", { detail: view })
        );
      })
      .catch(() => {
        document.getElementById("content-area").innerHTML =
          "<div class='alert alert-danger'>No se pudo cargar la vista solicitada.</div>";
      });
};