/**
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welab.wefe.board.service.api.project.dataset;

import com.welab.wefe.board.service.dto.entity.project.data_set.ProjectDataSetOutputModel;
import com.welab.wefe.board.service.service.ProjectDataSetService;
import com.welab.wefe.common.enums.DataSetType;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.fieldvalidate.annotation.Check;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.AbstractApiInput;
import com.welab.wefe.common.web.dto.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zane.luo
 */
@Api(path = "project/data_set/list", name = "list all of the project data sets")
public class ListApi extends AbstractApi<ListApi.Input, ListApi.Output> {

    @Autowired
    private ProjectDataSetService projectDataSetService;

    @Override
    protected ApiResult<Output> handle(Input input) throws StatusCodeWithException {
        List<ProjectDataSetOutputModel> list = projectDataSetService.list(input.projectId, input.dataSetType, input.memberId);
        return success(new Output(list));
    }

    public static class Input extends AbstractApiInput {
        @Check(name = "项目Id", require = true)
        private String projectId;

        @Check(name = "数据集类型")
        private DataSetType dataSetType;

        @Check(name = "成员Id", desc = "当此参数为空时，返回项目中所有数据集")
        private String memberId;


        //region getter/setter

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }


        public DataSetType getDataSetType() {
            return dataSetType;
        }

        public void setDataSetType(DataSetType dataSetType) {
            this.dataSetType = dataSetType;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        //endregion
    }

    public static class Output {
        private List<ProjectDataSetOutputModel> list;

        public Output(List<ProjectDataSetOutputModel> list) {
            this.list = list;
        }

        public List<ProjectDataSetOutputModel> getList() {
            return list;
        }

        public void setList(List<ProjectDataSetOutputModel> list) {
            this.list = list;
        }
    }
}
