package com.differ.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.differ.constants.Constants.LINE;
import static com.differ.constants.Constants.TAB;

@Service
public class DifferService {


    @Value("${result.base.path}")
    private String resultPath;

    public String diff(String source1, Map<String, Object> map1, String source2, Map<String, Object> map2) {

        String rowStart = getTab(2) + "<tr>\n";
        String rowEnd = getTab(2) + "</tr>\n";

        StringBuilder tds = new StringBuilder();
        StringBuilder row2Td = new StringBuilder();

        tds.append(getTab(3) + "<td class=\"source\">" + source1 + "</td>").append(LINE);
        row2Td.append(getTab(3) + "<td class=\"source\">" + source2 + "</td>").append(LINE);

        for (String key : map1.keySet()) {
            Object file1Value = map1.get(key);
            Object file2Value = map2==null || map1.isEmpty() ? "": map2.get(key);

            if (file1Value.equals(file2Value)) {
                tds.append(getTab(3) + "<td rowspan=\"2\" class=\"green\">" + file1Value + "</td>");
            } else {
                tds.append(getTab(3) + "<td class=\"red\">" + file1Value + "</td>");
                row2Td.append(getTab(3) + "<td class=\"red\">" + file2Value + "</td>");
                row2Td.append(LINE);
            }
            tds.append(LINE);
        }

        String row1 = rowStart + tds.toString() + rowEnd;
        String row2 = rowStart + row2Td.toString() + rowEnd;


        return row1 + row2;
    }

    public String getTab(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> TAB)
                .collect(Collectors.joining(""));

    }

    public String getHeaderRow(List<String> headers) {
        String row = getTab(2) + "<tr id=\"header\">";

        String tds = headers.stream()
                .map(h -> getTab(3) + "<td>" + h + "</td>")
                .collect(Collectors.joining(LINE));

        return row + LINE + tds + LINE + getTab(2) + "</tr>" + LINE;
    }

    public String getElement(String type, int colLength) {
        int colWidth = Math.round(100 / colLength);
        String style = "<style>table#demotable {table-layout: fixed ;width: 100% ;border-collapse: collapse ;border: 1px black solid ;}table#demotable td {width: COL_WIDTH% ;border: 1px black solid ;padding: 5px ;overflow: scroll;}table#demotable caption {font-style: italic ;}#header{background-color: lightgray;}.green{background-color: lightgreen;}.red{background-color: orangered;}.source{font-weight:bolder;color:forestgreen;}</style>";
        style = style.replace("COL_WIDTH", String.valueOf(colWidth));

        String top1 = "<!DOCTYPE html>\n<html>\n<head>\n";
        String top2 = "</head>\n<body>\n\t<table id=\"demotable\">\n";
        String bottom = "\t</table>\n</body>\n</html>";

        if (type.equals("top")) {
            return top1 + style + top2;
        } else if (type.equals("style")) {
            return style;
        } else {
            return bottom;
        }
    }

    public void saveHtml(String html, String filePrefix) {

        if(resultPath==null || resultPath.isEmpty()){
            resultPath = System.getProperty("user.dir") + "/output/result";
        }

        String dir = new SimpleDateFormat("yyyy_MM_dd_HH").format(new Date());
        String fileName = filePrefix + "_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_SSS'.html'").format(new Date());
        Path filePath = Path.of(resultPath, dir, fileName);

        try {
            Files.createDirectories(Path.of(resultPath, dir));
            Files.writeString(filePath, html, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
