/**
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welab.wefe.data.fusion.service.api.dataset;

import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.enums.ColumnDataType;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.util.ListUtil;
import com.welab.wefe.common.util.StringUtil;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.data.fusion.service.database.entity.DataSetColumnOutputModel;
import com.welab.wefe.data.fusion.service.database.entity.DataSetMySqlModel;
import com.welab.wefe.data.fusion.service.database.entity.DataSourceMySqlModel;
import com.welab.wefe.data.fusion.service.enums.DataResourceSource;
import com.welab.wefe.data.fusion.service.manager.JdbcManager;
import com.welab.wefe.data.fusion.service.service.dataset.DataSetService;
import com.welab.wefe.data.fusion.service.service.DataSourceService;
import com.welab.wefe.data.fusion.service.utils.AbstractDataSetReader;
import com.welab.wefe.data.fusion.service.utils.CsvDataSetReader;
import com.welab.wefe.data.fusion.service.utils.ExcelDataSetReader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Jakcy.jiang
 */
@Api(path = "data_set/preview", name = "Preview the dataset file")
public class PreviewApi extends AbstractApi<PreviewApi.Input, PreviewApi.Output> {

    private static final Pattern MATCH_INTEGER_PATTERN = Pattern.compile("^-?\\d{1,9}$");
    private static final Pattern MATCH_LONG_PATTERN = Pattern.compile("^-?\\d{10,}$");
    private static final Pattern MATCH_DOUBLE_PATTERN = Pattern.compile("^-?\\d+\\.\\d+$");

    @Autowired
    DataSourceService dataSourceService;

    @Autowired
    private DataSetService dataSetService;

    @Autowired
    protected DataSource dataSource;

    @Override
    protected ApiResult<Output> handle(Input input) throws StatusCodeWithException {
        DataResourceSource dataResourceSource = input.getDataResourceSource();
        Output output = new Output();
        if (dataResourceSource == null) {
            DataSetMySqlModel dataSetMySqlModel = dataSetService.findById(input.getId());
            if (dataSetMySqlModel == null) {
                throw new StatusCodeWithException(StatusCode.DATA_NOT_FOUND, "Data not available");
            }

            boolean isStoraged = dataSetMySqlModel.isStoraged();
            String rows = dataSetMySqlModel.getRows();
            List<String> rows_list = Arrays.asList(rows.split(","));

            if (isStoraged) {
                String tbName = "data_fusion_" + dataSetMySqlModel.getId();
//                String sql = "Select * from " + tbName;
                try {
                    output = readFromDB(input.sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else if (dataResourceSource.equals(DataResourceSource.UploadFile) || dataResourceSource.equals(DataResourceSource.LocalFile)) {
            File file = dataSetService.getDataSetFile(input.getDataResourceSource(), input.getFilename());
            try {
                output = readFile(file);
            } catch (IOException e) {
                LOG.error(e.getClass().getSimpleName() + " " + e.getMessage(), e);
                throw new StatusCodeWithException(StatusCode.SYSTEM_ERROR, "File reading failure");
            }
        } else if (dataResourceSource.equals(DataResourceSource.Sql)) {

//            DataSourceMySqlModel dataSourceMySqlModel = dataSourceService.getDataSourceById(input.id);
//            String sql = "select * from " + dataSourceMySqlModel.getDatabaseName();
            // Test whether SQL can be queried normally
            output = readFromSoruceDB(input.id, input.sql);
        }

        return success(output);
    }

    /**
     * Parse the dataset file
     */
    private Output readFile(File file) throws IOException, StatusCodeWithException {


        Output output = new Output();
        Map<String, DataSetColumnOutputModel> metadata = new LinkedHashMap<>();


        // Method to consume the first row of a column
        Consumer<List<String>> headRowConsumer = row -> {

            output.header.addAll(row);

            for (String name : output.header) {
                DataSetColumnOutputModel column = new DataSetColumnOutputModel();
                column.setName(name);
                metadata.put(name, column);
            }

        };

        // Data line consumer
        DataRowConsumer dataRowConsumer = new DataRowConsumer(metadata, output);


        AbstractDataSetReader reader = file.getName().endsWith("csv")
                ? new CsvDataSetReader(file)
                : new ExcelDataSetReader(file);

        try {
            // Obtain column head
            headRowConsumer.accept(reader.getHeader());
            // Read data row
            reader.read(dataRowConsumer, 10000, 25_000);
        } finally {
            reader.close();
        }

        output.setMetadataList(new ArrayList<>(metadata.values()));


        return output;
    }

    /**
     * Data line consumer
     */
    private static class DataRowConsumer implements Consumer<Map<String, Object>> {

        private Map<String, DataSetColumnOutputModel> metadata;
        private Output output;

        private boolean allColumnKnowDataType = false;


        public DataRowConsumer(Map<String, DataSetColumnOutputModel> metadata, Output output) {
            this.metadata = metadata;
            this.output = output;
        }

        @Override
        public void accept(Map<String, Object> x) {
            // The front end only previews 10 lines of data, and too many screens freeze.
            if (output.rawDataList.size() < 10) {
                output.rawDataList.add(x);
            }

            if (allColumnKnowDataType) {
                return;
            }

            // Inferred data type
            boolean hasUnkonow = true;
            for (String name : output.header) {

                DataSetColumnOutputModel column = metadata.get(name);
                if (column.getDataType() == null) {

                    Object value = x.get(name);
                    ColumnDataType dataType = inferDataType(String.valueOf(value));

                    if (dataType != null) {
                        column.setDataType(dataType);
                    } else {
                        hasUnkonow = true;
                    }
                }
            }

            if (!hasUnkonow) {
                allColumnKnowDataType = true;
            }

        }

        /**
         * Inferred data type
         */
        private ColumnDataType inferDataType(String value) {
            if (StringUtil.isEmpty(value)) {
                return null;
            }

            if (MATCH_DOUBLE_PATTERN.matcher(value).find()) {
                return ColumnDataType.Double;
            }

            if (MATCH_LONG_PATTERN.matcher(value).find()) {
                return ColumnDataType.Long;
            }

            if (MATCH_INTEGER_PATTERN.matcher(value).find()) {
                return ColumnDataType.Integer;
            }

            return ColumnDataType.String;
        }
    }

    private Output readFromDB(String sql) throws StatusCodeWithException, SQLException {


        JdbcManager jdbcManager = new JdbcManager();
        Connection conn = dataSource.getConnection();
        // The total number of rows based on the query statement
        long rowCountFromDB = jdbcManager.count(conn, sql);

        // Gets the data set column header
        List<String> header = jdbcManager.getRowHeaders(conn, sql);
        if (header.stream().distinct().count() != header.size()) {
            throw new StatusCodeWithException("The dataset contains duplicate fields. Please handle and re-upload.", StatusCode.PARAMETER_VALUE_INVALID);
        }

        // Convert capital Y to lowercase Y
        header = header.stream().map(x -> "Y".equals(x) ? "y" : x).collect(Collectors.toList());

        boolean containsY = header.contains("y");
        int yIndex = header.indexOf("y");

        if (containsY) {
            ListUtil.moveElement(header, yIndex, 1);
        }

        Output output = new Output();
        LinkedHashMap<String, DataSetColumnOutputModel> metadata = new LinkedHashMap<>();
        output.setHeader(header);

        for (String name : output.header) {
            DataSetColumnOutputModel column = new DataSetColumnOutputModel();
            column.setName(name);
            metadata.put(name, column);
        }

        // Data line consumer
        DataRowConsumer dataRowConsumer = new DataRowConsumer(metadata, output);

        jdbcManager.readWithFieldRow(conn, sql, dataRowConsumer, 10);


        output.setMetadataList(new ArrayList<>(metadata.values()));

        return output;
    }

    private Output readFromSoruceDB(String dataSourceId, String sql) throws StatusCodeWithException {
        DataSourceMySqlModel model = dataSourceService.getDataSourceById(dataSourceId);
        if (model == null) {
            throw new StatusCodeWithException("Data does not exist", StatusCode.DATA_NOT_FOUND);
        }

        JdbcManager jdbcManager = new JdbcManager();
        Connection conn = jdbcManager.getConnection(model.getDatabaseType(), model.getHost(), model.getPort()
                , model.getUserName(), model.getPassword(), model.getDatabaseName());

        // The total number of rows based on the query statement
        long rowCountFromDB = jdbcManager.count(conn, sql);

        // Gets the data set column header
        List<String> header = jdbcManager.getRowHeaders(conn, sql);
        if (header.stream().distinct().count() != header.size()) {
            throw new StatusCodeWithException("The dataset contains duplicate fields. Please handle and re-upload.", StatusCode.PARAMETER_VALUE_INVALID);
        }

        // Convert capital Y to lowercase Y
        header = header.stream().map(x -> "Y".equals(x) ? "y" : x).collect(Collectors.toList());

        // Gets the data set column header
        Output output = new Output();
        LinkedHashMap<String, DataSetColumnOutputModel> metadata = new LinkedHashMap<>();
        output.setHeader(header);

        for (String name : output.header) {
            DataSetColumnOutputModel column = new DataSetColumnOutputModel();
            column.setName(name);
            metadata.put(name, column);
        }

        // Data line consumer
        DataRowConsumer dataRowConsumer = new DataRowConsumer(metadata, output);

        jdbcManager.readWithFieldRow(conn, sql, dataRowConsumer, 10, header);


        output.setMetadataList(new ArrayList<>(metadata.values()));

        return output;
    }


    //region dto

    public static class Input extends AbstractApiInput {

        @Check(name = "数据id")
        private String id;

        private String filename;

        private DataResourceSource dataResourceSource;

        private String sql;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public DataResourceSource getDataResourceSource() {
            return dataResourceSource;
        }

        public void setDataResourceSource(DataResourceSource dataResourceSource) {
            this.dataResourceSource = dataResourceSource;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }
    }

    public static class Output {
        @Check(name = "字段列表")
        private List<String> header = new ArrayList<>();
        @Check(name = "元数据信息")
        private List<DataSetColumnOutputModel> metadataList = new ArrayList<>();
        @Check(name = "原始数据列表")
        private List<Map<String, Object>> rawDataList = new ArrayList<>();


        public List<String> getHeader() {
            return header;
        }

        public void setHeader(List<String> header) {
            this.header = header;
        }

        public List<Map<String, Object>> getRawDataList() {
            return rawDataList;
        }

        public void setRawDataList(List<Map<String, Object>> rawDataList) {
            this.rawDataList = rawDataList;
        }

        public List<DataSetColumnOutputModel> getMetadataList() {
            return metadataList;
        }

        public void setMetadataList(List<DataSetColumnOutputModel> metadataList) {
            this.metadataList = metadataList;
        }
    }


    //endregion
}
