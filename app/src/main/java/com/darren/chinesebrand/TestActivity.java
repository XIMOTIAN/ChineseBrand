package com.darren.chinesebrand;

import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.darren.chinesebrand.app.ui.HorizontalSelectorView;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: Darren on 2017/11/23 14:45
 * email: 240336124@qq.com
 * version: 1.0
 */
public class TestActivity extends BrandBaseActivity {

    @ViewById(R.id.web_view)
    private WebView mWebView;
    @ViewById(R.id.share_wx)
    private ImageView mShareWx;
    @ViewById(R.id.share_qq)
    private ImageView mShareQq;
    @ViewById(R.id.share_wb)
    private ImageView mShareWb;
    @ViewById(R.id.header_container)
    private LinearLayout mHeaderContainer;
    @ViewById(R.id.selector_view)
    private HorizontalSelectorView mSelectorView;

    @Override
    protected void initData() {
       /* String detailUrl = Constant.UrlConstant.URL_BASE + "detail/" + 33;
        GoHttp.create(this).url(detailUrl)
                .get()
                .execute(new HttpCallback<BannerDetailResult>() {

                    @Override
                    public void onError(Exception e) {
                        showToast(getString(R.string.net_error_tip));

                    }

                    @Override
                    protected void serverTransformError(ServerDataTransformException e) {
                        showToast(getString(R.string.server_error_tip));
                    }

                    @Override
                    protected void onSuccess(BannerDetailResult result) {
                        if (result.isOk()) {
                            mWebView.loadData(result.data.ai_content,"text/html;charset=utf-8","utf-8");
                        } else {
                            showToast(result.msg);
                        }
                    }
                });*/
    }

    @Override
    protected void initView() {
        List<String> items = new ArrayList<>();
        items.add("1111");
        items.add("2222");
        items.add("3333");
        mSelectorView.addSelectorStrs(items);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.layout_brand_detail_header);
    }

}
