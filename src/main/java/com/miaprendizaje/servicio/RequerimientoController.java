package com.miaprendizaje.servicio;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Esta es la ruta base
public class RequerimientoController {

    // 1. Requerimiento: "Quiero consultar el estado del servicio"
    @GetMapping("/estado")
    public String revisarEstado() {
        return "El servicio está funcionando correctamente en Java 8";
    }

    // 2. Requerimiento: "Quiero enviarte mi nombre y que me saludes"
    @GetMapping("/saludo/{nombre}")
    public String saludar(@PathVariable String nombre) {
        return "Hola " + nombre + ", este es tu primer requerimiento atendido.";
    }
}
