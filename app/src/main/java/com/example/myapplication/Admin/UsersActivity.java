package com.example.myapplication.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private DatabaseReference usersDatabase;
    private ArrayList<User> userList;
    private ArrayAdapter<User> userAdapter;
    private ListView usersList;
    private Button deleteButton;
    private ArrayList<String> selectedUsersIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        // Initialize database reference
        usersDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize user list and adapter
        userList = new ArrayList<>();
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, userList);

        // Initialize UI elements
        usersList = findViewById(R.id.users_list);
        deleteButton = findViewById(R.id.delete_button);

        // Set adapter to list view
        usersList.setAdapter(userAdapter);

        // Set choice mode to multiple
        usersList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        // Initialize selected users array
        selectedUsersIds = new ArrayList<>();

        // Set item click listener to update selected users array and delete button visibility
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Get selected user
                User user = userAdapter.getItem(position);

                // Check if item is checked or unchecked
                if (usersList.isItemChecked(position)) {
                    // Add user id to selected users array
                    selectedUsersIds.add(user.getId());
                } else {
                    // Remove user id from selected users array
                    selectedUsersIds.remove(user.getId());
                }

                // Show or hide delete button based on number of selected users
                if (selectedUsersIds.isEmpty()) {
                    deleteButton.setVisibility(View.GONE);
                } else {
                    deleteButton.setVisibility(View.VISIBLE);
                }
            }
        });



        // Set delete button click listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if any users are selected
                if (selectedUsersIds.isEmpty()) {
                    Toast.makeText(UsersActivity.this, "Please select at least one user to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete the selected users?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete selected users from database
                        for (String userId : selectedUsersIds) {
                            Query query = usersDatabase.orderByChild("id").equalTo(userId);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        dataSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Display error message
                                    Toast.makeText(UsersActivity.this, "Error deleting user from database", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        // Clear selected users array
                        selectedUsersIds.clear();

                        // Update user list
                        getUserList();
                        // Redirect to Admin activity
                        Intent intent = new Intent(UsersActivity.this, Admin.class);
                        startActivity(intent);
                        finish();
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        // Get user list from database
        getUserList();
    }

    private void getUserList() {
        usersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear user list
                userList.clear();

                // Loop through all users in database
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Get user object and add it to list
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }

                // Notify adapter of data change
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Display error message
                Toast.makeText(UsersActivity.this, "Error retrieving user list from database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

