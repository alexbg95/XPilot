package com.XPilot.XPilot.services;

import com.XPilot.XPilot.models.usuario;
import com.XPilot.XPilot.repositories.usuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - usuarioServicesManager")
class usuarioServicesManagerTest {

    @Mock
    private usuarioRepository repository;

    @InjectMocks
    private usuarioServicesManager servicesManager;

    private usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setEmail("cliente@xpilot.com");
        usuarioEjemplo.setUname("cliente01");
        usuarioEjemplo.setPassw("$2a$10$hashedpassword");
        usuarioEjemplo.setRol("USER");
    }

    // ─── PRUEBA 1 ────────────────────────────────────────────────
    @Test
    @DisplayName("save() debe persistir el usuario y retornarlo")
    void testSave_DebeRetornarUsuarioGuardado() {
        // Arrange
        when(repository.save(usuarioEjemplo)).thenReturn(usuarioEjemplo);

        // Act
        usuario resultado = servicesManager.save(usuarioEjemplo);

        // Assert
        assertNotNull(resultado);
        assertEquals("cliente@xpilot.com", resultado.getEmail());
        assertEquals("USER", resultado.getRol());
        verify(repository, times(1)).save(usuarioEjemplo);
    }

    // ─── PRUEBA 2 ────────────────────────────────────────────────
    @Test
    @DisplayName("find() debe retornar el usuario cuando existe")
    void testFind_CuandoExiste_DebeRetornarUsuario() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));

        // Act
        usuario resultado = servicesManager.find(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals("cliente01", resultado.getUname());
        verify(repository, times(1)).findById(1L);
    }

    // ─── PRUEBA 3 ────────────────────────────────────────────────
    @Test
    @DisplayName("find() debe retornar null cuando el usuario no existe")
    void testFind_CuandoNoExiste_DebeRetornarNull() {
        // Arrange
        when(repository.findById(50L)).thenReturn(Optional.empty());

        // Act
        usuario resultado = servicesManager.find(50L);

        // Assert
        assertNull(resultado);
        verify(repository, times(1)).findById(50L);
    }

    // ─── PRUEBA 4 ────────────────────────────────────────────────
    @Test
    @DisplayName("all() debe retornar todos los usuarios")
    void testAll_DebeRetornarListaDeUsuarios() {
        // Arrange
        usuario admin = new usuario();
        admin.setId(2L);
        admin.setEmail("admin@xpilot.com");
        admin.setRol("ADMIN");

        when(repository.findAll()).thenReturn(Arrays.asList(usuarioEjemplo, admin));

        // Act
        List<usuario> resultado = servicesManager.all();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("USER", resultado.get(0).getRol());
        assertEquals("ADMIN", resultado.get(1).getRol());
        verify(repository, times(1)).findAll();
    }

    // ─── PRUEBA 5 ────────────────────────────────────────────────
    @Test
    @DisplayName("delete() debe invocar deleteById correctamente")
    void testDelete_DebeInvocarDeleteById() {
        // Arrange
        doNothing().when(repository).deleteById(1L);

        // Act
        servicesManager.delete(1L);

        // Assert
        verify(repository, times(1)).deleteById(1L);
        verify(repository, never()).deleteById(99L);
    }

    // ─── PRUEBA 6 ────────────────────────────────────────────────
    @Test
    @DisplayName("update() debe actualizar email, uname y passw cuando existe")
    void testUpdate_CuandoExiste_DebeActualizarDatos() {
        // Arrange
        usuario datosNuevos = new usuario();
        datosNuevos.setUname("cliente_actualizado");
        datosNuevos.setEmail("nuevo@xpilot.com");
        datosNuevos.setPassw("$2a$10$nuevohash");

        when(repository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));
        when(repository.save(any(usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        usuario resultado = servicesManager.update(1L, datosNuevos);

        // Assert
        assertNotNull(resultado);
        assertEquals("cliente_actualizado", resultado.getUname());
        assertEquals("nuevo@xpilot.com", resultado.getEmail());
        verify(repository, times(1)).save(any(usuario.class));
    }

    // ─── PRUEBA 7 ────────────────────────────────────────────────
    @Test
    @DisplayName("update() debe retornar null cuando el usuario no existe")
    void testUpdate_CuandoNoExiste_DebeRetornarNull() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act
        usuario resultado = servicesManager.update(99L, new usuario());

        // Assert
        assertNull(resultado);
        verify(repository, never()).save(any());
    }
}
