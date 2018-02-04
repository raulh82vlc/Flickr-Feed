/*
 * Copyright (C) 2018 Raul Hernandez Lopez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.raulh82vlc.flickrj.feed.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Base Activity with common features
 * @author Raul Hernandez Lopez.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private Unbinder unbinder;

    protected abstract int getLayoutId();

    protected abstract AppCompatActivity getActivity();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(getActivity());
    }

    /**
     * Shows {@link android.app.ProgressDialog}
     */
    public void showLoaderWithTitleAndDescription(String tittleMessage, String descriptionMessage) {
        if (!isFinishing()) {
            if (dialog == null) {
                dialog = ProgressDialog.show(this, tittleMessage, descriptionMessage, true);
            } else {
                dialog.show();
            }
        }
    }

    /**
     * Hides {@link android.app.ProgressDialog}
     */
    public void hideLoader() {
        if (dialog != null
                && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
