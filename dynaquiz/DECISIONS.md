## Uso de Channel em eventos one-shot

```kotlin
private val _events = Channel<SplashEvent>(Channel.BUFFERED)
val events = _events.receiveAsFlow()
```

 Padrão recomendado pela comunidade e pelo próprio Google para garantir que o evento seja consumido exatamente uma vez, mesmo com mudanças de configuração ou quando o app vai para background

 ## Intent iniciais em Screens

 Não costumo deixar intents serem executadas por default em um `ViewModel`. Gosto de, explicitamente, chamar uma intent usando um `onIntent()` inicial para ajudar na visibilidade futura do código, tentando diminuir o número de "mágicas" que ocorrem no código.