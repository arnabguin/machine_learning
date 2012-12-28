/**
 * Copyright [2012] Arnab Guin
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

package com.aguin.stock.recommender;

import java.net.UnknownHostException;

import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;

import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public class MongoDBUserModel {
    private static final String dbHost = "localhost";
    private static final int dbPort = ServerAddress.defaultPort();
    private static final String dbUser = "userdb";
    private static final String dbCollectionName = "prefs";

    private static MongoDBDataModel mmdb = null;

    private MongoDBUserModel() {}
    public static boolean registered(String user) {
       if (mmdb != null) {
           return mmdb.isIDInModel(user);
       } else {
           return false;
       }
    }
    public static MongoDBDataModel instance () {
        if (mmdb == null) {
            try {
                mmdb = new MongoDBDataModel(
                    dbHost,
                    dbPort,
                    dbUser,
                    dbCollectionName,
                    true,
                    true,
                    null
                );
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
            catch (MongoException e) {
                e.printStackTrace();
            }
        }
        return mmdb;
    }
}

