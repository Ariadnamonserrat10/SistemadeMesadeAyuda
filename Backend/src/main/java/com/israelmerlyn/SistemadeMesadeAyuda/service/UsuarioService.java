package com.israelmerlyn.SistemadeMesadeAyuda.service;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario.UsuarioRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario.UsuarioResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Usuario;
import com.israelmerlyn.SistemadeMesadeAyuda.exception.ResourceNotFoundException;
import com.israelmerlyn.SistemadeMesadeAyuda.exception.ValidationException;
import com.israelmerlyn.SistemadeMesadeAyuda.mapper.UsuarioMapper;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.TicketRepository;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TicketRepository ticketRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, TicketRepository ticketRepository,
            UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.ticketRepository = ticketRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAll() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(Long id) {
        return usuarioMapper.toResponse(findEntityById(id));
    }

    public UsuarioResponse create(UsuarioRequest request) {
        validateUniqueEmail(request.getCorreo(), null);
        Usuario usuario = usuarioMapper.toEntity(request);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponse update(Long id, UsuarioRequest request) {
        Usuario usuario = findEntityById(id);
        validateUniqueEmail(request.getCorreo(), id);
        usuarioMapper.updateEntity(usuario, request);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    public void delete(Long id) {
        Usuario usuario = findEntityById(id);
        if (ticketRepository.countByUsuarioAsignadoId(id) > 0) {
            throw new IllegalStateException("No se puede eliminar un usuario con tickets asignados");
        }
        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario findEntityById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
    }

    private void validateUniqueEmail(String correo, Long currentId) {
        usuarioRepository.findByCorreoIgnoreCase(correo)
                .filter(existing -> !existing.getId().equals(currentId))
                .ifPresent(existing -> {
                    throw new ValidationException("Ya existe un usuario con el correo " + correo);
                });
    }
}