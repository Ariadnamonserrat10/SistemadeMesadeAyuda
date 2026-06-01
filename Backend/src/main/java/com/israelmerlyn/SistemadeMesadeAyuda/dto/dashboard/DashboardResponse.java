package com.israelmerlyn.SistemadeMesadeAyuda.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Indicadores principales del dashboard")
public class DashboardResponse {

    private final long totalTickets;
    private final long pendientes;
    private final long enProceso;
    private final long resueltos;
    private final long criticos;
}