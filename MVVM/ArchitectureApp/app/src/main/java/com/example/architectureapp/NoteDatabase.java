package com.example.architectureapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities= {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {

        public  NoteDao noteDao;
        private static NoteDatabase instance;

        public static synchronized NoteDatabase getInstance(Context context) {
            if (instance == null){
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        NoteDatabase.class,"note_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build();
            }
            return instance;
        }

        private static RoomDatabase.Callback roomCallback = new  RoomDatabase.Callback(){
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                new PopulateDbAsyncTask(instance).execute();
            }
        };

        private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
            private NoteDao noteDao;

            private PopulateDbAsyncTask(NoteDatabase db){
                this.noteDao = db.noteDao;
            }

            @Override
            protected Void doInBackground(Void... voids) {
                noteDao.insert(new Note("title 1", "description 1", 1));
                noteDao.insert(new Note("title 2", "description 2", 2));
                noteDao.insert(new Note("title 3", "description 3", 3));
                return null;
            }
        }

}
