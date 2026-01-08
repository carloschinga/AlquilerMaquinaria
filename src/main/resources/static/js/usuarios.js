let isEditing = false;

// Usamos el Toast global definido en principal.js, o creamos uno local por si acaso
const ToastUsuario = window.Toast || Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 2000,
    timerProgressBar: true
});

// Escuchar evento de carga de vista
document.addEventListener('vista-cargada', (e) => {
    if (e.detail.includes('usuarios.html')) {
        cargarUsuarios();
    }
});

async function cargarUsuarios() {
    try {
        const res = await fetch('/api/users');
        if (!res.ok) throw new Error("Error al cargar");
        const usuarios = await res.json();

        const tbody = document.querySelector('#tablaUsuarios tbody');
        tbody.innerHTML = '';

        usuarios.forEach(u => {
            const isActivo = u.estado === 'Activo';
            const badgeClass = isActivo
                ? 'bg-success-subtle text-success border-success'
                : 'bg-danger-subtle text-danger border-danger';
            const estadoTexto = isActivo ? 'Activo' : 'Bloqueado';

            const btnBloqueo = isActivo
                ? `<button class="btn btn-sm btn-outline-warning mx-1" onclick="cambiarEstado(${u.id})" title="Bloquear Acceso" data-bs-toggle="tooltip"><i class="fas fa-lock"></i></button>`
                : `<button class="btn btn-sm btn-outline-success mx-1" onclick="cambiarEstado(${u.id})" title="Desbloquear Acceso" data-bs-toggle="tooltip"><i class="fas fa-lock-open"></i></button>`;

            tbody.innerHTML += `
                <tr>
                    <td class="ps-4 fw-bold text-dark">${u.nombreUsuario}</td>
                    <td>${u.nombreCompleto || '-'}</td>
                    <td><span class="badge bg-primary">${u.role ? u.role.nombreRol : 'Sin Rol'}</span></td>
                    <td><span class="badge ${badgeClass} border px-3 rounded-pill">${estadoTexto}</span></td>
                    <td class="text-end pe-4 text-nowrap">
                        <button class="btn btn-sm btn-outline-primary mx-1" onclick="prepararEdicion(${u.id}, '${u.nombreUsuario}', '${u.nombreCompleto}', '${u.correoElectronico}', ${u.role ? u.role.id : ''})" title="Editar" data-bs-toggle="tooltip"><i class="fas fa-pencil-alt"></i></button>
                        ${btnBloqueo}
                        <button class="btn btn-sm btn-outline-danger mx-1" onclick="eliminarUsuario(${u.id})" title="Eliminar" data-bs-toggle="tooltip"><i class="fas fa-trash-alt"></i></button>
                    </td>
                </tr>
            `;
        });

        // Inicializar tooltips
        const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
        [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

    } catch (error) {
        console.error("Error:", error);
    }
}

function abrirModalUsuario() {
    isEditing = false;
    document.getElementById('formUsuario').reset();
    document.getElementById('userId').value = '';
    document.getElementById('nombreUsuario').disabled = false;
    document.getElementById('modalTitulo').innerText = 'Nuevo Usuario';

    // Ocultar campo password al crear (usa default)
    const nuevaContraDiv = document.getElementById('nuevaContrasena').parentElement;
    if(nuevaContraDiv) nuevaContraDiv.style.display = 'none';

    document.getElementById('msgPassword').style.display = 'flex';
    new bootstrap.Modal(document.getElementById('modalUsuario')).show();
}

function prepararEdicion(id, usuario, nombre, correo, rolId) {
    isEditing = true;
    document.getElementById('userId').value = id;
    document.getElementById('nombreUsuario').value = usuario;
    document.getElementById('nombreUsuario').disabled = true;
    document.getElementById('nombreCompleto').value = nombre === 'null' ? '' : nombre;
    document.getElementById('correoElectronico').value = correo === 'null' ? '' : correo;
    document.getElementById('rolId').value = rolId;

    document.getElementById('modalTitulo').innerText = 'Editar Usuario';

    // Mostrar campo password al editar
    const nuevaContraDiv = document.getElementById('nuevaContrasena').parentElement;
    if(nuevaContraDiv) nuevaContraDiv.style.display = 'block';

    document.getElementById('nuevaContrasena').value = '';
    document.getElementById('hintPasswordEdit').style.display = 'block';
    document.getElementById('msgPassword').style.display = 'none';
    new bootstrap.Modal(document.getElementById('modalUsuario')).show();
}

async function guardarUsuario() {
    const id = document.getElementById('userId').value;

    const dto = {
        nombreUsuario: document.getElementById('nombreUsuario').value,
        nombreCompleto: document.getElementById('nombreCompleto').value,
        correoElectronico: document.getElementById('correoElectronico').value,
        rolId: parseInt(document.getElementById('rolId').value)
    };

    if (isEditing) {
        const passField = document.getElementById('nuevaContrasena');
        if (passField && passField.value.trim() !== "") {
            dto.nuevaContrasena = passField.value.trim();
        }
    }

    const url = isEditing ? `/api/users/${id}` : '/api/users/admin-create';
    const method = isEditing ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dto)
        });

        if (res.ok) {
            // NOTIFICACIÓN PEQUEÑA (TOAST)
            ToastUsuario.fire({
                icon: 'success',
                title: isEditing ? 'Usuario actualizado exitosamente' : 'Usuario creado exitosamente'
            });

            bootstrap.Modal.getInstance(document.getElementById('modalUsuario')).hide();
            cargarUsuarios();
        } else {
            const txt = await res.text();
            Swal.fire('Error', txt, 'error'); // Errores grandes se mantienen
        }
    } catch (e) {
        Swal.fire('Error', 'Fallo de conexión', 'error');
    }
}

async function cambiarEstado(id) {
    try {
        const res = await fetch(`/api/users/${id}/toggle-status`, {
            method: 'PATCH'
        });
        if (res.ok) {
            // NOTIFICACIÓN PEQUEÑA (TOAST)
            ToastUsuario.fire({
                icon: 'success',
                title: 'Estado de acceso actualizado'
            });
            cargarUsuarios();
        }
    } catch (e) {
        console.error(e);
    }
}

async function eliminarUsuario(id) {
    const result = await Swal.fire({
        title: '¿Está seguro?',
        text: "Esta acción eliminará el usuario permanentemente.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    });

    if (result.isConfirmed) {
        const res = await fetch(`/api/users/${id}`, {
            method: 'DELETE'
        });
        if (res.ok) {
            // NOTIFICACIÓN PEQUEÑA (TOAST)
            ToastUsuario.fire({
                icon: 'success',
                title: 'Usuario eliminado correctamente'
            });
            cargarUsuarios();
        } else {
            Swal.fire('Error', 'No se pudo eliminar.', 'error');
        }
    }
}