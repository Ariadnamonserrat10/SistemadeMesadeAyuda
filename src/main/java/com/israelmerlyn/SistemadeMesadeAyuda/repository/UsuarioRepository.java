package com.israelmerlyn.SistemadeMesadeAyuda.repository;

import com.israelmerlyn.SistemadeMesadeAyuda.entity.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreoIgnoreCase(String correo);

    Optional<Usuario> findByCorreoIgnoreCase(String correo);
}