package com.example.chatdemo;

import android.app.*;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.PopupMenu;
import com.example.chatdemo.database.ChatContactAdapter;
import com.example.chatdemo.database.ChatContactCursorAdapter;
import com.example.chatdemo.database.DataProvider;


/**
 * Created by jeffreyfried on 4/25/15.
 */
public class ContactsListFragment extends ListFragment
                              implements LoaderManager.LoaderCallbacks<Cursor>, PopupMenu.OnMenuItemClickListener {
    //ChatContactDataSource chatContactDataSource;
    ChatContactCursorAdapter mAdapter;

    private String selectedContactName = "";

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
        View view = inflater.inflate(R.layout.chatcontacts_listfragment, container, false);
        return view;
    }
    static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
            DataProvider.COL_ID,
            DataProvider.COL_NAME,
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
//            setListShownNoAnimation(true);
            setListShown(true);
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
        selectedContactName = cursor.getString(cursor.getColumnIndexOrThrow(DataProvider.COL_NAME));

        showPopup(v);
    }

    private void viewMessagesFromContact(String contactName) {

        Common.setLastFragment(R.id.view_messages);

        ChatContactAdapter contact = new ChatContactAdapter(contactName);
        contact.updateCount(getActivity(), 0);

//        Bundle bundle = new Bundle();
//        bundle.putInt("position", position);
//        bundle.putLong("id", id);
//        bundle.putString(DataProvider.COL_NAME, contactName);
//        bundle.putInt(DataProvider.COL_COUNT, count);

        Common.setCurrentContact(contactName);
        ChatMessageListFragment cf = new ChatMessageListFragment();
//        cf.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentParentViewGroup, cf);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public static class DeleteContactDialogFragment extends DialogFragment {
        String contactName;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirm_remove_message)
                    .setPositiveButton(R.string.confirm_remove_contact, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ChatContactAdapter contact = new ChatContactAdapter(contactName);
                            contact.delete(getActivity());
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
    private void deleteContact(String contactName) {
        // to do: add confirm dialog
        DeleteContactDialogFragment newFragment = new DeleteContactDialogFragment();
        newFragment.contactName = contactName;
        newFragment.show(getFragmentManager(), "deleteContact");
    }

    private void showPopup(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.contact_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_messages:
                viewMessagesFromContact(selectedContactName);
                return true;
            case R.id.remove_contact:
                deleteContact(selectedContactName);
                return true;
            default:
                return false;
        }
    }
}