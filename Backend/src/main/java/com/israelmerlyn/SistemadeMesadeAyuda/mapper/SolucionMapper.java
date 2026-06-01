package com.israelmerlyn.SistemadeMesadeAyuda.mapper;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.solucion.SolucionResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Solucion;
import org.springframework.stereotype.Component;

@Component
public class SolucionMapper {

    public SolucionResponse toResponse(Solucion solucion) {
        return SolucionResponse.builder()
                .id(solucion.getId())
                .descripcionSolucion(solucion.getDescripcionSolucion())
                .fechaSolucion(solucion.getFechaSolucion())
                .tiempoInvertidoHoras(solucion.getTiempoInvertidoHoras())
                .ticketId(solucion.getTicket().getId())
                .build();
    }
}