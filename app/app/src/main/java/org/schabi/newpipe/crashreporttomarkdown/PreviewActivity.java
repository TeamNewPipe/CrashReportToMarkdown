package org.schabi.newpipe.crashreporttomarkdown;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.noties.markwon.Markwon;

public class PreviewActivity extends AppCompatActivity {

    private TextView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        preview = findViewById(R.id.preview_textview);
        Bundle b = getIntent().getExtras();

        if (b != null) {
            String text = (String) b.get(getString(R.string.markdown_text_key));
            final Markwon markwon = Markwon.builder(getApplicationContext())
                    .build();
            markwon.setMarkdown(preview, text);
        }
    }
}
