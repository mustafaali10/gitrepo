package com.example.notes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.notes.Model.Notes;
import com.example.notes.R;
import com.example.notes.ViewModel.NotesViewModel;
import com.example.notes.databinding.ActivityUpdateNotesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class UpdateNotesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    TextView notesData;

    String priority="1";
    ActivityUpdateNotesBinding binding;
    String stitle,ssubtitle,snotes,spriority;
    int iid;
    NotesViewModel notesViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesViewModel= ViewModelProviders.of(this).get(NotesViewModel.class);

        notesData=findViewById(R.id.notesData);


        iid=getIntent().getIntExtra("id",0);
        stitle=getIntent().getStringExtra("title");
        ssubtitle=getIntent().getStringExtra("subtitle");
        snotes=getIntent().getStringExtra("notes");
        spriority=getIntent().getStringExtra("priority");

        binding.upTitle.setText(stitle);
        binding.upSubtitle.setText(ssubtitle);
        binding.upNotes.setText(snotes);

        if(spriority.equals("1")){
            binding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
        }
        else if (spriority.equals("2")){
            binding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
        }
        else if (spriority.equals("3")) {
            binding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);
        }


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


        binding.updateNotesBtn.setOnClickListener(v -> {

           String title=binding.upTitle.getText().toString();
           String subtitle=binding.upSubtitle.getText().toString();
           String notes=binding.upNotes.getText().toString();

           UpdateNotes(title,subtitle,notes);
        });





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


    }

    private void UpdateNotes(String title, String subtitle, String notes) {

        Date date=new Date();
        CharSequence sequence= DateFormat.format("MMMM d,yyyy",date.getTime());
        Notes updateNotes=new Notes();

        updateNotes.id=iid;
        updateNotes.notesTitle=title;
        updateNotes.notesSubtitle=subtitle;
        updateNotes.notes=notes;
        updateNotes.notesPriority=priority;
        updateNotes.notesDate=sequence.toString();

        notesViewModel.updateNote(updateNotes);

        Toast.makeText(this, "Notes Updated Successfully", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delete_menu,menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.ic_delete) {

            BottomSheetDialog sheetDialog = new BottomSheetDialog(UpdateNotesActivity.this);

            View view= LayoutInflater.from(UpdateNotesActivity.this).
                    inflate(R.layout.delete_bottom_sheet,(LinearLayout)findViewById(R.id.bottomSheet));

            sheetDialog.setContentView(view);

            TextView yes,no;

            yes=view.findViewById(R.id.delete_yes);
            no=view.findViewById(R.id.delete_no);

            yes.setOnClickListener(v-> {

                notesViewModel.deleteNote(iid);

                finish();





            });

            no.setOnClickListener(v-> {

                sheetDialog.dismiss();

            });




            sheetDialog.show();


        }
        return true;

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