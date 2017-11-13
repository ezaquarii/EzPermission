// Copyright (C) EzPermission by Krzysztof Narkiewicz (hello@ezaquarii.com)
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.ezaquarii.ezpermission;

import android.app.Activity;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.Callable;

public class Fixture {

    public static final String PERMISSION_A = "permission_a";
    public static final String PERMISSION_B = "permission_b";
    public static final String[] PERMISSIONS = new String[] {PERMISSION_A, PERMISSION_B};

    public static final int REQUEST_CODE = 1;

    EzPermission mHelper;

    @Mock Runnable mOnGranted;
    @Mock Runnable mOnRationale;
    @Mock Runnable mOnDenied;
    @Mock Runnable mOnDeniedPermananetly;
    @Mock Runnable mOnRequest;
    @Mock Callable<Boolean> mCanShowRationale;
    @Mock Callable<Boolean> mIsPermissionGranted;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    public void initFixture(boolean modal) {
        mHelper = EzPermission.of(REQUEST_CODE, PERMISSIONS)
                .isModal(modal)
                .onGranted(mOnGranted)
                .onRationale(mOnRationale)
                .onDenied(mOnDenied)
                .onDeniedPermanantly(mOnDeniedPermananetly)
                .onRequest(mOnRequest)
                .canShowRationale(mCanShowRationale)
                .isPermissionGranted(mIsPermissionGranted)
                .build();
    }
}
