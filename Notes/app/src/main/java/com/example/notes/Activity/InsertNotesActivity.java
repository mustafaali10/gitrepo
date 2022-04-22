package com.example.notes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.notes.Model.Notes;
import com.example.notes.R;
import com.example.notes.ViewModel.NotesViewModel;
import com.example.notes.databinding.ActivityInsertNotesBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class InsertNotesActivity extends AppCompatActivity {

    ActivityInsertNotesBinding binding;
    String title,subtitle,notes;
    NotesViewModel notesViewModel;
    String priority="1";
    TextView notesData;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesData=findViewById(R.id.notesData);

        notesViewModel= ViewModelProviders.of(this).get(NotesViewModel.class);


        binding.ivMic.setOnClickListener(v -> {

                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                } catch (Exception e) {
                    Toast.makeText(this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

        });



        binding.greenPriority.setOnClickListener(v ->{
        priority="1";

        binding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
        binding.yellowPriority.setImageResource(0);
        binding.redPriority.setImageResource(0);

        });

        binding.yellowPriority.setOnClickListener(v ->{
            priority="2";

            binding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
            binding.greenPriority.setImageResource(0);
            binding.redPriority.setImageResource(0);

        });

        binding.redPriority.setOnClickListener(v ->{

            binding.greenPriority.setImageResource(0);
            binding.yellowPriority.setImageResource(0);
            binding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);

            priority="3";
        });


        //DONE BUTTON: After completing the notes to Save th notes
        binding.doneNotesBtn.setOnClickListener(v -> {

            title=binding.notesTitle.getText().toString();
            subtitle=binding.notesSubTitle.getText().toString();
            notes=binding.notesData.getText().toString();

            CreateNotes(title,subtitle,notes);


        } );


    }

    private void CreateNotes(String title, String subtitle, String notes) {
        //For Date
        Date date=new Date();
        CharSequence sequence= DateFormat.format("MMMM d,yyyy",date.getTime());


        Notes notes1=new Notes();
        notes1.notesTitle=title;
        notes1.notesSubtitle=subtitle;
        notes1.notes=notes;
        notes1.notesPriority=priority;
        notes1.notesDate=sequence.toString();
        notesViewModel.insertNote(notes1);
        Toast.makeText(this, "Notes Created Successfully", Toast.LENGTH_SHORT).show();

        finish();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                notesData.setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
    }


}