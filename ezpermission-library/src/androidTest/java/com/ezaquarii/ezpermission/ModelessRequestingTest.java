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

import android.content.pm.PackageManager;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class ModelessRequestingTest extends Fixture {

    @Before
    public void setUp() throws Exception {
        initFixture(false);
        when(mCanShowRationale.call()).thenReturn(false);
        when(mIsPermissionGranted.call()).thenReturn(false);
        mHelper.call();
        assertEquals(EzPermission.Fsm.State.REQUESTING, mHelper.getCurrentState());
    }

    @Test
    public void requestingCallbackCalled() throws Exception {
        verify(mOnRequest, times(1)).run();
    }

    @Test
    public void denied() throws Exception {
        when(mCanShowRationale.call()).thenReturn(true);
        mHelper.onRequestPermissionsResult(REQUEST_CODE,
                new String[] { PERMISSION },
                new int[] {PackageManager.PERMISSION_DENIED} );
        assertEquals(EzPermission.Fsm.State.RATIONALE, mHelper.getCurrentState());
        verify(mOnDenied, times(1)).run();
        verify(mOnDeniedPermananetly, never()).run();
        verify(mOnGranted, never()).run();
    }

    @Test
    public void deniedPermanently() throws Exception {
        when(mCanShowRationale.call()).thenReturn(false);
        mHelper.onRequestPermissionsResult(REQUEST_CODE,
                new String[] { PERMISSION },
                new int[] {PackageManager.PERMISSION_DENIED} );
        assertEquals(EzPermission.Fsm.State.DENIED, mHelper.getCurrentState());
        verify(mOnDenied, never()).run();
        verify(mOnDeniedPermananetly, times(1)).run();
        verify(mOnGranted, never()).run();
    }

    @Test
    public void granted() throws Exception {
        mHelper.setModalRationale(false);
        when(mCanShowRationale.call()).thenReturn(false);
        mHelper.onRequestPermissionsResult(REQUEST_CODE,
                new String[] { PERMISSION },
                new int[] {PackageManager.PERMISSION_GRANTED} );
        assertEquals(EzPermission.Fsm.State.GRANTED, mHelper.getCurrentState());
        verify(mOnDenied, never()).run();
        verify(mOnDeniedPermananetly, never()).run();
        verify(mOnGranted, times(1)).run();
    }
}
