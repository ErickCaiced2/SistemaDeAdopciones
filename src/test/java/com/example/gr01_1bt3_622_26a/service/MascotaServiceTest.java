package com.example.gr01_1bt3_622_26a.service;

import com.example.gr01_1bt3_622_26a.entity.Mascota;
import com.example.gr01_1bt3_622_26a.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * TDD - CICLOS COMPLETADOS
 *
 * TEST 1: 🔴 Cambiar estado (INICIANDO: RED PHASE)
 * TEST 2: 🟢 Cambiar estado (COMPLETADO: GREEN)
 * TEST 3: 🔵 Cambiar estado (COMPLETADO: REFACTOR)
 *
 * Caso de Uso: Cambiar el estado de una mascota
 * Descripción: Actualizar el estado y guardar cambios
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("🔴🟢🔵 TDD por Fases: MascotaService.cambiarEstado()")
public class MascotaServiceTest {

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private MascotaService mascotaService;

    private static final Long ID_MASCOTA = 1L;
    private Mascota mascotaPrueba;

    @BeforeEach
    void configurarDatos() {
        mascotaPrueba = new Mascota();
        mascotaPrueba.setId(ID_MASCOTA);
        mascotaPrueba.setNombre("Buddy");
        mascotaPrueba.setTipo("Perro");
        mascotaPrueba.setEstado("Disponible");
    }

    /**
     * 🔴 RED: Cambiar estado de mascota
     *
     * Este test FALLA porque:
     * - El comportamiento esperado aún no está implementado
     * - El estado no cambia correctamente
     */
    @ParameterizedTest(name = "RED - Cambiar estado a: {0}")
    @CsvSource({
            "Disponible",
            "Adoptado",
            "En proceso",
            "Perdida",
            "Recuperada"
    })
    @DisplayName("🔴 RED - Define comportamiento esperado")
    void red_debeAprobarSolicitudCambiandoEstadoCorrectamente(String nuevoEstado) {
        // ARRANGE: Preparar datos y mocks
        when(mascotaRepository.findById(ID_MASCOTA))
                .thenReturn(Optional.of(mascotaPrueba));
        when(mascotaRepository.save(any(Mascota.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT: Ejecutar el método
        Mascota resultado = mascotaService.cambiarEstado(ID_MASCOTA, nuevoEstado);

        // ASSERT: Validar comportamiento esperado
        assertThat(resultado)
                .as("La mascota no debe ser nula")
                .isNotNull();

        assertThat(resultado.getEstado())
                .as("El estado debe cambiar a: %s", nuevoEstado)
                .isEqualTo(nuevoEstado);

        assertThat(resultado.getId())
                .as("El ID debe mantenerse igual")
                .isEqualTo(ID_MASCOTA);
    }

    /**
     * 🟢 GREEN: Cambiar estado de mascota
     *
     * Este test PASA porque:
     * - Se implementa la lógica mínima
     * - El estado se actualiza correctamente
     */
    @ParameterizedTest(name = "GREEN - Cambiar estado a: {0}")
    @CsvSource({
            "Disponible",
            "Adoptado",
            "En proceso",
            "Perdida",
            "Recuperada"
    })
    @DisplayName("🟢 GREEN - Implementación mínima")
    void verde_debeAprobarSolicitudCambiandoEstadoCorrectamente(String nuevoEstado) {
        // ARRANGE: Preparar datos y mocks
        when(mascotaRepository.findById(ID_MASCOTA))
                .thenReturn(Optional.of(mascotaPrueba));
        when(mascotaRepository.save(any(Mascota.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT: Ejecutar el método
        Mascota resultado = mascotaService.cambiarEstado(ID_MASCOTA, nuevoEstado);

        // ASSERT: Verificar resultado correcto
        assertThat(resultado)
                .as("La mascota debe ser retornada")
                .isNotNull();

        assertThat(resultado.getEstado())
                .as("El estado debe ser: %s", nuevoEstado)
                .isEqualTo(nuevoEstado);

        assertThat(resultado.getId())
                .as("El ID debe ser: %d", ID_MASCOTA)
                .isEqualTo(ID_MASCOTA);
    }

    /**
     * 🔵 REFACTOR: Cambiar estado de mascota
     *
     * Este test mejora:
     * - Validación de interacciones con verify()
     * - Organización del código (AAA)
     */
    @ParameterizedTest(name = "REFACTOR - Cambiar estado a: {0}")
    @CsvSource({
            "Disponible",
            "Adoptado",
            "En proceso",
            "Perdida",
            "Recuperada"
    })
    @DisplayName("🔵 REFACTOR - Validaciones adicionales")
    void refactor_debeAprobarSolicitudCambiandoEstadoCorrectamente(String nuevoEstado) {
        // ARRANGE: Preparar datos y mocks
        when(mascotaRepository.findById(ID_MASCOTA))
                .thenReturn(Optional.of(mascotaPrueba));
        when(mascotaRepository.save(any(Mascota.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT: Ejecutar el método
        Mascota resultado = mascotaService.cambiarEstado(ID_MASCOTA, nuevoEstado);

        // ASSERT: Validar resultado
        assertThat(resultado)
                .as("La mascota debe ser retornada")
                .isNotNull();

        assertThat(resultado.getEstado())
                .as("El estado debe cambiar a: %s", nuevoEstado)
                .isEqualTo(nuevoEstado);

        assertThat(resultado.getId())
                .as("El ID debe mantenerse: %d", ID_MASCOTA)
                .isEqualTo(ID_MASCOTA);

        // VERIFY: Validar interacciones con el mock
        verify(mascotaRepository, times(1))
                .findById(ID_MASCOTA);

        verify(mascotaRepository, times(1))
                .save(any(Mascota.class));

        verifyNoMoreInteractions(mascotaRepository);
    }
}
