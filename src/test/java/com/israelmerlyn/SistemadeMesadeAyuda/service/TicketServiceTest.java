package com.israelmerlyn.SistemadeMesadeAyuda.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.PrioridadTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.exception.ResourceNotFoundException;
import com.israelmerlyn.SistemadeMesadeAyuda.mapper.SolucionMapper;
import com.israelmerlyn.SistemadeMesadeAyuda.mapper.TicketMapper;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.TicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private SolucionMapper solucionMapper;

    @InjectMocks
    private TicketService ticketService;

    private Usuario usuarioActivo;
    private Usuario usuarioActivo2;
    private Usuario usuarioInactivo;
    private Ticket ticketPendiente;
    private Ticket ticketEnProceso;

    @BeforeEach
    void setUp() {
        usuarioActivo = Usuario.builder().id(1L).nombre("Ana").correo("ana@local").activo(true).build();
        usuarioActivo2 = Usuario.builder().id(3L).nombre("Luis").correo("luis@local").activo(true).build();
        usuarioInactivo = Usuario.builder().id(2L).nombre("Mario").correo("mario@local").activo(false).build();

        ticketPendiente = Ticket.builder()
                .id(10L)
                .titulo("Fallo")
                .descripcion("Desc")
                .prioridad(PrioridadTicket.ALTA)
                .estado(EstadoTicket.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .usuarioAsignado(usuarioActivo)
                .build();

        ticketEnProceso = Ticket.builder()
                .id(11L)
                .titulo("VPN")
                .descripcion("No conecta")
                .prioridad(PrioridadTicket.MEDIA)
                .estado(EstadoTicket.EN_PROCESO)
                .fechaCreacion(LocalDateTime.now())
                .usuarioAsignado(usuarioActivo)
                .build();
    }

    @Test
    void shouldCreateTicketWhenAssignedUserIsActive() {
        TicketCreateRequest request = TicketCreateRequest.builder()
                .titulo("Nuevo ticket")
                .descripcion("Incidente critico")
                .prioridad(PrioridadTicket.CRITICA)
                .usuarioAsignadoId(1L)
                .build();
        when(usuarioService.findEntityById(1L)).thenReturn(usuarioActivo);
        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        when(ticketRepository.save(ticketCaptor.capture())).thenAnswer(invocation -> {
            Ticket saved = invocation.getArgument(0);
            saved.setId(99L);
            return saved;
        });
        when(ticketMapper.toResponse(org.mockito.ArgumentMatchers.any(Ticket.class))).thenReturn(
                TicketResponse.builder().id(99L).titulo("Nuevo ticket").descripcion("Incidente critico")
                        .prioridad(PrioridadTicket.CRITICA).estado(EstadoTicket.PENDIENTE)
                        .fechaCreacion(LocalDateTime.now()).usuarioAsignadoId(1L).build());

        TicketResponse response = ticketService.create(request);

        assertThat(response.getId()).isEqualTo(99L);
        assertThat(ticketCaptor.getValue().getEstado()).isEqualTo(EstadoTicket.PENDIENTE);
        assertThat(ticketCaptor.getValue().getUsuarioAsignado()).isEqualTo(usuarioActivo);
    }

    @Test
    void shouldReturnAllTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticketPendiente));
        when(ticketMapper.toListado(ticketPendiente)).thenReturn(
                TicketListadoResponse.builder().id(10L).titulo("Fallo").usuarioAsignadoId(1L).build());

        List<TicketListadoResponse> response = ticketService.findAll();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getUsuarioAsignadoId()).isEqualTo(1L);
    }

    @Test
    void shouldReturnAllDetailedTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticketPendiente));
        when(ticketMapper.toDetallado(ticketPendiente)).thenReturn(
                TicketDetalladoResponse.builder()
                        .id(10L)
                        .titulo("Fallo")
                        .estado(EstadoTicket.PENDIENTE)
                        .usuarioAsignado(
                                TicketDetalladoResponse.UsuarioAsignadoResumen.builder().id(1L).nombre("Ana").build())
                        .build());

        List<TicketDetalladoResponse> response = ticketService.findAllDetailed();

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().getUsuarioAsignado().getNombre()).isEqualTo("Ana");
    }

    @Test
    void shouldReturnTicketById() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticketPendiente));
        when(ticketMapper.toResponse(ticketPendiente)).thenReturn(
                TicketResponse.builder().id(10L).titulo("Fallo").descripcion("Desc").prioridad(PrioridadTicket.ALTA)
                        .estado(EstadoTicket.PENDIENTE).fechaCreacion(ticketPendiente.getFechaCreacion())
                        .usuarioAsignadoId(1L).build());

        TicketResponse response = ticketService.findById(10L);

        assertThat(response.getId()).isEqualTo(10L);
    }

    @Test
    void shouldUpdateTicket() {
        TicketUpdateRequest request = TicketUpdateRequest.builder()
                .titulo("Actualizado")
                .descripcion("Nuevo detalle")
                .prioridad(PrioridadTicket.CRITICA)
                .usuarioAsignadoId(3L)
                .build();
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticketPendiente));
        when(usuarioService.findEntityById(3L)).thenReturn(usuarioActivo2);
        when(ticketRepository.save(ticketPendiente)).thenReturn(ticketPendiente);
        when(ticketMapper.toResponse(ticketPendiente)).thenReturn(
                TicketResponse.builder().id(10L).titulo("Actualizado").descripcion("Nuevo detalle")
                        .prioridad(PrioridadTicket.CRITICA).estado(EstadoTicket.PENDIENTE)
                        .fechaCreacion(ticketPendiente.getFechaCreacion()).usuarioAsignadoId(3L).build());

        TicketResponse response = ticketService.update(10L, request);

        assertThat(response.getTitulo()).isEqualTo("Actualizado");
        assertThat(ticketPendiente.getUsuarioAsignado()).isEqualTo(usuarioActivo2);
    }

    @Test
    void shouldDeleteTicket() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticketPendiente));

        ticketService.delete(10L);

        verify(ticketRepository).delete(ticketPendiente);
    }

    @Test
    void shouldRejectTicketCreationWhenUserIsInactive() {
        TicketCreateRequest request = TicketCreateRequest.builder()
                .titulo("Nuevo ticket")
                .descripcion("Incidente")
                .prioridad(PrioridadTicket.ALTA)
                .usuarioAsignadoId(2L)
                .build();
        when(usuarioService.findEntityById(2L)).thenReturn(usuarioInactivo);

        assertThatThrownBy(() -> ticketService.create(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("debe estar activo");
    }

    @Test
    void shouldReassignTicketAndSetInProgress() {
        CambiarResponsableRequest request = CambiarResponsableRequest.builder().nuevoResponsableId(3L).build();
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticketPendiente));
        when(usuarioService.findEntityById(3L)).thenReturn(usuarioActivo2);
        when(ticketRepository.save(ticketPendiente)).thenReturn(ticketPendiente);
        when(ticketMapper.toResponse(ticketPendiente)).thenReturn(
                TicketResponse.builder().id(10L).titulo("Fallo").descripcion("Desc").prioridad(PrioridadTicket.ALTA)
                        .estado(EstadoTicket.EN_PROCESO).fechaCreacion(ticketPendiente.getFechaCreacion())
                        .usuarioAsignadoId(3L).build());

        TicketResponse response = ticketService.moveToInProgress(10L, request);

        assertThat(response.getEstado()).isEqualTo(EstadoTicket.EN_PROCESO);
        assertThat(ticketPendiente.getUsuarioAsignado()).isEqualTo(usuarioActivo2);
    }

    @Test
    void shouldResolveTicketWhenItIsInProgress() {
        ResolverTicketRequest request = ResolverTicketRequest.builder()
                .descripcionSolucion("Se reinstalo el controlador")
                .tiempoInvertidoHoras(2)
                .build();
        when(ticketRepository.findById(11L)).thenReturn(Optional.of(ticketEnProceso));
        when(ticketRepository.saveAndFlush(ticketEnProceso)).thenAnswer(invocation -> {
            Ticket persisted = invocation.getArgument(0);
            persisted.getSolucion().setId(100L);
            return persisted;
        });
        when(solucionMapper.toResponse(org.mockito.ArgumentMatchers.any(Solucion.class))).thenReturn(
                SolucionResponse.builder().id(100L).descripcionSolucion("Se reinstalo el controlador")
                        .fechaSolucion(LocalDateTime.now()).tiempoInvertidoHoras(2).ticketId(11L).build());

        SolucionResponse response = ticketService.resolve(11L, request);

        assertThat(response.getId()).isEqualTo(100L);
        assertThat(ticketEnProceso.getEstado()).isEqualTo(EstadoTicket.RESUELTO);
        verify(ticketRepository).saveAndFlush(ticketEnProceso);
    }

    @Test
    void shouldRejectResolveWhenTicketIsNotInProgress() {
        ResolverTicketRequest request = ResolverTicketRequest.builder()
                .descripcionSolucion("Solucion")
                .tiempoInvertidoHoras(1)
                .build();
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticketPendiente));

        assertThatThrownBy(() -> ticketService.resolve(10L, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("EN_PROCESO");
    }

    @Test
    void shouldFailWhenTicketDoesNotExist() {
        when(ticketRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ticketService.findById(404L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Ticket no encontrado");
    }
}