package com.israelmerlyn.SistemadeMesadeAyuda.service;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.solucion.SolucionResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.CambiarResponsableRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.ResolverTicketRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketCreateRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketDetalladoResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketListadoResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketUpdateRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Solucion;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Ticket;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Usuario;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.EstadoTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.exception.ResourceNotFoundException;
import com.israelmerlyn.SistemadeMesadeAyuda.mapper.SolucionMapper;
import com.israelmerlyn.SistemadeMesadeAyuda.mapper.TicketMapper;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioService usuarioService;
    private final TicketMapper ticketMapper;
    private final SolucionMapper solucionMapper;

    public TicketService(TicketRepository ticketRepository, UsuarioService usuarioService,
            TicketMapper ticketMapper, SolucionMapper solucionMapper) {
        this.ticketRepository = ticketRepository;
        this.usuarioService = usuarioService;
        this.ticketMapper = ticketMapper;
        this.solucionMapper = solucionMapper;
    }

    @Transactional(readOnly = true)
    public List<TicketListadoResponse> findAll() {
        return ticketRepository.findAll().stream().map(ticketMapper::toListado).toList();
    }

    @Transactional(readOnly = true)
    public List<TicketDetalladoResponse> findAllDetailed() {
        return ticketRepository.findAll().stream().map(ticketMapper::toDetallado).toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse findById(Long id) {
        return ticketMapper.toResponse(findEntityById(id));
    }

    public TicketResponse create(TicketCreateRequest request) {
        Usuario usuario = getActiveUsuario(request.getUsuarioAsignadoId());
        Ticket ticket = Ticket.builder()
                .titulo(request.getTitulo())
                .descripcion(request.getDescripcion())
                .prioridad(request.getPrioridad())
                .estado(EstadoTicket.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .usuarioAsignado(usuario)
                .build();
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    public TicketResponse update(Long id, TicketUpdateRequest request) {
        Ticket ticket = findEntityById(id);
        Usuario usuario = getActiveUsuario(request.getUsuarioAsignadoId());
        ticket.setTitulo(request.getTitulo());
        ticket.setDescripcion(request.getDescripcion());
        ticket.setPrioridad(request.getPrioridad());
        ticket.setUsuarioAsignado(usuario);
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    public void delete(Long id) {
        ticketRepository.delete(findEntityById(id));
    }

    public TicketResponse moveToInProgress(Long id, CambiarResponsableRequest request) {
        Ticket ticket = findEntityById(id);
        if (ticket.getEstado() == EstadoTicket.RESUELTO) {
            throw new IllegalStateException("No se puede reasignar un ticket resuelto");
        }
        Usuario usuario = getActiveUsuario(request.getNuevoResponsableId());
        ticket.setUsuarioAsignado(usuario);
        ticket.setEstado(EstadoTicket.EN_PROCESO);
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    public SolucionResponse resolve(Long id, ResolverTicketRequest request) {
        Ticket ticket = findEntityById(id);
        if (ticket.getEstado() != EstadoTicket.EN_PROCESO) {
            throw new IllegalStateException("Solo se pueden resolver tickets en estado EN_PROCESO");
        }
        Solucion solucion = Solucion.builder()
                .descripcionSolucion(request.getDescripcionSolucion())
                .fechaSolucion(LocalDateTime.now())
                .tiempoInvertidoHoras(request.getTiempoInvertidoHoras())
                .ticket(ticket)
                .build();
        ticket.setEstado(EstadoTicket.RESUELTO);
        ticket.setSolucion(solucion);
        Ticket persistedTicket = ticketRepository.saveAndFlush(ticket);
        return solucionMapper.toResponse(persistedTicket.getSolucion());
    }

    @Transactional(readOnly = true)
    public Ticket findEntityById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con id " + id));
    }

    private Usuario getActiveUsuario(Long usuarioId) {
        Usuario usuario = usuarioService.findEntityById(usuarioId);
        if (!usuario.isActivo()) {
            throw new IllegalStateException("El usuario asignado debe estar activo");
        }
        return usuario;
    }
}