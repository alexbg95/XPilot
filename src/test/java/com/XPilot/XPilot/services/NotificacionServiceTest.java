package com.XPilot.XPilot.services;

import com.XPilot.XPilot.models.Notificacion;
import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - NotificacionService")
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notifRepo;

    @InjectMocks
    private NotificacionService notificacionService;

    private usuario usuarioEjemplo;
    private Notificacion notifEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setEmail("cliente@xpilot.com");
        usuarioEjemplo.setUname("cliente01");

        notifEjemplo = new Notificacion();
        notifEjemplo.setId(1L);
        notifEjemplo.setMensaje("Tu contrato fue aceptado");
        notifEjemplo.setLeido(false);
        notifEjemplo.setUsuario(usuarioEjemplo);
    }

    // ─── PRUEBA 1 ────────────────────────────────────────────────
    @Test
    @DisplayName("crearNotificacion() debe guardar la notificacion con leido=false")
    void testCrearNotificacion_DebeGuardarConLeidoFalse() {
        // Arrange
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        when(notifRepo.save(any(Notificacion.class))).thenReturn(notifEjemplo);

        // Act
        notificacionService.crearNotificacion(usuarioEjemplo, "Tu contrato fue aceptado");

        // Assert
        verify(notifRepo, times(1)).save(captor.capture());
        Notificacion guardada = captor.getValue();
        assertEquals("Tu contrato fue aceptado", guardada.getMensaje());
        assertFalse(guardada.isLeido(), "La notificación debe crearse como no leída");
        assertEquals(usuarioEjemplo, guardada.getUsuario());
    }

    // ─── PRUEBA 2 ────────────────────────────────────────────────
    @Test
    @DisplayName("crearNotificacion() no debe guardar si el usuario es null")
    void testCrearNotificacion_ConUsuarioNull_NoDebeGuardar() {
        // Act
        notificacionService.crearNotificacion(null, "Mensaje de prueba");

        // Assert
        verify(notifRepo, never()).save(any());
    }

    // ─── PRUEBA 3 ────────────────────────────────────────────────
    @Test
    @DisplayName("obtenerNoLeidas() debe retornar lista de notificaciones no leidas")
    void testObtenerNoLeidas_DebeRetornarListaCorrecta() {
        // Arrange
        Notificacion notif2 = new Notificacion();
        notif2.setId(2L);
        notif2.setMensaje("Nuevo contrato pendiente");
        notif2.setLeido(false);
        notif2.setUsuario(usuarioEjemplo);

        when(notifRepo.findByUsuarioIdAndLeidoFalse(1L))
                .thenReturn(Arrays.asList(notifEjemplo, notif2));

        // Act
        List<Notificacion> resultado = notificacionService.obtenerNoLeidas(1L);

        // Assert
        assertEquals(2, resultado.size());
        assertFalse(resultado.get(0).isLeido());
        assertFalse(resultado.get(1).isLeido());
        verify(notifRepo, times(1)).findByUsuarioIdAndLeidoFalse(1L);
    }

    // ─── PRUEBA 4 ────────────────────────────────────────────────
    @Test
    @DisplayName("contarNoLeidas() debe retornar el numero correcto")
    void testContarNoLeidas_DebeRetornarConteoExacto() {
        // Arrange
        when(notifRepo.countByUsuarioIdAndLeidoFalse(1L)).thenReturn(3L);

        // Act
        long conteo = notificacionService.contarNoLeidas(1L);

        // Assert
        assertEquals(3L, conteo);
        verify(notifRepo, times(1)).countByUsuarioIdAndLeidoFalse(1L);
    }

    // ─── PRUEBA 5 ────────────────────────────────────────────────
    @Test
    @DisplayName("marcarLeida() debe cambiar leido a true")
    void testMarcarLeida_DebePonerLeidoEnTrue() {
        // Arrange
        when(notifRepo.findById(1L)).thenReturn(Optional.of(notifEjemplo));
        when(notifRepo.save(any(Notificacion.class))).thenReturn(notifEjemplo);

        // Act
        notificacionService.marcarLeida(1L);

        // Assert
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        verify(notifRepo, times(1)).save(captor.capture());
        assertTrue(captor.getValue().isLeido(), "La notificación debe marcarse como leída");
    }

    // ─── PRUEBA 6 ────────────────────────────────────────────────
    @Test
    @DisplayName("marcarLeida() debe lanzar excepcion si no existe la notificacion")
    void testMarcarLeida_CuandoNoExiste_DebeLanzarExcepcion() {
        // Arrange
        when(notifRepo.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> notificacionService.marcarLeida(99L),
                "Debe lanzar RuntimeException cuando la notificación no existe");
    }

    // ─── PRUEBA 7 ────────────────────────────────────────────────
    @Test
    @DisplayName("marcarTodas() debe marcar todas las notificaciones como leidas")
    void testMarcarTodas_DebeMarcarTodasComoLeidas() {
        // Arrange
        Notificacion n2 = new Notificacion();
        n2.setId(2L);
        n2.setLeido(false);
        n2.setUsuario(usuarioEjemplo);
        n2.setMensaje("Segunda notificación");

        when(notifRepo.findByUsuarioIdAndLeidoFalse(1L))
                .thenReturn(Arrays.asList(notifEjemplo, n2));

        // Act
        notificacionService.marcarTodas(1L);

        // Assert
        verify(notifRepo, times(1)).saveAll(anyList());
        assertTrue(notifEjemplo.isLeido(), "Primera notificacion debe ser leida");
        assertTrue(n2.isLeido(), "Segunda notificacion debe ser leida");
    }

    // ─── PRUEBA 8 ────────────────────────────────────────────────
    @Test
    @DisplayName("marcarTodas() no debe fallar si no hay notificaciones pendientes")
    void testMarcarTodas_SinNotificaciones_NoDebeFallar() {
        // Arrange
        when(notifRepo.findByUsuarioIdAndLeidoFalse(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertDoesNotThrow(() -> notificacionService.marcarTodas(1L));
        verify(notifRepo, times(1)).saveAll(Collections.emptyList());
    }
}
