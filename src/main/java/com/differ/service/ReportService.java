package com.differ.service;

import com.differ.enums.SourceType;
import com.differ.enums.TableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.differ.constants.Constants.COUNT;
import static com.differ.constants.Constants.SOURCE;

@Service
public class ReportService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private DifferService differService;
    private GenericService genericService;


    public ReportService(DifferService differService, GenericService genericService) {
        this.differService = differService;
        this.genericService = genericService;
    }

    public void generate() {

        Arrays.stream(TableType.values())
                .forEach(this::generatePluginsReport);
    }

    public void generatePluginsReport(TableType tableType){
        logger.info("---------- Report Generation Starts for {} ---------------", tableType);
        List<Map<String,Object>> rowsMysql = genericService.getData(SourceType.MYSQL, tableType);
        List<Map<String,Object>> rowsAurora = genericService.getData(SourceType.AURORA, tableType);
        generateWithResult(rowsMysql, rowsAurora, tableType);
        logger.info("************** Report Generation Completes for {} **************", tableType);

    }

    public void generateWithResult(List<Map<String,Object>> rowsMysql, List<Map<String,Object>> rowsAurora, TableType tableType){
        List<String> headers = new ArrayList<>();
        StringBuilder tds = new StringBuilder();
        headers.add(SOURCE);

        Map<String, Map<String,Object>> mysql = new HashMap<>();
        Map<String, Map<String,Object>> aurora = new HashMap<>();

        if(rowsMysql != null && !rowsMysql.isEmpty()){
            List<String> columns = rowsMysql.get(0).keySet().stream().collect(Collectors.toList());
            headers.addAll(columns);

            logger.info("Headers : {}", headers);

            rowsMysql.stream()
                    .forEach(e -> mysql.put((String) e.get(tableType.getPk()), e));
        }

        if(rowsAurora != null && !rowsAurora.isEmpty()){
            rowsAurora.stream()
                    .forEach(e -> aurora.put((String) e.get(tableType.getPk()), e));
        }

        logExtraRowsId(mysql.keySet(), aurora.keySet());

        if(mysql.size() >= aurora.size()){
            for(Map.Entry<String,Map<String,Object>> entry: mysql.entrySet()) {
                Map<String,Object> source = entry.getValue();
                Map<String,Object> target = aurora.get(entry.getKey()) == null ? Collections.emptyMap() : aurora.get(entry.getKey());
                String td = differService.diff(SourceType.MYSQL.name(), source,SourceType.AURORA.name(), target);
                tds.append(td);
            }
        }else{
            for(Map.Entry<String,Map<String,Object>> entry: aurora.entrySet()) {
                Map<String,Object> target = entry.getValue();
                Map<String,Object> source = mysql.get(entry.getKey()) == null ? Collections.emptyMap() : mysql.get(entry.getKey());
                String td = differService.diff(SourceType.MYSQL.name(), source,SourceType.AURORA.name(), target);
                tds.append(td);
            }
        }

        if(!differService.isAllMatch()){
            logger.info("Found Some issue with table : {}.................", tableType);
        }
        String countRow = getCountTr(SourceType.MYSQL, rowsMysql.size(), SourceType.AURORA, rowsAurora.size());
        saveReport(headers, countRow , tds.toString(), tableType.getTableName());
        differService.resetStatus();
    }

    public void logExtraRowsId(Set<String> mysql, Set<String> aurora){
        if(mysql.size() > aurora.size()){
            logger.info("MYSQL has more rows than AURORA, MYSQL: {} AURORA: {}", mysql.size(),aurora.size());

            List<String> extra = mysql.stream()
                    .filter(e->!aurora.contains(e))
                    .collect(Collectors.toList());

            logger.info("Extra Ids: {}", extra);
        } else if(mysql.size() < aurora.size()){
            logger.info("AURORA has more rows than MYSQL, MYSQL: {} AURORA: {}", mysql.size(),aurora.size());
            List<String> extra = aurora.stream()
                    .filter(e->!mysql.contains(e))
                    .collect(Collectors.toList());
            logger.info("Extra Ids: {}", extra);
        } else{
            logger.info("AURORA and MYSQL has same rows, MYSQL: {} AURORA: {}", mysql.size(),aurora.size());
        }
    }

    private void saveReport(List<String> headers, String countRow , String tds, String filePrefix) {
        int colLength = headers.size();
        String html = differService.getElement("top", colLength) +
                countRow +
                differService.getHeaderRow(headers) +
                tds +
                differService.getElement("bottom", colLength);

        differService.saveHtml(html, filePrefix);
    }

    private String getCountTr(SourceType source1, int size1, SourceType source2, int size2){
        String head = differService.getHeaderRow( List.of(SOURCE, COUNT));
        String td = differService.diff(source1.name(), Map.of(COUNT, size1),source2.name(), Map.of(COUNT, size2));

        return head + td;
    }
}
