package com.XPilot.XPilot.services;

import com.XPilot.XPilot.models.media;
import com.XPilot.XPilot.repositories.mediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias - mediaServicesManager")
class mediaServicesManagerTest {

    @Mock
    private mediaRepository repository;

    @Mock
    private com.cloudinary.Cloudinary cloudinary;

    @InjectMocks
    private mediaServicesManager servicesManager;

    private media mediaEjemplo;

    @BeforeEach
    void setUp() {
        mediaEjemplo = new media();
        mediaEjemplo.setId(1L);
        mediaEjemplo.setArtist("Alex Dark");
        mediaEjemplo.setStyle("Gótico");
        mediaEjemplo.setTags("rock,oscuro");
        mediaEjemplo.setUrlm("https://res.cloudinary.com/test/image.jpg");
        mediaEjemplo.setDatem(LocalDate.of(2026, 1, 15));
    }

    // ─── PRUEBA 1 ────────────────────────────────────────────────
    @Test
    @DisplayName("save() debe persistir el artista y retornarlo")
    void testSave_DebeRetornarMediaGuardado() {
        // Arrange
        when(repository.save(mediaEjemplo)).thenReturn(mediaEjemplo);

        // Act
        media resultado = servicesManager.save(mediaEjemplo);

        // Assert
        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals("Alex Dark", resultado.getArtist(), "El nombre del artista debe coincidir");
        assertEquals("Gótico", resultado.getStyle(), "El estilo debe coincidir");
        verify(repository, times(1)).save(mediaEjemplo);
    }

    // ─── PRUEBA 2 ────────────────────────────────────────────────
    @Test
    @DisplayName("find() debe retornar el artista cuando existe")
    void testFind_CuandoExiste_DebeRetornarMedia() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(mediaEjemplo));

        // Act
        media resultado = servicesManager.find(1L);

        // Assert
        assertNotNull(resultado, "Debe encontrar el artista");
        assertEquals(1L, resultado.getId(), "El ID debe ser 1");
        assertEquals("Alex Dark", resultado.getArtist());
        verify(repository, times(1)).findById(1L);
    }

    // ─── PRUEBA 3 ────────────────────────────────────────────────
    @Test
    @DisplayName("find() debe retornar null cuando no existe el artista")
    void testFind_CuandoNoExiste_DebeRetornarNull() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act
        media resultado = servicesManager.find(99L);

        // Assert
        assertNull(resultado, "Debe retornar null si no existe el artista");
        verify(repository, times(1)).findById(99L);
    }

    // ─── PRUEBA 4 ────────────────────────────────────────────────
    @Test
    @DisplayName("all() debe retornar la lista completa de artistas")
    void testAll_DebeRetornarListaDeArtistas() {
        // Arrange
        media media2 = new media();
        media2.setId(2L);
        media2.setArtist("Sara Jazz");
        media2.setStyle("Jazz");

        when(repository.findAll()).thenReturn(Arrays.asList(mediaEjemplo, media2));

        // Act
        List<media> resultado = servicesManager.all();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe retornar 2 artistas");
        assertEquals("Alex Dark", resultado.get(0).getArtist());
        assertEquals("Sara Jazz", resultado.get(1).getArtist());
        verify(repository, times(1)).findAll();
    }

    // ─── PRUEBA 5 ────────────────────────────────────────────────
    @Test
    @DisplayName("delete() debe invocar deleteById en el repositorio")
    void testDelete_DebeInvocarDeleteById() {
        // Arrange
        doNothing().when(repository).deleteById(1L);

        // Act
        servicesManager.delete(1L);

        // Assert
        verify(repository, times(1)).deleteById(1L);
    }

    // ─── PRUEBA 6 ────────────────────────────────────────────────
    @Test
    @DisplayName("update() debe actualizar los campos cuando el artista existe")
    void testUpdate_CuandoExiste_DebeActualizarCampos() {
        // Arrange
        media datosNuevos = new media();
        datosNuevos.setArtist("Alex Dark Pro");
        datosNuevos.setStyle("Metal");
        datosNuevos.setTags("metal,pesado");
        datosNuevos.setUrlm("https://nueva-url.jpg");
        datosNuevos.setDatem(LocalDate.of(2026, 4, 1));

        when(repository.findById(1L)).thenReturn(Optional.of(mediaEjemplo));
        when(repository.save(any(media.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        media resultado = servicesManager.update(1L, datosNuevos);

        // Assert
        assertNotNull(resultado);
        assertEquals("Alex Dark Pro", resultado.getArtist(), "El nombre debe actualizarse");
        assertEquals("Metal", resultado.getStyle(), "El estilo debe actualizarse");
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(media.class));
    }

    // ─── PRUEBA 7 ────────────────────────────────────────────────
    @Test
    @DisplayName("update() debe retornar null cuando el artista no existe")
    void testUpdate_CuandoNoExiste_DebeRetornarNull() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        media datosNuevos = new media();
        datosNuevos.setArtist("Fantasma");

        // Act
        media resultado = servicesManager.update(99L, datosNuevos);

        // Assert
        assertNull(resultado, "Debe retornar null si el artista no existe");
        verify(repository, never()).save(any());
    }
}
