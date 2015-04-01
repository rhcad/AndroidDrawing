package com.example.drawing1;

import rhcad.touchvg.IViewHelper;
import rhcad.touchvg.ViewFactory;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MainActivity extends Activity {
    private IViewHelper mHelper = ViewFactory.createHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper.createGraphView(this, (ViewGroup) this.findViewById(R.id.container));
        initButtons();
    }

    private void initButtons() {
        findViewById(R.id.line_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("line");
            }
        });
        findViewById(R.id.rect_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("rect");
            }
        });
        findViewById(R.id.triangle_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("triangle");
            }
        });
        findViewById(R.id.select_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("select");
            }
        });
        findViewById(R.id.erase_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("erase");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
