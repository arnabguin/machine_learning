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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import com.mongodb.ServerAddress;

public class StockRecommender {
    public static void run(DataModel model, Recommender rec) throws TasteException {
        if (!(model instanceof MongoDBDataModel)) {
            throw new ClassCastException("Data Model must be a-Mongo!");
        }
        MongoDBDataModel mgmodel = (MongoDBDataModel) model;
        System.out.println("Start recommender\n");
        LongPrimitiveIterator it = mgmodel.getUserIDs();
        while (it.hasNext()){
            long userId = it.nextLong();
            
            // get the recommendations for the user
            List<RecommendedItem> recommendations = rec.recommend(userId, 10);
            
            // if empty write something
            if (recommendations.size() == 0){
                System.out.print("User ");
                System.out.print(mgmodel.fromLongToId(userId));
                System.out.println(": no recommendations");
            }
                            
            // print the list of recommendations for each 
            for (RecommendedItem recommendedItem : recommendations) {
                System.out.print("User ");
                System.out.print(mgmodel.fromLongToId(userId));
                System.out.print(": ");
                System.out.println(recommendedItem);
            }
        }        
    }
    public static void main(String... args) throws FileNotFoundException, TasteException, IOException {
        
        MongoDBDataModel model = new MongoDBDataModel("localhost", ServerAddress.defaultPort(), "userdb","prefs", true, true, null); 
        // create a slope one recommender on the data
        CachingRecommender slopeOneRecommender = new CachingRecommender(new SlopeOneRecommender(model));
        run(model, slopeOneRecommender);
    }
}
