package com.differ.service.factory;

import com.differ.enums.TableType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.differ.constants.Queries.*;

@Component
public class TableQueryFactory {

    private Map<TableType, String> tableQueryMap;

    public String getQuery(TableType tableType){

        if(tableQueryMap==null || tableQueryMap.isEmpty()){
            tableQueryMap = new HashMap<>();
            tableQueryMap.put(TableType.PLUGINS, GET_ALL_PLUGINS);
            tableQueryMap.put(TableType.PLUGIN_VERSIONS, GET_ALL_PLUGIN_VERSIONS);
        }

        return tableQueryMap.get(tableType);
    }
}
