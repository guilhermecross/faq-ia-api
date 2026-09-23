# Assistente de FAQ com IA Generativa

Projeto de portfólio que integra Java + Spring Boot com a API da Anthropic (Claude),
aplicando engenharia de prompts para criar um assistente de atendimento que responde
perguntas de clientes com base numa base de conhecimento real — sem inventar informação.

## Problema que resolve

Simula um assistente de atendimento de uma loja de calçados: o cliente pergunta sobre
um produto ou uma política da loja (troca, entrega, pagamento), e a IA responde com base
apenas no que está cadastrado no banco de dados, mantendo tom e formato consistentes.

## Conceitos de engenharia de prompts aplicados

- **System prompt**: define o papel da IA e regras rígidas de comportamento
- **Grounding (aterramento)**: a IA só responde com base no contexto fornecido, evitando alucinação
- **Contexto dinâmico**: o prompt muda a cada pergunta, injetando só os dados relevantes
- **Controle de formato**: instrução explícita de tom, tamanho e o que NUNCA fazer

## Tecnologias

- Java 17, Spring Boot 3 (Web, Data JPA, Validation)
- RestClient (Spring) para integração HTTP com a API da Anthropic
- Banco H2 em memória
- springdoc-openapi (Swagger)

## Como rodar localmente

1. Gere uma chave de API em [console.anthropic.com](https://console.anthropic.com)
2. Configure a variável de ambiente `ANTHROPIC_API_KEY` com essa chave
   - No IntelliJ: Run > Edit Configurations > Environment variables > adicione `ANTHROPIC_API_KEY=sua-chave-aqui`
3. Rode a classe `FaqIaApiApplication`
4. A API sobe em `http://localhost:8080`, já com produtos e regras de exemplo cadastrados
5. Swagger: `http://localhost:8080/swagger-ui.html`

## Endpoints principais

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/perguntas` | Envia uma pergunta e recebe a resposta gerada pela IA |
| GET | `/conversas` | Lista o histórico de perguntas e respostas |
| POST | `/produtos` | Cadastra um novo produto na base de conhecimento |
| GET | `/produtos` | Lista os produtos cadastrados |
| POST | `/regras` | Cadastra uma nova regra de atendimento |
| GET | `/regras` | Lista as regras cadastradas |

## Exemplo de uso

```
POST /perguntas
{
  "pergunta": "O Tênis Runner tem numeração 41?"
}
```

Resposta (gerada pela IA com base no produto cadastrado):
```
{
  "resposta": "No momento, o Tênis Runner está disponível nos tamanhos 36 a 40. Não temos o 41 em estoque."
}
```

## Segurança

A chave de API nunca é escrita no código-fonte — é lida de uma variável de ambiente
(`ANTHROPIC_API_KEY`), seguindo a prática padrão de mercado para segredos de aplicação.

## Autor

Guilherme Cross Pereira
