/**
 * CineStar - Utilidades Base para Confitería
 * Funciones reutilizables para formateo y detección
 */

// ==========================================
// 🔢 FORMATEO DE NÚMERO DE TARJETA
// ==========================================

/**
 * Formatea número de tarjeta: "4111111111111111" → "4111 1111 1111 1111"
 */
function formatearTarjeta(numero) {
    const limpio = numero.replace(/\D/g, '');
    return limpio.replace(/(.{4})/g, '$1 ').trim();
}

/**
 * Limpia número de tarjeta (quita espacios)
 */
function limpiarTarjeta(numero) {
    return numero.replace(/\D/g, '');
}

// ==========================================
// 💳 DETECCIÓN DE TIPO DE TARJETA
// ==========================================

/**
 * Detecta el tipo de tarjeta según el número (BIN ranges)
 * @returns 'visa', 'mastercard', 'amex', 'discover', 'diners', 'unknown'
 */
function detectarTipoTarjeta(numero) {
    const limpio = limpiarTarjeta(numero);
    if (limpio.length === 0) return 'unknown';
    
    // Visa: Empieza con 4, longitud 13 o 16
    if (/^4[0-9]{12}(?:[0-9]{3})?$/.test(limpio)) return 'visa';
    
    // Mastercard: 51-55 o 2221-2720, longitud 16
    if (/^(5[1-5][0-9]{14}|2(22[1-9][0-9]{12}|2[3-9][0-9]{13}|[3-6][0-9]{14}|7[0-1][0-9]{13}|720[0-9]{12}))$/.test(limpio)) return 'mastercard';
    
    // American Express: 34 o 37, longitud 15
    if (/^3[47][0-9]{13}$/.test(limpio)) return 'amex';
    
    // Discover: 6011, 65, 644-649
    if (/^(6011|65[0-9]{2}|64[4-9][0-9])[0-9]{12}$/.test(limpio)) return 'discover';
    
    // Diners Club: 300-305, 36, 38, longitud 14
    if (/^3(?:0[0-5]|[68][0-9])[0-9]{11}$/.test(limpio)) return 'diners';
    
    return 'unknown';
}

/**
 * Obtiene el icono de Bootstrap según el tipo de tarjeta
 */
function obtenerIconoTarjeta(tipo) {
    const iconos = {
        'visa': 'bi-credit-card-2-front-fill',
        'mastercard': 'bi-credit-card-2-back-fill',
        'amex': 'bi-credit-card-fill',
        'discover': 'bi-credit-card',
        'diners': 'bi-credit-card',
        'unknown': 'bi-credit-card'
    };
    return iconos[tipo] || iconos['unknown'];
}

/**
 * Obtiene el nombre legible del tipo de tarjeta
 */
function obtenerNombreTarjeta(tipo) {
    const nombres = {
        'visa': 'Visa',
        'mastercard': 'Mastercard',
        'amex': 'American Express',
        'discover': 'Discover',
        'diners': 'Diners Club',
        'unknown': 'Tarjeta'
    };
    return nombres[tipo] || nombres['unknown'];
}

/**
 * Obtiene la longitud máxima según el tipo de tarjeta
 */
function obtenerLongitudMaxima(tipo) {
    const longitudes = {
        'visa': 16,
        'mastercard': 16,
        'amex': 15,
        'discover': 16,
        'diners': 14,
        'unknown': 16
    };
    return longitudes[tipo] || 16;
}

/**
 * Obtiene la longitud del CVV según el tipo de tarjeta
 */
function obtenerLongitudCVV(tipo) {
    return tipo === 'amex' ? 4 : 3;
}

// ==========================================
// 💰 FORMATEO DE MONEDA
// ==========================================

/**
 * Formatea un número como moneda peruana (S/ 10.00)
 */
function formatearMoneda(monto) {
    return 'S/ ' + parseFloat(monto).toFixed(2);
}