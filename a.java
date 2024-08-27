import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MainActivity extends Activity {
    private static final int PICK_APK_FILE = 1;
    private ListView contentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectButton = findViewById(R.id.selectButton);
        contentListView = findViewById(R.id.contentListView);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.android.package-archive");
                startActivityForResult(intent, PICK_APK_FILE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_APK_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            listApkContents(uri);
        }
    }

    private void listApkContents(Uri apkUri) {
        try {
            File tempFile = File.createTempFile("temp_apk", ".apk", getCacheDir());
            try (InputStream is = getContentResolver().openInputStream(apkUri);
                 FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }

            List<String> contents = new ArrayList<>();
            ZipFile zipFile = new ZipFile(tempFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                contents.add(entry.getName());
            }
            zipFile.close();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contents);
            contentListView.setAdapter(adapter);

            tempFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}