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
