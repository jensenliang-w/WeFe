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
package com.welab.wefe.common.web.api_document;

import com.welab.wefe.common.web.api_document.model.ApiItem;

/**
 * @author zane
 * @date 2021/12/3
 */
public class HtmlFormatter extends AbstractApiDocumentFormatter {
    @Override
    public String contentType() {
        return null;
    }

    @Override
    protected void formatApiItem(ApiItem item) {

    }

    @Override
    protected void formatGroupItem(String name) {

    }

    @Override
    protected String getOutput() {
        return null;
    }
}
