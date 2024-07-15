package me.ryan.banner;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

import me.ryan.banner.core.Banner;

public class MainActivity extends AppCompatActivity {

    private List<String> imageUrls = Arrays.asList(
            "https://lmg.jj20.com/up/allimg/4k/s/02/21092423235VL7-0-lp.jpg",
            "https://lmg.jj20.com/up/allimg/4k/s/02/21092423240420G-0-lp.jpg",
            "https://lmg.jj20.com/up/allimg/4k/s/02/2110021A2324239-0-lp.jpg"
    );

    private Banner banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBanner();
    }

    private void initBanner() {
        banner = findViewById(R.id.banner);
        if (banner != null) {
            banner.addLifecycle(getLifecycle());
            banner.setData(imageUrls);
        }
    }
}
