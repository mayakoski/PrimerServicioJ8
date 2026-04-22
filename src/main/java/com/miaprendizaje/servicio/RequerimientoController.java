package com.miaprendizaje.servicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api")
public class RequerimientoController {

    private static List<Usuario> listaUsuarios = new ArrayList<>();
    private final String ARCHIVO_DATOS = "usuarios.txt";

    /**
     * Este método se ejecuta AUTOMÁTICAMENTE apenas arranca el servidor.
     * Carga los datos del archivo a la lista en memoria.
     */
    @PostConstruct
    public void inicializar() {
        try {
            Path path = Paths.get(ARCHIVO_DATOS);
            if (Files.exists(path)) {
                List<String> lineas = Files.readAllLines(path);
                listaUsuarios.clear();
                for (String l : lineas) {
                    String[] datos = l.split(",");
                    if (datos.length >= 3) {
                        Usuario u = new Usuario();
                        u.setNombre(datos[0]);
                        u.setApellido(datos[1]);
                        u.setCorreo(datos[2]);
                        listaUsuarios.add(u);
                    }
                }
                System.out.println("LOG: Datos cargados automáticamente desde el disco.");
            }
        } catch (IOException e) {
            System.out.println("LOG: No se pudo realizar la carga automática: " + e.getMessage());
        }
    }

    // 1. GUARDAR (POST) - Registra y escribe en el archivo
    @PostMapping("/usuarios")
    public ResponseEntity<String> guardar(@RequestBody Usuario usuario) {
        // 1. Validación de nombre
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El nombre es obligatorio.");
        }
        // 2. Validación de correo (que contenga un @)
        if (usuario.getCorreo() == null || !usuario.getCorreo().contains("@")) {
            return ResponseEntity.badRequest().body("Error: El formato del correo es inválido.");
        }

        // 3. Validación de duplicados (No permite correos iguales)
        for (Usuario u : listaUsuarios) {
            if (u.getCorreo().equalsIgnoreCase(usuario.getCorreo())) {
                return ResponseEntity.status(400).body("Error: El correo " + usuario.getCorreo() + "' ya existe.");
            }
        }

        // Si pasa las validaciones, lo guardamos
        listaUsuarios.add(usuario);

        String linea = usuario.getNombre() + "," + usuario.getApellido() + "," + usuario.getCorreo() + System.lineSeparator();
        try {
            Files.write(Paths.get(ARCHIVO_DATOS), linea.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al guardar en disco.");
        }
        return ResponseEntity.ok("Usuario " + usuario.getNombre() + " registrado y guardado exitosamente.");
    }

    // 2. LISTAR TODOS (GET) - Para ver en el navegador
    @GetMapping("/usuarios")
    public List<Usuario> obtenerTodos() {
        return listaUsuarios;
    }

    // 3. BUSCAR POR NOMBRE (GET con variable)
    @GetMapping("/usuarios/{nombre}")
    public Object buscarPorNombre(@PathVariable String nombre) {
        for (Usuario u : listaUsuarios) {
            if (u.getNombre().equalsIgnoreCase(nombre)) {
                return u;
            }
        }
        return "Error: El usuario '" + nombre + "' no existe en el sistema.";
    }

    // 4. CARGAR MANUAL (Por si necesitas forzar la lectura del archivo)
    @GetMapping("/cargar-disco")
    public String cargarManual() {
        inicializar();
        return "Proceso de carga ejecutado.";
    }
}