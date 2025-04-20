package com.example.mrfish

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    onMarkerClick: (String) -> Unit = {}
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                // Настройка OSM
                Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osm", Context.MODE_PRIVATE))
                setTileSource(TileSourceFactory.MAPNIK)

                // Начальная позиция (Москва)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(55.7558, 37.6173))

                // Добавляем маркер
                Marker(this).apply {
                    position = GeoPoint(55.7558, 37.6173)
                    title = "Красная площадь"
                    setOnMarkerClickListener { _, _ ->
                        onMarkerClick(title)
                        true
                    }
                }.also { overlays.add(it) }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}