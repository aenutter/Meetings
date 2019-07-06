package org.example.aafinder;

import org.example.aafinder.R;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.example.aafinder.MyTodoContentProvider;
import org.example.aafinder.TodoTable;

/*
 * TodoDetailActivity allows to enter a new todo item 
 * or to change an existing
 */
public class TodoDetailActivity extends Activity {
  private TextView mGroupText;	
  private TextView mCityText;
  private TextView mStateText;
  private Uri todoUri;

  @Override
  protected void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.todo_view);

    mGroupText = (TextView) findViewById(R.id.todo_view_group);
    mCityText = (TextView) findViewById(R.id.todo_view_city);
    mStateText = (TextView) findViewById(R.id.todo_view_state);

    Bundle extras = getIntent().getExtras();

    // Check from the saved Instance
    todoUri = (bundle == null) ? null : (Uri) bundle
        .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);

    // Or passed from the other activity
    if (extras != null) {
      todoUri = extras
          .getParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE);

      fillData(todoUri);
    }

  }

  
  //String searchQuery = "column1 like '%" + searchKey + "%' or column2 like '%" + searchKey + "%'";
  //c = getContentResolver().query(uri, null, searchQuery, null, "date DESC");
  String searchQuery = TodoTable.COLUMN_ID + "< 10000";
  
  private void fillData(Uri uri) {
    String[] projection = { TodoTable.COLUMN_SUMMARY,
        TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY };
    Cursor cursor = getContentResolver().query(uri, projection, searchQuery, null, null);
    if (cursor != null) {
      cursor.moveToFirst();
      String category = cursor.getString(cursor
          .getColumnIndexOrThrow(TodoTable.COLUMN_CATEGORY));


      mGroupText.setText(cursor.getString(cursor.getColumnIndexOrThrow(TodoTable.COLUMN_GROUP_NAME)));

      mCityText.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(TodoTable.COLUMN_CITY)));
      mStateText.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(TodoTable.COLUMN_STATE)));

      // Always close the cursor
      cursor.close();
    }
  }

  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    //saveState();
    outState.putParcelable(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
  }

  @Override
  protected void onPause() {
    super.onPause();
    //saveState();
  }

  /*private void saveState() {
    String category = (String) mCategory.getSelectedItem();
    String summary = mTitleText.getText().toString();
    String description = mBodyText.getText().toString();

    // Only save if either summary or description
    // is available

    if (description.length() == 0 && summary.length() == 0) {
      return;
    }

    ContentValues values = new ContentValues();
    values.put(TodoTable.COLUMN_CATEGORY, category);
    values.put(TodoTable.COLUMN_SUMMARY, summary);
    values.put(TodoTable.COLUMN_DESCRIPTION, description);

    if (todoUri == null) {
      // New todo
      todoUri = getContentResolver().insert(MyTodoContentProvider.CONTENT_URI, values);
    } else {
      // Update todo
      getContentResolver().update(todoUri, values, null, null);
    }
  }*/

  private void makeToast() {
    Toast.makeText(TodoDetailActivity.this, "Please maintain a summary",
        Toast.LENGTH_LONG).show();
  }
} 