# Copyright 2021 The WeFe Authors. All Rights Reserved.
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import pymysql


def create_connection():
    return pymysql.connect(host='localhost',
                           port=3306,
                           user='root',
                           password="",
                           database='wefe_upgrade_test01')


def select_one(sql: str):
    connection = create_connection()
    cursor = connection.cursor()
    cursor.(sql)
    data = cursor.fetchone()
    connection.close()
    return data


def select_list(sql: str):
    connection = create_connection()
    cursor = connection.cursor()
    cursor.execute(sql)
    data = cursor.fetchall()
    connection.close()
    return data


def execute(sql: str):
    connection = create_connection()
    cursor = connection.cursor()
    data = cursor.execute(sql)
    connection.commit()
    connection.close()
    return data


def execute_multi(sql: str):
    connection = create_connection()
    cursor = connection.cursor()
    data = cursor.executemany(sql)
    connection.commit()
    connection.close()
    return data


if __name__ == '__main__':
    count = execute("update account set admin_role=true")
    print(count)

    rows = select_list("select nickname,admin_role from account")
    for row in rows:
        print(row)
