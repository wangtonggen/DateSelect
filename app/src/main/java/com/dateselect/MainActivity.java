package com.dateselect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dateselect.view.DateView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private DateView dateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setText("单选");
        dateView = (DateView) findViewById(R.id.dateView);

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                String strButton = button.getText().toString().trim();
                if ("单选".equals(strButton)){
                    dateView.setSelectMode(true);//多选
                    button.setText("多选");
                }else {
                    dateView.setSelectMode(false);//单选
                    button.setText("单选");
                }
                break;
        }
    }
}
