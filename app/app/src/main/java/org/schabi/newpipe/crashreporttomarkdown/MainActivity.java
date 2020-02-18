package org.schabi.newpipe.crashreporttomarkdown;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final String THEME = "theme";
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
        setSupportActionBar(toolbar);

        checkTheme();

        jsonEditText = findViewById(R.id.editTextError);
        humanReadableEditText = findViewById(R.id.markdownEditText);
        transform = findViewById(R.id.transform_button);
        copy = findViewById(R.id.copy_button);
        clear = findViewById(R.id.clear);
        preview = findViewById(R.id.preview_button);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

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
                if (text.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.empty_text, Toast.LENGTH_SHORT).show();
                } else {
                    String markdown = Converter.toMarkdown(text, getApplicationContext());
                    if (!markdown.equals(getString(R.string.error_while_parsing))) {
                        humanReadableEditText.setText(markdown);
                    }
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
                    Intent intent = new Intent(getApplicationContext(), PreviewActivity.class);
                    intent.putExtra(getString(R.string.markdown_text_key), text);
                    startActivity(intent);
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
                changeTheme();
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTheme() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences(THEME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Boolean isNightMode = settings.getBoolean("theme_pref", false);
        if (isNightMode) {
            editor.putBoolean(getString(R.string.theme_sharedpreferences_key), false);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            editor.putBoolean(getString(R.string.theme_sharedpreferences_key), true);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void checkTheme() {
        //if we create settings, we could create a selector, with system default theme, battery saver and time
        //https://developer.android.com/guide/topics/ui/look-and-feel/darktheme
        SharedPreferences settings = getApplicationContext().getSharedPreferences(THEME, 0);
        Boolean isNightMode = settings.getBoolean(getString(R.string.theme_sharedpreferences_key), false);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
