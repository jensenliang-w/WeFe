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

package com.welab.wefe.serving.service.feature;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.serving.sdk.dto.PredictParams;

import java.util.Map;

/**
 * @author hunter.zhao
 */
public abstract class AbstractFeatureDataHandle {

    /**
     *
     * Access to features
     * @param modelId
     * @param predictParams
     * @return featureDataMap
     * @throws StatusCodeWithException
     */
    public abstract Map<String, Object> handle(String modelId, PredictParams predictParams) throws StatusCodeWithException;

    /**
     * Batch feature acquisition
     * @param modelId
     * @param predictParams
     * @return featureDataMap
     * @throws StatusCodeWithException
     */
    public abstract Map<String, Map<String, Object>> batch(String modelId, PredictParams predictParams) throws StatusCodeWithException;
}
