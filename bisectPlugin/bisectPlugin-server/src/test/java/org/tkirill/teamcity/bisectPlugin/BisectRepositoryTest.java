package org.tkirill.teamcity.bisectPlugin;

import com.google.gson.Gson;
import jetbrains.buildServer.serverSide.CustomDataStorage;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.testng.Assert.*;

public class BisectRepositoryTest {
    CustomDataStorage storage;
    private BisectRepository sut;
    private Gson gson;

    @BeforeMethod
    public void setUp() throws Exception {
        storage = mock(CustomDataStorage.class);
        sut = new BisectRepository(storage);
        gson = new Gson();
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
    public void testCreate() throws Exception {

    }
}