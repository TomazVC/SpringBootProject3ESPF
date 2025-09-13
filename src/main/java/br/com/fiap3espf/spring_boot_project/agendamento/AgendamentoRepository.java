package br.com.fiap3espf.spring_boot_project.agendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    @Query("SELECT a FROM Agendamento a WHERE a.instrutor.id = :instrutorId AND a.dataHora = :dataHora AND a.cancelado = false")
    List<Agendamento> findByInstrutorIdAndDataHoraAndCanceladoFalse(Long instrutorId, LocalDateTime dataHora);
    
    @Query("SELECT a FROM Agendamento a WHERE a.aluno.id = :alunoId AND DATE(a.dataHora) = :data AND a.cancelado = false")
    List<Agendamento> findByAlunoIdAndDataAndCanceladoFalse(Long alunoId, LocalDate data);
    
    @Query("SELECT a FROM Agendamento a WHERE a.instrutor.ativo = true AND a.dataHora = :dataHora AND a.cancelado = false")
    List<Agendamento> findInstrutoresOcupadosNoHorario(LocalDateTime dataHora);
}
