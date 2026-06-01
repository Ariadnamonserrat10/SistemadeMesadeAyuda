package com.israelmerlyn.SistemadeMesadeAyuda.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario.UsuarioRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario.UsuarioResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Usuario;
import com.israelmerlyn.SistemadeMesadeAyuda.exception.ResourceNotFoundException;
import com.israelmerlyn.SistemadeMesadeAyuda.exception.ValidationException;
import com.israelmerlyn.SistemadeMesadeAyuda.mapper.UsuarioMapper;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.TicketRepository;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequest request;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        request = UsuarioRequest.builder()
                .nombre("Ana")
                .correo("ana@helpdesk.local")
                .departamento("Soporte")
                .activo(true)
                .build();

        usuario = Usuario.builder()
                .id(1L)
                .nombre("Ana")
                .correo("ana@helpdesk.local")
                .departamento("Soporte")
                .activo(true)
                .build();
    }

    @Test
    void shouldCreateUsuarioWhenEmailIsUnique() {
        when(usuarioRepository.findByCorreoIgnoreCase(request.getCorreo())).thenReturn(Optional.empty());
        when(usuarioMapper.toEntity(request)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario)).thenReturn(
                UsuarioResponse.builder().id(1L).nombre("Ana").correo("ana@helpdesk.local").departamento("Soporte")
                        .activo(true).build());

        UsuarioResponse response = usuarioService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void shouldReturnAllUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioMapper.toResponse(usuario)).thenReturn(
                UsuarioResponse.builder().id(1L).nombre("Ana").correo("ana@helpdesk.local").departamento("Soporte")
                        .activo(true).build());

        List<UsuarioResponse> response = usuarioService.findAll();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getCorreo()).isEqualTo("ana@helpdesk.local");
    }

    @Test
    void shouldReturnUsuarioById() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponse(usuario)).thenReturn(
                UsuarioResponse.builder().id(1L).nombre("Ana").correo("ana@helpdesk.local").departamento("Soporte")
                        .activo(true).build());

        UsuarioResponse response = usuarioService.findById(1L);

        assertThat(response.getNombre()).isEqualTo("Ana");
    }

    @Test
    void shouldUpdateUsuarioWhenEmailBelongsToSameUser() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByCorreoIgnoreCase(request.getCorreo())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toResponse(usuario)).thenReturn(
                UsuarioResponse.builder().id(1L).nombre("Ana").correo("ana@helpdesk.local").departamento("Soporte")
                        .activo(true).build());

        UsuarioResponse response = usuarioService.update(1L, request);

        assertThat(response.getId()).isEqualTo(1L);
        verify(usuarioMapper).updateEntity(usuario, request);
    }

    @Test
    void shouldDeleteUsuarioWithoutAssignedTickets() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(ticketRepository.countByUsuarioAsignadoId(1L)).thenReturn(0L);

        usuarioService.delete(1L);

        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void shouldRejectDuplicateEmail() {
        when(usuarioRepository.findByCorreoIgnoreCase(request.getCorreo())).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.create(request))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Ya existe un usuario con el correo");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void shouldNotDeleteUsuarioWithAssignedTickets() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(ticketRepository.countByUsuarioAsignadoId(1L)).thenReturn(3L);

        assertThatThrownBy(() -> usuarioService.delete(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede eliminar un usuario");
    }

    @Test
    void shouldFailWhenUsuarioDoesNotExist() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}