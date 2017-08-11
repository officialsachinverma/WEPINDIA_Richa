package com.wepindia.pos;

import android.view.View;

/**
 * Created by Administrator on 15-06-2017.
 */

public interface ClickListener {
    public void onClick(View view, int position);
    public void onLongClick(View view, int position);
}
