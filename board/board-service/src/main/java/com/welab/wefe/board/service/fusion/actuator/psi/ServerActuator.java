package com.welab.wefe.board.service.fusion.actuator.psi;

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


import com.welab.wefe.common.util.JObject;
import com.welab.wefe.fusion.core.actuator.psi.PsiServerActuator;
import com.welab.wefe.fusion.core.utils.bf.BloomFilters;

import java.math.BigInteger;
import java.util.List;

/**
 * @author hunter.zhao
 */
public class ServerActuator extends PsiServerActuator {
    public ServerActuator(String businessId, BloomFilters bloomFilters, BigInteger N, BigInteger e, BigInteger d) {
        super(businessId, bloomFilters, N, e, d);
    }

    @Override
    public void dump(List<JObject> fruit) {

    }

    @Override
    public void close() throws Exception {

    }
}
