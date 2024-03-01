package fr.isen.derollezcassandra.androiderestaurant


import android.net.Network
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.derollezcassandra.androiderestaurant.network.Basket
import fr.isen.derollezcassandra.androiderestaurant.network.BasketItem



class BasketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketView()
        }
    }
}

@Composable fun BasketView() {
    val context = LocalContext.current
    val basketItems = remember {
        mutableStateListOf<BasketItem>()
    }
    Column {
        LazyColumn {
            items(basketItems) {
                BasketItemView(it, basketItems)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(elevation = 8.dp)
                .background(color = colorResource(id = R.color.purple_500))
        ) {
            OutlinedButton(onClick = {
                Toast.makeText(context, "Merci pour votre commande !", Toast.LENGTH_LONG).show()
            }){
                Text(
                    text = "Commander"
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                "${ basketItems.sumBy { (it.dish?.prices?.first()?.price?.toFloat() ?: 0f).toInt() * it.count }.toString()}€",
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(horizontal = 56.dp)

            )

        }
    }
    basketItems.addAll(Basket.current(context).items)
}

@Composable fun BasketItemView(item: BasketItem, basketItems: MutableList<BasketItem>) {
    Card {
        val context = LocalContext.current
        Card(
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(Modifier.padding(8.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp)
                        .clip(RoundedCornerShape(10))
                        .padding(8.dp)
                )
                Column(
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(8.dp)
                ) {
                    Text(
                        text = item.dish.name,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                    Text("${item.dish.prices.first().price} €")
                }

                Spacer(Modifier.weight(1f))
                Text(
                    item.count.toString(),
                    Modifier.align(alignment = Alignment.CenterVertically)
                )
                TextButton(onClick = {
                    // delete item and redraw view
                    Basket.current(context).delete(item, context)
                    basketItems.remove(item)
                    /*basketItems.clear()
                    basketItems.addAll(Basket.current(context).items)*/
                }) {
                    Text(
                        " X ",
                        color = (colorResource(id = R.color.purple_500)),
                        modifier = Modifier
                            .background(color = colorResource(id = R.color.purple_500))
                    )

                }
            }
        }
    }
}