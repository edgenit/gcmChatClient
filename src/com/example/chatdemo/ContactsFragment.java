package com.example.chatdemo;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.chatdemo.database.ChatContactCursorAdapter;
import com.example.chatdemo.database.DataProvider;
import com.example.chatdemo.database.MySQLiteHelper;

/**
 * Created by jeffreyfried on 4/25/15.
 */
public class ContactsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    //ChatContactDataSource chatContactDataSource;
    ChatContactCursorAdapter mAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //can't call for custom content view
        setEmptyText(getString(R.string.no_contacts));
        //chatContactDataSource = new ChatContactDataSource(getActivity());
        //chatContactDataSource.open();
        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new ChatContactCursorAdapter(getActivity(), null);
        setListAdapter(mAdapter);
        // Start out with a progress indicator.
        setListShown(false);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void setEmptyText(CharSequence text) {
        //can't call for custom view list fragment
        //super.setEmptyText(text);
    }

    @Override
    public void setListShown(boolean shown) {
        //can't call for custom view list fragment
        //super.setListShown(shown);
    }

    @Override
    public void onResume() {
        getLoaderManager().restartLoader(0, null, this);
        //chatContactDataSource.open();
//        getLoaderManager().initLoader(0, null, this);
//        ContentResolver resolver = getActivity().getContentResolver();
//        ContentProviderClient client = resolver.acquireContentProviderClient(DataProvider.AUTHORITY);
//        DataProvider provider = (DataProvider) client.getLocalContentProvider();
//        provider.resetDatabase();
//        client.release();
        Log.i("Contacts", "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        //chatContactDataSource.close();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.threads, container, false);
        return view;
    }
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            DataProvider.COL_ID,
            DataProvider.COL_NAME,
            DataProvider.COL_EMAIL,
            DataProvider.COL_COUNT
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = Uri.withAppendedPath(DataProvider.CONTENT_URI_PROFILE
                    , Integer.toString(DataProvider.PROFILE_ALLROWS));
        return new CursorLoader(getActivity(), DataProvider.CONTENT_URI_PROFILE,
                CONTACTS_SUMMARY_PROJECTION, null, null,
                DataProvider.COL_NAME + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);

        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor)l.getAdapter().getItem(position);
        String name = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COL_NAME));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COL_EMAIL));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteHelper.COL_COUNT));

        Log.i("clicked email: ", cursor.getString(2));
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putLong("id", id);
        bundle.putString(MySQLiteHelper.COL_NAME, name);
        bundle.putString(MySQLiteHelper.COL_EMAIL, email);
        bundle.putInt(MySQLiteHelper.COL_COUNT, count);

//        ChatMessageListFragmentOld cf = new ChatMessageListFragmentOld();
        ChatMessageListFragment cf = new ChatMessageListFragment();
        cf.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentParentViewGroup, cf);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}