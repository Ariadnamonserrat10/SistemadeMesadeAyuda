package com.israelmerlyn.SistemadeMesadeAyuda.repository;

import com.israelmerlyn.SistemadeMesadeAyuda.entity.Ticket;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.EstadoTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.PrioridadTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    long countByEstado(EstadoTicket estado);

    long countByPrioridad(PrioridadTicket prioridad);

    long countByUsuarioAsignadoId(Long usuarioAsignadoId);
}