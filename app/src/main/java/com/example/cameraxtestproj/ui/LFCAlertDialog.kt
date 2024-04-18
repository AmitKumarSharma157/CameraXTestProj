
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun LFCAlertDialog(
    icon: ImageVector,
    dialogTitle: String,
    dialogText: String,
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit
) {
    if (isDialogOpen) {
        AlertDialog(
            icon = {
                Icon(icon, contentDescription = "Image Icon")
            },
            title = {
                Text(text = dialogTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = dialogText, textAlign = TextAlign.Center)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}