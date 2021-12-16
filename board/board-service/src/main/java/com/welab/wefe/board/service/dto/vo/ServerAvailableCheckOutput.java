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
package com.welab.wefe.board.service.dto.vo;

import java.util.List;

/**
 * @author zane
 * @date 2021/12/16
 */
public class ServerAvailableCheckOutput {
    public boolean available;
    public List<ServerCheckPointOutput> list;

    public ServerAvailableCheckOutput() {
    }

    public ServerAvailableCheckOutput(List<ServerCheckPointOutput> list) {
        this.list = list;
        this.available = list.stream().allMatch(x -> x.isSuccess());
    }
}
