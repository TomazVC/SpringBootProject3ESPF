package br.com.fiap3espf.spring_boot_project.agendamento;

import br.com.fiap3espf.spring_boot_project.aluno.Aluno;
import br.com.fiap3espf.spring_boot_project.aluno.AlunoRepository;
import br.com.fiap3espf.spring_boot_project.instrutor.Instrutor;
import br.com.fiap3espf.spring_boot_project.instrutor.InstrutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstrutorRepository instrutorRepository;

    public Agendamento agendarInstrucao(DadosAgendamento dados) {
        // Validar horário de funcionamento
        validarHorarioFuncionamento(dados.dataHora());

        // Validar antecedência mínima
        validarAntecedenciaMinima(dados.dataHora());

        // Buscar aluno
        Aluno aluno = alunoRepository.getReferenceById(dados.alunoId());
        if (!aluno.getAtivo()) {
            throw new RuntimeException("Não é possível agendar instrução com aluno inativo");
        }

        // Validar limite de 2 instruções por dia para o mesmo aluno
        validarLimiteInstrucoesPorDia(dados.alunoId(), dados.dataHora().toLocalDate());

        // Buscar ou escolher instrutor
        Instrutor instrutor;
        if (dados.instrutorId() != null) {
            instrutor = instrutorRepository.getReferenceById(dados.instrutorId());
            if (!instrutor.getAtivo()) {
                throw new RuntimeException("Não é possível agendar instrução com instrutor inativo");
            }
            // Validar se instrutor está disponível
            validarDisponibilidadeInstrutor(dados.instrutorId(), dados.dataHora());
        } else {
            instrutor = escolherInstrutorAleatorio(dados.dataHora());
        }

        return agendamentoRepository.save(new Agendamento(aluno, instrutor, dados.dataHora()));
    }

    public void cancelarInstrucao(DadosCancelamento dados) {
        Agendamento agendamento = agendamentoRepository.getReferenceById(dados.agendamentoId());
        
        // Validar antecedência mínima de 24 horas
        if (agendamento.getDataHora().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new RuntimeException("Instrução só pode ser cancelada com antecedência mínima de 24 horas");
        }

        agendamento.cancelar(dados.motivo());
        agendamentoRepository.save(agendamento);
    }

    private void validarHorarioFuncionamento(LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        LocalTime horario = dataHora.toLocalTime();

        if (diaSemana == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Auto-escola não funciona aos domingos");
        }

        if (horario.isBefore(LocalTime.of(6, 0)) || horario.isAfter(LocalTime.of(21, 0))) {
            throw new RuntimeException("Horário de funcionamento: Segunda a Sábado, das 06:00 às 21:00");
        }
    }

    private void validarAntecedenciaMinima(LocalDateTime dataHora) {
        if (dataHora.isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new RuntimeException("Instrução deve ser agendada com antecedência mínima de 30 minutos");
        }
    }

    private void validarLimiteInstrucoesPorDia(Long alunoId, java.time.LocalDate data) {
        List<Agendamento> instrucoesNoDia = agendamentoRepository.findByAlunoIdAndDataAndCanceladoFalse(alunoId, data);
        if (instrucoesNoDia.size() >= 2) {
            throw new RuntimeException("Aluno não pode ter mais de 2 instruções agendadas no mesmo dia");
        }
    }

    private void validarDisponibilidadeInstrutor(Long instrutorId, LocalDateTime dataHora) {
        List<Agendamento> instrucoesNoHorario = agendamentoRepository.findByInstrutorIdAndDataHoraAndCanceladoFalse(instrutorId, dataHora);
        if (!instrucoesNoHorario.isEmpty()) {
            throw new RuntimeException("Instrutor já possui instrução agendada neste horário");
        }
    }

    private Instrutor escolherInstrutorAleatorio(LocalDateTime dataHora) {
        // Buscar todos os instrutores ocupados no horário
        List<Agendamento> instrutoresOcupados = agendamentoRepository.findInstrutoresOcupadosNoHorario(dataHora);
        
        // Buscar todos os instrutores ativos
        List<Instrutor> todosInstrutores = instrutorRepository.findAllByAtivoTrue(null).getContent();
        
        // Filtrar instrutores disponíveis
        List<Instrutor> instrutoresDisponiveis = todosInstrutores.stream()
            .filter(instrutor -> instrutoresOcupados.stream()
                .noneMatch(agendamento -> agendamento.getInstrutor().getId().equals(instrutor.getId())))
            .toList();
        
        if (instrutoresDisponiveis.isEmpty()) {
            throw new RuntimeException("Nenhum instrutor disponível neste horário");
        }
        
        // Escolher aleatoriamente
        Random random = new Random();
        return instrutoresDisponiveis.get(random.nextInt(instrutoresDisponiveis.size()));
    }
}
