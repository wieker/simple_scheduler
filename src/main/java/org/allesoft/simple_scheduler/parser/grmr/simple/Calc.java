package org.allesoft.simple_scheduler.parser.grmr.simple;

public class Calc {
    void skipSpaces(IStream iStream) {
        do {
            StreamPosition tx = iStream.tx();
            String a = iStream.getString(1);
            if (!Character.isWhitespace(a.charAt(0))) {
                tx.rollback();
                return;
            }
        } while (true);
    }

    Tree parseInt(IStream stream) {
        skipSpaces(stream);
        int l = 0;
        StringBuilder v = new StringBuilder();
        do {
            StreamPosition tx = stream.tx();
            String a = stream.getString(1);
            if (Character.isDigit(a.charAt(0))) {
                v.append(a);
                l ++;
            } else if (l == 0) {
                tx.rollback();
                return null;
            } else {
                tx.rollback();
            }
            Tree result = new Tree();
            result.value = Integer.parseInt(v.toString());
            return result;
        } while(true);
    }

    Tree parsePlus(IStream stream) {
        skipSpaces(stream);
        StreamPosition tx = stream.tx();
        String a = stream.getString(1);
        if ("+".equalsIgnoreCase(a)) {
            return new Tree();
        } else {
            tx.rollback();
            return null;
        }
    }

    void parseStatement(IStream stream) {
        Tree left = parseInt(stream);
        Tree op = parsePlus(stream);
        if (op != null) {
            Tree right = parseInt(stream);
            System.out.println((Integer) left.value + (Integer) right.value);
        } else {
            System.out.println((Integer) left.value);
        }
    }

    public static void main(String[] args) {
        new Calc().parseStatement(new IStream(System.in));
    }
}
