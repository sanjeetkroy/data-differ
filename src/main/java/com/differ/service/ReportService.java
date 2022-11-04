package com.differ.service;

import com.differ.enums.SourceType;
import com.differ.enums.TableType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.differ.constants.Constants.SOURCE;

@Service
public class ReportService {

    private DifferService differService;
    private GenericService genericService;


    public ReportService(DifferService differService, GenericService genericService) {
        this.differService = differService;
        this.genericService = genericService;
    }

    public void generate() {
        generatePluginsReport(TableType.PLUGINS);
        generatePluginsReport(TableType.PLUGIN_VERSIONS);
    }

    public void generatePluginsReport(TableType tableType) {
        List<Map<String, Object>> rowsMysql = genericService.getData(SourceType.MYSQL, tableType);
        List<Map<String, Object>> rowsAurora = genericService.getData(SourceType.AURORA, tableType);
        generateWithMap(rowsMysql, rowsAurora, tableType);

    }

    public void generateWithMap(List<Map<String, Object>> rowsMysql, List<Map<String, Object>> rowsAurora, TableType tableType) {
        List<String> headers = new ArrayList<>();
        StringBuilder tds = new StringBuilder();
        headers.add(SOURCE);

        if (rowsMysql != null && !rowsMysql.isEmpty()) {
            List<String> columns = rowsMysql.get(0).keySet().stream().collect(Collectors.toList());
            headers.addAll(columns);

            int rows = rowsMysql.size();

            for (int i = 0; i < rows; i++) {
                Map<String, Object> mysqlRow = rowsMysql.get(i);
                Map<String, Object> auroraRow = rowsAurora == null || rowsAurora.isEmpty() ? null : rowsAurora.get(i);

                String td = differService.diff(SourceType.MYSQL.name(), mysqlRow, SourceType.AURORA.name(), auroraRow);
                tds.append(td);
            }
        }
        saveReport(headers, tds.toString(), tableType.getTableName());
    }

    private void saveReport(List<String> headers, String tds, String filePrefix) {
        int colLength = headers.size();
        String html = differService.getElement("top", colLength) +
                differService.getHeaderRow(headers) +
                tds +
                differService.getElement("bottom", colLength);

        differService.saveHtml(html, filePrefix);
    }
}
