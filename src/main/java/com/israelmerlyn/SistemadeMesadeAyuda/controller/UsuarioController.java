package com.israelmerlyn.SistemadeMesadeAyuda.controller;

import com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario.UsuarioRequest;
import com.israelmerlyn.SistemadeMesadeAyuda.dto.usuario.UsuarioResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.exception.ApiErrorResponse;
import com.israelmerlyn.SistemadeMesadeAyuda.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones CRUD de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios")
    public List<UsuarioResponse> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public UsuarioResponse findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public UsuarioResponse create(@Valid @RequestBody UsuarioRequest request) {
        return usuarioService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public UsuarioResponse update(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return usuarioService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar usuario")
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }
}