package com.cibertec.demo.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import com.cibertec.demo.dto.CarritoItem;
import com.cibertec.demo.dto.ValidationResult;
import com.cibertec.demo.entity.DetalleVenta;
import com.cibertec.demo.entity.ProductoConfiteria;
import com.cibertec.demo.entity.Venta;
import com.cibertec.demo.repository.DetalleVentaRepository;
import com.cibertec.demo.service.PasarelaPagoService;
import com.cibertec.demo.service.ProductoConfiteriaService;
import com.cibertec.demo.service.SnackPurchaseValidatorService;
import com.cibertec.demo.service.VentaService;

@Controller
public class ConfiteriaController {

    private final ProductoConfiteriaService productoService;
    private final SnackPurchaseValidatorService validatorService;
    private final VentaService ventaService;
    private final PasarelaPagoService pasarelaPagoService;
    private final DetalleVentaRepository detalleVentaRepository;

    public ConfiteriaController(
            ProductoConfiteriaService productoService,
            SnackPurchaseValidatorService validatorService,
            VentaService ventaService,
            PasarelaPagoService pasarelaPagoService,
            DetalleVentaRepository detalleVentaRepository) {
        this.productoService = productoService;
        this.validatorService = validatorService;
        this.ventaService = ventaService;
        this.pasarelaPagoService = pasarelaPagoService;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @GetMapping("/snacks")
    public String snacks(Model model) {
        model.addAttribute("listaProductos", productoService.listarTodos());
        return "snacks";
    }

    @PostMapping("/snacks/agregar")
    public String agregarAlCarrito(
            @RequestParam Integer idProducto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        String username = (String) session.getAttribute("usuarioLogueado");
        ValidationResult resultado = validatorService.validarCompra(username, idProducto, 1);
        
        if (resultado == ValidationResult.NO_AUTENTICADO) {
            redirectAttributes.addFlashAttribute("error", "no_autenticado");
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para agregar productos al carrito");
            return "redirect:/snacks";
        }
        if (resultado == ValidationResult.STOCK_INSUFICIENTE) {
            redirectAttributes.addFlashAttribute("error", "stock_insuficiente");
            redirectAttributes.addFlashAttribute("mensajeError", "No hay stock suficiente para este producto");
            return "redirect:/snacks";
        }

        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();

        boolean encontrado = false;
        for (CarritoItem item : carrito) {
            if (item.getProducto().getIdProducto().equals(idProducto)) {
                if (item.getCantidad() < item.getProducto().getStock()) {
                    item.setCantidad(item.getCantidad() + 1);
                } else {
                    redirectAttributes.addFlashAttribute("error", "stock_insuficiente");
                    redirectAttributes.addFlashAttribute("mensajeError", "Stock máximo alcanzado para este producto.");
                }
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            ProductoConfiteria producto = productoService.buscarPorId(idProducto);
            carrito.add(new CarritoItem(producto, 1));
        }

        actualizarTotalesCarrito(session, carrito);
        redirectAttributes.addFlashAttribute("exito", "independiente");
        redirectAttributes.addFlashAttribute("mensajeExito", "Snack agregado al carrito.");
        
        return "redirect:/snacks";
    }

    @PostMapping("/snacks/modificar-cantidad")
    public String modificarCantidad(
            @RequestParam Integer idProducto,
            @RequestParam String accion,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) return "redirect:/snacks";

        for (CarritoItem item : carrito) {
            if (item.getProducto().getIdProducto().equals(idProducto)) {
                if ("aumentar".equals(accion)) {
                    if (item.getCantidad() < item.getProducto().getStock()) {
                        item.setCantidad(item.getCantidad() + 1);
                    } else {
                        redirectAttributes.addFlashAttribute("error", "stock_insuficiente");
                        redirectAttributes.addFlashAttribute("mensajeError", "Stock máximo alcanzado.");
                    }
                } else if ("disminuir".equals(accion)) {
                    item.setCantidad(item.getCantidad() - 1);
                    if (item.getCantidad() <= 0) {
                        carrito.remove(item);
                    }
                }
                break;
            }
        }
        
        actualizarTotalesCarrito(session, carrito);
        return "redirect:/snacks";
    }

    @PostMapping("/snacks/eliminar")
    public String eliminarDelCarrito(
            @RequestParam Integer idProducto, 
            HttpSession session, 
            RedirectAttributes redirectAttributes) {
        
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.removeIf(item -> item.getProducto().getIdProducto().equals(idProducto));
            actualizarTotalesCarrito(session, carrito);
            redirectAttributes.addFlashAttribute("exito", "eliminado");
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado del carrito");
        }
        return "redirect:/snacks";
    }

    @GetMapping("/snacks/limpiar")
    public String limpiar(HttpSession session) {
        session.removeAttribute("carrito");
        session.removeAttribute("totalCarrito");
        session.removeAttribute("cantidad");
        return "redirect:/snacks";
    }

    @GetMapping("/checkout")
    public String mostrarCheckout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("usuarioLogueado");
        @SuppressWarnings("unchecked")
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");

        if (username == null || carrito == null || carrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "carrito_vacio");
            redirectAttributes.addFlashAttribute("mensajeError", "Tu carrito está vacío o debes iniciar sesión.");
            return "redirect:/snacks";
        }

        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", session.getAttribute("totalCarrito"));
        return "checkout"; 
    }

    @PostMapping("/checkout/pagar")
    public String procesarPago(
            @RequestParam String metodoPago,
            @RequestParam(required = false) String numeroTarjeta,
            @RequestParam(required = false) String nombreTitular,
            @RequestParam(required = false) String fechaExpiracion,
            @RequestParam(required = false) String cvv,
            @RequestParam(required = false) String codigoOperacionYape,
            @RequestParam(required = false) String codigoOperacionPlin,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        String username = (String) session.getAttribute("usuarioLogueado");
        @SuppressWarnings("unchecked")
        List<CarritoItem> carritoItems = (List<CarritoItem>) session.getAttribute("carrito");
        Double total = (Double) session.getAttribute("totalCarrito");

        // 🛡️ VALIDACIÓN BACKEND
        if ("TARJETA".equals(metodoPago)) {
            String tarjetaLimpia = numeroTarjeta != null ? numeroTarjeta.replaceAll("\\D", "") : "";
            if (tarjetaLimpia.length() < 13 || tarjetaLimpia.length() > 19) {
                return errorPago(model, carritoItems, total, "Número de tarjeta inválido.");
            }
            if (!validarLuhn(tarjetaLimpia)) {
                return errorPago(model, carritoItems, total, "El número de tarjeta no supera la validación de seguridad.");
            }
            if (nombreTitular == null || nombreTitular.trim().length() < 3) {
                return errorPago(model, carritoItems, total, "El nombre del titular es inválido.");
            }
            if (fechaExpiracion == null || !fechaExpiracion.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
                return errorPago(model, carritoItems, total, "Fecha de expiración inválida.");
            }
            if (cvv == null || !cvv.matches("^\\d{3,4}$")) {
                return errorPago(model, carritoItems, total, "CVV inválido.");
            }
        } else if ("YAPE".equals(metodoPago)) {
            if (codigoOperacionYape == null || !codigoOperacionYape.matches("^\\d{6}$")) {
                return errorPago(model, carritoItems, total, "Código de operación inválido. Debe tener 6 dígitos.");
            }
        } else if ("PLIN".equals(metodoPago)) {
            if (codigoOperacionPlin == null || !codigoOperacionPlin.matches("^\\d{6}$")) {
                return errorPago(model, carritoItems, total, "Código de operación inválido. Debe tener 6 dígitos.");
            }
        }

        // Número a enviar a la pasarela según método
        String numeroParaPasarela;
        if ("YAPE".equals(metodoPago)) {
            numeroParaPasarela = codigoOperacionYape;
        } else if ("PLIN".equals(metodoPago)) {
            numeroParaPasarela = codigoOperacionPlin;
        } else {
            numeroParaPasarela = numeroTarjeta;
        }

        boolean pagoAprobado = pasarelaPagoService.procesarPago(metodoPago, numeroParaPasarela, total);

        if (!pagoAprobado) {
            String mensaje = "Transacción rechazada. ";
            if ("TARJETA".equals(metodoPago)) {
                mensaje += "Prueba con una tarjeta que inicie con 4 (Visa).";
            } else if ("YAPE".equals(metodoPago) || "PLIN".equals(metodoPago)) {
                mensaje += "Usa el código de operación 123456 para simular pago aprobado.";
            }
            return errorPago(model, carritoItems, total, mensaje);
        }

        try {
            List<ProductoConfiteria> productosParaVenta = new ArrayList<>();
            for (CarritoItem item : carritoItems) {
                for (int i = 0; i < item.getCantidad(); i++) {
                    productosParaVenta.add(item.getProducto());
                }
            }

            Venta venta = ventaService.procesarCompraConCarrito(
                username, metodoPago, "INDEPENDIENTE", null, productosParaVenta
            );

            session.removeAttribute("carrito");
            session.removeAttribute("totalCarrito");
            session.removeAttribute("cantidad");

            redirectAttributes.addFlashAttribute("idVentaExitosa", venta.getIdVenta());
            return "redirect:/comprobante";

        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "stock_agotado");
            redirectAttributes.addFlashAttribute("mensajeError", "Lo sentimos, mientras pagabas, alguien más compró los productos.");
            return "redirect:/snacks";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "error_sistema");
            redirectAttributes.addFlashAttribute("mensajeError", "Ocurrió un error al procesar tu compra.");
            return "redirect:/snacks";
        }
    }

    /**
     * Helper para retornar error de pago
     */
    private String errorPago(Model model, List<CarritoItem> carrito, Double total, String mensaje) {
        model.addAttribute("errorPago", true);
        model.addAttribute("mensajeErrorPago", mensaje);
        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", total);
        return "checkout";
    }

    @GetMapping("/comprobante")
    public String mostrarComprobante(Model model) {
        Integer idVenta = (Integer) model.asMap().get("idVentaExitosa");
        if (idVenta == null) return "redirect:/snacks";
        
        Venta venta = ventaService.buscarPorId(idVenta);
        if (venta == null) return "redirect:/snacks";
        
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaIdVenta(idVenta);
        
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        
        System.out.println("=======================================================");
        System.out.println("📧 [SIMULACIÓN DE CORREO ENVIADO]");
        System.out.println("Para: " + venta.getUsername());
        System.out.println("Asunto: Tu compra en CineStar #" + venta.getIdVenta() + " fue exitosa");
        System.out.println("Total cobrado: S/ " + venta.getTotal());
        System.out.println("=======================================================");
        
        return "comprobante";
    }

    private void actualizarTotalesCarrito(HttpSession session, List<CarritoItem> carrito) {
        double total = carrito.stream().mapToDouble(CarritoItem::getSubtotal).sum();
        int cantidadTotalItems = carrito.stream().mapToInt(CarritoItem::getCantidad).sum();
        
        session.setAttribute("carrito", carrito);
        session.setAttribute("totalCarrito", total);
        session.setAttribute("cantidad", cantidadTotalItems);
    }

    /**
     * 🛡️ Algoritmo de Luhn (validación real de tarjetas)
     * Este es el MISMO algoritmo que usan los bancos.
     */
    private boolean validarLuhn(String numero) {
        if (numero == null || numero.isEmpty()) return false;
        
        int suma = 0;
        boolean alternar = false;
        
        for (int i = numero.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numero.charAt(i));
            
            if (alternar) {
                digito *= 2;
                if (digito > 9) digito -= 9;
            }
            
            suma += digito;
            alternar = !alternar;
        }
        
        return suma % 10 == 0;
    }
}