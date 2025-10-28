# 📚 Biblioteca Digital API

API REST para gerenciamento de biblioteca digital, desenvolvida como parte do **Santander Bootcamp 2023 - Fullstack Java+Angular**.

---

## 🎯 Sobre o Projeto

Sistema completo de gerenciamento de biblioteca com funcionalidades de cadastro de livros, usuários e controle de empréstimos.

**Tecnologias:**
- Java 17
- Spring Boot 3.5.7
- Spring Data JPA
- H2 Database (dev)
- PostgreSQL (prod)
- Maven
- OpenAPI/Swagger
- Lombok

---

## 🏗️ Arquitetura

### Entidades

**Book (Livro)**
- ID, título, autor, ISBN, ano de publicação
- Categoria, quantidade total e disponível
- Descrição

**User (Usuário)**
- ID, nome, email, CPF
- Telefone, status ativo/inativo

**Loan (Empréstimo)**
- ID, data de empréstimo, data prevista de devolução
- Data real de devolução, status
- Relacionamento com Book e User

### Status de Empréstimo
- `ACTIVE` - Empréstimo ativo
- `RETURNED` - Livro devolvido
- `LATE` - Empréstimo atrasado
- `CANCELLED` - Empréstimo cancelado

## 🚀 Como Executar

### Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- WSL/Linux ou Windows com PowerShell

### Executar Localmente

**Linux/WSL:**
Clonar o repositório
git clone https://github.com/AlineMirian/biblioteca-api.git
cd biblioteca-api

Dar permissão ao Maven Wrapper
chmod +x mvnw

Executar aplicação
./mvnw spring-boot:run


A aplicação estará disponível em: `http://localhost:8080`

---

## 🧪 Exemplos de Uso

### Criar um Livro

**Request:**
POST http://localhost:8080/api/books
Content-Type: application/json

{
"title": "Clean Code",
"author": "Robert C. Martin",
"isbn": "9780132350884",
"publicationYear": 2008,
"category": "Tecnologia",
"quantity": 5,
"availableQuantity": 5,
"description": "Manual de desenvolvimento ágil de software"
}


**Response:** `201 Created`

---

### Criar um Usuário

**Request:**
POST http://localhost:8080/api/users
Content-Type: application/json

{
"name": "João Silva",
"email": "joao@email.com",
"cpf": "12345678901",
"phone": "48999999999",
"active": true
}


**Response:** `201 Created`

---

### Criar um Empréstimo

**Request:**
POST http://localhost:8080/api/loans?bookId=1&userId=1&loanDays=14


**Response:** `201 Created`

O sistema automaticamente:
- ✅ Verifica se o livro está disponível
- ✅ Verifica se o usuário está ativo
- ✅ Diminui a quantidade disponível do livro
- ✅ Calcula a data de devolução

---

### Devolver um Livro

**Request:**
PUT http://localhost:8080/api/loans/1/return


**Response:** `200 OK`

O sistema automaticamente:
- ✅ Marca o empréstimo como devolvido
- ✅ Registra a data de devolução
- ✅ Aumenta a quantidade disponível do livro

---

## 🗄️ Banco de Dados

### H2 Console (Desenvolvimento)

Acesse: `http://localhost:8080/h2-console`

**Configurações:**
- JDBC URL: `jdbc:h2:mem:biblioteca`
- Username: `sa`
- Password: (deixe em branco)

### Tabelas Criadas

- `TB_BOOKS` - Livros
- `TB_USERS` - Usuários
- `TB_LOANS` - Empréstimos

---

## 📊 Documentação OpenAPI

**Swagger UI:** `http://localhost:8080/swagger-ui.html` 

**API Docs:** `http://localhost:8080/api-docs`

---

## 🎯 Funcionalidades Implementadas

### Livros
- ✅ CRUD completo
- ✅ Busca por ISBN, categoria, título, autor
- ✅ Listagem de livros disponíveis
- ✅ Controle de quantidade total e disponível

### Usuários
- ✅ CRUD completo
- ✅ Validação de email e CPF únicos
- ✅ Status ativo/inativo
- ✅ Busca por email

### Empréstimos
- ✅ Criação com validações de negócio
- ✅ Devolução com atualização automática de estoque
- ✅ Renovação de empréstimo
- ✅ Listagem por usuário, status, atrasos
- ✅ Cálculo automático de datas

### Regras de Negócio
- ✅ Usuário não pode emprestar o mesmo livro duas vezes
- ✅ Apenas usuários ativos podem fazer empréstimos
- ✅ Livros sem estoque não podem ser emprestados
- ✅ Empréstimos atrasados não podem ser renovados
- ✅ Devolução atualiza automaticamente o estoque

---

## 🛠️ Tecnologias e Conceitos Aplicados

- **Spring Boot 3** - Framework backend
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **H2 Database** - Banco em memória
- **Lombok** - Redução de boilerplate
- **Bean Validation** - Validação de dados
- **Exception Handling** - Tratamento global de erros
- **RESTful API** - Arquitetura REST
- **Maven** - Gerenciamento de dependências

---

## 📚 Conceitos de Engenharia de Software

- ✅ Arquitetura em camadas (Controller, Service, Repository)
- ✅ Separation of Concerns
- ✅ Injeção de Dependência
- ✅ Tratamento de exceções centralizado
- ✅ Validações de regras de negócio
- ✅ Relacionamentos JPA (@ManyToOne)
- ✅ Query Methods do Spring Data
- ✅ DTOs implícitos nas entidades

---

## 🚀 Deploy

(https://biblioteca-api-production-7a4b.up.railway.app/swagger-ui/index.html)

---

## 👨‍💻 Autor

**Aline Mirian**

- GitHub: [@AlineMirian](https://github.com/AlineMirian)
- Bootcamp: Santander Bootcamp 2023 - DIO.
