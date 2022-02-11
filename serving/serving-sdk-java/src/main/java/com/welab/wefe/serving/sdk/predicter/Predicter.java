/*
 * Copyright 2021 Tianmian Tech. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.welab.wefe.serving.sdk.predicter;

import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.serving.sdk.dto.PredictResult;
import com.welab.wefe.serving.sdk.model.BaseModel;
import com.welab.wefe.serving.sdk.processor.AbstractModelProcessor;

/**
 * @author hunter.zhao
 */
public interface Predicter {

    /**
     * Predict interface
     * @return PredictResult
     * @throws Exception Exception
     */
    PredictResult predict() throws Exception;

    /**
     *  get Model
     * @return BaseModel
     * @throws StatusCodeWithException
     */
    BaseModel getModel() throws StatusCodeWithException;


    /**
     * Feature engineering treatment
     */
    void featureEngineering();

    /**
     * processor
     * @return Model Processor
     */
    AbstractModelProcessor getProcessor();
}
