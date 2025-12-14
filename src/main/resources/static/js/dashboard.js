const API_DASHBOARD = "/dashboard";

function initDashboard() {
    const contenedor = document.getElementById("cards-dashboard");
    if (!contenedor) return;

    cargarDashboard();
}

document.addEventListener("vista-cargada", (e) => {
    if (e.detail.includes("dashboard.html")) initDashboard();
});

if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initDashboard);
} else {
    initDashboard();
}

// ---------------------------------------------------------
// CARGAR DATOS DEL DASHBOARD
// ---------------------------------------------------------
async function cargarDashboard() {
    try {
        // Mostrar indicador de carga
        document.getElementById('loading-indicator').style.display = 'flex';
        document.getElementById('cards-dashboard').style.display = 'none';
        document.getElementById('error-container').style.display = 'none';
        
        const [contratosRes, maquinariaRes, proveedoresRes] = await Promise.all([
            fetch(`${API_DASHBOARD}/contratos`).then(r => r.json()),
            fetch(`${API_DASHBOARD}/maquinaria`).then(r => r.json()),
            fetch(`${API_DASHBOARD}/cuentas-proveedores`).then(r => r.json())
        ]);

        renderDashboard({ contratosRes, maquinariaRes, proveedoresRes });
        
        // Ocultar indicador de carga
        document.getElementById('loading-indicator').style.display = 'none';
        document.getElementById('cards-dashboard').style.display = 'grid';

    } catch (error) {
        console.error("Error cargando dashboard:", error);
        
        // Mostrar error
        document.getElementById('loading-indicator').style.display = 'none';
        const errorContainer = document.getElementById('error-container');
        errorContainer.innerHTML = `
            <div class="error-message">
                <strong>Error al cargar el dashboard</strong><br>
                ${error.message || 'No se pudieron cargar los datos. Intente nuevamente.'}
            </div>
        `;
        errorContainer.style.display = 'block';
    }
}

// ---------------------------------------------------------
// RENDERIZAR DASHBOARD CREATIVO
// ---------------------------------------------------------
function renderDashboard({ contratosRes, maquinariaRes, proveedoresRes }) {
    const cont = document.getElementById("cards-dashboard");
    if (!cont) return;

    // Calcular totales
    const deudaTotalProveedores = proveedoresRes.reduce((acc, p) => acc + p.montoPendiente, 0);
    const facturasPendientes = proveedoresRes.reduce((acc, p) => acc + p.numeroFacturasPendientes, 0);
    const maquinaTop = maquinariaRes.top5MasUsadas?.length ? maquinariaRes.top5MasUsadas[0] : null;
    
    // Configurar colores de las tarjetas
    const cardColors = {
        contratos: { bg: '#e74c3c', icon: '📋' },
        maquinaria: { bg: '#3498db', icon: '🚜' },
        proveedores: { bg: '#2ecc71', icon: '👥' },
        topMaquina: { bg: '#9b59b6', icon: '⭐' }
    };
    
    cont.innerHTML = `
        <!-- Tarjeta de Contratos -->
        <div class="dashboard-card">
            <div class="card-header">
                <div class="card-icon" style="background: linear-gradient(135deg, ${cardColors.contratos.bg}, #c0392b);">
                    ${cardColors.contratos.icon}
                </div>
                <h3 class="card-title">Contratos</h3>
            </div>
            
            <div class="card-content">
                <div class="stat-item">
                    <span class="stat-label">Total de contratos</span>
                    <span class="stat-value">${contratosRes.totalContratos}</span>
                </div>
                
                <div class="stat-item">
                    <span class="stat-label">Contratos activos</span>
                    <span class="stat-value" style="color: #27ae60;">${contratosRes.contratosActivos}</span>
                </div>
                
                <div class="highlight-stat">
                    <div class="stat-item">
                        <span class="stat-label">Cuentas por cobrar</span>
                        <span class="stat-value currency">S/ ${contratosRes.montoPorCobrar?.toFixed(2)}</span>
                    </div>
                </div>
            </div>
            
            <div class="card-footer">
                <span>Actualizado recientemente</span>
                <span>${contratosRes.contratosActivos} activos</span>
            </div>
        </div>
        
        <!-- Tarjeta de Maquinaria -->
        <div class="dashboard-card">
            <div class="card-header">
                <div class="card-icon" style="background: linear-gradient(135deg, ${cardColors.maquinaria.bg}, #2980b9);">
                    ${cardColors.maquinaria.icon}
                </div>
                <h3 class="card-title">Maquinaria</h3>
            </div>
            
            <div class="card-content">
                <div class="stat-item">
                    <span class="stat-label">Total de máquinas</span>
                    <span class="stat-value">${maquinariaRes.totalMaquinas}</span>
                </div>
                
                <div class="stat-item">
                    <span class="stat-label">Disponibles</span>
                    <span class="stat-value" style="color: #27ae60;">${maquinariaRes.maquinasOperativas}</span>
                </div>
                
                <div class="stat-item">
                    <span class="stat-label">En mantenimiento</span>
                    <span class="stat-value" style="color: #e74c3c;">${maquinariaRes.maquinasEnMantenimiento}</span>
                </div>
            </div>
            
            <div class="card-footer">
                <span>Estado del equipo</span>
                <span>${Math.round((maquinariaRes.maquinasOperativas / maquinariaRes.totalMaquinas) * 100)}% operativas</span>
            </div>
        </div>
        
        <!-- Tarjeta de Proveedores -->
        <div class="dashboard-card">
            <div class="card-header">
                <div class="card-icon" style="background: linear-gradient(135deg, ${cardColors.proveedores.bg}, #27ae60);">
                    ${cardColors.proveedores.icon}
                </div>
                <h3 class="card-title">Proveedores</h3>
            </div>
            
            <div class="card-content">
                <div class="highlight-stat">
                    <div class="stat-item">
                        <span class="stat-label">Deuda total</span>
                        <span class="stat-value" style="color: #e74c3c;">S/ ${deudaTotalProveedores.toFixed(2)}</span>
                    </div>
                </div>
                
                <div class="stat-item">
                    <span class="stat-label">Facturas pendientes</span>
                    <span class="stat-value">${facturasPendientes}</span>
                </div>
                
                <div class="stat-item">
                    <span class="stat-label">Proveedores registrados</span>
                    <span class="stat-value">${proveedoresRes.length}</span>
                </div>
            </div>
            
            <div class="card-footer">
                <span>Estado de pagos</span>
                <span>${proveedoresRes.length} proveedores</span>
            </div>
        </div>
        
        <!-- Tarjeta de Máquina Top -->
        <div class="dashboard-card">
            <div class="card-header">
                <div class="card-icon" style="background: linear-gradient(135deg, ${cardColors.topMaquina.bg}, #8e44ad);">
                    ${cardColors.topMaquina.icon}
                </div>
                <h3 class="card-title">Máquina Destacada</h3>
            </div>
            
            <div class="card-content">
                ${maquinaTop ? `
                    <div class="top-machine">
                        <div class="machine-icon">🚜</div>
                        <div class="machine-info">
                            <h5>${maquinaTop.modelo || 'Modelo no disponible'}</h5>
                            <p>Máquina más utilizada</p>
                            <div class="hours-badge">${maquinaTop.horas} horas</div>
                        </div>
                    </div>
                    
                    <div class="stat-item" style="margin-top: 15px;">
                        <span class="stat-label">Posición en ranking</span>
                        <span class="stat-value">#1</span>
                    </div>
                ` : `
                    <div style="text-align: center; padding: 30px 0; color: #7b8a8b;">
                        <div style="font-size: 3rem; margin-bottom: 15px;">📊</div>
                        <p>No hay datos de máquinas disponibles</p>
                    </div>
                `}
            </div>
            
            <div class="card-footer">
                <span>Top 5 máquinas</span>
                <span>${maquinariaRes.top5MasUsadas?.length || 0} registradas</span>
            </div>
        </div>
    `;
    
    // Agregar animación de entrada a las tarjetas
    const cards = cont.querySelectorAll('.dashboard-card');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
}