package com.example.drawing1;

import rhcad.touchvg.IGraphView;
import rhcad.touchvg.IGraphView.OnContentChangedListener;
import rhcad.touchvg.IGraphView.OnSelectionChangedListener;
import rhcad.touchvg.IViewHelper;
import rhcad.touchvg.ViewFactory;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.chiralcode.colorpicker.ColorPickerDialog;
import com.chiralcode.colorpicker.ColorPickerDialog.OnColorSelectedListener;

public class MainActivity extends Activity implements OnSelectionChangedListener {
    private IViewHelper mHelper = ViewFactory.createHelper();
    private static final String PATH = "mnt/sdcard/Drawing1/";
    private SeekBar mLineWidthBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewGroup layout = (ViewGroup) this.findViewById(R.id.container);
        mHelper.createSurfaceView(this, layout, savedInstanceState);
        mHelper.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.background_repeat));

        initButtons();
        initUndo();
        updateButtons();
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
        findViewById(R.id.snapshot_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.savePNG(mHelper.extentSnapshot(4, true), PATH + "snapshot.png");
            }
        });

        mLineWidthBar = (SeekBar) findViewById(R.id.lineWidthBar);
        mLineWidthBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHelper.setStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHelper.setContextEditing(true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHelper.setContextEditing(false);
            }
        });
        mHelper.getGraphView().setOnSelectionChangedListener(this);

        findViewById(R.id.colorpicker_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog(MainActivity.this, mHelper.getLineColor(), new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        mHelper.setLineColor(color);
                    }
                }).show();
            }
        });
    }

    private void initUndo() {
        findViewById(R.id.undo_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.undo();
            }
        });
        findViewById(R.id.redo_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.redo();
            }
        });
        mHelper.getGraphView().setOnContentChangedListener(new OnContentChangedListener() {
            @Override
            public void onContentChanged(IGraphView view) {
                updateButtons();
            }
        });
        mHelper.startUndoRecord(PATH + "undo");
    }

    private void updateButtons() {
        findViewById(R.id.undo_btn).setEnabled(mHelper.canUndo());
        findViewById(R.id.redo_btn).setEnabled(mHelper.canRedo());
        mLineWidthBar.setProgress(mHelper.getStrokeWidth());
    }

    @Override
    public void onSelectionChanged(IGraphView view) {
        updateButtons();
    }

    @Override
    public void onDestroy() {
        mHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mHelper.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mHelper.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHelper.onSaveInstanceState(outState, PATH);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHelper.onRestoreInstanceState(savedInstanceState);
    }

}
