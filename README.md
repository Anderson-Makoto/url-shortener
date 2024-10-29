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

