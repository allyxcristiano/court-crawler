# Court Crawler

Tentando dominar o mundo crawleando tribunais

## Sumário

- O que é este projeto?
- O que você vai precisar
- Executando
- Uso
- Terminando execução
- Executando bateria de testes

## O que é este projeto?

Se trata de um desafio técnico onde o objetivo é "crawlear" tribunais de justiça brasileiros, atualmente este serviço é
capaz de ler páginas de processos de primeiro e segundo grau do [TJ AL](http://www.tjal.jus.br/) 
e [MS](https://www.tjms.jus.br/).

O termo crawlear se refere neste contexto a disponibilizar um serviço capaz de obter as informações primeira e segunda 
instância (caso existam) de um processo existente nestes tribunais. Estas informações serão salvas em uma estrutura de
documentos específica com o intuito de facilitar o manuseio destes dados e possibilitar melhor acesso por meio de 
outras ferramentas.

## O que você vai precisar

Você precisará ter instalado:

* [Docker compose / docker](https://github.com/Yelp/docker-compose/blob/master/docs/install.md)

## Executando

Após instalar o docker-compose e clonar este repositório execute o comando `docker-compose up -d` para subir 
uma instância da API com todas suas dependências configuradas.

A primeira execução demora um pouco pois todas as dependências serão baixadas e configuradas, após seguir este passo 
o serviço estará disponível em `http://localhost:7000`.

Bônus: Para acompanhar todos os logs execute o comando `docker-compose logs -f -t`.

## Uso

1. O swagger está disponível em `http://localhost:7000/docs`.

2. Para ajudar nos testes existem alguns demos dentro do projeto, você pode usar `court-of-al-demo.txt` 
e `court-of-ms-demo.txt` para fazer requisições de diversos processos com cenários específicos e corner cases para 
testar o crawler.

3. Para ajudar na análise dos processos crawleados e da estrutura de dados dos documentos sem ter que abrir um gestor do 
mongodb ou usar a linha de comando para analisar o conteúdo crawleado um GET sem paginação foi criado, você poderá 
acessa-lo em `http://localhost:7000/processes`.

## Terminando execução

Para terminar a execução execute o comando `docker-compose stop`.

## Executando bateria de testes

1. Instale o [gradle](https://gradle.org/install/)

2. Apague o conteúdo da pasta `/build` que está pasta raiz do projeto 

3. Inicie os containers necessários executando o comando `docker start hub-court-crawler chrome-court-crawler`

4. Execute o comando `gradle test` para rodar a bateria de testes

5. Após executar os testes pare a execução dos containers executando o comando 
`docker stop hub-court-crawler chrome-court-crawler`