package com.israelmerlyn.SistemadeMesadeAyuda.mapper;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketDetalladoResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketListadoResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponse toResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .titulo(ticket.getTitulo())
                .descripcion(ticket.getDescripcion())
                .prioridad(ticket.getPrioridad())
                .estado(ticket.getEstado())
                .fechaCreacion(ticket.getFechaCreacion())
                .usuarioAsignadoId(ticket.getUsuarioAsignado().getId())
                .build();
    }

    public TicketListadoResponse toListado(Ticket ticket) {
        return TicketListadoResponse.builder()
                .id(ticket.getId())
                .titulo(ticket.getTitulo())
                .usuarioAsignadoId(ticket.getUsuarioAsignado().getId())
                .build();
    }

    public TicketDetalladoResponse toDetallado(Ticket ticket) {
        return TicketDetalladoResponse.builder()
                .id(ticket.getId())
                .titulo(ticket.getTitulo())
                .estado(ticket.getEstado())
                .usuarioAsignado(TicketDetalladoResponse.UsuarioAsignadoResumen.builder()
                        .id(ticket.getUsuarioAsignado().getId())
                        .nombre(ticket.getUsuarioAsignado().getNombre())
                        .build())
                .build();
    }
}