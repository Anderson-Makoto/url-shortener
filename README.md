# url-shortener
System design, implementar um sistema de encurtamento de url.

## Levantamento de requisitos

### Requisitos funcionais

- gerar um **único** url curto para um dado url longo
- redirecionar os usuários para o url original quando o curto for acessado
- permitir que usuários customizem a url curta
- espirar url gerada quando o link não for acessado após um determinado período de tempo
- prover analise para links gerados

### Requisitos não funcionais

- alta disponibilidade (up-time de 99.9%)
- baixa latencia (gerar url e o redirecionamento deve acontecer em milisegundos)
- escalabilidade (o sistema deve suportar milhões de requisições por dia)
- durabilidade (url gerada deve funcionar por anos)
- segurança para prevenir phishing

## Estimativa de capacidade

Supondo as seguintes métricas:
- requests para gerar urls diariamente: 1 milhão
- taxa leitura:escrita: 100:1 (para cada url criada, esperamos 100 visitas)
- pico de tráfego: 10x a média de tráfego
- média de tamanho de url original de 100 caracteres

### Requisitos de throughput

- Média de writes/segundo: 1000000/ 86400 (segundos por dia) = 12
- Pico médio de writes/segundo: 12 * 10 = 120
- média de redirects por segundo: 12 * 100 = 1200
- pico de redirects por segundo: 120 * 100 = 12000
### Estimativas de storage

Para cada url, temos:
- short url: 7 caracteres
- original url: 100 caracteres (média)
- data de criação: 8 bytes
- data de exclusão: 8 bytes
- quantidade de cliques: 4 bytes
Total storage por url: 7 + 100 + 8 + 8 + 4 = 127 bytes

Em um ano:
- 127 * 1000000 * 365 = 46.4GB

### Estimativa de banda

Assumindo que um HTTP 301 (redirect) é uns 500 bytes (incluindo headers e short url):
- banda de leitura por dia: 100000000 * 500 bytes = 50GB
- pico de banda: 500 bytes * 12000 = 6MB/s

### Estimativa de cache

Seguindo a regra do 80-20, ou seja, 20 % das hot urls, representam 80% dos redirects, então podemos guardar 20% das hot urls em cache

1000000 * 0.2 * 127 bytes = 25.4MB

### Estimativa de infraestrutura

- API server: 4 a 6 instâncias cada um lidando com 200-300 requisições por segundo
- database: banco distribuido de 10-20 nós para lidar com o read/write thoughput
- cache: 3-4 nós, dependendo da carga

## High-level design

![url-shortener high-level diagram drawio](https://github.com/user-attachments/assets/a268e7d1-fb59-47eb-8e1e-3bd7afc81bbb)

## Database design

### tipo de banco

Para este problema, não é necessário uma estrutura complexa de dados robusto com ACID, nem uma rede complexa, então bancos relacionais não é necessário. Por se tratar de um problema onde a complexidade não é o foco, e sim o desempenho, um banco orientado a documentos faz mais sentido neste contexto.

### Schemas

![url-shortener high-level diagram drawio (1)](https://github.com/user-attachments/assets/7890e896-1070-4709-ae57-dff44e851d23)

## API design

Vamos usar Restful, pois é escalável, intuitivo, e eficiente

### API para criar a short url
endpoint: /api/v1/shorten

Request:
{
  url: 'http://example.com/path1/pah2',
  custom_alias: '',
  expiry_date: '2024-12-31',
  user_id: 1
}

Response:
{
  short_url: 'http://short.url/abc123',
  long_url: 'http://example.com/path1/pah2',
  created_at: '2024-12-01',
  expiry_date: '2025-12-01'
}

### API para redirecionar
endpoint: /{short_url_key}

Response: http 301 (redireciona para a long url)

## Aprofundando em conceitos chave

### Serviço gerador de url

Devemos gerar um url único para cada url original

- gerar urls menores é melhor, mas limita a quantidade de possibilidades
- escalabilidade: a funcionalidade deve ser eficiente até para bilhões de de urls simultaneas
- o algoritmo deve conseguir lidar com urls duplicadas

#### Primeira abordagem

1. Podemos usar um algoritmo de criptográfia, como md5 ou sha1, para criptografar a url original.
2. Com o hash, usamos o Base62 para gerar um código url friendly, pois gera um alfanumérico (A-Za-z0-9).
3. Pegamos os primeiros 6 bytes do hash, convertemos para hexadecimal, e convertemos para base62. (Essa escolha de primeiros 6 bytes é feito, pois assim podemos gerar um base62 de 7 caracteres).

O problema com essa abordagem é que podemos acabar gerando o mesmo código.
Para contornar isso, podemos verificar se a hash existe, e caso exista, adicionamos um sufixo na short url.

#### Segunda abordagem

Usar incremental ID, assim, podemos garantir que não teremos código repetido, se gerarmos a base62 do ID.

Os problemas são: por se tratar de IDs sequenciais, eles são previsíveis. E, se não tivermos um bom design de geração de ID, pode causar um bottleneck.

### Custom alias

O usuário pode mandar custom aliases para o short url, matendo as seguintes regras:

- Não pode ser um url repetido
- conter somente caracteres validados, como alfanuméricos, e hifen
- não usar paths já usadas pela API, como "admin"

### Link expiration

As urls têm um período de vida, após isso, elas são descartadas.

- O usuário pode definir a data de expiração
- O sistema pode definir uma data padrão de 1 ano se o usuário não definir

Para a lógica, podemos ter um cron, que verifica os vencidos, e descarta eles, ou podemos descartá-los ao acessar a url.

### Serviço de redirecionamento

Para o redirecionamento, a short url recebida deve ser:
1. Consultar o banco (ou cache) para a original url relacionada a esse short url
2. Retornar um HTTP 301 redirect com a original url.

#### Cache

Para reduzir a latencia, podemos guardar em cache as mais acessadas short urls

### Serviço de analytics

Para guardarmos o número de acessos de cada shor url, podemos ter um serviço de analytics, para isso podemos:
- Usar message queues para poder como Kafka para registrar cada evento de clique. Isso desacopla o analytics do serviço de redirect, o que evita de introduzir latência.
- Usar processamento em batch para enviar os logs de clique para um warehouse para análise.

## Problemas chave e bottleneck

### Escalabilidade

#### Camada API

Podemos criar várias instâncias de servidor e gerenciá-los por um load balancer. Assim podemos distribuí-los igualmente entre eles.

#### Sharding

Podemos distribuir os dados entre vários shards de banco de dados, para isso podemos ter, por exemplo:
- os dados armazenados em range-based shards, o que significa que um shar pode armazenar os dados com IDs de 1 à 1000000, o segundo shard de 1000001 à 2000000, e assim por diante.
- Pode ser hash-based, com isso, o ID da url é usada em um hash, e tirado o modulo da divisão com N, onde N é o número de shards.

### Disponibilidade

#### Replicação

Usar replicação de dados para garantir que eles estejam disponíveis mesmo que um banco caia.

#### Failover

Implementar mecanismos para trocar automaticamente para outros servidores ou bancos caso um caia.

#### Deploy geo-distribuído

Deploy do serviço em várias regiões para melhorar a eficiência e a disponibilidade

### Edge cases

1. Caso a url expire, o servidor deve retornar um http 410.
2. Caso tentem acessar uma url não existente, o servidor deve retornar um http 404.
3. Se um conflito ocorrer, onde uma short url mapeia para mais de uma original url, devemos lidar com isso, uma abordagem seria verificar qual usuário pediu a url, e então redirecionar para a original url deste usuário.

### Segurança

#### Rate limit

Para prevenir um ddos, ou um overflow, podemos criar um rate limit de requisições.

#### Validação de input

Garantir que a url original não contenha conteúdo malicioso

#### HTTPS

Devemos realizar a comunicação entre cliente-servidor usando https, assim evitamos ataques de eavesdropping e man-in-the-middle.

#### Monitoramento e alertas

Monitoriamento para padrões suspeitos de atividades, e triggerar alertas para potenciais ataques de DDoS
