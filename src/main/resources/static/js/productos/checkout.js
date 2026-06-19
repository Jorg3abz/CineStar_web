/**
 * CineStar - Lógica de Checkout con QR para Yape/Plin
 */

// Variable global para almacenar el monto total (para generar el QR)
let montoTotal = 0;

document.addEventListener('DOMContentLoaded', function() {
    const formPago = document.getElementById('formPago');
    if (!formPago) return;
    
    // ==========================================
    // 🎯 OBTENER ELEMENTOS DEL DOM
    // ==========================================
    const numeroTarjeta = document.getElementById('numeroTarjeta');
    const nombreTitular = document.getElementById('nombreTitular');
    const fechaExpiracion = document.getElementById('fechaExpiracion');
    const cvv = document.getElementById('cvv');
    const btnPagar = document.getElementById('btnPagar');
    const iconoTarjeta = document.getElementById('iconoTarjeta');
    const tipoTarjetaTexto = document.getElementById('tipoTarjetaTexto');
    const textoBotonPagar = document.getElementById('textoBotonPagar');
    
    // Yape
    const codigoOperacionYape = document.getElementById('codigoOperacionYape');
    // Plin
    const codigoOperacionPlin = document.getElementById('codigoOperacionPlin');
    
    // Contenedores
    const camposTarjeta = document.getElementById('camposTarjeta');
    const camposYape = document.getElementById('camposYape');
    const camposPlin = document.getElementById('camposPlin');
    
    // Radios
    const radiosMetodoPago = document.querySelectorAll('.metodo-pago');
    const tipTexto = document.getElementById('tipTexto');
    
    // Obtener monto total desde el DOM
    const totalSpan = document.querySelector('.col-lg-7 .fs-2.fw-bold.cine-gold-text');
    if (totalSpan) {
        const textoMonto = totalSpan.textContent.replace('S/', '').replace(',', '').trim();
        montoTotal = parseFloat(textoMonto) || 0;
    }
    
    console.log('[CineStar] Checkout inicializado. Monto total: S/', montoTotal);
    
    // ==========================================
    // 🔄 CAMBIO DE MÉTODO DE PAGO
    // ==========================================
    radiosMetodoPago.forEach(radio => {
        radio.addEventListener('change', function() {
            const metodo = this.value;
            
            // Ocultar todos
            camposTarjeta.style.display = 'none';
            camposYape.style.display = 'none';
            camposPlin.style.display = 'none';
            
            // Mostrar según método y actualizar botón
            if (metodo === 'TARJETA') {
                camposTarjeta.style.display = 'block';
                tipTexto.innerHTML = 'Usa una tarjeta que inicie con <span class="cine-gold-text fw-bold">4</span> (Visa) para pago aprobado.';
                textoBotonPagar.textContent = 'Pagar ';
            } else if (metodo === 'YAPE') {
                camposYape.style.display = 'block';
                tipTexto.innerHTML = 'Usa el código <span class="cine-gold-text fw-bold">123456</span> para simular pago aprobado. Cualquier otro será rechazado.';
                textoBotonPagar.textContent = 'Confirmar Pago ';
                generarQR('qrYape', 'YAPE', montoTotal);
            } else if (metodo === 'PLIN') {
                camposPlin.style.display = 'block';
                tipTexto.innerHTML = 'Usa el código <span class="cine-gold-text fw-bold">123456</span> para simular pago aprobado. Cualquier otro será rechazado.';
                textoBotonPagar.textContent = 'Confirmar Pago ';
                generarQR('qrPlin', 'PLIN', montoTotal);
            }
            
            actualizarEstadoBotonPagar();
        });
    });
    
    // ==========================================
    // 📱 GENERAR QR DINÁMICO
    // ==========================================
    function generarQR(elementId, metodo, monto) {
        const qrContainer = document.getElementById(elementId);
        if (!qrContainer) return;
        
        // Datos del QR (simulando lo que enviaría una app real)
        const qrData = JSON.stringify({
            comercio: 'CineStar SAC',
            metodo: metodo,
            monto: monto,
            moneda: 'PEN',
            concepto: 'Compra de confitería',
            fecha: new Date().toISOString()
        });
        
        // Limpiar contenedor
        qrContainer.innerHTML = '';
        
        // Generar QR con la librería
        if (typeof QRCode !== 'undefined') {
            new QRCode(qrContainer, {
                text: qrData,
                width: 200,
                height: 200,
                colorDark: metodo === 'YAPE' ? '#722F96' : '#00B4D8',
                colorLight: '#ffffff',
                correctLevel: QRCode.CorrectLevel.H
            });
        } else {
            // Fallback si la librería no cargó
            qrContainer.innerHTML = '<div class="d-flex align-items-center justify-content-center h-100 text-muted">QR no disponible</div>';
        }
    }
    
    // ==========================================
    // 💳 EVENTOS DE TARJETA (igual que antes)
    // ==========================================
    numeroTarjeta.addEventListener('input', function(e) {
        const limpio = limpiarTarjeta(e.target.value);
        const tipo = detectarTipoTarjeta(limpio);
        const maxDigitos = obtenerLongitudMaxima(tipo);
        const limitado = limpio.substring(0, maxDigitos);
        e.target.value = formatearTarjeta(limitado);
        
        iconoTarjeta.innerHTML = `<i class="bi ${obtenerIconoTarjeta(tipo)}"></i>`;
        
        if (limpio.length > 0) {
            const validacion = validarNumeroTarjeta(limpio);
            aplicarFeedback(e.target, validacion.valido, tipoTarjetaTexto, validacion.mensaje);
        } else {
            limpiarFeedback(e.target, tipoTarjetaTexto, 'Ingresa el número de tu tarjeta');
            iconoTarjeta.innerHTML = '<i class="bi bi-credit-card"></i>';
        }
        
        cvv.maxLength = obtenerLongitudCVV(tipo);
        cvv.placeholder = tipo === 'amex' ? '1234' : '123';
        
        actualizarEstadoBotonPagar();
    });
    
    nombreTitular.addEventListener('input', function(e) {
        e.target.value = e.target.value.toUpperCase();
        if (e.target.value.trim().length > 0) {
            const esValido = validarNombreTitular(e.target.value);
            e.target.style.borderColor = esValido ? 'rgba(40, 167, 69, 0.5)' : 'rgba(230, 57, 70, 0.5)';
            e.target.classList.remove('is-valid', 'is-invalid');
            e.target.classList.add(esValido ? 'is-valid' : 'is-invalid');
        } else {
            limpiarFeedback(e.target);
        }
        actualizarEstadoBotonPagar();
    });
    
    fechaExpiracion.addEventListener('input', function(e) {
        let valor = e.target.value.replace(/\D/g, '').substring(0, 4);
        if (valor.length >= 2) {
            let mes = parseInt(valor.substring(0, 2), 10);
            if (mes > 12) mes = 12;
            if (mes < 1 && valor.substring(0, 2) !== '0' && valor.substring(0, 2) !== '00') mes = 1;
            valor = String(mes).padStart(2, '0') + valor.substring(2);
        }
        if (valor.length >= 3) valor = valor.substring(0, 2) + '/' + valor.substring(2);
        e.target.value = valor;
        
        if (valor.length === 5) {
            const validacion = validarFechaExpiracion(valor);
            aplicarFeedback(e.target, validacion.valido, null, '');
            e.target.style.borderColor = validacion.valido ? 'rgba(40, 167, 69, 0.5)' : 'rgba(230, 57, 70, 0.5)';
        } else if (valor.length === 0) {
            limpiarFeedback(e.target);
        }
        actualizarEstadoBotonPagar();
    });
    
    cvv.addEventListener('input', function(e) {
        e.target.value = e.target.value.replace(/\D/g, '');
        const tipoTarjeta = detectarTipoTarjeta(numeroTarjeta.value);
        if (e.target.value.length > 0) {
            const validacion = validarCVV(e.target.value, tipoTarjeta);
            e.target.style.borderColor = validacion.valido ? 'rgba(40, 167, 69, 0.5)' : 'rgba(230, 57, 70, 0.5)';
            e.target.classList.remove('is-valid', 'is-invalid');
            e.target.classList.add(validacion.valido ? 'is-valid' : 'is-invalid');
        } else {
            limpiarFeedback(e.target);
        }
        actualizarEstadoBotonPagar();
    });
    
    // ==========================================
    // 🔢 EVENTOS DE CÓDIGO DE OPERACIÓN (Yape/Plin)
    // ==========================================
    [codigoOperacionYape, codigoOperacionPlin].forEach(input => {
        if (!input) return;
        
        input.addEventListener('input', function(e) {
            e.target.value = e.target.value.replace(/\D/g, '');
            
            if (e.target.value.length > 0) {
                const esValido = e.target.value.length === 6;
                e.target.style.borderColor = esValido ? 'rgba(40, 167, 69, 0.5)' : 'rgba(230, 57, 70, 0.5)';
                e.target.style.letterSpacing = '8px';
                e.target.classList.remove('is-valid', 'is-invalid');
                e.target.classList.add(esValido ? 'is-valid' : 'is-invalid');
            } else {
                limpiarFeedback(e.target);
            }
            
            actualizarEstadoBotonPagar();
        });
    });
    
    // ==========================================
    // 🔘 ACTUALIZAR ESTADO DEL BOTÓN PAGAR
    // ==========================================
    function actualizarEstadoBotonPagar() {
        const metodoSeleccionado = document.querySelector('.metodo-pago:checked').value;
        let esValido = false;
        
        if (metodoSeleccionado === 'TARJETA') {
            esValido = validarFormularioCompleto();
        } else if (metodoSeleccionado === 'YAPE') {
            esValido = codigoOperacionYape && codigoOperacionYape.value.length === 6;
        } else if (metodoSeleccionado === 'PLIN') {
            esValido = codigoOperacionPlin && codigoOperacionPlin.value.length === 6;
        }
        
        btnPagar.disabled = !esValido;
        btnPagar.style.opacity = esValido ? '1' : '0.5';
        btnPagar.style.cursor = esValido ? 'pointer' : 'not-allowed';
    }
    
    // ==========================================
    // 🚀 EVENTO: Submit del Formulario
    // ==========================================
    formPago.addEventListener('submit', function(e) {
        const metodoSeleccionado = document.querySelector('.metodo-pago:checked').value;
        let esValido = false;
        
        if (metodoSeleccionado === 'TARJETA') {
            esValido = validarFormularioCompleto();
        } else if (metodoSeleccionado === 'YAPE') {
            esValido = codigoOperacionYape && codigoOperacionYape.value.length === 6;
        } else if (metodoSeleccionado === 'PLIN') {
            esValido = codigoOperacionPlin && codigoOperacionPlin.value.length === 6;
        }
        
        if (!esValido) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Datos incompletos',
                text: 'Por favor completa todos los campos correctamente',
                confirmButtonColor: '#f5c518'
            });
            return;
        }
        
        btnPagar.disabled = true;
        btnPagar.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Procesando pago...';
    });
    
    // Inicializar
    actualizarEstadoBotonPagar();
});

/**
 * Valida número de celular peruano (por si lo necesitamos después)
 */
function validarNumeroCelular(numero) {
    const limpio = numero.replace(/\D/g, '');
    return limpio.length === 9 && limpio.startsWith('9');
}