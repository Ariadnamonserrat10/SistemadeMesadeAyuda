package com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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
@Schema(description = "Datos para resolver un ticket")
public class ResolverTicketRequest {

    @NotBlank(message = "La descripcion de la solucion es obligatoria")
    @Schema(example = "Se reinstalo el controlador")
    private String descripcionSolucion;

    @NotNull(message = "El tiempo invertido es obligatorio")
    @Min(value = 1, message = "El tiempo invertido debe ser mayor a cero")
    @Schema(example = "2")
    private Integer tiempoInvertidoHoras;
}