package com.example.gr01_1bt3_622_26a.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * REFACTORIZACIÓN 5: EXTRACT CLASS
 *
 * Servicio especializado para manejar carga y almacenamiento de fotos
 *
 * Beneficio:
 * - Separa responsabilidades de controlador
 * - Reutilizable en múltiples controladores
 * - Lógica centralizada y testeable
 * - Principio Single Responsibility
 * - Mejora mantenibilidad
 */
@Service
@Slf4j
public class FotoUploadService {

    private static final String UPLOAD_DIR = "uploads/fotos/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * Guarda archivo de foto en disco
     * @param file archivo a guardar
     * @return ruta completa del archivo guardado
     * @throws IOException si hay error al guardar
     * @throws IllegalArgumentException si el archivo es inválido
     */
    public String guardarFoto(MultipartFile file) throws IOException {
        validarArchivo(file);

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String nombreArchivo = generarNombreUnico(file.getOriginalFilename());
        Path rutaArchivo = uploadPath.resolve(nombreArchivo);
        Files.write(rutaArchivo, file.getBytes());

        log.info("Foto guardada exitosamente: {}", nombreArchivo);
        return UPLOAD_DIR + nombreArchivo;
    }

    /**
     * Valida que el archivo cumpla requisitos
     */
    private void validarArchivo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacío");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Archivo muy grande (máximo 5MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Solo se permiten imágenes");
        }
    }

    /**
     * Genera nombre único para archivo usando UUID
     */
    private String generarNombreUnico(String nombreOriginal) {
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * Elimina foto del disco
     */
    public void eliminarFoto(String rutaFoto) {
        try {
            Path path = Paths.get(rutaFoto);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("Foto eliminada: {}", rutaFoto);
            }
        } catch (IOException e) {
            log.error("Error al eliminar foto: {}", rutaFoto, e);
        }
    }
}

