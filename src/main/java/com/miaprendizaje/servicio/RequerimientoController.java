package com.miaprendizaje.servicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/api")
public class RequerimientoController {

    private static List<Usuario> listaUsuarios = new ArrayList<>();
    private final String ARCHIVO_DATOS = "usuarios.txt";

    // 1. GUARDAR (POST)
    @PostMapping("/usuarios")
    public ResponseEntity<String> guardar(@RequestBody Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El nombre es obligatorio.");
        }
        if (usuario.getCorreo() == null || !usuario.getCorreo().contains("@")) {
            return ResponseEntity.badRequest().body("Error: El formato del correo es inválido.");
        }

        listaUsuarios.add(usuario);

        String linea = usuario.getNombre() + "," + usuario.getApellido() + "," + usuario.getCorreo() + System.lineSeparator();
        try {
            Files.write(Paths.get(ARCHIVO_DATOS), linea.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al guardar en disco");
        }
        return ResponseEntity.ok("Usuario " + usuario.getNombre() + " registrado y guardado exitosamente.");
    }

    // 2. LISTAR TODOS (GET) - ¡Este es el que te faltaba!
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

    // 4. CARGAR DESDE DISCO (GET)
    @GetMapping("/cargar-disco")
    public String cargarDesdeDisco() throws IOException {
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
            return "Datos recuperados desde el archivo.";
        }
        return "No hay archivo de datos previo.";
    }
}