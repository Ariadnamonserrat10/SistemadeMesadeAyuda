package com.israelmerlyn.SistemadeMesadeAyuda.config;

import com.israelmerlyn.SistemadeMesadeAyuda.entity.Solucion;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Ticket;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.Usuario;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.EstadoTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.entity.enums.PrioridadTicket;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.SolucionRepository;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.TicketRepository;
import com.israelmerlyn.SistemadeMesadeAyuda.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private static final String[] DEPARTAMENTOS = {
            "Infraestructura", "Soporte", "Desarrollo", "Recursos Humanos", "Finanzas"
    };

    private static final String[] TITULOS = {
            "No puedo iniciar sesion", "Impresora sin conexion", "Error en correo", "VPN no conecta",
            "Actualizacion pendiente", "Lentitud en sistema", "No abre Excel", "Fallo en red",
            "Credenciales bloqueadas", "Se reinicia equipo"
    };

    @Bean
    CommandLineRunner loadInitialData(UsuarioRepository usuarioRepository, TicketRepository ticketRepository,
            SolucionRepository solucionRepository) {
        return args -> {
            if (usuarioRepository.count() > 0 || ticketRepository.count() > 0 || solucionRepository.count() > 0) {
                return;
            }

            List<Usuario> usuarios = crearUsuarios(usuarioRepository);
            crearTickets(ticketRepository, solucionRepository, usuarios);
        };
    }

    private List<Usuario> crearUsuarios(UsuarioRepository usuarioRepository) {
        List<Usuario> usuarios = new ArrayList<>();
        for (int index = 1; index <= 10; index++) {
            usuarios.add(Usuario.builder()
                    .nombre("Usuario " + index)
                    .correo("usuario" + index + "@helpdesk.local")
                    .departamento(DEPARTAMENTOS[(index - 1) % DEPARTAMENTOS.length])
                    .activo(index != 10)
                    .build());
        }
        return usuarioRepository.saveAll(usuarios);
    }

    private void crearTickets(TicketRepository ticketRepository, SolucionRepository solucionRepository,
            List<Usuario> usuarios) {
        List<Ticket> tickets = new ArrayList<>();
        for (int index = 1; index <= 50; index++) {
            EstadoTicket estado = index <= 15 ? EstadoTicket.RESUELTO
                    : index <= 30 ? EstadoTicket.EN_PROCESO : EstadoTicket.PENDIENTE;
            Usuario usuario = usuarios.get(index % 9);
            Ticket ticket = Ticket.builder()
                    .titulo(TITULOS[(index - 1) % TITULOS.length] + " #" + index)
                    .descripcion("Detalle del incidente " + index + " generado automaticamente para ambiente inicial")
                    .prioridad(PrioridadTicket.values()[index % PrioridadTicket.values().length])
                    .estado(estado)
                    .fechaCreacion(LocalDateTime.now().minusDays(ThreadLocalRandom.current().nextInt(1, 30)))
                    .usuarioAsignado(usuario)
                    .build();
            tickets.add(ticket);
        }

        List<Ticket> persistedTickets = ticketRepository.saveAll(tickets);
        List<Solucion> soluciones = new ArrayList<>();
        for (Ticket ticket : persistedTickets) {
            if (ticket.getEstado() == EstadoTicket.RESUELTO) {
                soluciones.add(Solucion.builder()
                        .descripcionSolucion("Solucion automatica aplicada al ticket " + ticket.getId())
                        .fechaSolucion(ticket.getFechaCreacion().plusHours(4))
                        .tiempoInvertidoHoras((int) (ticket.getId() % 4) + 1)
                        .ticket(ticket)
                        .build());
            }
        }
        if (!soluciones.isEmpty()) {
            solucionRepository.saveAll(soluciones);
        }
    }
}