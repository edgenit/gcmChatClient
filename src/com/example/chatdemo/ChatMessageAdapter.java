package com.example.chatdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.chatdemo.database.ChatMessage;

import java.util.ArrayList;

/**
 * Created by jeffreyfried on 4/8/15.
 */
public class ChatMessageAdapter extends BaseAdapter implements View.OnClickListener {

        /*********** Declare Used Variables *********/
        private Activity activity;
        private ArrayList data;
        private static LayoutInflater inflater=null;
//        public Resources res;
        ChatMessage tempValues=null;
        int i=0;

        /*************  CustomAdapter Constructor *****************/
        public ChatMessageAdapter(Activity a, ArrayList d/*,Resources resLocal*/) {

            /********** Take passed values **********/
            activity = a;
            data=d;
//            res = resLocal;

            /***********  Layout inflator to call external xml layout () ***********/
            inflater = ( LayoutInflater )activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        //public Button text;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;
        LinearLayout item;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.chatmessage, null);


            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
           // holder.text = (TextView) vi.findViewById(R.id.textMessage);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (ChatMessage) data.get( position );
            holder.text = (TextView)vi.findViewById(R.id.chatMessageText);
            holder.text.setText(tempValues.getMessage());

            /************  Set Model values in Holder elements ***********/
            LinearLayout root = (LinearLayout) vi;
            item = (LinearLayout)vi.findViewById(R.id.chatmessageitem);

            int boxId=R.drawable.box;
            Drawable box = ContextCompat.getDrawable(activity.getBaseContext(), R.drawable.box);

            if(tempValues.getSender().equals("you")) {
                holder.text.setGravity(Gravity.END);
                root.setPadding(150, 10, 10, 10);
                holder.text.setTextColor(Color.WHITE);
                setBoxColor(box, Color.MAGENTA);
                item.setBackground(box);
//                item.setBackgroundColor(Color.MAGENTA);
            }
            else {
               // holder.text = (TextView)vi.findViewById(R.id.text2);
                root.setPadding(10, 10, 50, 10);
//                item.setBackgroundColor(Color.LTGRAY);
                setBoxColor(box, Color.LTGRAY);
                item.setBackground(box);
                holder.text.setGravity(Gravity.END);
            }

//            holder.text.setBackgroundResource(R.drawable.box);

            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {

//
//            CustomListViewAndroidExample sct = (CustomListViewAndroidExample)activity;
//
//            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
//
//            sct.onItemClick(mPosition);
        }
    }

    private void setBoxColor(Drawable drawable, int color) {


        int red = (color & 0xFF0000) / 0xFFFF;
        int green = (color & 0xFF00) / 0xFF;
        int blue = color & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red
                , 0, 0, 0, 0, green
                , 0, 0, 0, 0, blue
                , 0, 0, 0, 1, 0 };

        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(colorFilter);
    }
}
