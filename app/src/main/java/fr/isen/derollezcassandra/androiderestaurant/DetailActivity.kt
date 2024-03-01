package fr.isen.derollezcassandra.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.derollezcassandra.androiderestaurant.network.Basket
import fr.isen.derollezcassandra.androiderestaurant.network.Dish



class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        setContent {
            if (dish != null) {
                DetailPage(dish)
            }
        }
    }

    companion object {
        const val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailPage(dish: Dish) {

    val context =  LocalContext.current
    val count = remember { mutableStateOf(1) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.purple_200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ingredients",
                fontFamily = FontFamily.Cursive,
                fontSize = 44.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            dish.ingredients.forEach {
                IngredientItem(ingredient = it.name)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.weight(1f))
                OutlinedButton(
                    onClick = {
                        count.value = kotlin.math.max(1, count.value - 1)
                        Toast.makeText(context, "Article supprimer", Toast.LENGTH_LONG).show()
                    }
                ) {
                    Text("-")
                }
                Text(count.value.toString())
                OutlinedButton(
                    onClick = {
                        count.value = count.value + 1
                        Toast.makeText(context, "Article ajouter", Toast.LENGTH_LONG).show()
                    }
                ) {
                    Text("+")
                }
                Spacer(Modifier.weight(1f))
            }
            Row {
                Button(
                    onClick = {
                        if (dish != null){
                            Basket.current(context).add(dish, count.value, context)
                        }
                    }
                ) {
                    Text("Commander")
                }
                Box {
                    Button(onClick = {
                        val intent = Intent(context, BasketActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Text("Voir mon panier")
                    }
                    Text(
                        text = Cart.itemCountInBasket.value.toString(),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(colorResource(id = R.color.purple_500), shape = CircleShape)
                            .padding(4.dp)
                    )
                }
            }

            HorizontalPager(
                state = rememberPagerState {
                    dish?.images?.count() ?: 0
                }
            ) { page ->
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxSize()
                ) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(dish?.images?.get(page))
                            .build(),
                        null,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10))
                            .size(320.dp)

                    )
                }
            }
        }
    }
}

@Composable
fun IngredientItem(ingredient: String) {
    Text(
        text = ingredient,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
