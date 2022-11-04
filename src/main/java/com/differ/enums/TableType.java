package com.differ.enums;

public enum TableType {
    PLUGINS("plugins"),
    PLUGIN_VERSIONS("plugin_versions");

    String name;

    TableType(String name) {
        this.name = name;
    }

    public String getTableName() {
        return name;
    }
}
