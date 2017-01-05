package mountainq.organization.dongguk.hanbokapp.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import mountainq.organization.dongguk.hanbokapp.R;

import static android.view.View.GONE;


/**
 * Created by dnay2 on 2016-11-16.
 */

public abstract class SActivity extends AppCompatActivity {

    protected Toolbar toolbar = null;
    private LinearLayout innerLayout = null;

    public TextView toolbarText;
    protected ImageView backBtn, logoImg;
    protected Typeface fontNanum;
    protected Context mContext;
    protected static final HashMap<String, String> menuHashMap = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState, int layout) {
        super.onCreate(savedInstanceState);
        Log.d("test", "abstract class created");
        if (layout == R.layout.activity_main){
            setContentView(layout);
        }
        else {
            setContentView(R.layout.toolbar_layout);
            if (findViewById(R.id.innerLayout) != null) {
                innerLayout = (LinearLayout) findViewById(R.id.innerLayout);
                LinearLayout ll = new LinearLayout(this);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ll.setLayoutParams(llp);
                ll = (LinearLayout) View.inflate(this, layout, null);
                innerLayout.addView(ll);
                ll.setGravity(Gravity.CENTER);
                Log.d("test", "레이아웃 위치 고정");
            }
        }

        if (super.findViewById(R.id.toolbar) != null) {
            toolbar = (Toolbar) super.findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            Log.d("test", "툴바 위치 고정");
            backBtn = (ImageView) super.findViewById(R.id.backBtn);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            toolbarText = (TextView) super.findViewById(R.id.toolbarText);
            logoImg = (ImageView) super.findViewById(R.id.logo);
            Picasso.with(this).load(R.drawable.hanbok_logo).fit().into(logoImg);
        }


    }

    /**
     * 툴바의 내용을 바꾸는 메소드
     *
     * @param text 바꿀 내용
     */
    protected void changeToolbarText(String text) {
        Log.d("test", "toolbar is ");
        if (toolbar == null) return;
        Log.d("test", "not null");
        toolbarText.setText(text);
    }

    protected void visibleLogo(boolean visible) {
        if (toolbar == null) return;
        if(visible) logoImg.setVisibility(View.VISIBLE);
        else logoImg.setVisibility(View.GONE);
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
        visibleBackBtn(true);
        if(toolbarText != null) toolbarText.setText(text);
    }

    protected void visibleBackBtn(boolean visible) {
        if(toolbar == null) return;
        if(visible) backBtn.setVisibility(View.VISIBLE);
        else backBtn.setVisibility(View.GONE);
    }

    protected void hideBackBtn() {
        if (backBtn != null) backBtn.setVisibility(GONE);
    }

}