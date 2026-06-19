/**
 * CineStar - Validadores Profesionales
 * Incluye algoritmo de Luhn (el mismo que usan bancos reales)
 */

// ==========================================
// 🔐 ALGORITMO DE LUHN (Validación real de tarjetas)
// ==========================================

/**
 * Valida un número de tarjeta usando el algoritmo de Luhn
 * Este es el MISMO algoritmo que usan Visa, Mastercard, etc.
 * @returns {boolean} true si el número es válido
 */
function validarLuhn(numero) {
    const limpio = limpiarTarjeta(numero);
    
    // Debe tener entre 13 y 19 dígitos
    if (limpio.length < 13 || limpio.length > 19) return false;
    
    // Algoritmo de Luhn
    let suma = 0;
    let alternar = false;
    
    for (let i = limpio.length - 1; i >= 0; i--) {
        let digito = parseInt(limpio.charAt(i), 10);
        
        if (alternar) {
            digito *= 2;
            if (digito > 9) digito -= 9;
        }
        
        suma += digito;
        alternar = !alternar;
    }
    
    return suma % 10 === 0;
}

// ==========================================
// 📧 VALIDADORES DE CAMPOS
// ==========================================

/**
 * Valida formato de email
 */
function validarEmail(email) {
    if (!email || email.trim() === '') return false;
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
}

/**
 * Valida nombre del titular (mínimo 3 caracteres, solo letras y espacios)
 */
function validarNombreTitular(nombre) {
    if (!nombre || nombre.trim().length < 3) return false;
    const regex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
    return regex.test(nombre.trim());
}

/**
 * Valida fecha de expiración (MM/AA)
 * @returns {Object} { valido: boolean, mensaje: string }
 */
function validarFechaExpiracion(fecha) {
    if (!fecha || fecha.trim() === '') {
        return { valido: false, mensaje: 'Ingresa la fecha' };
    }
    
    // Formato MM/AA
    const regex = /^(0[1-9]|1[0-2])\/(\d{2})$/;
    const match = fecha.match(regex);
    
    if (!match) {
        return { valido: false, mensaje: 'Formato inválido (MM/AA)' };
    }
    
    const mes = parseInt(match[1], 10);
    const anio = 2000 + parseInt(match[2], 10);
    
    // Obtener fecha actual
    const ahora = new Date();
    const mesActual = ahora.getMonth() + 1;
    const anioActual = ahora.getFullYear();
    
    // Validar que no esté vencida
    if (anio < anioActual || (anio === anioActual && mes < mesActual)) {
        return { valido: false, mensaje: 'Tarjeta vencida' };
    }
    
    // Validar que no sea más de 10 años en el futuro
    if (anio > anioActual + 10) {
        return { valido: false, mensaje: 'Fecha demasiado lejana' };
    }
    
    return { valido: true, mensaje: 'Fecha válida' };
}

/**
 * Valida CVV según el tipo de tarjeta
 */
function validarCVV(cvv, tipoTarjeta) {
    const limpio = cvv.replace(/\D/g, '');
    const longitudEsperada = obtenerLongitudCVV(tipoTarjeta);
    
    if (limpio.length !== longitudEsperada) {
        return { 
            valido: false, 
            mensaje: `Debe tener ${longitudEsperada} dígitos` 
        };
    }
    
    return { valido: true, mensaje: 'CVV válido' };
}

/**
 * Valida número de tarjeta completo (Luhn + longitud)
 */
function validarNumeroTarjeta(numero) {
    const limpio = limpiarTarjeta(numero);
    
    if (limpio.length === 0) {
        return { valido: false, mensaje: 'Ingresa el número' };
    }
    
    const tipo = detectarTipoTarjeta(limpio);
    const longitudEsperada = obtenerLongitudMaxima(tipo);
    
    if (limpio.length < 13) {
        return { valido: false, mensaje: 'Número muy corto' };
    }
    
    if (limpio.length !== longitudEsperada) {
        return { valido: false, mensaje: `Debe tener ${longitudEsperada} dígitos` };
    }
    
    if (!validarLuhn(limpio)) {
        return { valido: false, mensaje: 'Número inválido' };
    }
    
    return { 
        valido: true, 
        mensaje: `${obtenerNombreTarjeta(tipo)} válida`,
        tipo: tipo
    };
}

// ==========================================
// 🎨 FEEDBACK VISUAL (clases de Bootstrap)
// ==========================================

/**
 * Aplica feedback visual a un campo
 * @param {HTMLElement} input - El campo input
 * @param {boolean} esValido - Si la validación pasó
 * @param {HTMLElement} feedbackEl - Elemento donde mostrar el mensaje
 * @param {string} mensaje - Mensaje a mostrar
 */
function aplicarFeedback(input, esValido, feedbackEl = null, mensaje = '') {
    if (esValido) {
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
        input.style.borderColor = 'rgba(40, 167, 69, 0.5)';
        if (feedbackEl) {
            feedbackEl.innerHTML = `<i class="bi bi-check-circle-fill me-1" style="color: #28a745;"></i><span style="color: #28a745;">${mensaje}</span>`;
        }
    } else {
        input.classList.remove('is-valid');
        input.classList.add('is-invalid');
        input.style.borderColor = 'rgba(230, 57, 70, 0.5)';
        if (feedbackEl) {
            feedbackEl.innerHTML = `<i class="bi bi-x-circle-fill me-1" style="color: #e63946;"></i><span style="color: #e63946;">${mensaje}</span>`;
        }
    }
}

/**
 * Limpia el feedback de un campo
 */
function limpiarFeedback(input, feedbackEl = null, mensajeOriginal = '') {
    input.classList.remove('is-valid', 'is-invalid');
    input.style.borderColor = '';
    if (feedbackEl) {
        feedbackEl.innerHTML = mensajeOriginal;
        feedbackEl.className = 'form-text text-muted small mt-1';
    }
}

// ==========================================
// ✅ VALIDACIÓN COMPLETA DEL FORMULARIO
// ==========================================

/**
 * Valida todos los campos del formulario de pago
 * @returns {boolean} true si todo es válido
 */
function validarFormularioCompleto() {
    const numeroTarjeta = document.getElementById('numeroTarjeta');
    const nombreTitular = document.getElementById('nombreTitular');
    const fechaExpiracion = document.getElementById('fechaExpiracion');
    const cvv = document.getElementById('cvv');
    
    if (!numeroTarjeta || !nombreTitular || !fechaExpiracion || !cvv) {
        return false;
    }
    
    const validacionTarjeta = validarNumeroTarjeta(numeroTarjeta.value);
    const validacionNombre = validarNombreTitular(nombreTitular.value);
    const validacionFecha = validarFechaExpiracion(fechaExpiracion.value);
    const tipoTarjeta = detectarTipoTarjeta(numeroTarjeta.value);
    const validacionCVV = validarCVV(cvv.value, tipoTarjeta);
    
    return validacionTarjeta.valido && 
           validacionNombre && 
           validacionFecha.valido && 
           validacionCVV.valido;
}