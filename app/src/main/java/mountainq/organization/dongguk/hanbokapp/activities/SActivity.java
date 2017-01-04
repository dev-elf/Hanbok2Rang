package mountainq.organization.dongguk.hanbokapp.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import mountainq.organization.dongguk.hanbokapp.R;


/**
 * Created by dnay2 on 2016-11-16.
 */

public abstract class SActivity extends AppCompatActivity {

    protected Toolbar toolbar = null;
    private LinearLayout innerLayout = null;

    public TextView toolbarText;
    protected ImageView backBtn;
    protected Typeface fontNanum;
    protected Context mContext;
    protected static final HashMap<String, String> menuHashMap = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState, int layout) {
        super.onCreate(savedInstanceState);
        Log.d("test", "abstract class created");
        if (layout == R.layout.activity_main)
            setContentView(layout);
        else {
            setContentView(R.layout.toolbar_layout);
            if (findViewById(R.id.innerLayout) != null) {
                innerLayout = (LinearLayout) findViewById(R.id.innerLayout);
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ll.setLayoutParams(llp);
                ll = (LinearLayout) View.inflate(this, layout, null);
                innerLayout.addView(ll);
                Log.d("ggg", "레이아웃 위치 고정");
            }
        }

        if (findViewById(R.id.toolbar) != null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            Log.d("ggg", "툴바 위치 고정");
        }


    }

    /**
     * 툴바의 내용을 바꾸는 메소드
     *
     * @param text 바꿀 내용
     */
    protected void changeToolbarText(String text) {
        if (toolbar == null) return;
        ((TextView) findViewById(R.id.toolbarText)).setText(text);
    }

    protected void visibleLogo(boolean visible) {
        if (toolbar == null) return;
        ImageView logo = (ImageView) findViewById(R.id.logo);
        Picasso.with(this).load(R.drawable.hanbok_logo).fit().into(logo);
        logo.setVisibility(View.VISIBLE);
    }

    /**
     * 툴바 밑에 들어가는 뷰에서 id에 대한 뷰를 찾는다.
     *
     * @param id 아이디로 찾자
     * @return 리턴값은 역시 뷰다
     */
    @Override
    public View findViewById(@IdRes int id) {
        if (innerLayout == null) return super.findViewById(id);
        else return innerLayout.findViewById(id);
    }

    /**
     * 만약에 툴바의 백버튼이 필요한 경우 사용되는 메소드
     *
     * @param text
     */
    protected void setToolbar(String text) {
        if (toolbar == null) return;
        backBtn = (ImageView) super.findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbarText = (TextView) super.findViewById(R.id.toolbarText);
        toolbarText.setText(text);
    }

    protected void showBackBtn() {
        if (findViewById(R.id.backBtn) == null) {
            backBtn = (ImageView) super.findViewById(R.id.backBtn);
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            backBtn.setVisibility(View.VISIBLE);
        }
    }

    protected void hideBackBtn() {
        if (backBtn != null) backBtn.setVisibility(View.GONE);
    }

}