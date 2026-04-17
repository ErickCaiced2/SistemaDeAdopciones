package com.example.gr01_1bt3_622_26a.service;

import com.example.gr01_1bt3_622_26a.entity.Foto;
import com.example.gr01_1bt3_622_26a.repository.FotoRepository;
import com.example.gr01_1bt3_622_26a.repository.MascotaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Servicio para gestionar fotos de mascotas
 */
@Service
@Slf4j
@Transactional
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    /**
     * Agregar foto a mascota
     */
    public Foto agregarFoto(Long mascotaId, Foto foto) {
        log.info("Agregando foto a mascota con ID: {}", mascotaId);
        return mascotaRepository.findById(mascotaId).map(mascota -> {
            foto.setMascota(mascota);
            return fotoRepository.save(foto);
        }).orElse(null);
    }

    /**
     * Obtener fotos de una mascota
     */
    public List<Foto> obtenerFotosDeMascota(Long mascotaId) {
        log.info("Obteniendo fotos de mascota con ID: {}", mascotaId);
        return fotoRepository.findByMascotaId(mascotaId);
    }
}
