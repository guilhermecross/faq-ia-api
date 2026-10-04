# Assistente de FAQ com IA Generativa

Esse é o segundo projeto do meu portfólio de back-end. Depois de fazer a API de
controle de estoque (Java + Spring Boot puro), quis ir um passo além e testar como
é integrar um back-end de verdade com uma API de IA generativa — nesse caso, a API
da Anthropic (Claude).

A ideia surgiu pensando no meu trabalho na Larroudé: um assistente que responde
perguntas de clientes sobre produtos e políticas da loja (troca, entrega, pagamento),
mas **sem inventar informação** — só respondendo com base no que está realmente
cadastrado no sistema.

## Por que esse projeto

Eu já tinha feito o CRUD "tradicional" no projeto 1. Aqui, o desafio era diferente:
como faço a IA responder de forma útil e confiável, sem ela "alucinar" (inventar
tamanho de produto, prazo de entrega, etc.)? Isso me levou a estudar engenharia de
prompts na prática, não só na teoria.

## O que o sistema faz

1. Alguém pergunta algo (ex: *"o Tênis Runner tem numeração 41?"*)
2. O back-end procura, no banco, qual produto ou regra da loja tem a ver com a pergunta
3. Monta um prompt levando esse contexto + regras de como a IA deve se comportar
4. Manda pra API da Anthropic e recebe a resposta gerada
5. Salva a pergunta e a resposta no banco (histórico de conversas)

## Arquitetura (visão rápida)

```
Cliente → FaqController → FaqService → monta contexto (Produto/RegraAtendimento)
                                     → monta system prompt
                                     → AnthropicClientService → API da Anthropic
                        ← salva Conversa no banco
                        ← devolve resposta
```

Separei em camadas parecidas com o projeto 1 (model, repository, service,
controller), mas aqui o `FaqService` tem uma responsabilidade a mais: ele não só
orquestra os dados, ele **constrói o prompt**. Achei melhor deixar isso isolado do
`AnthropicClientService`, que só sabe conversar com a API — assim, se um dia eu
quiser trocar de provedor de IA, só uma classe muda.

## Tecnologias

- Java 17
- Spring Boot 3 (Web, Data JPA, Validation)
- RestClient (do próprio Spring) para chamar a API da Anthropic via HTTP
- Banco H2 em memória
- springdoc-openapi (Swagger)
- Docker + Render (deploy)

## Engenharia de prompts — o que eu apliquei

- **System prompt fixo**: define o papel da IA e regras de comportamento (tom,
  tamanho da resposta, o que nunca fazer)
- **Contexto dinâmico**: a cada pergunta, busco só os dados relevantes no banco
  (não mando o catálogo inteiro — economiza tokens e evita confundir o modelo)
- **Grounding**: a IA só pode responder com base no contexto fornecido. Se não
  achar informação, ela admite e sugere encaminhar pra um atendente humano, em vez
  de inventar
- **Normalização de texto**: a busca por produto ignora acentos (bug que eu mesmo
  encontrei testando — perguntei "Tenis" sem acento e a IA disse que não achou o
  produto, porque o nome cadastrado tinha acento. Corrigi normalizando os dois lados
  da comparação)

## Como rodar localmente

1. Crie uma conta em [console.anthropic.com](https://console.anthropic.com) e gere
   uma chave de API
2. Configure a variável de ambiente `ANTHROPIC_API_KEY` com essa chave
   - No IntelliJ: Run > Edit Configurations > Environment variables
3. Rode a classe `FaqIaApiApplication`
4. A API sobe em `http://localhost:8080`, já com produtos e regras de exemplo
   cadastrados automaticamente (veja `DataSeeder`)
5. Swagger: `http://localhost:8080/swagger-ui.html`

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/perguntas` | Envia uma pergunta, recebe a resposta gerada pela IA |
| GET | `/conversas` | Lista o histórico de perguntas e respostas |
| POST | `/produtos` | Cadastra um produto na base de conhecimento |
| GET | `/produtos` | Lista os produtos cadastrados |
| POST | `/regras` | Cadastra uma regra de atendimento |
| GET | `/regras` | Lista as regras cadastradas |

## Exemplo real

```
POST /perguntas
{
  "pergunta": "O Tênis Runner tem numeração 41?"
}
```

```json
{
  "resposta": "Infelizmente, o Tênis Runner não está disponível no tamanho 41. Os tamanhos disponíveis são 36, 37, 38, 39 e 40. Gostaria de escolher um desses tamanhos ou precisa de ajuda com outra informação?"
}
```

## Sobre o custo e a segurança

Diferente do projeto 1, esse endpoint **custa dinheiro de verdade** — cada pergunta
é uma chamada paga à API da Anthropic. Por isso, antes de publicar o link, adicionei
um filtro simples (`DemoAccessFilter`) que exige um header `X-Demo-Key` pra acessar
`/perguntas`. Sem isso, qualquer pessoa que achasse a URL pública poderia gastar meus
créditos fazendo perguntas. Não é autenticação "de verdade" (isso seria Spring
Security + JWT, que pretendo estudar no próximo projeto), mas resolve o problema
real que eu tinha.

## Desafios que enfrentei (e o que aprendi)

- **Configuração de ambiente**: perdi bastante tempo com o IntelliJ não reconhecendo
  o projeto como Maven, Lombok não gerando getters/setters, e variáveis de ambiente
  bagunçadas entre diferentes run configurations. Aprendi a debugar isso lendo o
  stack trace com calma em vez de tentar de qualquer jeito.
- **Deploy no Render com Docker**: errei o caminho do Dockerfile umas 3 vezes
  (confundindo "Root Directory", "Dockerfile Path" e "Docker Build Context
  Directory" — são campos diferentes, e cada um espera um tipo de valor).
- **Créditos da API zerados**: minha conta não veio com crédito grátis aplicado
  automaticamente, precisei adicionar manualmente antes de conseguir testar de
  verdade.
- **Acento na busca**: o bug mais sutil do projeto — expliquei acima, na seção de
  engenharia de prompts.

## Próximos passos (roadmap pessoal)

- [ ] Trocar o H2 por PostgreSQL de verdade
- [ ] Autenticação real (Spring Security + JWT) em vez do filtro simples
- [ ] Testes de integração com MockMvc
- [ ] Explorar RAG (Retrieval Augmented Generation) como evolução natural desse
      projeto

## Autor

Guilherme Cross Pereira — estudante de Engenharia de Software, em busca da primeira
oportunidade como desenvolvedor back-end.

[LinkedIn](https://linkedin.com/in/guilherme-cross) · [GitHub](https://github.com/guilhermecross)
