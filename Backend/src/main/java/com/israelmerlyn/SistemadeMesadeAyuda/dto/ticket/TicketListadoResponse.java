package com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Vista resumida de tickets")
public class TicketListadoResponse {

    @Schema(example = "1")
    private final Long id;

    @Schema(example = "No puedo acceder al sistema")
    private final String titulo;

    @Schema(example = "2")
    private final Long usuarioAsignadoId;
}