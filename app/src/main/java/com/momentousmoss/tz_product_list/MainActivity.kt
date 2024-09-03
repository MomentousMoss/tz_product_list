package com.momentousmoss.tz_product_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.momentousmoss.tz_product_list.database.AppDatabase
import com.momentousmoss.tz_product_list.database.Product
import com.momentousmoss.tz_product_list.utils.MutableSingleLiveEvent
import com.momentousmoss.tz_product_list.utils.convertStringToListString
import com.momentousmoss.tz_product_list.utils.dateStringToDateFormat
import com.momentousmoss.tz_product_list.utils.toProductEntity

class MainActivity : ComponentActivity() {

    private val searchLiveEvent = MutableSingleLiveEvent<Unit>()

    private val database by lazy {
        Room.databaseBuilder(baseContext, AppDatabase::class.java, "database")
            .createFromAsset("database.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainList()
        }
    }

    @Composable
    fun MainList() {
        val productsList = remember {
            mutableStateListOf<Product>()
        }
        val productsCursor = database.ProductsDao().getProducts()
        productsCursor.let {
            if (it.moveToFirst()) {
                do {
                    productsList.add(
                        Product(
                            id = it.getInt(0),
                            name = it.getString(1),
                            time = it.getLong(2),
                            tags = it.getString(3),
                            amount = it.getInt(4)
                        )
                    )
                } while (it.moveToNext())
            }
        }
        LazyColumn {
            item {
                SearchBar(productsList)
            }
            productsList.forEach {
                item {
                    ProductCardView(it)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBar(productsList: SnapshotStateList<Product>) {
        var searchText by rememberSaveable { mutableStateOf("") }
        searchLiveEvent.observeForever {
            searchBarAction(searchText, productsList)
        }
        SearchBar(
            query = searchText,
            onQueryChange = {
                searchText = it
                searchLiveEvent.call()
            },
            onSearch = {
                searchLiveEvent.call()
            },
            active = false,
            onActiveChange = {},
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "searchIcon")
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        searchText = ""
                        searchLiveEvent.call()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "closeIcon"
                    )
                }
            },
            placeholder = { Text("Поиск товаров") }
        ) {}
    }

    private fun searchBarAction(searchText: String, productsList: SnapshotStateList<Product>) {
        val searchProductsCursor = database.ProductsDao().getProductsBySearch(searchText)
        productsList.clear()
        searchProductsCursor.let {
            if (it.moveToFirst()) {
                do {
                    productsList.add(
                        Product(
                            id = it.getInt(0),
                            name = it.getString(1),
                            time = it.getLong(2),
                            tags = it.getString(3),
                            amount = it.getInt(4)
                        )
                    )
                } while (it.moveToNext())
            }
        }
    }

    @Composable
    fun DialogChange(product: Product, onDismissRequest: () -> Unit) {
        var productAmount by rememberSaveable { mutableIntStateOf(product.amount) }
        AlertDialog(
            icon = {
                Icon(Icons.Default.Settings, contentDescription = "dialogIcon")
            },
            title = {
                Text(text = "Количество товара")
            },
            text = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.button_minus),
                        contentDescription = "buttonMinus",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
                            .clickable {
                                if (productAmount > 0) {
                                    productAmount--
                                }
                            }
                    )
                    Text(
                        text = "$productAmount",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.button_plus),
                        contentDescription = "buttonPlus",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp)
                            .clickable {
                                productAmount++
                            }
                    )
                }
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        product.amount = productAmount
                        database.ProductsDao().updateProduct(product.toProductEntity())
                        searchLiveEvent.call()
                        onDismissRequest()
                    }
                ) {
                    Text("Принять")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    @Composable
    fun DialogDelete(productId: Int, onDismissRequest: () -> Unit) {
        AlertDialog(
            icon = {
                Icon(Icons.Default.Warning, contentDescription = "dialogIcon")
            },
            title = {
                Text(text = "Удаление товара")
            },
            text = {
                Text(text = "Вы действительно хотите удалить выбранный товар?")
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        database.ProductsDao().deleteById(productId)
                        searchLiveEvent.call()
                        onDismissRequest()
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Нет")
                }
            }
        )
    }

    @Composable
    fun ProductCardView(product: Product) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            ProductHeaderView(product)
            ProductTagsView(product.tags)
            ProductStatisticsView(product)
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun ProductHeaderView(product: Product) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProductTitleView(titleText = "${product.id}. ${product.name}")
            ButtonsView(product)
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun ProductTagsView(productTags: String) {
        FlowRow(
            modifier = Modifier.padding(start = 8.dp, top = 12.dp, end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val productTagsList = convertStringToListString(productTags)
            productTagsList.forEach {
                ProductTag(productTag = it)
            }
        }
    }

    @Composable
    fun ProductStatisticsView(product: Product) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProductAmountView(product = product)
            ProductTimeView(productTime = product.time)
        }
    }

    @Composable
    fun ProductTitleView(titleText: String) {
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
    fun ButtonsView(product: Product) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ButtonChangeAmount(product)
            ButtonDelete(product.id)
        }
    }

    @Composable
    fun ButtonChangeAmount(product: Product) {
        val showChangeDialog = remember { mutableStateOf(false) }
        Image(
            painter = painterResource(id = R.drawable.button_edit),
            contentDescription = "buttonChange",
            modifier = Modifier.clickable {
                showChangeDialog.value = !showChangeDialog.value
            }
        )
        if (showChangeDialog.value) {
            DialogChange(product, onDismissRequest = { showChangeDialog.value = false })
        }
    }

    @Composable
    fun ButtonDelete(productId: Int) {
        val showDeleteDialog = remember { mutableStateOf(false) }
        Image(
            painter = painterResource(id = R.drawable.button_delete),
            contentDescription = "buttonDelete",
            modifier = Modifier.clickable {
                showDeleteDialog.value = !showDeleteDialog.value
            }
        )
        if (showDeleteDialog.value) {
            DialogDelete(productId, onDismissRequest = { showDeleteDialog.value = false })
        }
    }

    @Composable
    fun ProductTag(productTag: String) {
        OutlinedCard(
            border = BorderStroke(0.dp, Color.Black),
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = TextStyle(fontSize = 12.sp, color = Color.Black),
                text = productTag
            )
        }
    }

    @Composable
    fun ProductAmountView(product: Product) {
        Column {
            Text(
                style = TextStyle(fontSize = 10.sp, color = Color.Black),
                text = "На складе"
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                style = TextStyle(
                    fontSize = 10.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Light
                ),
                text = if (product.amount < 1) "Нет в наличии" else "${product.amount}"
            )
        }

    }

    @Composable
    fun ProductTimeView(productTime: Long) {
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
                text = dateStringToDateFormat(productTime)
            )
        }
    }
}