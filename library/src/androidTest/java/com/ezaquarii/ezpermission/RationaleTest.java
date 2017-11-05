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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RationaleTest extends Fixture {

    @Before
    public void setUp() throws Exception {
        initFixture(true);
        when(mCanShowRationale.call()).thenReturn(true);
        when(mIsPermissionGranted.call()).thenReturn(false);
        mHelper.call();
        assertEquals(EzPermission.Fsm.State.RATIONALE, mHelper.getCurrentState());
    }

    @Test
    public void rationaleCallbackCalled() {
        verify(mOnRationale, times(1)).run();
    }

    @Test
    public void canRejectRationale() {
        mHelper.rejectRationale();
        assertEquals(EzPermission.Fsm.State.START, mHelper.getCurrentState());
    }

    @Test
    public void canAcceptModalRationale() {
        mHelper.call();
        assertEquals(EzPermission.Fsm.State.REQUESTING, mHelper.getCurrentState());
        verify(mOnRequest, times(1)).run();
    }

    @Test
    public void cannotRejectNonModalRationale() {
        mHelper.setIsModalRationale(false);
        mHelper.rejectRationale();
        assertEquals(EzPermission.Fsm.State.RATIONALE, mHelper.getCurrentState());
        verify(mOnRequest, times(0)).run();
    }
}
