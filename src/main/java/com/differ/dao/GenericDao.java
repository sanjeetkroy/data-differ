package com.differ.dao;

import com.differ.constants.Queries;
import com.differ.enums.SourceType;
import com.differ.enums.TableType;
import com.differ.service.factory.TableQueryFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GenericDao {

    private JdbcTemplate jdbcTemplateMysql;
    private JdbcTemplate jdbcTemplateAurora;
    private TableQueryFactory tableQueryFactory;

    public GenericDao(JdbcTemplate jdbcTemplateMysql, JdbcTemplate jdbcTemplateAurora, TableQueryFactory tableQueryFactory) {
        this.jdbcTemplateMysql = jdbcTemplateMysql;
        this.jdbcTemplateAurora = jdbcTemplateAurora;
        this.tableQueryFactory = tableQueryFactory;
    }

    public List<Map<String, Object>> getData(SourceType sourceType, TableType tableType) {
        String query = tableQueryFactory.getQuery(tableType);
        if (sourceType == SourceType.MYSQL)
            return jdbcTemplateMysql.queryForList(query);

        return jdbcTemplateAurora.queryForList(query);
    }
}
