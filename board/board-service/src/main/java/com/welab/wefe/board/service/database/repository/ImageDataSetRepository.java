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
package com.welab.wefe.board.service.database.repository;

import com.welab.wefe.board.service.database.entity.data_set.ImageDataSetMysqlModel;
import com.welab.wefe.board.service.database.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author zane
 * @date 2021/11/10
 */
@Repository
public interface ImageDataSetRepository extends BaseRepository<ImageDataSetMysqlModel, String> {
    @Query(value = "select count(*) from #{#entityName} where name=?1", nativeQuery = true)
    int countByName(String name);
}
