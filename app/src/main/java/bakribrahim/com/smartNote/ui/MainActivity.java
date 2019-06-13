package bakribrahim.com.smartNote.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.OutputStreamWriter;

import androidx.appcompat.app.AppCompatActivity;
import bakribrahim.com.smartNote.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void createfolder(View view) {
        File path=getFilesDir();
        OutputStreamWriter out;
        try {
            File f = new File(path.getCanonicalPath() + "/myfile.txt");
            out = new OutputStreamWriter(openFileOutput( f.getPath(), MODE_PRIVATE));
            out.write("test");
            out.close();
            Toast.makeText(MainActivity.this, f.getPath(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex ){
            Log.e("bakr", ex.getMessage());
        }
    }
}
