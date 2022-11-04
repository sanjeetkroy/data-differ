package com.differ.service;

import com.differ.dao.GenericDao;
import com.differ.enums.SourceType;
import com.differ.enums.TableType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GenericService {

    private GenericDao genericDao;

    public GenericService(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    public List<Map<String, Object>> getData(SourceType sourceType, TableType tableType) {
        return genericDao.getData(sourceType, tableType);
    }
}
