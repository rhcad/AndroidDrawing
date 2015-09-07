package com.example.drawing1;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.ViewById;

import rhcad.touchvg.IGraphView;
import rhcad.touchvg.IGraphView.OnContentChangedListener;
import rhcad.touchvg.IGraphView.OnSelectionChangedListener;
import rhcad.touchvg.IViewHelper;
import rhcad.touchvg.ViewFactory;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.chiralcode.colorpicker.ColorPickerDialog;
import com.chiralcode.colorpicker.ColorPickerDialog.OnColorSelectedListener;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements OnSelectionChangedListener {
    private IViewHelper mHelper = ViewFactory.createHelper();
    private static final String PATH = "mnt/sdcard/Drawing1/";
    
    @ViewById(R.id.lineWidthBar) SeekBar mLineWidthBar;
    
    @Click void line_btn() {
    	mHelper.setCommand("line");
    }
    
    @Click void rect_btn() {
    	mHelper.setCommand("rect");
    }
    
    @Click void triangle_btn() {
    	mHelper.setCommand("triangle");
    }
    
    @Click void select_btn() {
    	mHelper.setCommand("select");
    }
    
    @Click void erase_btn() {
    	mHelper.setCommand("erase_btn");
    }
    
    @Click void snapshot_btn() {
    	mHelper.savePNG(mHelper.extentSnapshot(4, true), PATH + "snapshot.png");
    }
    
    @Click void colorpicker_btn() {
    	new ColorPickerDialog(MainActivity.this, mHelper.getLineColor(), new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                mHelper.setLineColor(color);
            }
        }).show();
    }
    
    @SeekBarProgressChange(R.id.lineWidthBar)
    void onProgressChanged(SeekBar seekBar, int progress) {
    	mHelper.setStrokeWidth(progress);
    }
    
    @SeekBarTouchStart(R.id.lineWidthBar)
    void onStartTrackingTouch(SeekBar seekBar) {
        mHelper.setContextEditing(true);
    }
    
    @SeekBarTouchStop(R.id.lineWidthBar)
    void onStopTrackingTouch(SeekBar seekBar) {
        mHelper.setContextEditing(false);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewGroup layout = (ViewGroup) this.findViewById(R.id.container);
        mHelper.createSurfaceView(this, layout, savedInstanceState);
        mHelper.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.background_repeat));
    }
    
    @AfterViews
    void init() {
        mHelper.getGraphView().setOnSelectionChangedListener(this);
        mHelper.getGraphView().setOnContentChangedListener(new OnContentChangedListener() {
            @Override
            public void onContentChanged(IGraphView view) {
                updateButtons();
            }
        });
        mHelper.startUndoRecord(PATH + "undo");
    }
    
    @Click void undo_btn() {
    	mHelper.undo();
    }
    
    @Click void redo_btn() {
    	mHelper.redo();
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
