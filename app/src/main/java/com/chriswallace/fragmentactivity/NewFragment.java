package com.chriswallace.fragmentactivity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chriswallace.fragmentactivity.Database.ChatEntry;
import com.chriswallace.fragmentactivity.Database.HandlerDatabase;
import com.chriswallace.fragmentactivity.Database.ModelDatabase;
import com.chriswallace.fragmentactivity.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by cwallace on 9/11/14.
 */
public class NewFragment extends Fragment{

    public NewFragment() {
    }

    HandlerDatabase handler;
    ArrayList<String> textList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        final ListView myListView = (ListView) rootView.findViewById(R.id.my_list_view);
        final Button myButton = (Button) rootView.findViewById(R.id.my_button);




        handler = new HandlerDatabase(getActivity());
        handler.open();
         ArrayList<ChatEntry> returnedChats = handler.getAllMessages(); //CREATED OBJECT OF THE DATA I SHOULD GET FROM DATABASE
        ArrayList<String> messageList = new ArrayList<String>();

        for ( ChatEntry chat : returnedChats){
            messageList.add(chat.Date + " : " + chat.user + " : " +chat.Message); //FOR DISPLAY PURPOSES COMBINE THEM INTO ONE FIELD
            //I NOW REALIZE THAT THEY SHOULD BE IN SEPARATE FIELDS SO I CAN MORE EASILY ACCESS EACH ONE
        }


        String[] listChats = {};
        textList = new ArrayList<String>(Arrays.asList(listChats));

        myListView.setAdapter(new ChatAdapter(getActivity(), R.layout.chat_layout,
                messageList));
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView <?> arg0, View arg1, int arg2,
            long arg3) {
               editSelectedText(arg2); //ARG 2 is the index of what was clicked on



            }
        });
// MY BUTTON LISTENER, ADDS TEXT TO DATABASE, ALSO PREVENTS EMPTY LINES FROM BEING ENTERED
        myButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                myButton.setText(R.string.button_text);
                MyActivity activity = (MyActivity)getActivity();
                String date = getDate("MM/dd/yyyy hh:mm:ss");
                EditText editText = (EditText) rootView.findViewById(R.id.text_to_add);
                String stringToAdd =  editText.getText().toString();
                String userNameandChat = date + " : " + activity.username + " : " + stringToAdd; //FORMAT TO DISPLAY ON LISTVIEW
                if (stringToAdd.length() == 0 ) {
                    myButton.setText("ADD TEXT");
                }
                else{
                textList.add(userNameandChat);
                editText.setText("");
                handler.addInfoToDatabase(activity.username,stringToAdd,date);
                myListView.setAdapter(new ChatAdapter(getActivity(), R.layout.chat_layout,
                        textList));}


            }
        });




        return rootView;
    }

    public void clearText() { //CLEARS THE DATABASE AND THE LISTVIEW

        final ListView myListView = (ListView) getView().findViewById(R.id.my_list_view);
        myListView.setAdapter(null);

        handler.deleteDataBase();
        textList.clear();

    }
    public void editSelectedText(final int which){ //CALLED WHEN A LISTVIEW ITEM IS CLICKED ON
        //CREATES ALERTDIALOG WITH BUTTONS

        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .create();
        ad.setCancelable(false);
        ad.setTitle("Change text");

        final EditText input = new EditText(getActivity());
        ad.setView(input);
        ad.setButton(-1,"Accept", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int nothing) { //EDIT TEXT IS ACCEPTED WHEN THIS IS CLICKED

                String Value = input.getText().toString();
                final ListView myListView = (ListView) getView().findViewById(R.id.my_list_view);
                MyActivity activity = (MyActivity)getActivity();
                //MODFY ENTRY IN DATABASE BY PASSING ID TO A FUNCTION WITHIN MODEL DATABASE THEN RELOAD THE DATABASE
                handler = new HandlerDatabase(getActivity());

                handler.modifyChats(Value, which, activity.username); //WHICH INDICATES THE ROW OF LISTVIEW CLICKED ON
                ArrayList<ChatEntry> returnedChats = handler.getAllMessages(); //RELAOD DATABASE
                ArrayList<String> messageList = new ArrayList<String>();

                for ( ChatEntry chat : returnedChats){
                    messageList.add(chat.Date + " : " + chat.user + " : " +chat.Message);
                }


                String[] listChats = {};
                textList = new ArrayList<String>(Arrays.asList(listChats));

                myListView.setAdapter(new ChatAdapter(getActivity(), R.layout.chat_layout,
                        messageList)); //REPOPULATE LISTVIEW WITH EDITTED TEXT

                dialog.dismiss();

            }
        });
        //REFACTOR TO USE SAME onClick function with cases


        ad.setButton(-2,"Cancel", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }

    });
        ad.show();

    }
    public void change_user_alert() //CALLED FROM MyActivity, CREATES ALERT DIALOG WITH BUTTONS TO CHANGE USERNAME

    {


        AlertDialog ad = new AlertDialog.Builder(getActivity())
                .create();
        ad.setCancelable(false);
        ad.setTitle("Change Username");

        final EditText input = new EditText(getActivity());
        ad.setView(input);
       ad.setButton(-1,"Accept", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {
                String Value = input.getText().toString();
                MyActivity activity = (MyActivity)getActivity();
                activity.username = Value;
                dialog.dismiss();

            }
        });
        //REFACTOR TO USE SAME onClick function with separate cases is better practice, didn't have time


        ad.setButton(-2,"Cancel", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        ad.show();

    }



    public static String getDate(String dateFormat)
    {
        long milliSeconds = System.currentTimeMillis();
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
