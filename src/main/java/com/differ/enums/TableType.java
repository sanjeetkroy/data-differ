package com.differ.enums;

import com.differ.constants.Queries;

public enum TableType {
    TABLE_1("table1","Sid", Queries.TABLE_1),
    TABLE_2("table1","Sid", Queries.TABLE_2);

    String name;
    String pk;
    String query;

    TableType(String name,String pk, String query) {
        this.name = name;
        this.pk = pk;
        this.query = query;
    }

    public String getTableName() {
        return name;
    }

    public String getPk() {
        return pk;
    }

    public String getQuery() {
        return query;
    }
}
