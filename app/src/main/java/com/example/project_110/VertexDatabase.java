package com.example.project_110;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {VertexInfoStorable.class}, version = 1, exportSchema = false)
public abstract class VertexDatabase extends RoomDatabase{
    
    
    private static VertexDatabase singleton = null;
    public abstract VertexInfoStorableDao vertexInfoDao();

    public synchronized static VertexDatabase getSingleton(Context context){
        if (singleton ==null){
            singleton = VertexDatabase.makeDatabase(context);
        }
        return singleton;
    }
    private static VertexDatabase makeDatabase(Context context){
       // context.deleteDatabase("select_list.db");
        return Room.databaseBuilder(context, VertexDatabase.class, "select_list.db")

                .allowMainThreadQueries()

                //Probably don't need this, it loads a list from a json and we don't start with any
                /*.addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db){
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(() ->{
                            List<VertexInfoStorable> todos = VertexInfoStorable
                                    .loadJSON(context, "demo_todos.json");
                            getSingleton(context).todoListItemDao().insertAll(todos);
                        });
                    }
                })*/


                .build();
    }
    @VisibleForTesting
    public static void injectTestDatabase(VertexDatabase testDatabase){
        if (singleton !=null){
            singleton.close();
        }
        singleton = testDatabase;
    }





}


