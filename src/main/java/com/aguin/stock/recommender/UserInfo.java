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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.apache.mahout.cf.taste.common.NoSuchItemException;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import com.google.common.collect.Lists;

interface WriteUserInfo {
    public void writeToDB();
}

interface ReadUserInfo {
    public void readFromDB();
}

class UserItemPrefFile implements WriteUserInfo {
    private String filename;
    private String user;
    private UserItemPrefFile() {}
    public  UserItemPrefFile(String u, String f) {
        user = u;
        filename = f;
    }
    public void writeToDB() {
        BufferedReader in = null;
        try {
            in = new BufferedReader (new FileReader (filename));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String s = in.readLine();
            while ((s = in.readLine()) != null) {
                String [] tokens = s.split(",");
                UserItemPreference it = new UserItemPreference (user, tokens[0], tokens[1]);
                it.writeToDB();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class UserItem implements ReadUserInfo {
    private String item;
    private String user;
    private UserItem() {}
    public UserItem(String u, String i) { item = i; user = u; }
    public void readFromDB() {
        MongoDBDataModel md = MongoDBUserModel.instance();
        if (!MongoDBUserModel.registered(user)) {
        	System.out.format("User %s not registered. Please use -ip or -f options to add preferences first", user);
            return;
        }
        try {
            FastIDSet idset = md.getItemIDsFromUser(Long.parseLong(md.fromIdToLong(user,true)));
            if (idset.contains(Long.parseLong(md.fromIdToLong(item,false)))) {
                System.out.format("Found in db: user=%s,item=%s\n", user,item);
            }
            else {
                System.out.format("Not found: user=%s,item=%s\n", user, item);
            }
            LongPrimitiveIterator fi = idset.iterator();
            StringBuilder sb = new StringBuilder ();
            while (fi.hasNext()) {
                sb.append("Item "); 
                sb.append(md.fromLongToId(fi.next()));
                sb.append("\n");
            }
            System.out.println(sb);
         }
         catch (TasteException e) {
            e.printStackTrace();
         }
    }
}

class UserPreference implements ReadUserInfo {
    private String user;
    private String pref;
    private UserPreference() {}
    public UserPreference(String u, String p) { user = u; pref = p; }
    public void readFromDB() {
        MongoDBDataModel md = MongoDBUserModel.instance();
        if (!MongoDBUserModel.registered(user)) {
        	System.out.format("User %s not registered. Please use -ip or -f options to add preferences first", user);
            return;
        }
        PreferenceArray pa = null;
        try {
            pa = md.getPreferencesFromUser(Long.parseLong(md.fromIdToLong(user,true)));
        }
        catch (TasteException e) {
            e.printStackTrace();
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (pa.length() == 0) {
            System.out.format("Preferences for user %s\n", user);
            System.out.println("None");
        }
        else {
            System.out.format("Preferences for user %s\n", user);
            System.out.println(pa.toString());
        }
    }
}

class UserItemPreference implements WriteUserInfo {
    private String user;
    private String item;
    private String pref;
    private UserItemPreference() {}
    public UserItemPreference(String u, String i, String p) { 
        user = u; pref = p; item = i; 
    }
    public void writeToDB() {
        MongoDBDataModel md = MongoDBUserModel.instance();
        Collection<List<String>> items = Lists.newArrayList();
        List<String> it = Lists.newArrayList();
        it.add(item);it.add(pref);
        items.add(it);
        try {
            System.out.println("Refreshing database\n");
            md.refreshData(user, items, true);
        }
        catch (NoSuchUserException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        catch (NoSuchItemException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}

