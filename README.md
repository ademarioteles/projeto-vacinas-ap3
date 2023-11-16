<img src="http://img.shields.io/static/v1?label=STATUS&message=EM%20DESENVOLVIMENTO&color=GREEN&style=for-the-badge"/>
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
<!-- - [Testes](#testes) -->
<!-- - [Docker](#docker) -->
- [Contribuição](#contribuição)
- [Autores](#autores)
<!-- - [Licença](#licença) -->
- [Referências](#referências)

  
##  📝  Visão Geral

A API é projetada para fornecer as seguintes funcionalidades:

- Registro de vacinações de pacientes.
- Gerenciamento de informações sobre vacinas e pacientes.
- Estatísticas sobre vacinação, como doses aplicadas, doses atrasadas e vacinas por fabricante.

## 📦 Requisitos

Antes de iniciar, certifique-se de que possui os seguintes requisitos:

- [Java (versão 17)](https://www.java.com/)
- [MongoDB (versão 1.40.4)](https://www.mongodb.com/try/download/compass)
- [Postman ](https://www.postman.com/downloads/)
<!-- - [Docker](https://www.docker.com/)
- [Docker-Compose](https://www.docker.com/) -->

##  Endpoint de Teste

Para facilitar a verificação rápida da API, você pode usar o seguinte endpoint de teste:

Endpoint: **GET** 
```bash
https://wb-api-vacinas-v2.azurewebsites.net/pacientes
```
Descrição: Retorna uma mensagem simples indicando que a API está em funcionamento.

```bash
{"status": "API de Gerenciamento de Vacinação em Desenvolvimento"}
```

## ⚙️ Configuração

Se você encontrar problemas ao acessar o endpoint online ou simplesmente deseja executar a API localmente para desenvolvimento ou teste, siga estas instruções:

### Clone este repositório:

```bash
git clone https://github.com/seu-usuario/api-gerenciamento-vacinacao.git
```

<!-- ### Instale as dependências:
```bash

``` -->

###  Configure as variáveis de ambiente no arquivo (application.properties) para definir as configurações do banco de dados, URLs de outras APIs, etc.

```bash
MONGODB_URI=mongodb://localhost:27017/vacinacao
API_PACIENTES_URL=http://localhost:8080
API_VACINAS_URL=http://localhost:8081
```

###  Inicie o servidor:

```bash

A API estará acessível em http://localhost:8080.
```

###  ▶️ Uso

A API oferece vários endpoints para criar, ler, atualizar e excluir registros de vacinação, bem como para consultar informações estatísticas. Certifique-se de seguir a documentação dos endpoints.

###  🛣️ Endpoints

[/pacientes](#pacientes): Gerenciamento de informações sobre pacientes.
  
 *Este endpoint é responsável por fornecer funcionalidades relacionadas ao gerenciamento de informações sobre pacientes. Aqui estão algumas ações comuns que podem ser associadas a este endpoint:*
 
   - **GET /pacientes**: Retorna a lista de todos os pacientes cadastrados.
   - **GET /pacientes/{id}**: Retorna os detalhes de um paciente específico com base no ID.
   - **POST /pacientes**: Cadastra um novo paciente com base nos dados fornecidos no corpo da solicitação.
   - **PUT /pacientes/{id}**: Atualiza as informações de um paciente existente com base no ID.
   - **DELETE /pacientes/{id}**: Exclui um paciente específico com base no ID.
  
  
  [/pacientes/cadastrar](#pacientes/cadastrar): 

*Este endpoint é especificamente dedicado à ação de cadastrar um novo paciente. Geralmente, ele aceitará dados do paciente no corpo da solicitação usando o método POST. Por exemplo:*

  - **POST /pacientes/cadastrar:** Aceita dados do paciente no corpo da solicitação e cadastra um novo paciente.

[/pacientes/todos](#pacientes/todos): 
  
  *Este endpoint é destinado a recuperar a lista completa de todos os pacientes cadastrados. O método associado a este endpoint geralmente será o GET. Exemplo:*

 - **GET /pacientes/todos:** Retorna a lista completa de todos os pacientes cadastrados no sistema.

<!-- Consulte a documentação dos endpoints para obter detalhes sobre como usar cada um deles. -->

<!-- ### 🧪 Testes
A API inclui testes automatizados para garantir o funcionamento correto dos endpoints. Execute os testes da seguinte maneira: -->

<!-- ```bash

Comando de testes

``` -->
<!-- ###  🐳 Docker

Se desejar, você pode executar a API em um contêiner Docker. Use o Docker Compose para criar o ambiente completo, incluindo o banco de dados MongoDB e outras dependências:

```bash

xxxxxxxxxxxx

```

A API estará acessível em http://localhost:5000, e o banco de dados MongoDB estará em execução no contêiner. -->

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
