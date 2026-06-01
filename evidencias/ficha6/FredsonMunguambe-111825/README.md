# Evidências — Ficha Laboratorial nº 6

**Elemento do grupo:** FredsonMunguambe-111825
**Projeto:** BlackBattleShip
**Unidade curricular:** Engenharia de Software

Este documento reúne as evidências das tarefas realizadas localmente durante a Ficha Laboratorial nº 6.

---

## A — Instalação e validação do Docker Desktop

Foi validada a instalação e execução do Docker Desktop.

Foram utilizados os seguintes comandos:

```bash
docker version
docker compose version
docker images
docker ps -a
```

### Evidência

![Verificação do ambiente Docker](./A-docker-desktop/01-verificacao-ambiente.png)

---

## B — Criação de um contentor XAMPP

Foi descarregada a imagem:

```text
tomsik68/xampp
```

Foi criado o contentor:

```text
myXampp
```

A pasta local `my_web_pages` foi ligada à pasta `/www` do contentor através de um volume Docker.

Primeiro foi criada uma página PHP simples com a mensagem:

```text
O meu teste com um contentor Docker
```

Posteriormente, a página foi substituída por um jogo Tic-Tac-Toe em PHP contra o computador.

### Evidências

![Página PHP simples no navegador](./B-xampp/01-pagina-php.png)

![Contentor XAMPP em execução](./B-xampp/02-contentor-xampp.png)

---

## C — WordPress com MySQL através de Docker Compose

Foi utilizado o ficheiro `docker-compose.yml` disponibilizado pelo docente.

O ficheiro cria dois serviços:

* `db`: base de dados MySQL;
* `wordpress`: sistema de gestão de conteúdos ligado à base de dados.

Foi validada a execução dos contentores através do Docker Compose.

### Evidência

![WordPress e MySQL através de Docker Compose](./C-wordpress-compose/01-wordpress-compose.png)

---

## D — MongoDB num contentor Docker

Foi descarregada a imagem oficial:

```text
mongo:latest
```

Foi criado o contentor:

```text
mongodb
```

Dentro da consola `mongosh`, foi criada a base de dados `testedb`.

Foram inseridos e pesquisados os seguintes registos:

```javascript
db.people.insertOne({ firstname: "José", lastname: "Miguel" })

db.people.insertOne({ firstname: "Sara", lastname: "Miguel" })

db.people.find({ lastname: "Miguel" })
```

### Evidência

![Inserção e pesquisa de registos no MongoDB](./D-mongodb/01-mongodb-registos.png)

---

## Próximas tarefas

As evidências das tarefas seguintes serão adicionadas progressivamente:

* E — Publicação da imagem Docker da Batalha Naval;
* F — DevContainer;
* G — GitHub Pages e Javadoc;
* H — Pipeline completa de CI/CD.
