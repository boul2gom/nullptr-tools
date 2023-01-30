package io.github.nullptr.tools.data;

import java.util.List;

/**
 * Interface for key-value data managers.
 * @param <Key> The type of the key.
 * @param <Data> The type of the data.
 */
public interface IKeyValueDataManager<Key, Data> {

    /**
     * Gets the data for the given key.
     * @param key The key of the data.
     * @return The data associated with the key.
     */
    Data getData(Key key);

    /**
     * Saves the data in the manager.
     * @param key The key of the data.
     * @param data The data to save.
     */
    void saveData(Key key, Data data);

    /**
     * Deletes the data for the given key.
     * @param key The key of the data.
     */
    void deleteData(Key key);

    /**
     * Get all the keys stored in the data manager.
     * @return The keys.
     */
    List<Key> getAllKeys();

    /**
     * Get all the data stored in the data manager, usually deserialized if the data type is an object.
     * @return The data.
     */
    List<Data> getAllData();
}
