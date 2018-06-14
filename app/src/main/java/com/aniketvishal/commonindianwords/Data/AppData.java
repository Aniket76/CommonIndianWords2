package com.aniketvishal.commonindianwords.Data;

import com.aniketvishal.commonindianwords.Models.AllWordsModel;
import com.aniketvishal.commonindianwords.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aniketvishal on 27/11/17.
 */

public class AppData {

    //-------------Main Words Category--------------
    public static List<AllWordsModel> getCategoryData(){
        List<AllWordsModel> data = new ArrayList<>();

                data.add(new AllWordsModel("Family Members", R.drawable.family));
                data.add(new AllWordsModel("Cricket",R.drawable.cricket));
                data.add(new AllWordsModel("Football",R.drawable.football));
                //data.add(new AllWordsModel("Other Sports", R.drawable.other_sports));
                data.add(new AllWordsModel("The Environment", R.drawable.the_environment));
                data.add(new AllWordsModel("Jobs", R.drawable.jobs));
                data.add(new AllWordsModel("Economic", R.drawable.economic));
                data.add(new AllWordsModel("Words For Emotions", R.drawable.words_of_emotions));
                data.add(new AllWordsModel("Parts Of Your Body", R.drawable.parts_of_body));
                data.add(new AllWordsModel("Time Adverbs", R.drawable.time_adverbs));
                data.add(new AllWordsModel("Health", R.drawable.health));
                data.add(new AllWordsModel("Personality Types", R.drawable.personality_types));
                data.add(new AllWordsModel("Periods Of Time", R.drawable.periods_of_time));
                data.add(new AllWordsModel("Academic Writing", R.drawable.academic));
                data.add(new AllWordsModel("Non-Count Nouns", R.drawable.noncount));
                data.add(new AllWordsModel("College And University Life", R.drawable.college_life));
                data.add(new AllWordsModel("Weather", R.drawable.weather));
                data.add(new AllWordsModel("Employment", R.drawable.jobs));

        return data;
    }

}
