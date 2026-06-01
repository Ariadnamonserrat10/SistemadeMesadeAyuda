package com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear o actualizar un usuario")
public class UsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(example = "Juan Perez")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido")
    @Schema(example = "juan.perez@empresa.com")
    private String correo;

    @Schema(example = "Soporte")
    private String departamento;

    @Schema(example = "true")
    private Boolean activo;
}