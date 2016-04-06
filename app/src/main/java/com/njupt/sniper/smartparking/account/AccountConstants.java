/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.njupt.sniper.smartparking.account;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication constants
 */
public class AccountConstants {

    private AccountConstants() {

    }

    /**
     * Account type
     */
    public static final String ACCOUNT_TYPE = "com.hithinksoft.noodles";

    public static final String AUTHTOKEN_TYPE_HR = "com.hithinksoft.noodles.hr";

    public static final String AUTHTOKEN_TYPE_HR_LABEL = "hr access to noodles account";

    public static final String AUTHTOKEN_TYPE_STU = "com.hithinksoft.noodles.stu";

    public static final String AUTHTOKEN_TYPE_STU_LABEL = "student access to noodles account";

    public static final Map<String, String> AUTHTOEKN_TYPE_SCOPES;

    static {
        Map<String, String> authTypeScopes = new HashMap<String, String>();
        authTypeScopes.put(AUTHTOKEN_TYPE_HR, "hr");
        authTypeScopes.put(AUTHTOKEN_TYPE_STU, "read");
        AUTHTOEKN_TYPE_SCOPES = Collections.unmodifiableMap(authTypeScopes);
    }

}
