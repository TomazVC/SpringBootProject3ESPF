create table agendamentos(
    id bigint not null auto_increment,
    aluno_id bigint not null,
    instrutor_id bigint not null,
    data_hora datetime not null,
    cancelado tinyint not null default 0,
    motivo_cancelamento varchar(50),

    primary key(id),
    constraint fk_agendamentos_aluno_id foreign key(aluno_id) references alunos(id),
    constraint fk_agendamentos_instrutor_id foreign key(instrutor_id) references instrutores(id)
);
