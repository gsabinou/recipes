package br.com.fiap.recipes.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.recipes.R
import br.com.fiap.recipes.navigation.Destination
import br.com.fiap.recipes.ui.theme.RecipesTheme

@Composable
fun InitialScreen(navController: NavController){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ){
        TopEndCard(modifier = Modifier.align(Alignment.TopEnd))
        BottomStartCard(modifier = Modifier.align(Alignment.BottomStart))
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp) // isso também podia
                // ter sido inserido apenas passando o dp que ele iria aplicar a todos os lados
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.cooking),
                contentDescription = "Imagem de uma mulher cozinhando",
                modifier = Modifier
                    .size(190.dp)
            )
            Spacer(modifier = Modifier.height(100.dp)) // usado para fazer espaçamento, alternativa para padding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(200.dp) se n especificado o tamanho, a column se adequa ao conteúdo
            ) {
                Text(
                    text = stringResource(R.string.unlimited_recipes),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 16.dp)
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Button(
                        onClick = {
                            navController
                                .navigate(
                                    Destination.LoginScreen.route
                                )
                        },
                        colors = ButtonDefaults
                            .buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.tertiary
                        ),
                        modifier = Modifier
                            .size(width = 128.dp, height = 48.dp)
                        // 48 dp é a altura mínima para componentes clicáveis.

                    ) {
                        Text(
                            text = stringResource(R.string.button_login),
                            color = MaterialTheme.colorScheme.onPrimary, // essas classe "onPrimary" é usada on the componentes que estiverem dentro ou cobertos pela cor primary
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            navController
                                .navigate(
                                Destination.SignUpScreen.route
                            )
                        },
                        colors = ButtonDefaults
                            .buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary
                            ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
//                            .width(128.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.button_sign_up),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

        }
    }
}

@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
fun InitialScreenPreview() {
    RecipesTheme {
        InitialScreen(rememberNavController())
    }
}