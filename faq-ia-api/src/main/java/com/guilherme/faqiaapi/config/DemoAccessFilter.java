package com.guilherme.faqiaapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Protege o endpoint /perguntas (o unico que gera custo real, porque
// chama a API da Anthropic) exigindo um header secreto que so voce
// conhece. Sem isso, qualquer pessoa que achasse a URL publica
// poderia gastar seus creditos fazendo perguntas.
@Component
public class DemoAccessFilter extends OncePerRequestFilter {

    @Value("${demo.access.key:}")
    private String chaveEsperada;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        boolean rotaProtegida = "/perguntas".equals(request.getRequestURI())
                && "POST".equalsIgnoreCase(request.getMethod());

        if (rotaProtegida) {
            String chaveRecebida = request.getHeader("X-Demo-Key");
            boolean protecaoAtiva = chaveEsperada != null && !chaveEsperada.isBlank();

            if (protecaoAtiva && !chaveEsperada.equals(chaveRecebida)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"erro\":\"Acesso restrito. Este endpoint requer uma chave valida.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
