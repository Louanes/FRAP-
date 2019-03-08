package fr.android.ppe.frap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseLink {
    //firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public FirebaseLink()
    {
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReferenceFromUrl("EDMT_FIREBASE");
    }

}
