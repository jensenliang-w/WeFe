/*
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

package com.welab.wefe.common.wefe.enums;

/**
 * @author hunter.zhao
 */
public enum ProjectFlowStatus {

    /**
     * editing
     */
    editing,
    /**
     * running
     */
    running,
    /**
     * finished
     */
    finished,
    /**
     * Process execution error
     */
    error_on_running,
    /**
     * People pause
     */
    stop_on_running,
    /**
     * Waiting for running
     */
    wait_run,
    /**
     * Waiting for the end
     */
    wait_stop,
    /**
     * Success (normal end)
     */
    success,
    /**
     * Wait for success
     */
    wait_success

}
