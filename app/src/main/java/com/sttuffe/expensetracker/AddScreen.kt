package com.sttuffe.expensetracker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen() {
    // TODO: 임시 변수 제거
    var amount by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    // TODO: 수입/지출 선택 임시 플래그
    var isIncomeSelected by remember { mutableStateOf(true) }

    val focusManagerForKeyboard = LocalFocusManager.current
    // 날짜 선택 다이얼 플래그, 키보드 포커스 유사
    var showDatePicker by remember { mutableStateOf(false) }

    // 날짜 선택기=DatePicker에서 사용됨
    // 초기 날짜 설정 = curretnTime밀리초
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    // 날짜는 밀리초 값 -> yyyy년 mm월 dd일 파싱
    val selectedDateText = remember(datePickerState.selectedDateMillis) {
        val millis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
        convertMillisToDateString(millis)
    }

    // 달력 다이얼, picker 플래그가 참일 때만 플로팅 동작
    // 플래그는 다이얼의 확인/취소, 날짜 영역 TextField 누를 때
    if (showDatePicker) {
        DatePickerDialog(
            // 포커스 동작
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.dismiss))
                }
            }
        ) {
            //다이얼에 datePicker 채움
            DatePicker(state = datePickerState)
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(text = stringResource(R.string.addTransaction))
        },
        bottomBar = {
            BottomBar(
                text = stringResource(R.string.toAdd),
                onClick = {/*TODO*/ }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            // 줄 간격
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 유형
            InputRow(label = stringResource(R.string.type)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // '수입'
                    TypeButton(
                        text = stringResource(R.string.income),
                        color = Color(0xFF01579B),
                        isSelected = isIncomeSelected,
                        onClick = { isIncomeSelected = true },
                        modifier = Modifier.weight(1f)
                    )
                    // '지출'
                    TypeButton(
                        text = stringResource(R.string.spending),
                        color = Color(0xFFB71C1C),
                        isSelected = !isIncomeSelected,
                        onClick = { isIncomeSelected = false },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 날짜
            InputRow(label = stringResource(R.string.date)) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedDateText,
                        onValueChange = {},
                        readOnly = true,//없으면 키보드 동작함
                        trailingIcon = {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Default.DateRange, // 달력 아이콘
                                contentDescription = "날짜 선택"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 텍스트 필드 위의 투명 터치 영역
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
                    )
                }
            }
            // 금액
            InputRow(label = stringResource(R.string.amount)) {
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    placeholder = { Text(stringResource(R.string.amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 내용
            InputRow(label = stringResource(R.string.content)) {
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text(stringResource(R.string.contentExam)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun InputRow(
    label: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(60.dp)
        )
        // 라벨과 입력창 사이 간격
        Spacer(modifier = Modifier.width(10.dp))

        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}

// 수입/지출 선택 버튼
@Composable
fun TypeButton(
    text: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) color else Color.White
    val contentColor = if (isSelected) Color.White else color
    val borderColor = if (isSelected) Color.Transparent else color

    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        // 버튼 테두리
        border = if (isSelected) null else BorderStroke(
            1.dp,
            borderColor
        ),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier.height(48.dp)
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddScreenPreview() {
    AddScreen()
}

private fun convertMillisToDateString(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    return Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(formatter)
}