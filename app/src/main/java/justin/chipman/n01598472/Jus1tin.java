package justin.chipman.n01598472;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Jus1tin extends Fragment {

    private EditText courseNameEdt, courseDescEdt;
    private Button addBtn, saveBtn, deleteBtn;
    private RecyclerView courseRV;

    private CourseAdapter adapter; // Make sure to create an adapter class for RecyclerView
    private ArrayList<CourseModal> courseModalArrayList;

    public Jus1tin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jus1tin, container, false);

        courseNameEdt = view.findViewById(R.id.idEdtCourseName);
        courseDescEdt = view.findViewById(R.id.idEdtCourseDescription);
        addBtn = view.findViewById(R.id.idBtnAdd);
        saveBtn = view.findViewById(R.id.idBtnSave);
        deleteBtn = view.findViewById(R.id.idBtnDelete);
        courseRV = view.findViewById(R.id.idRVCourses);

        loadData();
        buildRecyclerView();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseModalArrayList.add(new CourseModal(courseNameEdt.getText().toString(), courseDescEdt.getText().toString()));
                adapter.notifyItemInserted(courseModalArrayList.size());
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the list is empty
                if (!courseModalArrayList.isEmpty()) {
                    // Clear the list
                    courseModalArrayList.clear();
                    // Notify the adapter to update the RecyclerView
                    adapter.notifyDataSetChanged();
                    // Save the empty list to SharedPreferences
                    saveData();
                } else {
                    // Show a toast message if there's nothing to delete
                    Toast.makeText(getContext(), "No data to delete.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void buildRecyclerView() {
        adapter = new CourseAdapter(courseModalArrayList, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        courseRV.setHasFixedSize(true);
        courseRV.setLayoutManager(manager);
        courseRV.setAdapter(adapter);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("courses", null);
        Type type = new TypeToken<ArrayList<CourseModal>>() {}.getType();
        courseModalArrayList = gson.fromJson(json, type);

        if (courseModalArrayList == null) {
            courseModalArrayList = new ArrayList<>();
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(courseModalArrayList);
        editor.putString("courses", json);
        editor.apply();
        Toast.makeText(getContext(), "Saved Array List to Shared preferences.", Toast.LENGTH_SHORT).show();
    }
}