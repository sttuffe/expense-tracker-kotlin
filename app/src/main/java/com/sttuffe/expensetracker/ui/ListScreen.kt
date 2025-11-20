package com.sttuffe.expensetracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sttuffe.expensetracker.R
import com.sttuffe.expensetracker.TransactionViewModel
import com.sttuffe.expensetracker.data.TransactionLog
import com.sttuffe.expensetracker.data.TransactionType
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter

@Composable
fun ListScreen(
    onNavigateToAddScreen: () -> Unit,
    viewModel: TransactionViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(text = stringResource(R.string.transactions))
        },
        bottomBar = {
            BottomBar(
                text = stringResource(R.string.addTransaction),
                onClick = onNavigateToAddScreen
            )
        }
    ) { innerPadding ->
        //내역 리스트 표시 영역
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(transactions) { item ->
                TransactionRow(log = item)
            }
        }
    }
}

@Composable
fun TransactionRow(log: TransactionLog) {
    // 날짜 포맷
    val dateFormatter = DateTimeFormatter.ofPattern("MM.dd")
    val dateString = log.date.format(dateFormatter)

    // 금액 포맷
    val decimalFormat = DecimalFormat("#,###")
    val amountString = decimalFormat.format(log.amount) + "원"

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 날짜
            Text(
                text = dateString,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(end = 12.dp)
            )

            // 내용
            Text(
                text = log.content,
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, //  ... 처리
                modifier = Modifier.weight(1f) //
            )

            // 금액
            if (log.type == TransactionType.INCOME) {
                //수입
                Text(
                    text = "+ $amountString",
                    color = Color(0xFF01579B),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            } else {
                //지출
                Text(
                    text = "- $amountString",
                    color = Color(0xFFB71C1C),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun ListScreenPreview() {
    ListScreen({})
}
