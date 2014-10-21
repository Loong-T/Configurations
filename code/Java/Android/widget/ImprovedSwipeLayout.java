/*
 * Copyright 2014 Zheng Xuqiang
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

package in.nerd_is.inactive_weibo.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import in.nerd_is.inactive_weibo.R;

/**
 * Created by Zheng Xuqiang on 14-9-29.
 */
public class ImprovedSwipeLayout extends SwipeRefreshLayout {

    private static final String TAG = ImprovedSwipeLayout.class.getCanonicalName();
    private int mScrollableChildId;
    private View mScrollableChild;

    public ImprovedSwipeLayout(Context context) {
        this(context, null);
    }

    public ImprovedSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.ImprovedSwipeLayoutAttrs);
        mScrollableChildId = a.getResourceId(R.styleable.ImprovedSwipeLayoutAttrs_scrollableChildId, 0);
        mScrollableChild = findViewById(mScrollableChildId);
        a.recycle();
    }

    @Override
    public boolean canChildScrollUp() {
        ensureScrollableChild();

        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mScrollableChild instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mScrollableChild;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return mScrollableChild.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mScrollableChild, -1);
        }
    }

    private void ensureScrollableChild() {
        if (mScrollableChild == null) {
            mScrollableChild = findViewById(mScrollableChildId);
        }
    }

}
