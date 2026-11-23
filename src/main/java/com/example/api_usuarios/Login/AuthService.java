package com.example.api_usuarios.Login;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.api_usuarios.Model.Usuario;
import com.example.api_usuarios.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        // Se usa el repositorio de este microservicio (findByCorreo_elec con @Query JPQL)
        Usuario usuario = usuarioRepository.findByCorreo_elec(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        // Comparacion interna: el hash NUNCA sale del microservicio
        if (!passwordEncoder.matches(request.getPassword(), usuario.getContrasena())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        // El gateway extrae 'userId' como Integer.class, por eso se convierte el Long local
        Integer userId = Math.toIntExact(usuario.getId_usuario());

        // Mismos claims que generaba el API Gateway
        String token = jwtUtil.generateToken(
                usuario.getCorreo_elec(),
                Boolean.TRUE.equals(usuario.getPermiso_admin()),
                userId
        );

        return new AuthResponse(token);
    }
}
