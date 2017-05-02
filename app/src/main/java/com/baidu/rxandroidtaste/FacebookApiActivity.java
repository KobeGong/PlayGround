package com.baidu.rxandroidtaste;

import android.app.FakeKeyguardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.facebook.applinks.AppLinkData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bolts.AppLinks;

import static com.baidu.rxandroidtaste.CommonUtils.dpToPx;

public class FacebookApiActivity extends AppCompatActivity {

    private static String TAG = "facebook";

    private ListView lvFbAds;
    private LinearLayout layout_ad;
    private ImageView imgLogo;
    private TextView tvTitle;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_api);
        layout_ad = (LinearLayout) findViewById(R.id.layout_ad);
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        lvFbAds = (ListView) findViewById(R.id.lv_fbads);
        AdSettings.addTestDevice("a124b9b46b100bc255e0f90e2d2a8699");
        loadAd("795662960560177_795668073892999");
        AppLinkData appLinkData = AppLinkData.createFromActivity(this);
        Uri uri = appLinkData.getTargetUri();
        Log.d(TAG, "onCreate: uri = "+uri.toString());

        AppLinks.getTargetUrlFromInboundIntent(getApplicationContext(),getIntent());

        AppLinkData.fetchDeferredAppLinkData(this, "",  new AppLinkData.CompletionHandler() {
            @Override
            public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                appLinkData.getTargetUri();
            }
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        String location = data.getScheme()+data.getHost();
        Log.d(TAG, "onCreate: location = "+location);
    }

    private void loadAd(String adId) {
        //加载多条广告
        final List<NativeAd> nativeAds = new ArrayList<>();
        final NativeAdsManager nam = new NativeAdsManager(FacebookApiActivity.this, adId, 10);
        nam.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                int count = nam.getUniqueNativeAdCount();
                while (count > 0) {
                    nativeAds.add(nam.nextNativeAd());
                    count--;
                }
                FbAdAdapter fba = new FbAdAdapter(nativeAds, FacebookApiActivity.this);
                lvFbAds.setAdapter(fba);
            }

            @Override
            public void onAdError(AdError paramAdError) {
                Log.e(TAG, "FbNotificationNativeAd:onError:" + paramAdError.getErrorCode() + ", " + paramAdError.getErrorMessage());
            }
        });
        nam.loadAds();
        //加载单条广告
        final NativeAd nativeAd = new NativeAd(FacebookApiActivity.this, adId);
        nativeAd.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                Log.e(TAG, "FbNotificationNativeAd:onError:" + error.getErrorCode() + ", " + error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(final Ad ad) {
                if (ad != nativeAd)
                    return;
                if (nativeAd.getAdIcon() == null || nativeAd.getAdIcon().getUrl() == null)
                    return;
                tvTitle.setText(nativeAd.getAdTitle());
                tvContent.setText(nativeAd.getAdSubtitle());
                Picasso.with(getApplicationContext()).load(nativeAd.getAdIcon().getUrl().replaceFirst("https", "http")).resize(200, 200).into(imgLogo);
                nativeAd.registerViewForInteraction(layout_ad);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }
        });
        nativeAd.loadAd();
    }

    static class FbAdAdapter extends BaseAdapter {
        List<NativeAd> nativeAds;
        Context mContext;

        public FbAdAdapter(List<NativeAd> fbNativeAds, Context context) {
            nativeAds = fbNativeAds;
            mContext = context;
        }

        @Override
        public int getCount() {
            return nativeAds.size();
        }

        @Override
        public Object getItem(int position) {
            return nativeAds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_list_item_facebook, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imgLogo = (ImageView) convertView.findViewById(R.id.img_logo);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                viewHolder.tvContent = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String url = nativeAds.get(position).getAdIcon().getUrl();
            Log.d(TAG, "getView: url = "+url);
            Picasso.with(mContext).load(url).resize((int) dpToPx(72), (int) dpToPx(72)).into(viewHolder.imgLogo);
            viewHolder.tvTitle.setText(nativeAds.get(position).getAdTitle());
            viewHolder.tvContent.setText(nativeAds.get(position).getAdSubtitle());
            nativeAds.get(position).registerViewForInteraction(convertView);
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView imgLogo;
        TextView tvTitle;
        TextView tvContent;
    }

    private static class FbContext extends ContextWrapper {
        public FbContext(Context base) {
            super(base);
        }

        @Override
        public Object getSystemService(String name) {
            if (name.equals(Context.KEYGUARD_SERVICE)) {
                return new FakeKeyguardManager();
            }
            return super.getSystemService(name);
        }


    }
}
