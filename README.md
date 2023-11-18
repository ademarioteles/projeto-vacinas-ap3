[![Status](http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge)](https://docs.google.com/document/d/1gmlTeWkoDIqYmJoyexdaRENjw6iERnu1/edit)
</p>

# 💉 API de Gerenciamento de Vacinação

<code><img height="20" src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white"></code>
<code><img height="20" src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white"></code>
<code><img height="20" src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white"></code>

## 🚀 Começando


Esta é uma API de Gerenciamento de Vacinação desenvolvida pela equipe Sanhok permite o controle e registro de vacinações de pacientes, gerenciamento de vacinas e pacientes, e fornece informações estatísticas sobre a vacinação. A API foi desenvolvida para atender aos requisitos do projeto "Programação Web 2 - Oficial 2".

## 📋 Conteúdo do README

- [Visão Geral](#visão-geral)
- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Uso](#uso)
- [Endpoints](#endpoints)
- [Contribuição](#contribuição)
- [Autores](#autores)
- [Referências](#referências)

  
##  📝  Visão Geral

A API é projetada para fornecer as seguintes funcionalidades:

- Registro de vacinações de pacientes.
- Gerenciamento de informações sobre vacinas e pacientes.
- Estatísticas sobre vacinação, como doses aplicadas, doses atrasadas e vacinas por fabricante.

##  🧪  Testes BDD

Nossos testes de Desenvolvimento Orientado a Comportamento (BDD) estão documentados [aqui](https://docs.google.com/document/d/1gmlTeWkoDIqYmJoyexdaRENjw6iERnu1/edit?usp=sharing&ouid=104507896264921397464&rtpof=true&sd=true).

## 📦 Requisitos

Antes de iniciar, certifique-se de que possui os seguintes requisitos:

- [Java (versão 17)](https://www.java.com/)
- [MongoDB (versão 1.40.4)](https://www.mongodb.com/try/download/compass)
- [Postman ](https://www.postman.com/downloads/)
- [Apache Maven](https://maven.apache.org/)

##  Endpoint de Teste

Para facilitar a verificação rápida da API, você pode usar o seguinte endpoint de teste:

Endpoint: **GET** 
```bash
https://wb-api-vacinas-v1.azurewebsites.net/sanhok
```
Descrição: Retorna uma mensagem simples indicando que a API está em funcionamento.

```bash
API de Gerenciamento de Vacinação desenvolvida pela equipe Sanhok para atender aos requisitos do projeto 'Programação Web 2 - Oficial 2
```

## ⚙️ Configuração

Se você encontrar problemas ao acessar o endpoint online ou simplesmente deseja executar a API localmente para desenvolvimento ou teste, siga estas instruções:

### Clone este repositório:

```bash
git clone https://github.com/ademarioteles/projeto-vacinas-ap3.git
```

###  Configure as variáveis de ambiente no arquivo (application.properties) para definir as configurações do banco de dados, URLs de outras APIs, etc.

```bash
API_VACINAS_URL=http://localhost:8080
API_PACIENTES_URL=http://localhost:8081
API_REGISTROS_VACINACAO_URL=http://localhost:8082
```

###  Inicie o servidor:

```bash

A API estará acessível em http://localhost:8080.
```

###  ▶️ Uso

A API oferece vários endpoints para criar, ler, atualizar e excluir registros de vacinação, bem como para consultar informações estatísticas. Certifique-se de seguir a documentação dos endpoints.

### 🛣️ Endpoints

#### [/vacinas](#vacinas)
- **POST /vacinas/cadastrar**: Adiciona uma nova vacina ao sistema.
  - **Request Body**: Um objeto JSON contendo as informações da nova vacina.
  - **Response**: Retorna a vacina recém-adicionada com status 201 (Created).

- **POST /vacinas/inject**: Simula a administração de vacinas, adicionando registros fictícios.
  - **Response**: Retorna a lista atualizada de todas as vacinas com status 200 (OK).

- **GET /vacinas**: Obtém a lista de todas as vacinas cadastradas.
  - **Response**: Retorna a lista de vacinas com status 200 (OK).

- **GET /vacinas/{id}**: Obtém informações sobre uma vacina específica com base no ID.
  - **Path Variable**: `id` - O ID da vacina desejada.
  - **Response**: Retorna os detalhes da vacina com status 200 (OK).

- **PUT /vacinas**: Edita uma vacina com base nas informações fornecidas no corpo da requisição.
  - **Request Body**: Um objeto JSON contendo as informações atualizadas da vacina.
  - **Response**: Retorna a vacina editada com status 200 (OK).

- **PUT /vacinas/{id}**: Edita uma vacina específica com base no ID e nas informações fornecidas no corpo da requisição.
  - **Path Variable**: `id` - O ID da vacina a ser editada.
  - **Request Body**: Um objeto JSON contendo as informações atualizadas da vacina.
  - **Response**: Retorna a vacina editada com status 200 (OK).

- **PATCH /vacinas/{id}**: Atualiza parcialmente uma vacina específica com base no ID e nas informações fornecidas no corpo da requisição.
  - **Path Variable**: `id` - O ID da vacina a ser atualizada parcialmente.
  - **Request Body**: Um objeto JSON contendo as informações a serem atualizadas.
  - **Response**: Retorna a vacina parcialmente atualizada com status 200 (OK).

- **PATCH /vacinas**: Atualiza parcialmente uma vacina com base nas informações fornecidas no corpo da requisição.
  - **Request Body**: Um objeto JSON contendo as informações a serem atualizadas.
  - **Response**: Retorna a vacina parcialmente atualizada com status 200 (OK).

- **DELETE /vacinas/{id}**: Exclui uma vacina específica com base no ID.
  - **Path Variable**: `id` - O ID da vacina a ser excluída.
  - **Response**: Retorna uma mensagem de sucesso com status 200 (OK).

- **DELETE /vacinas/todos**: Exclui todas as vacinas do sistema.
  - **Response**: Retorna uma mensagem de sucesso com status 200 (OK).

#### [/sanhok](#sanhok)
- **GET /sanhok**: Retorna uma mensagem de boas-vindas personalizada para a API de Gerenciamento de Vacinação desenvolvida pela equipe Sanhok.
  - **Response**: Retorna a mensagem de boas-vindas com status 200 (OK) e tipo de conteúdo TEXT_PLAIN.

- **GET /sanhok/inject**: Simula a injeção de dados para o registro de vacinação.
  - **Response**: Retorna a mensagem "Usuários injetados" com status 200 (OK) e tipo de conteúdo TEXT_PLAIN.

#### [/pacientes](#pacientes)
- **GET /pacientes/{id}/vacinas**: Obtém o registro resumido de vacinação para um paciente específico com base no ID.
  - **Path Variable**: `id` - O ID do paciente.
  - **Response**: Retorna o registro resumido de vacinação com status 200 (OK).

- **GET /pacientes/vacinas/atrasadas**: Obtém a lista de pacientes com doses de vacinas atrasadas.
  - **Request Parameter**: `estado` - (Opcional) Estado dos pacientes.
  - **Response**: Retorna a lista de pacientes com doses atrasadas com status 200 (OK).

#### [/registros-de-vacinacao](#registros-de-vacinacao)
- **POST /registros-de-vacinacao/cadastrar**: Cria um novo registro de vacinação.
  - **Request Body**: Um objeto JSON contendo as informações do novo registro de vacinação.
  - **Response**: Retorna uma mensagem de sucesso com status 201 (Created).

- **POST /registros-de-vacinacao/editar/{id}**: Edita um registro de vacinação com base no ID e nas informações fornecidas no corpo da requisição.
  - **Path Variable**: `id` - O ID do registro de vacinação a ser editado.
  - **Request Body**: Um objeto JSON contendo as informações atualizadas do registro de vacinação.
  - **Response**: Retorna uma mensagem de sucesso com status 200 (OK).

- **PATCH /registros-de-vacinacao/editar/{id}**: Edita parcialmente um registro de vacinação com base no ID e nas informações fornecidas no corpo da requisição.
  - **Path Variable**: `id` - O ID do registro de vacinação a ser editado parcialmente.
  - **Request Body**: Um objeto JSON contendo as informações a serem atualizadas.
  - **Response**: Retorna uma mensagem de sucesso com status 200 (OK).

- **GET /registros-de-vacinacao/apagar/{id}**: Apaga um registro de vacinação com base no ID.
  - **Path Variable**: `id` - O ID do registro de vacinação a ser apagado.
  - **Response**: Retorna uma mensagem de sucesso com status 200 (OK).

- **GET /registros-de-vacinacao/paciente/{id}**: Obtém a lista de registros de vacinação para um paciente específico com base no ID.
  - **Path Variable**: `id` - O ID do paciente.
  - **Response**: Retorna a lista de registros de vacinação com status 200 (OK).

- **GET /registros-de-vacinacao/vacina/{id}**: Obtém a lista de registros de vacinação para uma vacina específica com base no ID.
  - **Path Variable**: `id` - O ID da vacina.
  - **Response**: Retorna a lista de registros de vacinação com status 200 (OK).

- **GET /registros-de-vacinacao/lista**: Obtém a lista de todos os registros de vacinação.
  - **Response**: Retorna a lista de registros de vacinação com status 200 (OK).

#### [/vacinas-aplicadas](#vacinas-aplicadas)
- **GET /vacinas-aplicadas/quantidade**: Obtém a quantidade total de vacinações.
  - **Request Parameter**: `estado` - (Opcional) Estado das vacinações.
  - **Response**: Retorna a quantidade total de vacinações com status 200 (OK).

- **GET /vacinas-aplicadas**: Obtém a lista de doses aplicadas.
  - **Request Parameter**: `estado` - (Opcional) Estado das vacinações.
  - **Request Parameter**: `fabricantes` - (Opcional) Fabricantes das vacinas.
  - **Response**: Retorna a lista de doses aplicadas com status 200 (OK).



## 🤝 Contribuição

Se desejar contribuir para o desenvolvimento deste projeto, siga estas etapas:

1. Crie um fork do repositório.
2. Crie uma branch com sua feature: `git checkout -b minha-feature`
3. Faça commit das alterações: `git commit -m 'Adicionando nova feature'`
4. Faça push para a branch: `git push origin minha-feature`
5. Envie um Pull Request.

## ✍️ Autores


- Ademario Teles - [GitHub](https://github.com/ademarioteles)
- Ana Beatriz  - [GitHub](https://github.com/anabiajferreira)
- Maiara Rodrigues  - [GitHub](hthttps://github.com/maia-ra)
- Vanessa Santana  - [GitHub](https://github.com/nessa1408)
- Victor Caetano - [GitHub](https://github.com/vctor-c)
- Taysa Barbosa  - [GitHub](https://github.com/taysa-barbosa)


## 📚 Referências

- https://www.java.com/pt-BR
- https://www.mongodb.com/products/tools/compass
- https://spring.io/projects/spring-boot
- https://www.postman.com/
