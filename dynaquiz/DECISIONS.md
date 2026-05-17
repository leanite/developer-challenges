## Uso de Channel em eventos one-shot

```kotlin
private val _events = Channel<SplashEvent>(Channel.BUFFERED)
val events = _events.receiveAsFlow()
```

 Padrão recomendado pela comunidade e pelo próprio Google para garantir que o evento seja consumido exatamente uma vez, mesmo com mudanças de configuração ou quando o app vai para background

 ## Intent iniciais em Screens

 Não costumo deixar intents serem executadas por default em um `ViewModel`. Gosto de, explicitamente, chamar uma intent usando um `onIntent()` inicial para ajudar na visibilidade futura do código, tentando diminuir o número de "mágicas" que ocorrem no código.

 ## Apenas um PNG com resolução suficiente em `drawable`
 
 Para facilitar a geração de resources, vou usar apenas um *.png com resolução suficiente para funcionar bem na aparesentação/uso da maioria dos dispositivos

## Uso de commonTest
Optei por usar commonTest tendo consciência do trade-off entre ter testes de UI rodando em nível unit e não instrumentado vs ter testes de UI rodando instrumentado apenas no Android. Minha ideia é ter validação de qualidade de forma automática também no iOS e apoiar qualquer verificação instrumentada utilizando testes manuais direto nos aparelhos Google Pixel 7 Pro e iPhone Air 17.

## Detekt fora do projeto
A compatibilidade do detekt com Kotlin 2.x só veio nas versões alpha. As versões estáveis não possuem esse suporte, então opto por deixar de fora essa lib e manter apenas o ktlint.

## Usar portrait no AndroidManifest
Decidi manter a definição de portrait na MainActivity pelo AndroidManifest mesmo com o warning novo do Android Studio/Lint indicando que travar orientação dessa forma deixou de ser a direção recomendada por causa das mudanças do Android 16 em telas grandes.

## Compose Stability
Mantive os modelos de domain livres de qualquer dependência de androidx (UI), removendo @Immutable da implementação. Decidi usar Compose Stability para que o Compose pule recomposição dos @Composables que recebem esses modelos diretamente fora de um UiState (Immutable), como RankingEntryCard, QuestionCard, FinalScorePanel, ChallengeModeOption e PlayingContent.