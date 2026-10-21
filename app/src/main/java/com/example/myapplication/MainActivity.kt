package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodManagerApp()
        }
    }
}

data class FoodItem(
    val id: String,
    val name: String,
    val category: String,
    val price: String,
    val status: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodManagerApp() {
    // Trạng thái ngôn ngữ: "vi" là Tiếng Việt, "en" là Tiếng Anh
    var currentLang by remember { mutableStateOf("vi") }

    var foods by remember {
        mutableStateOf(
            listOf(
                FoodItem("1", "Thịt bò tươi nhập khẩu", "Thịt & Hải sản", "250.000 đ", "Còn hàng", Color(0xFFff6b6b)),
                FoodItem("2", "Rau cải xanh hữu cơ", "Rau củ quả", "35.000 đ", "Sắp hết", Color(0xFF51cf66))
            )
        )
    }
    var nameInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }
    var isOnline by remember { mutableStateOf(true) }

    // Từ điển ngôn ngữ
    val texts = mapOf(
        "vi" to mapOf(
            "title" to "🍏 Food Manager Pro",
            "subtitle" to "Mã SV: BIT240087",
            "langBtn" to "🌐 English",
            "statusOnline" to "Trạng thái: Online (Đã đồng bộ)",
            "statusOffline" to "Trạng thái: Offline",
            "formTitle" to "➕ Thêm món ăn mới",
            "namePlaceholder" to "Nhập tên thực phẩm...",
            "pricePlaceholder" to "Nhập giá tiền (VNĐ)...",
            "addBtn" to "Thêm vào kho",
            "listTitle" to "Danh sách thực phẩm kho",
            "deleteBtn" to "Xóa",
            "inStock" to "Còn hàng",
            "lowStock" to "Sắp hết"
        ),
        "en" to mapOf(
            "title" to "🍏 Food Manager Pro",
            "subtitle" to "Student ID: BIT240087",
            "langBtn" to "🌐 Tiếng Việt",
            "statusOnline" to "Status: Online (Synced)",
            "statusOffline" to "Status: Offline",
            "formTitle" to "➕ Add New Food",
            "namePlaceholder" to "Enter food name...",
            "pricePlaceholder" to "Enter price...",
            "addBtn" to "Add to Inventory",
            "listTitle" to "Food Inventory List",
            "deleteBtn" to "Delete",
            "inStock" to "In Stock",
            "lowStock" to "Low Stock"
        )
    )

    val t = texts[currentLang]!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(t["title"]!!, fontWeight = FontWeight.Bold, color = Color(0xFF2b8a3e))
                        Text(t["subtitle"]!!, fontSize = 12.sp, color = Color.Gray)
                    }
                },
                actions = {
                    // Nút đổi ngôn ngữ ở góc trên bên phải
                    TextButton(onClick = { currentLang = if (currentLang == "vi") "en" else "vi" }) {
                        Text(t["langBtn"]!!, fontWeight = FontWeight.Bold, color = Color(0xFF1a73e8))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFf8f9fa))
                .padding(16.dp)
        ) {
            // Thanh trạng thái mạng
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isOnline) Color(0xFFe6f4ea) else Color(0xFFfce8e6), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(if (isOnline) Color(0xFF34a853) else Color(0xFFea4335), CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isOnline) t["statusOnline"]!! else t["statusOffline"]!!,
                    color = if (isOnline) Color(0xFF137333) else Color(0xFFc5221f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Khung nhập liệu thêm món
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(t["formTitle"]!!, fontWeight = FontWeight.SemiBold, color = Color(0xFF3c4043))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        placeholder = { Text(t["namePlaceholder"]!!) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = priceInput,
                        onValueChange = { priceInput = it },
                        placeholder = { Text(t["pricePlaceholder"]!!) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (nameInput.isNotBlank() && priceInput.isNotBlank()) {
                                val newItem = FoodItem(
                                    id = System.currentTimeMillis().toString(),
                                    name = nameInput.trim(),
                                    category = "Thực phẩm kho",
                                    price = "$priceInput đ",
                                    status = t["inStock"]!!,
                                    color = Color(0xFF339af0)
                                )
                                foods = listOf(newItem) + foods
                                nameInput = ""
                                priceInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2b8a3e)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(t["addBtn"]!!, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("${t["listTitle"]} (${foods.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Danh sách món ăn
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(foods) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF202124))
                                Text("${item.category} • ${item.status}", fontSize = 12.sp, color = Color.Gray)
                                Text(item.price, fontWeight = FontWeight.Bold, color = Color(0xFF2b8a3e), fontSize = 14.sp)
                            }
                            Button(
                                onClick = {
                                    foods = foods.filter { it.id != item.id }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFfce8e6)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(t["deleteBtn"]!!, color = Color(0xFFc5221f), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}