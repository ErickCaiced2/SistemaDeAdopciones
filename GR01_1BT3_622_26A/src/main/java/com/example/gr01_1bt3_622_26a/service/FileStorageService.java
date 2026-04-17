package com.example.gr01_1bt3_622_26a.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Servicio para gestionar el almacenamiento de archivos en disco
 */
@Service
@Slf4j
public class FileStorageService {

    private static final String UPLOAD_DIR = "uploads/fotos/";

    /**
     * Guarda un archivo en el directorio de subidas y retorna el nombre único
     * generado.
     *
     * @param file archivo a guardar
     * @return nombre único del archivo guardado
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public String guardarArchivo(MultipartFile file) throws IOException {
        // Crear directorio si no existe
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generar nombre único para el archivo
        String nombreArchivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path rutaArchivo = uploadPath.resolve(nombreArchivo);

        // Guardar archivo
        Files.write(rutaArchivo, file.getBytes());

        log.info("Archivo guardado en: {}", rutaArchivo);
        return nombreArchivo;
    }

    /**
     * Retorna la ruta relativa de subida base.
     *
     * @return directorio de subidas
     */
    public String getUploadDir() {
        return UPLOAD_DIR;
    }
}