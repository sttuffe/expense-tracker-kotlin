package com.sttuffe.expensetracker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    // 삭제 대기 임시 변수
    var transactionToDelete by remember { mutableStateOf<TransactionLog?>(null) }

    // 삭제 확인 다이얼로그
    if (transactionToDelete != null) {
        DeleteDialog(
            onConfirm = {
                transactionToDelete?.let { viewModel.deleteTransaction(it) }
                transactionToDelete = null
            },
            onDismiss = {
                transactionToDelete = null
            }
        )
    }

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
            items(
                items = transactions,
                key = { transaction -> transaction.id }
            ) { item ->
                TransactionRow(
                    log = item,
                    onDeleteClick = {
                        transactionToDelete = item
                    })
            }
        }
    }
}

@Composable
fun TransactionRow(
    log: TransactionLog,
    onDeleteClick: () -> Unit
) {
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

            // 삭제 버튼
            IconButton(
                modifier = Modifier
//                    .padding(start = 8.dp)
                    .size(36.dp),
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete)
                )
            }
        }
        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
    }
}

@Composable
fun DeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.transactionDelete)) },
        text = { Text(text = stringResource(R.string.deleteConfirm)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    stringResource(R.string.delete),
                    color = Color(0xFFB71C1C)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun ListScreenPreview() {
    ListScreen({})
}
