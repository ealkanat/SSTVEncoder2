/*
Copyright 2017 Olga Miller <olga.rgb@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package om.sstvencoder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import om.sstvencoder.ColorPalette.ColorPaletteView;
import om.sstvencoder.TextOverlay.Label;

public class EditTextActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int REQUEST_CODE = 101;
    public static final String EXTRA = "EDIT_TEXT_EXTRA";
    private EditText mEditText;
    private ColorPaletteView mColorPaletteView;
    private float mTextSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mColorPaletteView = (ColorPaletteView) findViewById(R.id.edit_color);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Label label = (Label) getIntent().getSerializableExtra(EXTRA);
        mEditText.setText(label.getText());
        mTextSize = label.getTextSize();
        initTextSizeSpinner(textSizeToPosition(mTextSize));
        ((CheckBox) findViewById(R.id.edit_italic)).setChecked(label.getItalic());
        ((CheckBox) findViewById(R.id.edit_bold)).setChecked(label.getBold());
        mColorPaletteView.setColor(label.getForeColor());
    }

    private void initTextSizeSpinner(int position) {
        Spinner editTextSize = (Spinner) findViewById(R.id.edit_text_size);
        editTextSize.setOnItemSelectedListener(this);
        String[] textSizeList = new String[]{"Small", "Normal", "Large", "Huge"};
        editTextSize.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, textSizeList));
        editTextSize.setSelection(position);
    }

    private int textSizeToPosition(float textSize) {
        int position = (int) (textSize - 1f);
        if (0 <= position && position <= 3)
            return position;
        return 1;
    }

    private float positionToTextSize(int position) {
        return position + 1f;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mTextSize = positionToTextSize(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                done();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void done() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA, getLabel());
        setResult(RESULT_OK, intent);
        finish();
    }

    @NonNull
    private Label getLabel() {
        Label label = new Label();
        label.setText(mEditText.getText().toString());
        label.setTextSize(mTextSize);
        label.setItalic(((CheckBox) findViewById(R.id.edit_italic)).isChecked());
        label.setBold(((CheckBox) findViewById(R.id.edit_bold)).isChecked());
        label.setForeColor(mColorPaletteView.getColor());
        return label;
    }
}