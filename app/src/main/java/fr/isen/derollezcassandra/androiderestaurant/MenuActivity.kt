package fr.isen.derollezcassandra.androiderestaurant


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.android.volley.Request.Method
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.derollezcassandra.androiderestaurant.network.Category
import fr.isen.derollezcassandra.androiderestaurant.network.Dish
import fr.isen.derollezcassandra.androiderestaurant.network.MenuResult
import fr.isen.derollezcassandra.androiderestaurant.network.NetworkConstants
import org.json.JSONObject

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = (intent.getSerializableExtra(CATEGORY_EXTRA_KEY) as? DishType) ?: DishType.STARTER

        setContent{
            MenuView(type)
        }
    }
    override fun onPause(){
        super.onPause()
    }

    override fun onResume(){
        super.onResume()
    }

    override fun onDestroy(){
        super.onDestroy()
    }
    companion object{
        val CATEGORY_EXTRA_KEY = "CATEGORY_EXTRA_KEY"
    }
}

@Composable
fun MenuView(type: DishType) {
    val category = remember {
        mutableStateOf<Category?>(null)
    }
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.purple_200),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = type.title(),
                fontFamily = FontFamily.Cursive,
                fontSize = 26.sp, // Taille de la police
                textAlign = TextAlign.Center, // Centrage du texte
                modifier = Modifier.fillMaxWidth() // Pour étendre le texte sur toute la largeur
            )
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn {
                category.value?.let {
                    items(it.items) { dish ->
                        dishRow(dish)
                    }
                }
            }
        }
    }
    postData(type, category)
}

@Composable
fun dishRow(dish: Dish){
    val context = LocalContext.current
    Card (
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.DISH_EXTRA_KEY, dish)
                context.startActivity(intent)
            }
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dish.images.first())
                    .build(),
                null,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(10))
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    dish.name,
                    Modifier.align(Alignment.Start)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${dish.prices.first().price}€",
                    Modifier.align(Alignment.Start)
                )
            }
        }
    }
}


@Composable
fun postData(type: DishType, category: MutableState<Category?>){
    val currentCategory = type.title()
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val params = JSONObject()
    params.put(NetworkConstants.ID_SHOP, "1")

    val request = JsonObjectRequest(
        Method.POST,
        NetworkConstants.URL,
        params,
        {
            Log.d("request", it.toString(2))
            val result = GsonBuilder().create().fromJson(it.toString(), MenuResult::class.java)
            val filteredResult = result.data.first { it.name == currentCategory }
            category.value = filteredResult
            Log.d("request", "")
        },
        {
            Log.e("request", it.toString())
        }
    )
    queue.add(request)
}
