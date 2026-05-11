## Q&A

Como se gerencia dependências no KMP?
> A forma de gerenciar dependências é a mesma. Cataloga em `gradle/libs.versions.toml` e declara o uso em `composeApp/build.gradle.kts`

Quem é darwin em `iosMain.dependencies { implementation(libs.ktor.client.darwin) }`?
> A lib Darwin usada pelo Ktor no iOS é o mecanismo nativo de cliente HTTP do Kotlin/Native que utiliza a infraestrutura de rede da Apple, especificamente a NSURLSession

Qual a lib que monitora e reporta crashes no KMP?
> Sentry KMP

Onde coloco os resources do projeto Android?
> androidMain/res

## TBD

**Splash nativa no Android:**
- Lib androidx-core-splashscreen
- Criar theme
- Substituir theme na Activity launcher
- `installSplashScreen()` antes de `super.onCreate()` na Activity launcher 

**Modificadores `expect` e `actual`:**
Expect: declara assinatura (nome, params, retorno) sem corpo. Cada source set de plataforma é **OBRIGADO** a fornecer um actual correspondente, senão o build falha. Funciona como um "contrato" multiplatforma.

Actual: repete a assinatura do expect (precisa bater exatamente — params, retorno, modificadores) e fornece o corpo. Manter o mesmo nome dos arquivo nos source sets facilita pra IDE encontrar, mas não é obrigatório.

No caso da configuração do client http:
> Compose app
```kotlin
internal expect fun httpClientEngine(): HttpClientEngine
```

> Android app
```kotlin
internal actual fun httpClientEngine(): HttpClientEngine = OkHttp.create()
```

> iOS app
```kotlin
internal actual fun httpClientEngine(): HttpClientEngine = Darwin.create()
```

## iOS

O gerenciamento de "resources" (chamados Assets) é feito em `Assets.xcassets`

Info.plist é um arquivo de metadados configurações fundamentais de um aplicativo, funcionando como um dicionário de pares chave-valor

A cor de background da ViewController launcher tem seu próprio asset em`LaunchBackground.colorset/` e seu conteúdo em `Content.json`

## Aprendizagem futura:

1. Como se comporta um projeto multi módulo no KMP?
2. É possível fazer `build-logic`/convention plugins em arquitetura multi módulo no KMP?
3. O que acontece com o app quando muda de orientação no KMP/iOS?
