const API_REPORTE_RENTABILIDAD = "/api/reportes/rentabilidad";
let tablaRentabilidad = null;

// Formateador de moneda
const formatterRenta = new Intl.NumberFormat('es-PE', {
    style: 'currency',
    currency: 'PEN',
    minimumFractionDigits: 2,
});

// Inicialización
function initReporteRentabilidad() {
    const tabla = document.getElementById("tabla-rentabilidad");
    if (!tabla) return;
    tablaRentabilidad = tabla.querySelector("tbody");
    cargarReporteRentabilidad();
}

// Event Listeners para carga de vista
document.addEventListener("vista-cargada", (e) => {
    if (e.detail.includes("reporteRentabilidad.html")) {
        initReporteRentabilidad();
    }
});

if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initReporteRentabilidad);
} else {
    initReporteRentabilidad();
}

// Renderizar Tabla
function renderizarTabla(data) {
    const tbody = tablaRentabilidad;
    if (!tbody) return;

    tbody.innerHTML = "";

    data.forEach((c) => {
        const utilidadBruta = c[7] ?? 0;
        const utilidadClass = utilidadBruta >= 0 ? "utilidad-bruta" : "utilidad-negativa";

        tbody.innerHTML += `<tr>
            <td>#${c[0]}</td>
            <td>${c[1]}</td>
            <td>${c[2]}</td>
            <td>${formatearFechaTabla(c[3]) ?? "N/A"}</td>
            <td>${formatterRenta.format(c[4] ?? 0)}</td>
            <td>${formatterRenta.format(c[5] ?? 0)}</td>
            <td>${formatterRenta.format(c[6] ?? 0)}</td>
            <td class="${utilidadClass}">
                ${formatterRenta.format(utilidadBruta)}
            </td>
        </tr>`;
    });
}

// Cargar Datos de API
async function cargarReporteRentabilidad() {
    try {
        const res = await fetch(API_REPORTE_RENTABILIDAD);
        const data = await res.json();

        // Guardar data completa para filtros
        const tablaEl = document.getElementById("tabla-rentabilidad");
        if(tablaEl) {
            tablaEl.dataset.fullData = JSON.stringify(data);
        }

        renderizarTabla(data);
    } catch (error) {
        console.error("Error cargando reporte de rentabilidad:", error);
    }
}

// Filtrar Datos
function filtrarRentabilidad() {
    const clienteInput = document.getElementById("filtro-cliente");
    const fechaInput = document.getElementById("filtro-fechaInicio");

    if(!clienteInput || !fechaInput) return;

    const cliente = clienteInput.value.toLowerCase().trim();
    const fechaInicio = fechaInput.value;

    const tablaEl = document.getElementById("tabla-rentabilidad");
    if (!tablaEl || !tablaEl.dataset.fullData) return;

    const data = JSON.parse(tablaEl.dataset.fullData);

    const filtrados = data.filter((c) => {
        const nombreCliente = (c[1] ?? "").toLowerCase();
        const fecha = c[3] ?? "";
        const cumpleCliente = !cliente || nombreCliente.includes(cliente);
        const cumpleFecha = !fechaInicio || fecha.startsWith(fechaInicio);
        return cumpleCliente && cumpleFecha;
    });

    renderizarTabla(filtrados);
}

// Obtener datos filtrados para exportación
function getDatosFiltrados() {
    const clienteInput = document.getElementById("filtro-cliente");
    const fechaInput = document.getElementById("filtro-fechaInicio");

    // Si no estamos en la vista correcta, retornar vacio
    if(!clienteInput) return { datos: [], totalUtilidad: 0 };

    const cliente = clienteInput.value.toLowerCase().trim();
    const fechaInicio = fechaInput.value;

    const tablaEl = document.getElementById("tabla-rentabilidad");
    if(!tablaEl || !tablaEl.dataset.fullData) return { datos: [], totalUtilidad: 0 };

    const data = JSON.parse(tablaEl.dataset.fullData);
    let totalUtilidad = 0;

    const datos = data.filter((c) => {
        const nombreCliente = (c[1] ?? "").toLowerCase();
        const fecha = c[3] ?? "";
        return ((!cliente || nombreCliente.includes(cliente)) && (!fechaInicio || fecha.startsWith(fechaInicio)));
    }).map((c) => {
        const rowData = [
            `#${c[0]}`,
            c[1],
            c[2],
            formatearFechaTabla(c[3]),
            `S/ ${c[4]?.toFixed(2) ?? "0.00"}`,
            `S/ ${c[5]?.toFixed(2) ?? "0.00"}`,
            `S/ ${c[6]?.toFixed(2) ?? "0.00"}`,
            `S/ ${c[7]?.toFixed(2) ?? "0.00"}`,
        ];
        totalUtilidad += parseFloat(c[7] ?? 0);
        return rowData;
    });

    return { datos, totalUtilidad };
}

// --- EXPORTAR EXCEL ---
function exportarExcel() {
    if (typeof XLSX === 'undefined') {
        Swal.fire("Error", "Librería Excel no cargada. Refresca la página.", "error");
        return;
    }

    const { datos, totalUtilidad } = getDatosFiltrados();
    if (datos.length === 0) {
        Swal.fire("Atención", "No hay datos para exportar", "warning");
        return;
    }

    const wb = XLSX.utils.book_new();
    const wsData = [
        ["ID Contrato", "Cliente", "Maquinaria", "Fecha Inicio", "Ingresos Alquiler", "Costo Choferes", "Costo Combustible", "Utilidad Bruta"],
        ...datos,
        ["", "", "", "", "", "", "Total Utilidad", `S/ ${totalUtilidad.toFixed(2)}`]
    ];

    const ws = XLSX.utils.aoa_to_sheet(wsData);
    ws["!cols"] = [{ wch: 10 }, { wch: 25 }, { wch: 25 }, { wch: 20 }, { wch: 18 }, { wch: 18 }, { wch: 18 }, { wch: 18 }];

    XLSX.utils.book_append_sheet(wb, ws, "Rentabilidad");
    XLSX.writeFile(wb, "Reporte_Rentabilidad.xlsx");
}

// --- EXPORTAR PDF ---
function exportarPDF() {
    if (!window.jspdf) {
        Swal.fire("Error", "Librería PDF no cargada. Refresca la página.", "error");
        return;
    }

    const { datos, totalUtilidad } = getDatosFiltrados();
    if (datos.length === 0) {
        Swal.fire("Atención", "No hay datos para exportar", "warning");
        return;
    }

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF("l", "mm", "a4");

    doc.setFontSize(18);
    doc.text("Reporte de Rentabilidad", 14, 15);

    const filtroCliente = document.getElementById("filtro-cliente") ? document.getElementById("filtro-cliente").value : "Todos";
    doc.setFontSize(10);
    doc.text(`Cliente: ${filtroCliente}`, 14, 22);

    doc.autoTable({
        head: [["ID", "Cliente", "Maquinaria", "Fecha", "Ingresos", "Choferes", "Combustible", "Utilidad"]],
        body: datos,
        startY: 28,
        theme: "striped",
        headStyles: { fillColor: [44, 62, 80], textColor: 255, fontStyle: "bold" },
        foot: [["", "", "", "", "", "", "Total", `S/ ${totalUtilidad.toFixed(2)}`]],
        footStyles: { fillColor: [245, 166, 35], textColor: 0, fontStyle: "bold" },
        columnStyles: { 7: { fontStyle: "bold" } }
    });

    doc.save("Reporte_Rentabilidad.pdf");
}

// --- UTILIDADES FECHA ---
function formatearFechaTabla(fechaIso) {
    if (!fechaIso) return "N/A";
    const fechaStr = String(fechaIso).replace(" ", "T");
    const date = new Date(fechaStr);
    if (isNaN(date.getTime())) return fechaIso;

    return date.toLocaleString("es-CL", {
        year: "numeric", month: "2-digit", day: "2-digit",
        hour: "2-digit", minute: "2-digit", hour12: false
    });
}

// Exponer funciones globalmente
window.filtrarRentabilidad = filtrarRentabilidad;
window.exportarExcel = exportarExcel;
window.exportarPDF = exportarPDF;