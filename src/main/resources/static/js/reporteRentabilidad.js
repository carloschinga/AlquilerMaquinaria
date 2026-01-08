const API_REPORTE_RENTABILIDAD = "/api/reportes/rentabilidad";
let tablaRentabilidad = null;


const formatterRenta = new Intl.NumberFormat('es-PE', {
    style: 'currency',
    currency: 'PEN',
    minimumFractionDigits: 2,
});

function initReporteRentabilidad() {
  const tabla = document.getElementById("tabla-rentabilidad");

  if (!tabla) return;

  tablaRentabilidad = tabla.querySelector("tbody"); 

  cargarReporteRentabilidad();
}

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

/**
 * Función para renderizar los datos en el tbody.
 * @param {Array<Array<any>>} data - La lista de contratos con sus métricas.
 */
function renderizarTabla(data) {
  const tbody = tablaRentabilidad;
  if (!tbody) return;

  tbody.innerHTML = "";

  data.forEach((c) => {
 
    const utilidadBruta = c[7] ?? 0;

 
    const utilidadClass =
      utilidadBruta >= 0 ? "utilidad-bruta" : "utilidad-negativa";

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
</tr>
`;
  });
}

async function cargarReporteRentabilidad() {
  try {
    const res = await fetch(API_REPORTE_RENTABILIDAD);
    const data = await res.json();

    
    document.getElementById("tabla-rentabilidad").dataset.fullData =
      JSON.stringify(data);

    renderizarTabla(data);
  } catch (error) {
    console.error("Error cargando reporte de rentabilidad:", error);
  }
}

function filtrarRentabilidad() {
  const cliente = document
    .getElementById("filtro-cliente")
    .value.toLowerCase()
    .trim();
  const fechaInicio = document.getElementById("filtro-fechaInicio").value;

  // Recuperar todos los datos almacenados
  const fullDataAttr =
    document.getElementById("tabla-rentabilidad").dataset.fullData;
  if (!fullDataAttr) return;

  const data = JSON.parse(fullDataAttr);

  const filtrados = data.filter((c) => {
    const nombreCliente = (c[1] ?? "").toLowerCase();
    const fecha = c[3] ?? "";
  
    const cumpleCliente = !cliente || nombreCliente.includes(cliente);
    const cumpleFecha = !fechaInicio || fecha.startsWith(fechaInicio);

    return cumpleCliente && cumpleFecha;
  });

  renderizarTabla(filtrados);
}


function getDatosFiltrados() {
  const cliente = document
    .getElementById("filtro-cliente")
    .value.toLowerCase()
    .trim();
  const fechaInicio = document.getElementById("filtro-fechaInicio").value;

  // Recuperar todos los datos
  const fullDataAttr =
    document.getElementById("tabla-rentabilidad").dataset.fullData;
  if (!fullDataAttr) return { datos: [], totalUtilidad: 0 };

  const data = JSON.parse(fullDataAttr);

  let totalUtilidad = 0;

  // Filtrar los datos completos
  const datos = data
    .filter((c) => {
      const nombreCliente = (c[1] ?? "").toLowerCase();
      const fecha = c[3] ?? "";
      return (
        (!cliente || nombreCliente.includes(cliente)) &&
        (!fechaInicio || fecha.startsWith(fechaInicio))
      );
    })
    .map((c) => {
      // Formatear los datos para la exportación
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

// Excel
function exportarExcel() {
  const { datos, totalUtilidad } = getDatosFiltrados();


  const wb = XLSX.utils.book_new();
  const wsData = [
    [
      "ID Contrato",
      "Cliente",
      "Maquinaria",
      "Fecha Inicio",
      "Ingresos Alquiler",
      "Costo Choferes",
      "Costo Combustible",
      "Utilidad Bruta",
    ],
    ...datos,
    [
      "",
      "",
      "",
      "",
      "",
      "",
      "Total Utilidad",
      `S/ ${totalUtilidad.toFixed(2)}`,
    ],
  ];

  const ws = XLSX.utils.aoa_to_sheet(wsData); 

  const wsCols = [
    { wch: 10 },
    { wch: 25 },
    { wch: 25 },
    { wch: 20 },
    { wch: 18 },
    { wch: 18 },
    { wch: 18 },
    { wch: 18 },
  ];
  ws["!cols"] = wsCols;

  XLSX.utils.book_append_sheet(wb, ws, "Rentabilidad");
  XLSX.writeFile(wb, "reporte_rentabilidad.xlsx");
  alert("Exportando a Excel..."); 
}

// PDF
function exportarPDF() {
  const { datos, totalUtilidad } = getDatosFiltrados();
 
  const { jsPDF } = window.jspdf;
  const doc = new jsPDF("l", "mm", "a4")

  doc.setFontSize(18);
  doc.text("Reporte de Rentabilidad por Contrato", 14, 15);
  doc.setFontSize(10);
  doc.text(
    `Filtro Cliente: ${
      document.getElementById("filtro-cliente").value || "Todos"
    }`,
    14,
    22
  );

  const headers = [
    [
      "ID Contrato",
      "Cliente",
      "Maquinaria",
      "Fecha Inicio",
      "Ingresos Alquiler",
      "Costo Choferes",
      "Costo Combustible",
      "Utilidad Bruta",
    ],
  ];
  const rows = datos;

  doc.autoTable({
    head: headers,
    body: rows,
    startY: 30,
    theme: "striped",
    headStyles: { fillColor: [44, 62, 80], textColor: 255, fontStyle: "bold" }, // Azul oscuro SafeWay
    foot: [
      [
        "",
        "",
        "",
        "",
        "",
        "",
        "Total Utilidad",
        `S/ ${totalUtilidad.toFixed(2)}`,
      ],
    ],
    footStyles: { fillColor: [245, 166, 35], textColor: 0, fontStyle: "bold" }, 
    columnStyles: {
      7: { fontStyle: "bold" }, // Columna de Utilidad
    },
  });

  doc.save("reporte_rentabilidad.pdf");
  alert("Exportando a PDF..."); 
}

/* -------------------------------------
   UTILIDADES FECHA
------------------------------------- */
function formatearFechaTabla(fechaIso) {
    if (!fechaIso) return "N/A";

    const fechaStr = String(fechaIso);

    const fechaFormato = fechaStr.replace(" ", "T");
    const date = new Date(fechaFormato);

    if (isNaN(date.getTime())) return fechaStr;

    return date.toLocaleString("es-CL", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
    });
}

// Exponer funciones globalmente
window.filtrarRentabilidad = filtrarRentabilidad;
window.exportarExcel = exportarExcel;
window.exportarPDF = exportarPDF;
