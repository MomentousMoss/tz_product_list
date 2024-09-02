package com.momentousmoss.tz_product_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ItemCard()
        }
    }
}

@Preview
@Composable
fun ItemCard() {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        Header()
        Tags()
        Statistics()
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Header() {
    FlowRow(
        modifier = Modifier.fillMaxWidth() .padding(start = 8.dp, top = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
            ItemTitle(titleText = "iPhone")
            CardButtons()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Tags() {
    FlowRow(
        modifier = Modifier.padding(start = 8.dp, top = 12.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ItemTag(name = "Телефон")
        ItemTag(name = "Теasdлефон")
        ItemTag(name = "Телефон")
        ItemTag(name = "1фон")
        ItemTag(name = "ефон")
        ItemTag(name = "Теasasddлефон")
        ItemTag(name = "Телефон")
    }
}

@Composable
fun Statistics() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, top = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ItemCount(name = "15")
        ItemDate(name = "01.10.2021")
    }
}

@Composable
fun ItemTitle(titleText: String) {
    Text(
        style = TextStyle(
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        ),
        text = titleText
    )
}

@Composable
fun CardButtons() {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ButtonChangeCount()
        ButtonDelete()
    }
}

@Composable
fun ButtonChangeCount() {
    Image(
        painter = painterResource(id = R.drawable.button_edit),
        contentDescription = "buttonChange",
        modifier = Modifier.clickable {

        }
    )
}

@Composable
fun ButtonDelete() {
    Image(
        painter = painterResource(id = R.drawable.button_delete),
        contentDescription = "buttonDelete",
        modifier = Modifier.clickable {

        }
    )
}

@Composable
fun ItemTag(name: String) {
    OutlinedCard(
        border = BorderStroke(0.dp, Color.Black),
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = TextStyle(fontSize = 12.sp, color = Color.Black),
            text = name
        )
    }
}

@Composable
fun ItemCount(name: String) {
    Column {
        Text(
            style = TextStyle(fontSize = 10.sp, color = Color.Black),
            text = "На складе"
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            style = TextStyle(fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Light),
            text = name
        )
    }

}

@Composable
fun ItemDate(name: String) {
    Column {
        Text(
            style = TextStyle(fontSize = 10.sp, color = Color.Black),
            text = "Дата добавления"
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            style = TextStyle(
                fontSize = 10.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light
            ),
            text = name
        )
    }
}