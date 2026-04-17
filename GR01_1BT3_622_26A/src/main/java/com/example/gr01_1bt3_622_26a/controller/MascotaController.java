package com.example.gr01_1bt3_622_26a.controller;

import com.example.gr01_1bt3_622_26a.entity.Mascota;
import com.example.gr01_1bt3_622_26a.entity.Foto;
import com.example.gr01_1bt3_622_26a.service.MascotaService;
import com.example.gr01_1bt3_622_26a.service.FotoUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controlador para gestionar mascotas
 */
@Controller
@RequestMapping("/mascotas")
@Slf4j
public class MascotaController {

    private static final String UPLOAD_DIR = "uploads/fotos/";

    @Autowired
    private MascotaService mascotaService;

    @Autowired
    private FotoUploadService fotoUploadService; // REFACTORIZACIÓN 5: Inyectar nuevo servicio

    /**
     * REFACTORIZACIÓN 3: SUBSTITUTE ALGORITHM
     * Genera nombres únicos de archivo usando UUID en lugar de timestamp
     * Beneficio: Mejor rendimiento, elimina colisiones de nombres, más robusto
     */
    private String generarNombreArchivoUnico(String nombreOriginal) {
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        return java.util.UUID.randomUUID().toString() + extension;
    }

    /**
     * Mostrar lista de todas las mascotas
     */
    @GetMapping("/lista")
    public String mostrarListaMascotas(Model model) {
        log.info("Accediendo a la lista de mascotas");
        List<Mascota> mascotas = mascotaService.obtenerTodasLasMascotas();
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("totalMascotas", mascotas.size());
        return "mascotas/listaMascotas";
    }

    /**
     * Mostrar lista de mascotas disponibles
     */
    @GetMapping("/disponibles")
    public String mostrarMascotasDisponibles(Model model) {
        log.info("Accediendo a la lista de mascotas disponibles");
        List<Mascota> mascotas = mascotaService.obtenerMascotasDisponibles();
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("totalMascotas", mascotas.size());
        return "mascotas/listaMascotasDisponibles";
    }

    /**
     * Mostrar formulario de registro de mascota
     */
    @GetMapping("/registrar")
    public String mostrarFormularioRegistro(Model model) {
        log.info("Mostrando formulario de registro de mascota");
        model.addAttribute("mascota", new Mascota());
        return "mascotas/formularioRegistroMascota";
    }

    /**
     * Registrar nueva mascota
     */
    @PostMapping("/registrar")
    public String registrarMascota(@Valid @ModelAttribute("mascota") Mascota mascota,
                                   BindingResult bindingResult,
                                   Model model) {
        log.info("Registrando nueva mascota: {}", mascota.getNombre());

        if (bindingResult.hasErrors()) {
            log.warn("Errores en la validación del formulario");
            model.addAttribute("mascota", mascota);
            return "mascotas/formularioRegistroMascota";
        }

        try {
            Mascota mascotaRegistrada = mascotaService.registrarMascota(mascota);
            log.info("Mascota registrada exitosamente con ID: {}", mascotaRegistrada.getId());
            return "redirect:/mascotas/detalle/" + mascotaRegistrada.getId() + "?exito=true";
        } catch (Exception e) {
            log.error("Error al registrar mascota", e);
            model.addAttribute("error", "Error al registrar la mascota");
            return "mascotas/formularioRegistroMascota";
        }
    }

    /**
     * Mostrar detalle de mascota
     */
    @GetMapping("/detalle/{id}")
    public String mostrarDetalleMascota(@PathVariable Long id, Model model) {
        log.info("Mostrando detalle de mascota con ID: {}", id);

        Mascota mascota = mascotaService.obtenerMascotaPorId(id)
            .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));

        List<Foto> fotos = mascotaService.obtenerFotosDeMascota(id);

        model.addAttribute("mascota", mascota);
        model.addAttribute("fotos", fotos);
        return "mascotas/detalleMascota";
    }

    /**
     * Mostrar formulario de edición
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        log.info("Mostrando formulario de edición para mascota con ID: {}", id);

        Mascota mascota = mascotaService.obtenerMascotaPorId(id)
            .orElseThrow(() -> new RuntimeException("Mascota no encontrada con ID: " + id));

        model.addAttribute("mascota", mascota);
        return "mascotas/formularioEditarMascota";
    }

    /**
     * Actualizar mascota
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarMascota(@PathVariable Long id,
                                    @Valid @ModelAttribute("mascota") Mascota mascota,
                                    BindingResult bindingResult) {
        log.info("Actualizando mascota con ID: {}", id);

        if (bindingResult.hasErrors()) {
            log.warn("Errores en la validación del formulario de actualización");
            return "mascotas/formularioEditarMascota";
        }

        try {
            mascotaService.actualizarMascota(id, mascota);
            log.info("Mascota actualizada exitosamente");
            return "redirect:/mascotas/detalle/" + id + "?exito=true";
        } catch (Exception e) {
            log.error("Error al actualizar mascota", e);
            return "redirect:/mascotas/detalle/" + id + "?error=true";
        }
    }

    /**
     * Eliminar mascota
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarMascota(@PathVariable Long id) {
        log.info("Eliminando mascota con ID: {}", id);

        try {
            mascotaService.eliminarMascota(id);
            log.info("Mascota eliminada exitosamente");
            return "redirect:/mascotas/lista?exito=true";
        } catch (Exception e) {
            log.error("Error al eliminar mascota", e);
            return "redirect:/mascotas/lista?error=true";
        }
    }

    /**
     * Buscar mascotas por nombre
     */
    @GetMapping("/buscar")
    public String buscarMascotas(@RequestParam(value = "nombre", required = false) String nombre, Model model) {
        log.info("Buscando mascotas con nombre: {}", nombre);

        List<Mascota> mascotas;
        if (nombre != null && !nombre.isEmpty()) {
            mascotas = mascotaService.buscarPorNombre(nombre);
        } else {
            mascotas = mascotaService.obtenerTodasLasMascotas();
        }

        model.addAttribute("mascotas", mascotas);
        model.addAttribute("nombre", nombre);
        model.addAttribute("totalMascotas", mascotas.size());
        return "mascotas/resultadosBusqueda";
    }

    /**
     * Cargar foto para mascota
     * REFACTORIZACIÓN 5: EXTRACT CLASS
     * Utiliza FotoUploadService para responsabilidad de carga
     */
    @PostMapping("/cargarFoto/{mascotaId}")
    public String cargarFoto(@PathVariable Long mascotaId,
                            @RequestParam("foto") MultipartFile file) {
        log.info("Cargando foto para mascota con ID: {}", mascotaId);

        try {
            // Usar servicio extraído para guardar foto
            String rutaFoto = fotoUploadService.guardarFoto(file);

            // Crear y guardar objeto Foto
            Foto foto = new Foto();
            foto.setNombreArchivo(file.getOriginalFilename());
            foto.setRutaFoto(rutaFoto);
            foto.setEsPrincipal(false);

            mascotaService.agregarFoto(mascotaId, foto);

            return "redirect:/mascotas/detalle/" + mascotaId + "?exito=true";
        } catch (IllegalArgumentException e) {
            log.warn("Validación fallida: {}", e.getMessage());
            return "redirect:/mascotas/detalle/" + mascotaId + "?error=validacion";
        } catch (IOException e) {
            log.error("Error al cargar foto", e);
            return "redirect:/mascotas/detalle/" + mascotaId + "?error=true";
        }
    }
}

