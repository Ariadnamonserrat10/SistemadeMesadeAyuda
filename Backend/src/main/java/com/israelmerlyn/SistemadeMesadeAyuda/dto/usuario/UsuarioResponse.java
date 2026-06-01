package com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Representacion de un usuario")
public class UsuarioResponse {

    @Schema(example = "1")
    private final Long id;

    @Schema(example = "Juan Perez")
    private final String nombre;

    @Schema(example = "juan.perez@empresa.com")
    private final String correo;

    @Schema(example = "Soporte")
    private final String departamento;

    @Schema(example = "true")
    private final boolean activo;
}