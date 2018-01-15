package com.lonchi.andrej.mathio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;
    Button mButton1;
    Button mButton2;

    //  Celkovy koren realtime databazy
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //  Odkaz ku premennej "condition", ktora je pod mRootRef
    DatabaseReference mConditionRef = mRootRef.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Firebase
        //  Get UI elements
        mTextView = findViewById(R.id.tv_try);
        mButton1 = findViewById(R.id.btn_try1);
        mButton2 = findViewById(R.id.btn_try2);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //  Zachytava kazdu zmenu hodnoty u "condition"
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mTextView.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //  Po klilknuti nastavi hodnotu na "Rainy"
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConditionRef.setValue("Rainy");
            }
        });

        //  Po kliknuti nastavi hodnotu na "Sunny"
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConditionRef.setValue("Sunny");
            }
        });

    }

    private void testUpload() {
        int i = 1 + 1;

        int ii = i + i;
    }
}
