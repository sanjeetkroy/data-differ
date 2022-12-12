package com.differ.dao;

import com.differ.enums.SourceType;
import com.differ.enums.TableType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GenericDao {

    private JdbcTemplate jdbcTemplateMysql;
    private JdbcTemplate jdbcTemplateAurora;

    public GenericDao(JdbcTemplate jdbcTemplateMysql, JdbcTemplate jdbcTemplateAurora) {
        this.jdbcTemplateMysql = jdbcTemplateMysql;
        this.jdbcTemplateAurora = jdbcTemplateAurora;
    }

    public List<Map<String, Object>> getData(SourceType sourceType, TableType tableType) {
        String query = tableType.getQuery();
        if (sourceType == SourceType.MYSQL)
            return jdbcTemplateMysql.queryForList(query);

        return jdbcTemplateAurora.queryForList(query);
    }
}
