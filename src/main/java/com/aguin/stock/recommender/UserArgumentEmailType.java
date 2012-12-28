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

import com.aguin.stock.recommender.UserArgumentStringType;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.IllegalArgumentException;

public class UserArgumentEmailType extends UserArgumentStringType 
{
    public UserArgumentEmailType() { super(); }
    public UserArgumentEmailType(String arg) {
        super(arg);
        check();
    }
    public String toString() {
        return getarg().toString();
    }
    public boolean check() {
        super.check();
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+[.][a-zA-Z0-9-.]+$");
        Matcher emailMatcher = emailPattern.matcher(toString());
        if (!emailMatcher.matches()) {
            throw new IllegalArgumentException("Email address does not seem valid");
        }
        return true;
    }
}
