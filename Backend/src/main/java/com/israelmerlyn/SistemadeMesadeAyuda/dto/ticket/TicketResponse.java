package com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket;

import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.EstadoTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.PrioridadTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Representacion completa de un ticket")
public class TicketResponse {

    private final Long id;
    private final String titulo;
    private final String descripcion;
    private final PrioridadTicket prioridad;
    private final EstadoTicket estado;
    private final LocalDateTime fechaCreacion;
    private final Long usuarioAsignadoId;
}