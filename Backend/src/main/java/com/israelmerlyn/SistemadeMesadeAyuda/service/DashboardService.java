package com.israelmerlyn.SistemadeMesadeAyuda.service;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.dashboard.DashboardResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.EstadoTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.PrioridadTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final TicketRepository ticketRepository;

    public DashboardService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public DashboardResponse getDashboard() {
        return DashboardResponse.builder()
                .totalTickets(ticketRepository.count())
                .pendientes(ticketRepository.countByEstado(EstadoTicket.PENDIENTE))
                .enProceso(ticketRepository.countByEstado(EstadoTicket.EN_PROCESO))
                .resueltos(ticketRepository.countByEstado(EstadoTicket.RESUELTO))
                .criticos(ticketRepository.countByPrioridad(PrioridadTicket.CRITICA))
                .build();
    }
}