package com.cnsc.research.misc.storage;

import java.io.IOException;

public abstract class StorageUtility {

    public abstract void upload(byte[] streamData, String fileName) throws IOException;

    public abstract void uploadOverwrite(byte[] streamData, String fileName) throws IOException;

    public abstract void delete(String filename);

    public abstract StorageUtility inContainer(String container);
}
