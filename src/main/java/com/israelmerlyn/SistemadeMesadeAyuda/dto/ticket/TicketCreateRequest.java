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
@Schema(description = "Datos para crear un ticket")
public class TicketCreateRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Schema(example = "No puedo acceder al sistema")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    @Schema(example = "Al intentar iniciar sesion aparece un error de autenticacion")
    private String descripcion;

    @NotNull(message = "La prioridad es obligatoria")
    @Schema(example = "ALTA")
    private PrioridadTicket prioridad;

    @NotNull(message = "El usuario asignado es obligatorio")
    @Schema(example = "1")
    private Long usuarioAsignadoId;
}