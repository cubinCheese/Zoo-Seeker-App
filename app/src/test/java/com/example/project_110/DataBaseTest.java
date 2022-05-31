package com.example.project_110;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class DataBaseTest {
    private VertexInfoStorableDao dao;
    private VertexDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, VertexDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.vertexInfoDao();
    }
    @After
    public void closeDb() throws IOException{
        db.close();
    }

    @Test
    public void testInsert(){
        VertexInfoStorable item1 = new VertexInfoStorable(
                new ZooData.VertexInfo("grizz", "no_parent", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>(), 0.0, 0.0));
        VertexInfoStorable item2 = new VertexInfoStorable(
                new ZooData.VertexInfo("grizz", "no_parent", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>(), 0.0, 0.0));

        long id1 = dao.insert(item1);
        long id2 = dao.insert(item2);

        assertNotEquals(id1, id2);
    }
/*
    @Test
    public void testGet(){
        VertexInfoStorable insertedItem = new VertexInfoStorable(
                new ZooData.VertexInfo("grizz", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>()));
        long id = dao.insert(insertedItem);

        VertexInfoStorable item = dao.get(id);
        assertEquals(id,item.id);
        assertEquals(insertedItem.id,item.id);
        assertEquals(insertedItem.name, item.name);
        assertEquals(insertedItem.kind, item.kind);

    }

    @Test
    public void testUpdate(){
        VertexInfoStorable item = new VertexInfoStorable(
                new ZooData.VertexInfo("grizz", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>()));
        long id = dao.insert(item);

        item = dao.get(id);
        item.id = "Photos of Spider-Man";
        int itemsUpdated = dao.update(item);
        assertEquals(1, itemsUpdated);

        item = dao.get(id);
        assertNotNull(item);
        assertEquals("Photos of Spider-Man",item.id);

    }

    @Test
    public void testDelete(){
        VertexInfoStorable item = new VertexInfoStorable(
                new ZooData.VertexInfo("grizz", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>()));
        long id = dao.insert(item);

        item = dao.get(id);
        int itemsDeleted = dao.delete(item);
        assertEquals(1,itemsDeleted);
        assertNull(dao.get(id));
    }
*/


}
