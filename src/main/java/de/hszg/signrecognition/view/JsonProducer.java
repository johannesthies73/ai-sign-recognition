package de.hszg.signrecognition.view;

import com.google.gson.Gson;
import de.hszg.signrecognition.evaluation.entity.ChartData;
import de.hszg.signrecognition.evaluation.entity.Series;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JsonProducer {


    public static String getChartDataAsString(ChartData chartData) {

        List<List<Object>> dataRows = new ArrayList<>();

        for (Series series : chartData.getSeriesList()) {
            dataRows.add(Arrays.asList(series.getName(), series.getAverageValue(), series.getLowerBound(), series.getUpperbound()));
        }
        dataRows.stream().sorted();
        return new Gson().toJson(dataRows);
        }
}
