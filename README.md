# GovBR Signature Integration

Este repositório fornece uma solução simples para integração de assinatura digital utilizando o GovBR. Ideal para aplicações que exigem autenticação segura e assinatura eletrônica conforme os padrões estabelecidos pelo governo brasileiro.

## Funcionalidades

- Integração com o GovBR para autenticação e assinatura digital.
- Geração de documentos assinados conforme padrões da ICP-Brasil.
- Suporte a configurações customizadas para atender necessidades específicas.

## Requisitos

Antes de iniciar, certifique-se de ter os seguintes requisitos:

- **Java**: Versão 11 ou superior.
- **Maven**: Para gerenciamento de dependências.
- Credenciais de acesso ao GovBR (certificado digital).

## Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/FeliciLab/govbr-signature-integration.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
   cd govbr-signature-integration
   ```

3. Compile o projeto utilizando Maven:
   ```bash
   mvn clean install
   ```

4. Configure as variáveis de ambiente conforme descrito abaixo.

## Configuração

🔥 **Variáveis de ambiente usadas** 🔥

Descrição das variáveis de ambiente que usamos nesse projeto:

```shell
- **REDIRECT_URI**: URL de redirecionamento usado pela API do GovBR.
- **CLIENT_ID**: Identificador do cliente pela API do GovBR.
- **SECRET**: Segredo usado pela API do GovBR.
- **SERVIDOR_OAUTH**: Caminho para o servidor OAUTH usado pela API do GovBR.
- **ASSINATURA_API_URI**: URL para o caminho da API de assinatura digital.
- **SERVER_PORT**: Porta usada pela aplicação.
- **IMG_RUBRIC_SOURCE**: Caminho para a imagem da rúbrica que fica no projeto.
```

As variáveis **REDIRECT_URI**, **CLIENT_ID**, **SECRET**, **SERVIDOR_OAUTH** e **ASSINATURA_API_URI** podem ser melhor entendidas lendo a documentação de integração. As outras variáveis são usadas como configurações obrigatórias deste projeto para configurar ambientes de homologação, produção e desenvolvimento.

## Uso

Abaixo está um exemplo simples de como usar a integração:

```java
import com.felicilab.govbr.GovBrSignatureService;

public class Main {
    public static void main(String[] args) {
        GovBrSignatureService signatureService = new GovBrSignatureService();

        String documento = "Conteúdo do documento a ser assinado";
        String assinatura = signatureService.assinar(documento);

        System.out.println("Documento Assinado: " + assinatura);
    }
}
```

## Testes

Execute os testes utilizando o Maven:

```bash
mvn test
```

Certifique-se de configurar um ambiente de teste com as credenciais apropriadas antes de executar os testes.

## Contribuições

Contribuições são bem-vindas! Para contribuir:

1. Fork este repositório.
2. Crie uma branch para sua feature ou correção:
   ```bash
   git checkout -b minha-feature
   ```
3. Faça commit de suas alterações:
   ```bash
   git commit -m "Adiciona minha feature"
   ```
4. Envie para o repositório remoto:
   ```bash
   git push origin minha-feature
   ```
5. Abra um pull request.

## Licença

Este projeto está licenciado sob a Licença MIT. Consulte o arquivo `LICENSE` para mais informações.


## Referência

Este repositório pode ser encontrado em: [FeliciLab/govbr-signature-integration](https://github.com/FeliciLab/govbr-signature-integration)

## Documentação da Integração

Para informações detalhadas sobre a integração com o GovBR, consulte a [Documentação da Integração](https://github.com/FeliciLab/govbr-signature-integration/wiki).

## Verificador de Homologação

Utilize o [Verificador de Homologação](https://gov.br/assinatura/homologacao) para validar suas configurações e garantir o funcionamento correto da integração.

---

**Happy Coding!**

