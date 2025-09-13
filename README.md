# API ReST Auto-Escola - Checkpoint 1

## Integrantes do Grupo
- Rony Ken Nagai - RM: 551549
- Tomáz Versolato Carballo - RM: 551417

## Descrição do Projeto

API ReST desenvolvida com Spring Boot para gerenciamento de agendamentos de instruções de uma auto-escola. O sistema permite o cadastro, listagem, atualização e exclusão de instrutores e alunos, além do agendamento e cancelamento de instruções com validações de regras de negócio.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.4
- Spring Data JPA
- Spring Validation
- MySQL
- Flyway (para migrações)
- Lombok
- Maven

## Funcionalidades Implementadas

### Instrutores
- ✅ Cadastro de instrutores (nome, email, telefone, CNH, especialidade, endereço)
- ✅ Listagem paginada de instrutores ativos (ordenada por nome)
- ✅ Atualização de dados (nome, telefone, endereço)
- ✅ Exclusão lógica (inativação)

### Alunos
- ✅ Cadastro de alunos (nome, email, telefone, CPF, endereço)
- ✅ Listagem paginada de alunos ativos (ordenada por nome)
- ✅ Atualização de dados (nome, telefone, endereço)
- ✅ Exclusão lógica (inativação)

### Agendamento de Instruções
- ✅ Agendamento de instruções com validações:
  - Horário de funcionamento (Segunda a Sábado, 06:00 às 21:00)
  - Antecedência mínima de 30 minutos
  - Máximo 2 instruções por dia por aluno
  - Verificação de disponibilidade do instrutor
  - Escolha automática de instrutor quando não especificado
- ✅ Cancelamento de instruções com antecedência mínima de 24 horas

## Endpoints da API

### Instrutores
- `POST /instrutor` - Cadastrar instrutor
- `GET /instrutor` - Listar instrutores (paginado)
- `PUT /instrutor` - Atualizar instrutor
- `DELETE /instrutor/{id}` - Excluir instrutor

### Alunos
- `POST /aluno` - Cadastrar aluno
- `GET /aluno` - Listar alunos (paginado)
- `PUT /aluno` - Atualizar aluno
- `DELETE /aluno/{id}` - Excluir aluno

### Agendamentos
- `POST /agendamento` - Agendar instrução
- `POST /agendamento/cancelar` - Cancelar instrução

## Estrutura do Banco de Dados

O projeto utiliza Flyway para controle de migrações. As tabelas criadas são:
- `instrutores` - Dados dos instrutores
- `alunos` - Dados dos alunos
- `agendamentos` - Agendamentos de instruções

## Configuração do Banco

Configure as seguintes propriedades no `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost/auto_escola
spring.datasource.username=root
spring.datasource.password=fiap
```

## Como Executar

1. Clone o repositório
2. Configure o banco de dados MySQL
3. Execute: `mvn spring-boot:run`
4. A API estará disponível em `http://localhost:8080`

## Validações e Regras de Negócio

- Todas as informações são obrigatórias exceto número e complemento do endereço
- Email e CNH dos instrutores não podem ser alterados
- Email e CPF dos alunos não podem ser alterados
- Instruções só podem ser agendadas no horário de funcionamento
- Alunos e instrutores inativos não podem ter instruções agendadas
- Validação de antecedência para agendamentos e cancelamentos
