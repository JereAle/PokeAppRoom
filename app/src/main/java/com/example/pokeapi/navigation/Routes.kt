package com.example.pokeapi.navigation

import com.example.pokeapi.R

sealed class Routes(
    val icon: Int,
    val title: String,
    val route: String
){
    object PokemonView: Routes(
        icon = R.drawable.pokedex,
        title = "Pokemon",
        route = "PokemonView"
    )
    object ItemView: Routes(
        icon = R.drawable.pokemonball,
        title = "Item",
        route = "ItemView"
    )
    object AbilityView: Routes(
        icon = R.drawable.abilities,
        title = "Ability",
        route = "AbilityView"
    )
}