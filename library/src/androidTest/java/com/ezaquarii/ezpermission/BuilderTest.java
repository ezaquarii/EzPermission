package com.ezaquarii.ezpermission;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class BuilderTest extends Fixture {

    private FragmentActivity mMockActivity = mock(FragmentActivity.class);
    private Fragment mMockFragment = mock(Fragment.class);

    @Test(expected = IllegalArgumentException.class)
    public void noContextRequiresCallbacks() {
        EzPermission helper = EzPermission.of(REQUEST_CODE, PERMISSIONS)
                .isModal(false)
                .onGranted(mOnGranted)
                .onRationale(mOnRationale)
                .onDenied(mOnDenied)
                .onDeniedPermanantly(mOnDeniedPermananetly)
                // missing .onRequest(mOnRequest)
                // missing .canShowRationale(mCanShowRationale)
                // missing .isPermissionGranted(mIsPermissionGranted)
                .build();
    }

    @Test
    public void noContext() {
        EzPermission helper = EzPermission.of(REQUEST_CODE, PERMISSIONS)
                .isModal(false)
                .onGranted(mOnGranted)
                .onRationale(mOnRationale)
                .onDenied(mOnDenied)
                .onDeniedPermanantly(mOnDeniedPermananetly)
                .onRequest(mOnRequest)
                .canShowRationale(mCanShowRationale)
                .isPermissionGranted(mIsPermissionGranted)
                .build();
        assertNull(helper.getContext());
    }

    @Test
    public void fragmentEnablesDefaultCallbacksAndProvidesContext() {
        when(mMockFragment.getContext()).thenReturn(mMockActivity);
        EzPermission helper = EzPermission.of(mMockFragment, REQUEST_CODE, PERMISSIONS)
                .isModal(false)
                .onGranted(mOnGranted)
                .onRationale(mOnRationale)
                .onDenied(mOnDenied)
                .onDeniedPermanantly(mOnDeniedPermananetly)
                // missing .onRequest(mOnRequest)
                // missing .canShowRationale(mCanShowRationale)
                // missing .isPermissionGranted(mIsPermissionGranted)
                .build();
        assertNotNull(helper.getContext());
    }

    @Test
    public void activityEnablesDefaultCallbacksAndProvidesContext() {
        EzPermission helper = EzPermission.of(mMockActivity, REQUEST_CODE, PERMISSIONS)
                .isModal(false)
                .onGranted(mOnGranted)
                .onRationale(mOnRationale)
                .onDenied(mOnDenied)
                .onDeniedPermanantly(mOnDeniedPermananetly)
                // missing .onRequest(mOnRequest)
                // missing .canShowRationale(mCanShowRationale)
                // missing .isPermissionGranted(mIsPermissionGranted)
                .build();
        assertEquals(mMockActivity, helper.getContext());
    }

}
