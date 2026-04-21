package com.miaprendizaje.servicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RequerimientoController {

    // Lista estática: actuará como nuestra base de datos temporal
    private static List<Usuario> listaUsuarios = new ArrayList<>();

    @GetMapping("/usuarios")
    public List<Usuario> listar() {
        return listaUsuarios;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<String> guardar(@RequestBody Usuario usuario) {

        // 1. Validación de nombre
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
        return ResponseEntity.badRequest().body("Error: El nombre es obligatorio.");
        }

        // 2. Validación de correo (que contenga un @)
        if (usuario.getCorreo() == null || !usuario.getCorreo().contains("@")) {
            return ResponseEntity.badRequest().body("Error: El formato del correo es inválido. ");
        }

        // 3. Si pasa las validaciones, lo guardamos
        listaUsuarios.add(usuario);
        return ResponseEntity.ok("Usuario " + usuario.getNombre() + " registrado existosamente. ");
    }

    @GetMapping("/usuarios/{nombre}")
    public Object buscarPorNombre(@PathVariable String nombre) {
        // Recorremos la lista que tenemos en memoria
        for (Usuario u : listaUsuarios) {
            // Comparamos el nombre (Ignorando mayusculas/minúsculas)
            if (u.getNombre().equalsIgnoreCase(nombre)) {
                return u; // Si lo encuentra, devuelve el objeto Usuario (Spring lo harpa JSON)
            }
        }

        // Si llega aqu´´i es porque no lo encontrpo
        return "Error: El usuario '" + nombre + "' noexiste en el sistema.";
    }
}