package com.israelmerlyn.SistemadeMesadeAyuda.dto.solucion;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Representacion de una solucion")
public class SolucionResponse {

    private final Long id;
    private final String descripcionSolucion;
    private final LocalDateTime fechaSolucion;
    private final Integer tiempoInvertidoHoras;
    private final Long ticketId;
}