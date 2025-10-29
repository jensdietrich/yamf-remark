package io.github.yamf.remark;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class JSONResultTableExporter implements ResultTableExporter {

    final static Logger LOG = LoggerFactory.getLogger(JSONResultTableExporter.class);

    @Override
    public void export(ResultTable table, Path file) throws IOException {
        JSONArray jsonResults = new JSONArray();
        table.rows().forEach(row -> {
            JSONArray array = new JSONArray();
            row.cells().forEach(cell -> {
                JSONObject cellJson = new JSONObject();
                cellJson.put("question",cell.question());
                cellJson.put("mark",cell.value());
                cellJson.put("possible-answers",toString(cell.allAnswers()));
                cellJson.put("correct-answers",toString(cell.correctAnswers()));
                cellJson.put("correctly-selected",toString(cell.correctAnswersSelected()));
                cellJson.put("incorrectly-selected",toString(cell.incorrectAnswersSelected()));
                array.put(cellJson);
            });
            JSONObject jsonRow = new JSONObject();
            jsonRow.put("id",row.id());
            jsonRow.put("results",array);
            jsonRow.put("total-marks",row.sum());
            jsonResults.put(jsonRow);
        });
        Files.writeString(file,jsonResults.toString(4));
        LOG.info("results written to {}", file);
    }

    private static String toString(Collection<String> list) {
        return list.stream().map(Object::toString).collect(Collectors.joining(",","{","}"));
    }
}
