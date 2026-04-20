package com.example.gr01_1bt3_622_26a.service;

import com.example.gr01_1bt3_622_26a.entity.Solicitud;
import com.example.gr01_1bt3_622_26a.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * TDD - CICLOS COMPLETADOS
 *
 * TEST 5: ✅ Aprobar Solicitud (COMPLETADO: RED → GREEN → REFACTOR)
 *
 * Caso de Uso: Aprobación de solicitud de adopción
 * Descripción: Cambiar el estado de una solicitud existente a "Aprobada" y guardarla
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TDD: Ciclo Completo - Aprobar Solicitud (RED → GREEN → REFACTOR)")
public class SolicitudServiceTest {
    @Mock
    private SolicitudRepository solicitudRepository;

    @InjectMocks
    private SolicitudService solicitudService;

    /**
     * Datos de prueba que se reutilizan en cada test
     */
    private Solicitud solicitudPrueba;
    private static final Long ID_SOLICITUD_EXISTENTE = 1L;

    /**
     * Se ejecuta ANTES de cada test
     * Inicializa los datos que usaremos en los tests
     */
    @BeforeEach
    void configurarDatos() {
        // Crear una solicitud con estado "Pendiente"
        solicitudPrueba = new Solicitud();
        solicitudPrueba.setId(ID_SOLICITUD_EXISTENTE);
        solicitudPrueba.setEstado("Pendiente");
        solicitudPrueba.setFechaSolicitud(LocalDateTime.now());
    }

    /**
     * 🔴 RED: Aprobar Solicitud
     *
     * Este test FALLA porque:
     * - La lógica aún no está implementada correctamente
     * - El estado de la solicitud no cambia a "Aprobada"
     * - El método no cumple el comportamiento esperado
     */
    @Test
    @DisplayName("🔴 RED - Debe cambiar el estado de 'Pendiente' a 'Aprobada'")
    void red_debeAprobarSolicitudCambiandoEstadoAAprobada() {
        // ARRANGE (Preparar)
        // Configurar el mock para devolver la solicitud cuando se llame a findById
        when(solicitudRepository.findById(ID_SOLICITUD_EXISTENTE))
                .thenReturn(Optional.of(solicitudPrueba));
        // También configurar el mock para devolver la solicitud modificada al guardar
        when(solicitudRepository.save(any(Solicitud.class)))
                .thenReturn(solicitudPrueba);

        //ACT (Actuar): Llamar al método que queremos probar
        Solicitud resultado = solicitudService.aprobarSolicitud(ID_SOLICITUD_EXISTENTE);

        // ASSERT (Verificar): El test fallería aquí si el método no estuviera implementado correctamente
        assertThat(resultado)
                .as("La solicitud no debe ser nula")
                .isNotNull();

        assertThat(resultado.getEstado())
                .as("El estado debe cambiar a 'Aprobada'")
                .isEqualTo("Aprobada");
    }

    /**
     * 🟢 GREEN: Aprobar Solicitud
     *
     * Este test PASA porque:
     * - Se implementa la lógica mínima del método
     * - El estado cambia correctamente a "Aprobada"
     * - El comportamiento esperado se cumple
     */
    @Test
    @DisplayName("🟢 GREEN - El método aprueba correctamente la solicitud")
    void green_aprobacionExitosaDeSolicitud() {
        // ARRANGE (Preparar)
        when(solicitudRepository.findById(ID_SOLICITUD_EXISTENTE))
                .thenReturn(Optional.of(solicitudPrueba));

        when(solicitudRepository.save(any(Solicitud.class)))
                .thenReturn(solicitudPrueba);

        // ACT (Actuar)
        Solicitud resultado = solicitudService.aprobarSolicitud(ID_SOLICITUD_EXISTENTE);

        // ASSERT (Verificar)
        // ✅ EN LA FASE GREEN, estas aserciones PASAN
        assertThat(resultado).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("Aprobada");
    }

    /**
     * 🔵 REFACTOR: Aprobar Solicitud
     *
     * En esta fase se mejora el código sin cambiar su comportamiento:
     * - Se optimiza la claridad y organización del test
     * - Se agregan más validaciones (verify)
     * - Se mejora la mantenibilidad del código
     */

    @Test
    @DisplayName("🔵 REFACTOR - Aprobación con verificación de interacciones")
    void refactor_debeAprobarSolicitudYGuardarEnBD() {

        // ARRANGE: Preparar datos y comportamiento del mock
        Long idSolicitud = 1L;
        Solicitud solicitudPendiente = crearSolicitudPendiente(idSolicitud);

        when(solicitudRepository.findById(idSolicitud))
                .thenReturn(Optional.of(solicitudPendiente));
        when(solicitudRepository.save(any(Solicitud.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT: Ejecutar el método a probar
        Solicitud solicitudAprobada = solicitudService.aprobarSolicitud(idSolicitud);

        // ASSERT: Verificar resultados e interacciones
        assertThat(solicitudAprobada).isNotNull();
        assertThat(solicitudAprobada.getEstado()).isEqualTo("Aprobada");
        assertThat(solicitudAprobada.getId()).isEqualTo(idSolicitud);

        verify(solicitudRepository).findById(idSolicitud);
        verify(solicitudRepository).save(solicitudPendiente);
        verifyNoMoreInteractions(solicitudRepository);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MÉTODOS AUXILIARES (Helper Methods)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Crea una solicitud con estado "Pendiente" para uso en tests
     * Reduce la duplicación de código (DRY: Don't Repeat Yourself)
     *
     * @param id el ID de la solicitud
     * @return una Solicitud con estado Pendiente
     */
    private Solicitud crearSolicitudPendiente(Long id) {
        Solicitud solicitud = new Solicitud();
        solicitud.setId(id);
        solicitud.setEstado("Pendiente");
        solicitud.setFechaSolicitud(LocalDateTime.now());
        return solicitud;
    }
}

