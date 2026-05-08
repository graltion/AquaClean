package com.graltion.aquaclean.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.graltion.aquaclean.R
import com.graltion.aquaclean.ui.Screen

@Composable
fun DrawerMenu(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    val items = listOf(
        Screen.Main.route to R.string.nav_home,
        Screen.Settings.route to R.string.nav_settings,
        Screen.About.route to R.string.nav_about,
        Screen.Terms.route to R.string.nav_terms,
        Screen.Privacy.route to R.string.nav_privacy,
        Screen.Licenses.route to R.string.nav_licenses,
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.75f)
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "AquaClean",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "by Graltion Corporation",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

        Spacer(modifier = Modifier.height(16.dp))

        items.forEachIndexed { index, (route, labelRes) ->
            val isSelected = currentRoute == route

            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigate(route)
                        onClose()
                    }
                    .padding(vertical = 14.dp)
            )

            if (index == 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}
