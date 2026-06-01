package com.israelmerlyn.SistemadeMesadeAyuda.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.dashboard.DashboardResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.EstadoTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.PrioridadTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void shouldBuildDashboardFromRepositoryCounts() {
        when(ticketRepository.count()).thenReturn(50L);
        when(ticketRepository.countByEstado(EstadoTicket.PENDIENTE)).thenReturn(20L);
        when(ticketRepository.countByEstado(EstadoTicket.EN_PROCESO)).thenReturn(15L);
        when(ticketRepository.countByEstado(EstadoTicket.RESUELTO)).thenReturn(15L);
        when(ticketRepository.countByPrioridad(PrioridadTicket.CRITICA)).thenReturn(12L);

        DashboardResponse response = dashboardService.getDashboard();

        assertThat(response.getTotalTickets()).isEqualTo(50L);
        assertThat(response.getPendientes()).isEqualTo(20L);
        assertThat(response.getEnProceso()).isEqualTo(15L);
        assertThat(response.getResueltos()).isEqualTo(15L);
        assertThat(response.getCriticos()).isEqualTo(12L);
    }
}