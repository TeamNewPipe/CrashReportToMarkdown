package org.schabi.newpipe.crashreporttomarkdown;

import android.content.Context;
import android.widget.Toast;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

public class Converter {

    /**
     * Converts a crash report from NewPipe human-readable Markdown
     *
     * @return a human readable crash report, in a String.
     */
    public static String toMarkdown(String bugreport, Context c) {
        String output = "";
        JsonObject report;
        JsonArray exceptions;
        try {
            report = JsonParser.object().from(bugreport);
            exceptions = report.getArray("exceptions");
        } catch (JsonParserException e) {
            Toast.makeText(c, c.getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
            return c.getString(R.string.error_while_parsing);
        }
        output += "## Exception";
        output += "\n* __User Action:__ " + report.getString("user_action");
        output += "\n* __Request:__ " + report.getString("request");
        output += "\n* __Content Language:__ " + report.getString("content_language");
        output += "\n* __GMT Time:__ " + report.getString("time");
        output += "\n* __Package:__ " + report.getString("package");
        output += "\n* __Version:__ " + report.getString("version");
        output += "\n* __Service:__ " + report.getString("service");
        output += "\n* __Operating System:__ " + report.getString("os");

        output += "\n" + report.getString("user_comment") + "\n";

        output += "\n<details><summary><b>Crash log</b></summary><p>\n";
        output += "\n```\n";
        int i = 0;
        while (i < exceptions.size() - 1) {
            output += exceptions.get(i);
            output += "\n-------------------\n\n";
            i++;
        }
        output += exceptions.get(i); //last one
        output += "\n```\n";
        output += "</p></details>\n";
        output += "<hr>\n";

        return output;
    }
}
