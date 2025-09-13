package br.com.fiap3espf.spring_boot_project.agendamento;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamento(
        @NotNull
        Long agendamentoId,
        
        @NotNull
        MotivoCancelamento motivo) {
}
