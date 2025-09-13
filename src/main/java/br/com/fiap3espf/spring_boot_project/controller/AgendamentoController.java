package br.com.fiap3espf.spring_boot_project.controller;

import br.com.fiap3espf.spring_boot_project.agendamento.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> agendarInstrucao(@RequestBody @Valid DadosAgendamento dados) {
        try {
            Agendamento agendamento = agendamentoService.agendarInstrucao(dados);
            return ResponseEntity.ok("Instrução agendada com sucesso. ID: " + agendamento.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancelar")
    @Transactional
    public ResponseEntity<String> cancelarInstrucao(@RequestBody @Valid DadosCancelamento dados) {
        try {
            agendamentoService.cancelarInstrucao(dados);
            return ResponseEntity.ok("Instrução cancelada com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
