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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;

class MongoStockConnection {
    private static final MongoURI dbURI = new MongoURI("mongodb://localhost");
    private static final String dbStock = "stockdb";
    private static final String dbCollectionName = "nasdaq";

    private static Mongo dbConnect = null;
    private static DB db = null;
    private static DBCollection dbCol = null;

    private MongoStockConnection() {}
    public static DB instance () {
        if (dbConnect == null) {
            try {
                dbConnect = new Mongo (dbURI);
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
                
            db = dbConnect.getDB(dbStock);
            dbCol = db.getCollection(dbCollectionName);
        }
        return db;
    }
    public static DBCollection collection () {
        if (dbConnect == null) {
            try {
                dbConnect = new Mongo (dbURI);
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
            
            db = dbConnect.getDB(dbStock);
            dbCol = db.getCollection(dbCollectionName);
        }
        return dbCol;
    }
}

