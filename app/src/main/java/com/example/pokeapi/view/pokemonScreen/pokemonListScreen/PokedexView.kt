package com.example.pokeapi.view.pokemonScreen.pokemonListScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokeapi.data.local.entities.PokeData
import com.example.pokeapi.data.model.Resource
import com.example.pokeapi.ui.theme.principal
import com.example.pokeapi.view.pokemonScreen.PokeViewModel

@Composable
fun PokeListView(
    pokeViewModel: PokeViewModel,
    paddingValues: PaddingValues,
    navController: NavController
) {
    val searchQuery = remember { mutableStateOf("") }

    AllPokemonScreen(pokeViewModel, searchQuery, paddingValues, navController)
}

@Composable
fun AllPokemonScreen(
    viewModel: PokeViewModel,
    searchQuery: MutableState<String>,
    paddingValues: PaddingValues,
    navController: NavController
) {

    val allPokemon by viewModel.allPokemon.collectAsState()

    when (allPokemon) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp), color = Color.Gray
                )
            }
        }
        is Resource.Success -> {
            val pokeList = (allPokemon as Resource.Success<List<PokeData>>).data


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                PokemonSearchScreen(searchQuery)
                LazyColumn() {
                    if (pokeList != null) {
                        items(pokeList.size) { poke ->
                            if (searchQuery.value.isNotEmpty()) {
                                val pokemon = pokeList[poke]

                                if (pokemon.name.contains(
                                        searchQuery.value,
                                        ignoreCase = true
                                    )
                                ) {
                                    RenderPokemon(pokemon, navController)
                                }


                            } else {
                                val pokemon = pokeList[poke]
                                RenderPokemon(pokemon, navController)
                            }
                        }
                    }
                }
            }

        }

        is Resource.Error -> {
            (allPokemon as Resource.Error).message?.let {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(principal)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = it, textAlign = TextAlign.Center, color = Color.White)
                }
            }
        }
    }

}

@Composable
fun RenderPokemon(pokeData: PokeData, navController: NavController) {

    ListItem(
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("pokemonDetail/${pokeData.name}") }) {
                Row {
                    Column {
                        Text(
                            text = pokeData.name.uppercase(),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                )
            }
        }
    )

}

@Composable
fun PokemonSearchScreen(searchQuery: MutableState<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            label = { Text("Enter Pokémon") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
