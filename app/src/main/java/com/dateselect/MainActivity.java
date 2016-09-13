package com.dateselect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dateselect.view.DateView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private DateView dateView;
    private TextView pre;
    private TextView current;
    private TextView next;
    private RelativeLayout rl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pre = (TextView) findViewById(R.id.pre);
        current = (TextView) findViewById(R.id.current);
        next = (TextView) findViewById(R.id.next);
        rl = (RelativeLayout) findViewById(R.id.rl);

        button = (Button) findViewById(R.id.button);
        button.setText("单选");
        dateView = (DateView) findViewById(R.id.dateView);

        current.setText(dateView.getCurrentDay());
        button.setOnClickListener(this);
        pre.setOnClickListener(this);
        next.setOnClickListener(this);

        dateView.setDateClick(new DateView.DateClick() {
            @Override
            public void onClickOnDate() {
                current.setText(dateView.getCurrentDay());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                String strButton = button.getText().toString().trim();
                if ("单选".equals(strButton)){
                    dateView.setSelectMode(true);//多选
                    button.setText("多选");
                    rl.setVisibility(View.GONE);
                }else {
                    dateView.setSelectMode(false);//单选
                    button.setText("单选");
                    rl.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.pre:
                dateView.onLeftClick();
                break;
            case R.id.next:
                dateView.onRightClick();
                break;
        }
    }
}
