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

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class UserOptions extends Object
{
    private static Options options = null;
    private static BasicParser parser = null;

    public static void printUsage()
    {
    	System.out.println("Usage : \n" +
		           "[-h] Display Help \n" +
		           "-u <userid> \n" + 
		           "[-f <userfile> ...]\n" +
		           "[-i <stock ticker> ...]\n" + 
		           "[-p <preference> ...]\n" + 
		           "[-ip <stock:preference> ...]\n");
        System.exit(0);
    }
    public static void main( String[] args )
    {
        options = new Options (); 
        Option optU  = OptionBuilder.hasArg().withArgName("uid").isRequired(true)
                                    .withType(String.class)
                                    .withDescription("User ID : must be a valid email address which serves as the unique identity of the portfolio").create("u");

        Option optI  = OptionBuilder.hasOptionalArgs().withArgName("item")
                                         .withType(String.class)
                                         .isRequired(false)
                                         .withDescription("Stock(s) in your portfolio: Print all stocks selected by this option and preferences against them").create("i");

        Option optP  = OptionBuilder.hasOptionalArgs().withArgName("pref").isRequired(false)
                                         .withType(Long.class)
                                         .withDescription("Preference(s) against stocks in portfolio : Print all preferences specified in option and all stocks listed against that preference. Multiple preferences may be specified to draw on the many-many relationship between stocks and preferences matrices").create("p");

        Option optIP = OptionBuilder.hasArg().withArgName("itempref")
                                   .withValueSeparator(':').isRequired(false)
                                   .withDescription("Enter stock and preferences over command line. Any new stock will be registered as a new entry along with preference. Each new preference for an already existing stock will overwrite the existing preference(so be careful!)").create("ip");

        Option optF = OptionBuilder.hasArg().withArgName("itempreffile")
                                   .withType(String.class)
                                   .withDescription("File to read stock preference data from").isRequired(false).create("f");
        
        Option optH = OptionBuilder.hasArg(false).withArgName("help")
        		                   .withType(String.class)
        		                   .withDescription("Display usage").isRequired(false).create("h");
        
        options.addOption(optU);
        options.addOption(optI);
        options.addOption(optP);
        options.addOption(optIP);
        options.addOption(optF);
        options.addOption(optH);

        parser = new BasicParser();
        CommandLine line = null;

        try {
            line = parser.parse(options,args);   
        }
        catch (MissingOptionException e) {
        	System.out.println("Missing options");
        	printUsage();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
             
        if (line.hasOption("help")) {
        	printUsage();
        }
        UserArgumentEmailType uEmailId = new UserArgumentEmailType(line.getOptionValue("u"));
        String uEmailIdStr = uEmailId.toString();

        if (MongoDBUserModel.registered(uEmailIdStr)) {
            System.out.format("Already registered user %s\n", uEmailIdStr);    
        }
        else {
            System.out.format("+--Starting transaction for user %s --+\n", uEmailIdStr);    
        }

        if (line.hasOption("f")) {
           System.out.println("Query::UpdateDB: itempreffile(s)\n");
           String [] uItemPrefFiles = line.getOptionValues("f");
           for (String it : uItemPrefFiles) {
        	   System.out.format("Updating db with user file %s\n",it);
               UserItemPrefFile uItemPrefFile = new UserItemPrefFile(uEmailIdStr, it);
               uItemPrefFile.writeToDB();
           }
        }
        if (line.hasOption("i")) {
           System.out.println("Query::ReadDB: item(s)\n");
           String [] uItems = line.getOptionValues("i"); 
           for (String it : uItems) {
        	   System.out.format("Searching for item %s in db\n",it);
               UserItem uItem = new UserItem(uEmailIdStr, it);
               uItem.readFromDB();
           }
        }
        if (line.hasOption("p")) {
           System.out.println("Query::ReadDB: preference(s)");
           String [] uPrefs = line.getOptionValues("p");
           for (String it : uPrefs) {
        	   System.out.format("Searching for preference %s in db\n",it);
               UserPreference uPref = new UserPreference(uEmailIdStr, it);
               uPref.readFromDB();
           }
        }
        if (line.hasOption("ip")) {
           System.out.println("Query::UpdateDB: itempref(s)\n");
           String [] uItemPrefs = line.getOptionValues("ip");
           for (String it : uItemPrefs) {
               System.out.format("Updating item:preference pair %s\n",it);
               String [] pair = it.split(":");
               System.out.format("%s:%s:%s",uEmailIdStr,pair[0],pair[1]);
               UserItemPreference uItemPref = new UserItemPreference(uEmailIdStr, pair[0], pair[1]);
               uItemPref.writeToDB();
           }
        }
        System.out.format("+-- Ending transaction for user %s --+\n", uEmailIdStr);    
        
    }
}
