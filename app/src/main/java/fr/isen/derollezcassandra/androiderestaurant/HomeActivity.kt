package fr.isen.derollezcassandra.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.derollezcassandra.androiderestaurant.ui.theme.AndroidERestaurantTheme

enum class DishType {
    STARTER, MAIN, DESSERT;

    @Composable
    fun title(): String {
        return when(this) {
            STARTER -> stringResource(id = R.string.menu_starter)
            MAIN -> stringResource(id = R.string.menu_main)
            DESSERT -> stringResource(id = R.string.menu_dessert)
        }
    }
}

interface MenuInterface {
    fun dishPressed(dishType: DishType)
}



class HomeActivity : ComponentActivity(), MenuInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupView(this)
                }
            }
        }
        Log.d("lifeCycle", "Home Activity - OnCreate")
    }

    override fun dishPressed(dishType: DishType) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.CATEGORY_EXTRA_KEY,dishType)
        startActivity(intent)
    }

    override fun onPause(){
        Log.d("LifeCycle", "Menu Activity - OnPause")
        super.onPause()
    }

    override fun onResume(){
        super.onResume()
        Log.d("LifeCycle", "Menu Activity - OnResume")
    }
    override fun onDestroy() {
        Log.d("LifeCycle", "Menu Activity - OnDestroy")
        super.onDestroy()
    }
}



@Composable
fun SetupView(menu: MenuInterface) {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = colorResource(id = R.color.purple_200),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "RelAx'olotl",
                color = colorResource(id = R.color.black),
                fontFamily = FontFamily.Cursive,
                fontSize = 74.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            Image(painterResource(R.drawable.axolotl), null)
            CustomButton(type = DishType.STARTER, menu)
            Divider()
            CustomButton(type = DishType.MAIN, menu)
            Divider()
            CustomButton(type = DishType.DESSERT, menu)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = "⭐",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Restaurant étoilé de Cassandra Derollez",
                    color = colorResource(id = R.color.black),
                    fontSize = 10.sp,
                )
                Text(
                    text = "⭐",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
                }


        }
    }

}

@Composable fun CustomButton(type: DishType, menu: MenuInterface) {
    TextButton(
        onClick = { menu.dishPressed(type) },
        modifier = Modifier.padding(vertical = 15.dp),
    ) {
        Text(
            text = type.title(),
            fontSize = 30.sp,
            color = colorResource(id = R.color.black),
        )

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        SetupView(HomeActivity())
    }
}
