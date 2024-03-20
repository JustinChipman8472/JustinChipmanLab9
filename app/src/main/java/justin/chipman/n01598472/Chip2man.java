// justin chipman n01598472
package justin.chipman.n01598472;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Chip2man extends Fragment {

    private ToggleButton fileType;
    private EditText fileName, fileContents;

    public Chip2man() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chip2man, container, false);

        fileName = view.findViewById(R.id.activity_internalstorage_filename);
        fileContents = view.findViewById(R.id.jusactivity_internalstorage_filecontents);

        fileType = view.findViewById(R.id.activity_internalstorage_filetype);
        fileType.setChecked(true);

        view.findViewById(R.id.jusactivity_internalstorage_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile(fileType.isChecked());
            }
        });

        view.findViewById(R.id.jusactivity_internalstorage_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(fileType.isChecked());
            }
        });

        view.findViewById(R.id.jusactivity_internalstorage_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFile(fileType.isChecked());
            }
        });

        view.findViewById(R.id.jusactivity_internalstorage_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile(fileType.isChecked());
            }
        });

        return view;
    }

    private void createFile(boolean isPersistent) {
        File file;
        Context context = getContext(); // Use getContext() to get the Context in a Fragment
        if (isPersistent) {
            file = new File(context.getFilesDir(), fileName.getText().toString());
        } else {
            file = new File(context.getCacheDir(), fileName.getText().toString());
        }

        if (!file.exists()) {
            String input;
            try {
                input = getString(R.string.file_s_has_been_created);
                file.createNewFile();
                Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                input = getString(R.string.file_s_creation_failed);
                Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            }
        } else {
            String input = getString(R.string.file_s_already_exists);
            Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    private void writeFile(boolean isPersistent) {
        Context context = getContext();
        String input;
        try {
            FileOutputStream fileOutputStream;
            if (isPersistent) {
                fileOutputStream = context.openFileOutput(fileName.getText().toString(), Context.MODE_PRIVATE);
            } else {
                File file = new File(context.getCacheDir(), fileName.getText().toString());
                fileOutputStream = new FileOutputStream(file);
            }
            input = getString(R.string.write_to_s_successful);
            fileOutputStream.write(fileContents.getText().toString().getBytes(Charset.forName("UTF-8")));
            Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            input = getString(R.string.write_to_file_s_failed);
            Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    private void readFile(boolean isPersistent) {
        Context context = getContext();
        String input;
        try {
            FileInputStream fileInputStream;
            if (isPersistent) {
                fileInputStream = context.openFileInput(fileName.getText().toString());
            } else {
                File file = new File(context.getCacheDir(), fileName.getText().toString());
                fileInputStream = new FileInputStream(file);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            fileContents.setText(TextUtils.join("\n", lines));
            input = getString(R.string.read_from_file_s_successful);
            Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            input = getString(R.string.read_from_file_s_failed);
            Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            fileContents.setText("");

        }
    }

    private void deleteFile(boolean isPersistent) {
        Context context = getContext();
        String input;
        File file;
        if (isPersistent) {
            file = new File(context.getFilesDir(), fileName.getText().toString());
        } else {
            file = new File(context.getCacheDir(), fileName.getText().toString());
        }
        if (file.exists()) {
            file.delete();
            input = getString(R.string.file_s_has_been_deleted);
            Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } else {
            input = getString(R.string.file_s_doesn_t_exist);
            Toast.makeText(context, String.format(input, fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }
}