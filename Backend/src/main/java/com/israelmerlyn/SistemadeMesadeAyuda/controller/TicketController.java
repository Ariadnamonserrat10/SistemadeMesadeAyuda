package com.israelmerlyn.SistemadeMesadeAyuda.controller;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.solucion.SolucionResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.CambiarResponsableRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.ResolverTicketRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketCreateRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketDetalladoResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketListadoResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.ticket.TicketUpdateRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.exception.ApiErrorResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Operaciones de ciclo de vida de tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    @Operation(summary = "Listar tickets resumidos")
    public List<TicketListadoResponse> findAll() {
        return ticketService.findAll();
    }

    @GetMapping("/detallado")
    @Operation(summary = "Listar tickets detallados")
    public List<TicketDetalladoResponse> findAllDetailed() {
        return ticketService.findAllDetailed();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ticket por id")
    public TicketResponse findById(@PathVariable Long id) {
        return ticketService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear ticket")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ticket creado"),
            @ApiResponse(responseCode = "400", description = "Usuario inactivo o datos invalidos", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public TicketResponse create(@Valid @RequestBody TicketCreateRequest request) {
        return ticketService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ticket")
    public TicketResponse update(@PathVariable Long id, @Valid @RequestBody TicketUpdateRequest request) {
        return ticketService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar ticket")
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }

    @PutMapping("/{id}/en-proceso")
    @Operation(summary = "Cambiar ticket a EN_PROCESO")
    public TicketResponse moveToInProgress(@PathVariable Long id,
            @Valid @RequestBody CambiarResponsableRequest request) {
        return ticketService.moveToInProgress(id, request);
    }

    @PutMapping("/{id}/resolver")
    @Operation(summary = "Resolver ticket")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket resuelto"),
            @ApiResponse(responseCode = "400", description = "El ticket no esta en proceso o datos invalidos", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public SolucionResponse resolve(@PathVariable Long id, @Valid @RequestBody ResolverTicketRequest request) {
        return ticketService.resolve(id, request);
    }
}