package org.allesoft.simple_scheduler.parser.grmr.injected;

public class StringLexerStream implements LexerStream {
    final String buffer;
    int position = 0;

    public StringLexerStream(String buffer) {
        this.buffer = buffer;
    }

    @Override
    public LexerPoint begin() {
        return new LexerPoint() {
            int save = position;

            @Override
            public void rollback() {
                position = save;
            }
        };
    }

    public String getPiece(int length) throws ParseException {
        if (length + position > buffer.length()) {
            throw new ParseException();
        } else {
            String substring = buffer.substring(position, position + length);
            position += length;
            return substring;
        }
    }
}
