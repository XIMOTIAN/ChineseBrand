package com.darren.chinesebrand.framework.toolbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.darren.chinesebrand.R;

/**
 * Created by hcDarren on 2017/9/23.
 * 可以拿过来直接使用的 默认样式导航栏
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder> {

    protected DefaultNavigationBar(Builder builder) {
        super(builder);
    }

    @Override
    public void attachNavigationParams() {
        super.attachNavigationParams();
        // 处理特有的
        ImageView leftView = findViewById(R.id.left_iv);
        leftView.setVisibility(getBuilder().mLeftVisible);

        ImageView rightView = findViewById(R.id.right_iv);
        rightView.setVisibility(getBuilder().mRightVisible);


        if (getBuilder().leftIcon != 0) {
            leftView.setImageResource(getBuilder().leftIcon);
        }
    }

    /**
     * 导航栏的Builder
     */
    public static class Builder extends AbsNavigationBar.Builder<Builder> {
        public int mLeftVisible = View.VISIBLE;
        public int mRightVisible = View.VISIBLE;

        int leftIcon;
        private String title;

        public Builder(Context context, ViewGroup parent) {
            super(context, R.layout.ui_defualt_navigation_bar, parent);
        }

        @Override
        public DefaultNavigationBar create() {
            return new DefaultNavigationBar(this);
        }

        public Builder setLeftIcon(int leftIcon) {
            this.leftIcon = leftIcon;
            return this;
        }

        public Builder setRightIcon(String text) {
            setText(R.id.left_iv, text);
            return this;
        }

        public Builder setLeftClickListener(View.OnClickListener clickListener) {
            setOnClickListener(R.id.left_iv, clickListener);
            return this;
        }

        public Builder setRightClickListener(View.OnClickListener clickListener) {
            setOnClickListener(R.id.right_iv, clickListener);
            return this;
        }

        public Builder hideRightIcon() {
            mRightVisible = View.INVISIBLE;
            return this;
        }

        public Builder hideLeftIcon() {
            mLeftVisible = View.INVISIBLE;
            return this;
        }

        public Builder setTitle(String title) {
            setText(R.id.title_tv,title);
            return this;
        }
    }
}
