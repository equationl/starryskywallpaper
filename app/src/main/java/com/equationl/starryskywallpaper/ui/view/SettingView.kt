package com.equationl.starryskywallpaper.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.storage.datastore.rememberDataStoreBooleanSettingState
import com.alorma.compose.settings.storage.datastore.rememberDataStoreFloatSettingState
import com.alorma.compose.settings.ui.SettingsSlider
import com.alorma.compose.settings.ui.SettingsSwitch
import com.equationl.starryskywallpaper.utills.*

@Composable
fun MainSetting() {
    val isUsingRandom = rememberDataStoreBooleanSettingState(key = isUsingRandomKey, defaultValue = false)
    val starNum = rememberDataStoreFloatSettingState(key = starNumKey, defaultValue = 20f)
    val meteorVelocity = rememberDataStoreFloatSettingState(key = meteorVelocityKey, defaultValue = 10f)
    val meteorLength = rememberDataStoreFloatSettingState(key = meteorLengthKey, defaultValue = 500f)
    val meteorStrokeWidth = rememberDataStoreFloatSettingState(key = meteorStrokeWidthKey, defaultValue = 1f)
    val isUsingMeteorRandomAngle = rememberDataStoreBooleanSettingState(key = isUsingMeteorRandomAngleKey, defaultValue = false)
    val meteorAngle = rememberDataStoreFloatSettingState(key = meteorAngleKey, defaultValue = 45f)
    val isUsingMeteorRandomScaleTime = rememberDataStoreBooleanSettingState(key = isUsingMeteorRandomScaleTimeKey, defaultValue = false)
    val meteorScaleTime = rememberDataStoreFloatSettingState(key = meteorScaleTimeKey, defaultValue = 500f)
    val meteorRunningTime = rememberDataStoreFloatSettingState(key = meteorRunningTimeKey, defaultValue = 300f)


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SettingsSwitch(
            state = isUsingRandom,
            title = {
                Text(text = "随机生成")
            },
            subtitle = {
                Text(text = "开启后壁纸每次启动都会随机生成背景星空和流星")
            },
            icon = {
                Icon(imageVector = Icons.Rounded.Quiz, contentDescription = null)
            }
        )

        Divider(Modifier.padding(bottom = 8.dp))

        SettingsSlider(
            state = starNum,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "星星数量")
                    Text(text = starNum.value.toInt().toString(), modifier = Modifier.padding(end = 4.dp))
                }
            },
            valueRange = 0f..500f,
            icon = {
                Icon(imageVector = Icons.Rounded.Pin, contentDescription = null)
            }
        )

        Divider(Modifier.padding(bottom = 8.dp))

        SettingsSwitch(
            state = isUsingMeteorRandomAngle,
            title = {
                Text(text = "随机流星角度")
            },
            subtitle = {
                Text(text = "开启后流星划过的角度将每次都随机生成")
            },
            icon = {
                Icon(imageVector = Icons.Rounded.Quiz, contentDescription = null)
            }
        )

        SettingsSlider(
            state = meteorAngle,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "流星划出角度")
                    Text(text = meteorAngle.value.toInt().toString(), modifier = Modifier.padding(end = 4.dp))
                }
            },
            enabled = !isUsingMeteorRandomAngle.value,
            valueRange = 0f..360f,
            icon = {
                Icon(imageVector = Icons.Rounded.AutoMode, contentDescription = null)
            }
        )

        Divider(Modifier.padding(bottom = 8.dp))

        SettingsSwitch(
            state = isUsingMeteorRandomScaleTime,
            title = {
                Text(text = "随机生成流星间隔时间")
            },
            subtitle = {
                Text(text = "开启后生成流星的间隔时间将随机")
            },
            icon = {
                Icon(imageVector = Icons.Rounded.Quiz, contentDescription = null)
            }
        )

        SettingsSlider(
            state = meteorScaleTime,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "流星生成间隔时间")
                    Text(text = meteorScaleTime.value.toInt().toString(), modifier = Modifier.padding(end = 4.dp))
                }
            },
            enabled = !isUsingMeteorRandomScaleTime.value,
            valueRange = 1f..3000f,
            icon = {
                Icon(imageVector = Icons.Rounded.HourglassTop, contentDescription = null)
            }
        )

        SettingsSlider(
            state = meteorRunningTime,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "流星运行时间")
                    Text(text = meteorRunningTime.value.toInt().toString(), modifier = Modifier.padding(end = 4.dp))
                }
            },
            valueRange = 1f..1000f,
            icon = {
                Icon(imageVector = Icons.Rounded.Schedule, contentDescription = null)
            }
        )

        Divider(Modifier.padding(bottom = 8.dp))

        SettingsSlider(
            state = meteorVelocity,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "流星速度")
                    Text(text = meteorVelocity.value.toInt().toString(), modifier = Modifier.padding(end = 4.dp))
                }
            },
            valueRange = 1f..80f,
            icon = {
                Icon(imageVector = Icons.Rounded.Speed, contentDescription = null)
            }
        )

        SettingsSlider(
            state = meteorLength,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "流星拖尾长度（0表示无限长）")
                    Text(text = meteorLength.value.toInt().toString(), modifier = Modifier.padding(end = 4.dp))
                }
            },
            valueRange = 0f..1000f,
            icon = {
                Icon(imageVector = Icons.Rounded.Straighten, contentDescription = null)
            }
        )

        SettingsSlider(
            state = meteorStrokeWidth,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(text = "流星宽度")
                    Text(text = meteorStrokeWidth.value.toInt().toString(), modifier = Modifier.padding(end = 4.dp))
                }
            },
            valueRange = 1f..20f,
            icon = {
                Icon(imageVector = Icons.Rounded.WidthNormal, contentDescription = null)
            }
        )
    }
}