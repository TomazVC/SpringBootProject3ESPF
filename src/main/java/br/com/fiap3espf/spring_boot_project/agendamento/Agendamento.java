package br.com.fiap3espf.spring_boot_project.agendamento;

import br.com.fiap3espf.spring_boot_project.aluno.Aluno;
import br.com.fiap3espf.spring_boot_project.instrutor.Instrutor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "agendamentos")
@Entity(name = "Agendamento")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    Instrutor instrutor;

    LocalDateTime dataHora;

    Boolean cancelado = false;

    @Enumerated(EnumType.STRING)
    MotivoCancelamento motivoCancelamento;

    public Agendamento(Aluno aluno, Instrutor instrutor, LocalDateTime dataHora) {
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.dataHora = dataHora;
    }

    public void cancelar(MotivoCancelamento motivo) {
        this.cancelado = true;
        this.motivoCancelamento = motivo;
    }
}
