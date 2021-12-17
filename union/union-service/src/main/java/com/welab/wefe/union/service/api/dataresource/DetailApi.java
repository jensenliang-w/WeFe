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

package com.welab.wefe.union.service.api.dataresource;

import com.welab.wefe.common.StatusCode;
import com.welab.wefe.common.data.mongodb.dto.dataresource.DataResourceQueryOutput;
import com.welab.wefe.common.data.mongodb.repo.BloomFilterMongoReop;
import com.welab.wefe.common.data.mongodb.repo.ImageDataSetMongoReop;
import com.welab.wefe.common.data.mongodb.repo.TableDataSetMongoReop;
import com.welab.wefe.common.enums.DataResourceType;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.web.api.base.AbstractApi;
import com.welab.wefe.common.web.api.base.Api;
import com.welab.wefe.common.web.dto.ApiResult;
import com.welab.wefe.union.service.dto.dataresource.ApiDataResourceDetailInput;
import com.welab.wefe.union.service.dto.dataresource.ApiDataResourceQueryOutput;
import com.welab.wefe.union.service.mapper.BloomFilterMapper;
import com.welab.wefe.union.service.mapper.ImageDataSetMapper;
import com.welab.wefe.union.service.mapper.TableDataSetMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yuxin.zhang
 **/
@Api(path = "data_resource/detail", name = "data_resource_detail", rsaVerify = false, login = false)
public class DetailApi extends AbstractApi<ApiDataResourceDetailInput, ApiDataResourceQueryOutput> {

    @Autowired
    protected BloomFilterMongoReop bloomFilterMongoReop;
    @Autowired
    protected ImageDataSetMongoReop imageDataSetMongoReop;
    @Autowired
    protected TableDataSetMongoReop tableDataSetMongoReop;

    protected TableDataSetMapper tableDataSetMapper = Mappers.getMapper(TableDataSetMapper.class);
    protected ImageDataSetMapper imageDataSetMapper = Mappers.getMapper(ImageDataSetMapper.class);
    protected BloomFilterMapper bloomFilterMapper = Mappers.getMapper(BloomFilterMapper.class);


    @Override
    protected ApiResult<ApiDataResourceQueryOutput> handle(ApiDataResourceDetailInput input) throws StatusCodeWithException {
        DataResourceQueryOutput dataResourceQueryOutput;
        switch (input.getDataResourceType()) {
            case BloomFilter:
                dataResourceQueryOutput = bloomFilterMongoReop.findCurMemberCanSee(input.getDataResourceId(), input.getCurMemberId());
                break;
            case TableDataSet:
                dataResourceQueryOutput = tableDataSetMongoReop.findCurMemberCanSee(input.getDataResourceId(), input.getCurMemberId());
                break;
            case ImageDataSet:
                dataResourceQueryOutput = imageDataSetMongoReop.findCurMemberCanSee(input.getDataResourceId(), input.getCurMemberId());
            default:
                throw new StatusCodeWithException(StatusCode.INVALID_PARAMETER, "dataResourceType");
        }

        return success(getOutput(dataResourceQueryOutput));
    }

    protected ApiDataResourceQueryOutput getOutput(DataResourceQueryOutput dataResourceQueryOutput) {
        if (dataResourceQueryOutput == null) {
            return null;
        }
        if (dataResourceQueryOutput.getDataResourceType().compareTo(DataResourceType.TableDataSet) == 0) {
            return tableDataSetMapper.transferDetail(dataResourceQueryOutput);
        } else if (dataResourceQueryOutput.getDataResourceType().compareTo(DataResourceType.ImageDataSet) == 0) {
            return imageDataSetMapper.transferDetail(dataResourceQueryOutput);
        } else if (dataResourceQueryOutput.getDataResourceType().compareTo(DataResourceType.BloomFilter) == 0) {
            return bloomFilterMapper.transferDetail(dataResourceQueryOutput);
        } else {
            return null;
        }
    }

}
