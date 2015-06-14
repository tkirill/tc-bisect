package org.tkirill.teamcity.bisectPlugin;

import jetbrains.buildServer.serverSide.CustomDataStorage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class BisectRepositoryTest {
    private CustomDataStorage storage;
    private BisectRepository sut;

    @BeforeMethod
    public void setUp() throws Exception {
        storage = mock(CustomDataStorage.class);
        sut = new BisectRepository(storage);
    }

    @Test
    public void Exists_StorageReturnsNull_ReturnsFalse() throws Exception {
        stub(storage.getValue("1")).toReturn(null);

        boolean actual = sut.exists(1);

        assertFalse(actual);
    }

    @Test
    public void Exists_StorageReturnsEmptyString_ReturnsFalse() throws Exception {
        stub(storage.getValue("1")).toReturn("");

        boolean actual = sut.exists(1);

        assertFalse(actual);
    }

    @Test
    public void Exists_StorageReturnsInvalidJson_ReturnsFalse() throws Exception {
        stub(storage.getValue("1")).toReturn("{\"foo:1}");

        boolean actual = sut.exists(1);

        assertFalse(actual);
    }

    @Test
    public void Exists_StorageReturnsBisect_ReturnsTrue() throws Exception {
        stub(storage.getValue("1")).toReturn("{\"buildId\":1}");

        boolean actual = sut.exists(1);

        assertTrue(actual);
    }

    @Test
    public void Create_Always_CallsStorage() throws Exception {
        sut.create(10);

        verify(storage).putValue("10", "{\"buildId\":10,\"builds\":[],\"isFinished\":false,\"changes\":[],\"solved\":false,\"answer\":0}");
    }

    @Test
    public void GetAllNotFinished_InvalidJson_ReturnsEmpty() throws Exception {
        Map<String, String> values = new HashMap<String, String>();
        values.put("10", "{foo: 3}");
        values.put("11", "{buildId: }");
        values.put("12", "");
        stub(storage.getValues()).toReturn(values);

        Bisect[] actual = sut.getAllNotFinished();

        assertEquals(actual.length, 0);
    }

    @Test
    public void GetAllNotFinished_Finished_ReturnsEmpty() throws Exception {
        Map<String, String> values = new HashMap<String, String>();
        values.put("10", "{\"buildId\":10,\"builds\":[],\"isFinished\":true}");
        stub(storage.getValues()).toReturn(values);

        Bisect[] actual = sut.getAllNotFinished();

        assertEquals(actual.length, 0);
    }

    @Test
    public void GetAllNotFinished_NotFinished_Returns() throws Exception {
        Map<String, String> values = new HashMap<String, String>();
        values.put("10", "{\"buildId\":10,\"builds\":[],\"isFinished\":false}");
        stub(storage.getValues()).toReturn(values);

        Bisect[] actual = sut.getAllNotFinished();

        assertEquals(actual.length, 1);
        assertEquals(actual[0].getBuildId(), 10);
    }

    @Test
    public void Get_Exists_Returns() throws Exception {
        stub(storage.getValue("10")).toReturn("{\"buildId\":10,\"builds\":[],\"isFinished\":true,\"changes\":[1, 2, 3],\"solved\":false,\"answer\":0}");

        Bisect actual = sut.get(10);

        assertEquals(actual.getChanges().size(), 3);
        assertTrue(actual.isFinished());
    }
}