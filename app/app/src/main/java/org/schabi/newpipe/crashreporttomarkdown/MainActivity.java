package org.schabi.newpipe.crashreporttomarkdown;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText jsonEditText;
    private EditText humanReadableEditText;
    private Button transform;
    private Button copy;
    private Button clear;
    private Button preview;
    private ClipboardManager clipboardManager;
    private ClipData clipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.home));
        setSupportActionBar(toolbar);

        jsonEditText = findViewById(R.id.editTextError);
        humanReadableEditText = findViewById(R.id.markdownEditText);
        transform = findViewById(R.id.transform_button);
        copy = findViewById(R.id.copy_button);
        clear = findViewById(R.id.clear);
        preview = findViewById(R.id.preview);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        FloatingActionButton emailButton = findViewById(R.id.email_button);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.wip), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton githubIssue = findViewById(R.id.github_button);
        githubIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TeamNewPipe/NewPipe/issues/new"));
                startActivity(browserIntent);
            }
        });

        transform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = jsonEditText.getText().toString();
                String markdown = Converter.toMarkdown(text, getApplicationContext());
                if (!markdown.equals(getString(R.string.error_while_parsing))) {
                    humanReadableEditText.setText(markdown);
                }
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = humanReadableEditText.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_text), Toast.LENGTH_SHORT).show();
                } else {
                    clipData = ClipData.newPlainText("text", text);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(getApplicationContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
                }
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = humanReadableEditText.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_text, Toast.LENGTH_SHORT).show();
                } else if (text.equals(getString(R.string.error_while_parsing))) {
                    Toast.makeText(getApplicationContext(), R.string.parsing_error_message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.wip, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case (R.id.clear):
                if (jsonEditText.getText().toString().isEmpty() && humanReadableEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.already_empty, Toast.LENGTH_SHORT).show();
                } else {
                    jsonEditText.setText("");
                    humanReadableEditText.setText("");
                    Toast.makeText(getApplicationContext(), R.string.text_cleared, Toast.LENGTH_SHORT).show();
                }
                break;
            case (R.id.theme):
                Toast.makeText(getApplicationContext(), R.string.wip, Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
