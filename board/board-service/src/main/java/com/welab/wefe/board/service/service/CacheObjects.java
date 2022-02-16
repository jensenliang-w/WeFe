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

package com.welab.wefe.board.service.service;

import com.welab.wefe.board.service.database.entity.AccountMysqlModel;
import com.welab.wefe.board.service.database.repository.AccountRepository;
import com.welab.wefe.board.service.database.repository.BlacklistRepository;
import com.welab.wefe.board.service.database.repository.data_resource.DataResourceRepository;
import com.welab.wefe.board.service.dto.globalconfig.MemberInfoModel;
import com.welab.wefe.board.service.sdk.union.UnionService;
import com.welab.wefe.board.service.sdk.union.dto.MemberBaseInfo;
import com.welab.wefe.board.service.service.globalconfig.GlobalConfigService;
import com.welab.wefe.common.Convert;
import com.welab.wefe.common.exception.StatusCodeWithException;
import com.welab.wefe.common.util.StringUtil;
import com.welab.wefe.common.web.Launcher;
import com.welab.wefe.common.wefe.enums.DataResourceType;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * Global cache
 * <p>
 * The unchanged or little changed data in the system is stored
 * to reduce the complexity of database query and coding.
 * <p>
 * cache list：
 * - login status
 * - local member info
 * - local data set tags
 * - local account list
 * - union some data
 * - ...
 *
 * @author Zane
 */
public class CacheObjects {


    private static long LAST_REFRESH_MEMBER_MAP_TIME = 0;

    private static String MEMBER_ID;
    private static String RSA_PRIVATE_KEY;
    private static String RSA_PUBLIC_KEY;
    private static String MEMBER_NAME;

    /**
     * Data resource tags
     * tag : count
     */
    private static final TreeMap<String, Long> DATA_RESOURCE_TAGS = new TreeMap<>();
    private static final TreeMap<String, Long> TABLE_DATA_SET_TAGS = new TreeMap<>();
    private static final TreeMap<String, Long> IMAGE_DATA_SET_TAGS = new TreeMap<>();
    private static final TreeMap<String, Long> BLOOM_FILTER_TAGS = new TreeMap<>();

    /**
     * accountId : nickname
     */
    private static final LinkedHashMap<String, String> ACCOUNT_MAP = new LinkedHashMap<>();

    /**
     * accountIds
     */
    private static final List<String> ACCOUNT_ID_LIST = new ArrayList<>();

    /**
     * memberId : member base info
     */
    private static LinkedHashMap<String, MemberBaseInfo> MEMBER_MAP = new LinkedHashMap<>();

    /**
     * member blacklist
     */
    private static final Set<String> MEMBER_BLACKLIST = new HashSet<>();

    public static Set<String> getMemberBlackList() {
        if (MEMBER_BLACKLIST.isEmpty()) {
            refreshMemberBlacklist();
        }
        return MEMBER_BLACKLIST;
    }

    public synchronized static void refreshMemberBlacklist() {
        BlacklistRepository repository = Launcher.getBean(BlacklistRepository.class);
        MEMBER_BLACKLIST.clear();
        repository.findAll().forEach(x -> MEMBER_BLACKLIST.add(x.getBlacklistMemberId()));
    }

    public static String getMemberId() {
        if (MEMBER_ID == null) {
            refreshMemberInfo();
        }
        return MEMBER_ID;
    }

    /**
     * 判断指定的 member_id 是属于当前本地成员
     */
    public static boolean isCurrentMember(String memberId) {
        return getMemberId().equals(memberId);
    }

    public static String getRsaPrivateKey() {
        if (RSA_PRIVATE_KEY == null) {
            refreshMemberInfo();
        }
        return RSA_PRIVATE_KEY;
    }

    public static String getRsaPublicKey() {
        if (RSA_PUBLIC_KEY == null) {
            refreshMemberInfo();
        }
        return RSA_PUBLIC_KEY;
    }

    public static String getMemberName() {
        if (MEMBER_NAME == null) {
            refreshMemberInfo();
        }
        return MEMBER_NAME;
    }

    public static TreeMap<String, Long> getDataResourceTags(DataResourceType dataResourceType) {
        TreeMap<String, Long> map = null;

        if (dataResourceType == null) {
            map = DATA_RESOURCE_TAGS;
        } else {
            switch (dataResourceType) {
                case TableDataSet:
                    map = TABLE_DATA_SET_TAGS;
                    break;
                case ImageDataSet:
                    map = IMAGE_DATA_SET_TAGS;
                    break;
                case BloomFilter:
                    map = BLOOM_FILTER_TAGS;
                    break;
                default:

            }
        }

        if (map.isEmpty()) {
            refreshDataResourceTags(dataResourceType, map);
        }

        return map;
    }

    public static List<String> getAccountIdList() {
        if (ACCOUNT_ID_LIST.isEmpty()) {
            refreshAccountMap();
        }
        return ACCOUNT_ID_LIST;
    }

    public static LinkedHashMap<String, String> getAccountMap() {
        if (ACCOUNT_MAP.isEmpty()) {
            refreshAccountMap();
        }
        return ACCOUNT_MAP;
    }

    /**
     * Get the account's nickname
     */
    public static synchronized String getNickname(String accountId) {
        if (accountId == null) {
            return null;
        }
        return getAccountMap().get(accountId) == null ? getMemberName(accountId) : getAccountMap().get(accountId);
    }

    /**
     * Determine whether accountId belongs to the current member
     */
    public static synchronized boolean isCurrentMemberAccount(String accountId) {
        return getAccountIdList().contains(accountId);
    }

    private static LinkedHashMap<String, MemberBaseInfo> getMemberMap() throws StatusCodeWithException {
        if (MEMBER_MAP.isEmpty()) {
            refreshMemberMap();
        }
        return MEMBER_MAP;
    }

    /**
     * Check if an id is member_id
     */
    public static boolean isMemberId(String memberId) {
        try {
            return getMemberMap().get(memberId) != null;
        } catch (StatusCodeWithException e) {
            return false;
        }
    }

    public static synchronized String getMemberName(String memberId) {
        if (StringUtil.isEmpty(memberId)) {
            return null;
        }

        try {
            MemberBaseInfo member = getMemberMap().get(memberId);
            if (member == null) {
                CacheObjects.refreshMemberMap();
                member = getMemberMap().get(memberId);
            }

            if (member == null) {
                return null;
            }
            return member.name;

        } catch (StatusCodeWithException e) {
            return null;
        }

    }

    /**
     * Reload member information
     */
    public static synchronized void refreshMemberInfo() {
        GlobalConfigService service = Launcher.getBean(GlobalConfigService.class);
        MemberInfoModel model = service.getMemberInfo();

        if (model == null) {
            return;
        }

        MEMBER_ID = model.getMemberId();
        RSA_PUBLIC_KEY = model.getRsaPublicKey();
        RSA_PRIVATE_KEY = model.getRsaPrivateKey();
        MEMBER_NAME = model.getMemberName();
    }

    public static synchronized void refreshDataResourceTags(DataResourceType dataResourceType) {
        TreeMap<String, Long> map = getDataResourceTags(dataResourceType);
        refreshDataResourceTags(dataResourceType, map);
    }

    public static synchronized void refreshDataResourceTags(DataResourceType dataResourceType, TreeMap<String, Long> map) {

        // Query all tags from the database
        DataResourceRepository repo = Launcher.getBean(DataResourceRepository.class);
        List<Object[]> rows = dataResourceType == null
                ? repo.listAllTags()
                : repo.listAllTags(dataResourceType.name());

        map.clear();
        // Count the number of data sets corresponding to each tag
        for (Object[] row : rows) {
            List<String> tags = StringUtil.splitWithoutEmptyItem(String.valueOf(row[0]), ",");
            long count = Convert.toLong(row[1]);
            for (String tag : tags) {
                if (!map.containsKey(tag)) {
                    map.put(tag, 0L);
                }
                map.put(tag, map.get(tag) + count);
            }
        }
    }


    /**
     * Reload account list
     */
    public static synchronized void refreshAccountMap() {
        AccountRepository repo = Launcher.getBean(AccountRepository.class);
        List<AccountMysqlModel> list = repo.findAll(Sort.by("nickname"));

        ACCOUNT_MAP.clear();
        ACCOUNT_ID_LIST.clear();
        for (AccountMysqlModel item : list) {
            ACCOUNT_MAP.put(item.getId(), item.getNickname());
            ACCOUNT_ID_LIST.add(item.getId());
        }
    }


    /**
     * Reload the list of union members
     */
    public static synchronized void refreshMemberMap() throws StatusCodeWithException {
        // Prohibit high frequency refresh
        if (System.currentTimeMillis() - LAST_REFRESH_MEMBER_MAP_TIME < 60_000) {
            return;
        }
        LAST_REFRESH_MEMBER_MAP_TIME = System.currentTimeMillis();

        UnionService service = Launcher.getBean(UnionService.class);
        MEMBER_MAP.clear();
        MEMBER_MAP = service.getMemberMap();

    }

}
