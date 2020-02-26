package org.allesoft.simple_scheduler.parser.grmr.simple;

public class StreamPositionImpl implements StreamPosition {
    int position;
    IStream iStream;

    public StreamPositionImpl(int position, IStream iStream) {
        this.position = position;
        this.iStream = iStream;
    }

    @Override
    public void rollback() {
        iStream.rollback(position);
    }
}
