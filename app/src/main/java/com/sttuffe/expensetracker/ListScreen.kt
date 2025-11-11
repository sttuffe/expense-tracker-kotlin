package com.sttuffe.expensetracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListScreen() {
    //TODO: testData
    val testData = List(30) { "testData $it" }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar()
        }
    ) { innerPadding ->
        //내역 리스트 표시 영역
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(testData) { item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    fontSize = 18.sp
                )
                // 구분선
                androidx.compose.material3.HorizontalDivider(
                    color = Color.LightGray
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ListScreenPreview() {
    ListScreen()
}
