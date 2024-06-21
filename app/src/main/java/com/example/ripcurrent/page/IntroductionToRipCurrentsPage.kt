package com.example.ripcurrent.page

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ripcurrent.R
import com.example.ripcurrent.Screens
import com.example.ripcurrent.tool.backHandler.BackHandlerPress
import com.example.ripcurrent.tool.custmozed.UdmImage


@Composable
fun IntroductionToRipCurrentsPage(modifier: Modifier, navController: NavHostController) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 200.dp, height = 100.dp)
            .padding(10.dp)
    ) {
        LazyColumn {
            item {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    UdmImage(imageResource = R.drawable.ripcurrent_example, width = 400.dp, height = 300.dp)
                }

                Text(text = stringResource(R.string.rip_current), fontSize = 30.sp)
                Text(
                    text = stringResource(R.string.it_is_a_kind_of_sea_current_that_carries_from_the_coast_to_the_sea_therefore_when_you_go_to_the_beach_to_play_in_the_water_you_should_first_observe_the_terrain_and_sea_surface_conditions_if_you_see_waves_on_both_sides_like_the_picture_above_but_a_relatively_calm_and_wave_free_area_in_the_middle_you_should_avoid_that_area_play_in_water_and_stay_away_as_much_as_possible),
                    Modifier.padding(horizontal = 20.dp)
                )
                Text(
                    text = stringResource(R.string.how_to_deal_with_being_carried_out_to_sea_by_rip_currents),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.error
                )
                Text(
                    text = stringResource(R.string.rip_currents_will_only_carry_people_away_from_the_coast_but_will_not_sweep_them_to_the_bottom_of_the_sea_if_you_take_enough_breath_the_human_body_will_float_and_you_can_save_yourself_by_doing_a_basic_jellyfish_float_don_t_swim_directly_to_shore_and_don_t_fight_the_current),
                    Modifier.padding(horizontal = 20.dp)
                )
                Text(
                    text = stringResource(R.string.self_rescue_method_take_it_to_the_open_sea_with_the_current_and_then_swim_back_to_the_shore_with_the_current),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

        }

    }
    BackHandlerPress( ){
        Log.i("9453","返回")
        navController.navigate(Screens.SettingPage.name)
    }
}