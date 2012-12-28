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

import com.aguin.stock.recommender.UserArgumentType;


class UserArgumentStringType implements UserArgumentType
{
    private String arg = null;
    public UserArgumentStringType() {}
    public UserArgumentStringType(String argument) {
        arg = argument;
        check();
    }
    public void setarg(String argument) { arg = argument; }
    public String getarg() { return arg; }
    public String toString() { return arg.toString(); }

    public boolean check() { 
        return true;
    }
}
