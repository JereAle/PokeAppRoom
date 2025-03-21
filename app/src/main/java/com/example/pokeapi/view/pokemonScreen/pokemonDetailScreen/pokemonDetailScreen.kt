package com.example.pokeapi.view.pokemonScreen.pokemonDetailScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.rememberAsyncImagePainter
import com.example.pokeapi.ui.theme.PixelifySans
import com.example.pokeapi.ui.theme.principal
import com.example.pokeapi.ui.theme.secundario
import com.example.pokeapi.ui.theme.tertario
import com.example.pokeapi.view.abilityScreen.AbilityViewModel
import com.example.pokeapi.view.abilityScreen.abilityDetailScreen.AbilityDetailScreen1
import com.example.pokeapi.view.abilityScreen.abilityDetailScreen.AbilityDetailScreen2
import com.example.pokeapi.view.pokemonScreen.PokeViewModel

@Composable
fun PokemonDetailScreen(viewModel: PokeViewModel,abilityViewModel: AbilityViewModel, name: String, paddingValues: PaddingValues) {

    val isLoading by viewModel.isLoading.observeAsState(initial = true)

    LaunchedEffect(name) {
        viewModel.fetchPokemonDetail(name)
    }
// Interfaz de la pantalla
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Centrar el indicador de carga
    ) {
        if (isLoading) {
            // Mostrar el indicador de carga mientras los datos se cargan
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            // Renderizar el contenido cuando los datos están disponibles
            DetailScreen(viewModel,abilityViewModel,name,paddingValues)
            }
        }


}

@Composable
fun DetailScreen(viewModel: PokeViewModel,abilityViewModel: AbilityViewModel, name: String, paddingValues: PaddingValues){
    val pokemonDetail by viewModel.pokemonDetail.collectAsState()
    val pokemonImage = viewModel.pokemonImage.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(principal)
    ) {
        pokemonDetail?.let { pokemonDetail ->
            val height = pokemonDetail.height * 10
            val weight = (pokemonDetail.weight * 100) / 1000
            val ability1 = pokemonDetail.abilities[0].ability.name
            val ability2 = pokemonDetail.abilities[1].ability.name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(width = 4.dp, color = secundario),
                    colors = CardDefaults.cardColors(tertario)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = pokemonDetail.name.uppercase(),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontFamily = PixelifySans,
                            style = MaterialTheme.typography.titleLarge,

                            )
                        Spacer(modifier = Modifier.height(8.dp))
                        pokemonDetail?.let {
                            Text(
                                text = "Types: ${it.types.joinToString { type -> type.type.name.uppercase() }}",
                                fontWeight = FontWeight.Light,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "Base Experience: ${pokemonDetail.experience} xp",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Height: ${height} cm",
                            textAlign = TextAlign.Justify,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Weight: ${weight} kg",
                            textAlign = TextAlign.Justify,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Card(
                    modifier = Modifier.size(125.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(width = 4.dp, color = secundario),
                    colors = CardDefaults.cardColors(tertario)
                ) {

                    Image(
                        painter = rememberAsyncImagePainter(pokemonImage?.sprites?.imageFront),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,

                        modifier = Modifier
                            .size(150.dp)
                            .fillMaxSize(),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(width = 4.dp, color = secundario),
                    colors = CardDefaults.cardColors(tertario)
                ) {

                    LazyColumn (modifier = Modifier.padding(16.dp)) {
                        items(1){

                            Text(
                                text = "ABILITIES: ",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = PixelifySans,

                                )
                            AbilityDetailScreen1(abilityViewModel, ability1)
                            AbilityDetailScreen2(abilityViewModel, ability2)

                            AudioPlayerScreen(pokemonDetail.cries.latest)

                        }

                    }

                }
            }
        }
    }
}

@Composable
fun AudioPlayerScreen(audioUrl: String) {
    // Contexto necesario para crear el ExoPlayer
    val context = LocalContext.current

    // Estado del ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    // Actualizar el MediaItem cuando cambia la URL
    LaunchedEffect(audioUrl) {
        val mediaItem = MediaItem.fromUri(audioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    // Liberar el ExoPlayer cuando la composición se elimina
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Interfaz del usuario
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Botón de Play/Reset
        Button(
            onClick = {
                if (exoPlayer.isPlaying) {
                    exoPlayer.seekTo(0) // Reiniciar al principio
                } else {
                    exoPlayer.seekTo(0)
                    exoPlayer.play() // Reproducir
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = secundario)
        ) {
            Text(text = "Cries out", color = Color.White, fontFamily = PixelifySans)
        }
    }
}