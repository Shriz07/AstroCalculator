package com.example.astroweather;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import static android.os.Looper.getMainLooper;

public class MoonFragment extends Fragment
{
    private static AstroInfo astroInfo;
    private TextView tvMoonRise;
    private TextView tvMoonSet;
    private TextView tvFullMoon;
    private TextView tvNewMoon;
    private TextView tvPhase;
    private TextView tvSynodicMonth;
    private final Handler refreshInfoHandler = new Handler(getMainLooper());

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static MoonFragment newInstance(AstroInfo info)
    {
        astroInfo = info;
        MoonFragment moonFragment = new MoonFragment();
        return moonFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.moon_fragment, container, false);

        tvMoonRise = view.findViewById(R.id.tvMoonRise);
        tvMoonSet = view.findViewById(R.id.tvMoonSet);
        tvFullMoon = view.findViewById(R.id.tvFullMoon);
        tvNewMoon = view.findViewById(R.id.tvNewMoon);
        tvPhase = view.findViewById(R.id.tvPhase);
        tvSynodicMonth = view.findViewById(R.id.tvSynodicMonth);

        refreshInfoHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshInfoHandler.postDelayed(this, (long)SettingsActivity.refreshTime * 60000);

                tvMoonRise.setText(astroInfo.getMoonrise().toString());
                tvMoonSet.setText(astroInfo.getMoonset().toString());
                tvFullMoon.setText(astroInfo.getNextFullMoon().toString());
                tvNewMoon.setText(astroInfo.getNextNewMoon().toString());
                tvPhase.setText(astroInfo.getPhase() + "%");
                tvSynodicMonth.setText(astroInfo.getSynodicMonth() + "");
            }
        }, 1);

        return view;
    }

    @Override
    public void onPause() {
        refreshInfoHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStop() { super.onStop(); }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
