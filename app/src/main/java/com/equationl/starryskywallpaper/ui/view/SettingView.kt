package com.equationl.starryskywallpaper.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alorma.compose.settings.storage.datastore.rememberDataStoreBooleanSettingState
import com.alorma.compose.settings.storage.datastore.rememberDataStoreFloatSettingState
import com.alorma.compose.settings.ui.SettingsSlider
import com.alorma.compose.settings.ui.SettingsSwitch
import com.equationl.starryskywallpaper.R
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
                Text(text = stringResource(id = R.string.setting_random_generate_title))
            },
            subtitle = {
                Text(text = stringResource(id = R.string.setting_random_generate_subTitle))
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
                    Text(text = stringResource(id = R.string.setting_star_num_title))
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
                Text(text = stringResource(id = R.string.setting_random_meteor_angle_title))
            },
            subtitle = {
                Text(text = stringResource(id = R.string.setting_random_meteor_angle_subTitle))
            },
            icon = {
                Icon(imageVector = Icons.Rounded.Quiz, contentDescription = null)
            }
        )

        SettingsSlider(
            state = meteorAngle,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(id = R.string.setting_meteor_angle_title))
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
                Text(text = stringResource(id = R.string.setting_random_meteor_scaleTime_title))
            },
            subtitle = {
                Text(text = stringResource(id = R.string.setting_random_meteor_scaleTime_subTitle))
            },
            icon = {
                Icon(imageVector = Icons.Rounded.Quiz, contentDescription = null)
            }
        )

        SettingsSlider(
            state = meteorScaleTime,
            title = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(id = R.string.setting_meteor_scaleTime_title))
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
                    Text(text = stringResource(id = R.string.setting_meteor_runningTime_title))
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
                    Text(text = stringResource(id = R.string.setting_meteor_speed_title))
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
                    Text(text = stringResource(id = R.string.setting_meteor_length_title))
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
                    Text(text = stringResource(id = R.string.setting_meteor_width_title))
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