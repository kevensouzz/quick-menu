# Quickmenu

Quickmenu é um repositório de cardápios digitais, criado a partir de uma ideia de facilitar o acesso à cardápios de restaurantes e coisas do tipo... onde o usuário só precisaria pesquisar o nome do restaurante, proprietário ou cardápio na barra de pesquisa e vizualizar todas as opções e sua disponibilidade.

## Desenvolvido usando:

### - Backend
1. PostgreSQL
2. Java
3. Spring

### - Frontend
1. Typescript
2. Angular

## Rodando localmente

### Requisitos
- Docker
- Docker-Compose

### Step-By-Step

- git clone https://github.com/kevensouzz/quick-menu.git
- cd quick-menu/
- cd client/ && docker build -t client . && cd ../server && docker build -t server . && cd ../
- docker compose up -d

### Path

- **Server Swagger Docs**: http://localhost:8080/swagger-ui/index.html
- **Client Interface**: http://localhost:4200