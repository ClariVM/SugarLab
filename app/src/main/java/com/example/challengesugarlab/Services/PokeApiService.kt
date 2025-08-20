package com.example.challengesugarlab.Services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Modelos de datos que representan la estructura JSON recibida
data class Pokemon(
    val name: String,                      // Nombre del Pokémon
    val sprites: Sprites,                  // URLs de imágenes del Pokémon
    val stats: List<Stat>,                 // Lista de estadísticas (ataque, defensa, etc)
    val types: List<TypeSlot>              // Lista de tipos (agua, fuego, etc)
)

data class Sprites(val front_default: String)             // URL de la imagen frontal
data class Stat(val base_stat: Int, val stat: StatName)   // Valor numérico de la estadística
data class StatName(val name: String)                     // Nombre de la estadística
data class TypeSlot(val type: TypeName)                   // Nombre, e.g. "attack"
data class TypeName(val name: String)                     // Nombre del tipo, e.g. "fire"

// API Service: Interfaz que define las llamadas a la API
interface PokeApiService {
    @GET("pokemon/{id}")                                // Llamada GET a endpoint "pokemon/{id}"
    // Función suspend para obtener un Pokémon por su ID
    suspend fun getPokemon(@Path("id") id: Int): Pokemon
}

// Singleton de Retrofit
object RetrofitInstance {
    val api: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")               // URL base de la API
            .addConverterFactory(GsonConverterFactory.create()) //Convierte JSON a objetos Kotlin
            .build()
            .create(PokeApiService::class.java)                 // Crear implementación del servicio
    }
}
