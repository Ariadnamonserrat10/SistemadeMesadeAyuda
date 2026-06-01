package com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket;

import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.EstadoTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Vista detallada de tickets")
public class TicketDetalladoResponse {

    private final Long id;
    private final String titulo;
    private final EstadoTicket estado;
    private final UsuarioAsignadoResumen usuarioAsignado;

    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "Resumen del usuario asignado")
    public static class UsuarioAsignadoResumen {

        private final Long id;
        private final String nombre;
    }
}