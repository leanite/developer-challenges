package com.leanite.dynaquiz.core.data.network

import kotlinx.serialization.json.Json

/*
* Quando Koin entrar:** isso vira `single<Json> { Json { ... } }` no `coreModule`
* e os consumidores recebem `Json` por construtor, eliminando este top-level val.
*/
internal val DynaquizJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}