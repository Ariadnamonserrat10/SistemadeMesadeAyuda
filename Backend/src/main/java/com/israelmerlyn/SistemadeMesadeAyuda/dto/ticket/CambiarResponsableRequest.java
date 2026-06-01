package com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Datos para mover un ticket a en proceso y cambiar el responsable")
public class CambiarResponsableRequest {

    @NotNull(message = "El nuevo responsable es obligatorio")
    @Schema(example = "3")
    private Long nuevoResponsableId;
}