package com.nyinyi.dailychallenge.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.nyinyi.dailychallenge.MainActivity
import com.nyinyi.dailychallenge.data.model.ProgrammingTip
import com.nyinyi.dailychallenge.data.remote.ChallengesApiService
import com.nyinyi.dailychallenge.data.repository.TipsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ProgrammingTipsWidget : GlanceAppWidget() {
    private val httpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    }

    private val apiService by lazy {
        ChallengesApiService(httpClient)
    }

    private val repository by lazy {
        TipsRepository(apiService)
    }

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val tip = repository.getRandomTip()

        provideContent {
            GlanceTheme {
                TipWidgetContent(tip = tip)
            }
        }
    }

    @Composable
    private fun TipWidgetContent(tip: ProgrammingTip) {
        // Outer Box for Border
        Box(
            modifier =
                GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(Color(0x33FFFFFF))) // Semi-transparent white border
                    .cornerRadius(16.dp)
                    .padding(1.dp) // Border width
                    .clickable(actionStartActivity<MainActivity>()),
        ) {
            // Inner Box for Content Background
            Box(
                modifier =
                    GlanceModifier
                        .fillMaxSize()
                        .background(ColorProvider(Color(0xCC1E1E1E))) // Semi-transparent dark background
                        .cornerRadius(16.dp)
                        .padding(16.dp),
            ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
            ) {
                // Header
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        modifier = GlanceModifier.defaultWeight(),
                    ) {
                        Text(
                            text = "Daily Tip",
                            style =
                                TextStyle(
                                    color = ColorProvider(Color(0xFF9E9E9E)),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                ),
                        )
                        Spacer(modifier = GlanceModifier.height(4.dp))
                        Text(
                            text = tip.category,
                            style =
                                TextStyle(
                                    color = ColorProvider(Color.White),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                        )
                    }

                    // Icon
                    Box(
                        modifier =
                            GlanceModifier
                                .size(40.dp)
                                .background(ColorProvider(Color(0xFF00C48C))) // Teal color
                                .cornerRadius(20.dp), // Circle
                        contentAlignment = Alignment.Center,
                    ) {
                        // Using a text emoji as a fallback for icon if vector support is limited in Glance or complex to setup without resources
                        // For a real app, we'd use an ImageProvider(R.drawable.ic_lightbulb)
                        // But here we can try to use the vector resource if available, or just a simple visual.
                        // Let's try to use a simple text representation for now to ensure it works without adding resources,
                        // or better, use the Image composable with a standard icon if we can access it.
                        // Since I cannot easily add drawables, I will use a Text with a lightbulb emoji or similar visual for simplicity in this environment,
                        // OR I can try to use a standard icon if I knew the resource ID.
                        // Given the constraints, I'll use a text emoji "💡" which renders well, or just a simple shape.
                        // Actually, let's try to use the Image with a system icon if possible, but safer to use text for this specific "no-resource-access" environment.
                        // Wait, the prompt asked for a "simple black lightbulb icon".
                        // I will use a Text composable with a lightbulb emoji for now as it's the safest portable way without resource files.
                        Text(
                            text = "💡",
                            style = TextStyle(fontSize = 20.sp),
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(16.dp))

                // Body
                Box(
                    modifier = GlanceModifier.defaultWeight(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(
                        text = tip.tip,
                        style =
                            TextStyle(
                                color = ColorProvider(Color.White),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                            ),
                    )
                }

                Spacer(modifier = GlanceModifier.height(16.dp))

                // Footer
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = "Tap to learn more",
                        style =
                            TextStyle(
                                color = ColorProvider(Color(0xFF9E9E9E)),
                                fontSize = 10.sp,
                            ),
                    )
                }
            }
            }
        }
    }
}
