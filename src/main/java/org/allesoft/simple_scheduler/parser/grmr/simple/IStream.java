package org.allesoft.simple_scheduler.parser.grmr.simple;

import java.io.InputStream;
import java.util.Stack;

public class IStream {
    StringBuilder array;
    InputStream inputStream;
    Stack<Integer> positions = new Stack<>();
    int position = 0;

    public IStream(InputStream inputStream) {
        this.array = new StringBuilder();
        this.inputStream = inputStream;
    }

    String getString(int length) throws RuntimeException {
        byte[] toRead = new byte[length];
        if (length == 0) {
            return "";
        }
        if (position == array.length()) {
            try {
                if (inputStream.read(toRead, 0, length) < length) {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String portion = new String(toRead);
            array.append(portion);
            position += length;
            return portion;
        } else {
            String first = array.substring(position, Math.min(array.length(), position + length));
            position += first.length();
            String second = array.length() < position + length ?
                    getString(length + position - array.length() - 1) :
                    "";
            return first.concat(second);
        }
    }

    StreamPosition tx() {
        return new StreamPositionImpl(position, this);
    }

    void rollback(int position) {
        this.position = position;
    }
}
