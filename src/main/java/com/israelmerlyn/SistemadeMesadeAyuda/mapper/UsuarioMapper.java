package com.israelmerlyn.SistemadeMesadeAyuda.mapper;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario.UsuarioRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario.UsuarioResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequest request) {
        return Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .departamento(request.getDepartamento())
                .activo(request.getActivo() == null || request.getActivo())
                .build();
    }

    public void updateEntity(Usuario target, UsuarioRequest request) {
        target.setNombre(request.getNombre());
        target.setCorreo(request.getCorreo());
        target.setDepartamento(request.getDepartamento());
        target.setActivo(request.getActivo() == null || request.getActivo());
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .departamento(usuario.getDepartamento())
                .activo(usuario.isActivo())
                .build();
    }
}