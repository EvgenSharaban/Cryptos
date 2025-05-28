package com.jeksonsoftsolutions.cryptos.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeksonsoftsolutions.cryptos.R

@Composable
fun <T> LoadResultContent(
    loadResult: LoadResult<T>,
    content: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    onTryAgainAction: () -> Unit = {},
    // TODO if LoadResult.Error doesn't need delete it and everything connected with it (both lines below)
    exceptionToMessageMapper: ExceptionToMessageMapper = ExceptionToMessageMapper.DEFAULT
) {
    // TODO if LoadResult.Error doesn't need delete it and everything connected with it (both lines below)
    var countClicker by rememberSaveable { mutableIntStateOf(0) }
    val isCounterFull by remember { derivedStateOf { countClicker < 3 } }

    when (loadResult) {
        LoadResult.Loading -> Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        is LoadResult.Success -> content(loadResult.data)
        is LoadResult.Empty -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(loadResult.message),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        // TODO if LoadResult.Error doesn't need delete it and everything connected with it
        is LoadResult.Error -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = exceptionToMessageMapper.getUserMessage(
                        loadResult.exception,
                        LocalContext.current,
                    ),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                if (loadResult.exception is NoInternetException) {
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.connect_to_internet_and_try_again),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.size(16.dp))
                Button(
                    onClick = {
                        countClicker++
                        onTryAgainAction()
                    },
                    enabled = isCounterFull
                ) {
                    Text(stringResource(R.string.try_again))
                }
                Spacer(Modifier.size(16.dp))
                if (!isCounterFull) {
                    Text(
                        stringResource(R.string.try_again_later),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// TODO if LoadResult.Error doesn't need delete it and everything connected with it
fun interface ExceptionToMessageMapper {
    fun getUserMessage(exception: Exception, context: Context): String

    companion object {
        val DEFAULT = ExceptionToMessageMapper { exception, context ->
            when (exception) {
                is LoadDataException -> context.getString(R.string.failed_to_load_data_error)
                is NoInternetException -> exception.message ?: context.getString(R.string.network_unavailable_error)
                else -> context.getString(R.string.unknown_error)
            }
        }
    }
}