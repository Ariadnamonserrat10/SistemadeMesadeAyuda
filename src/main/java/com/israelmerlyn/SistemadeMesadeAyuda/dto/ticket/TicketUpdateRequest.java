package com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket;

import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.PrioridadTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Datos para actualizar un ticket")
public class TicketUpdateRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Schema(example = "No puedo acceder al VPN")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Schema(example = "El cliente VPN rechaza las credenciales")
    private String descripcion;

    @NotNull(message = "La prioridad es obligatoria")
    @Schema(example = "MEDIA")
    private PrioridadTicket prioridad;

    @NotNull(message = "El usuario asignado es obligatorio")
    @Schema(example = "4")
    private Long usuarioAsignadoId;
}