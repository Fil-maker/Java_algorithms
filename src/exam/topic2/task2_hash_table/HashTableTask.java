package exam.topic2.task2_hash_table;

import java.io.*;
import java.util.*;

public class HashTableTask {

    static class Node {
        final int key;
        int value;
        Node next;

        Node(int key, int value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    static class HashTable {
        private static final int M = 100_003;
        private final Node[] buckets = new Node[M];

        private int hash(int key) {
            return ((key % M) + M) % M;
        }

        private Node find(int key) {
            for (Node node = buckets[hash(key)]; node != null; node = node.next) {
                if (node.key == key) return node;
            }
            return null;
        }

        void put(int key, int value) {
            Node node = find(key);
            if (node != null) {
                node.value = value;
            } else {
                int h = hash(key);
                buckets[h] = new Node(key, value, buckets[h]);
            }
        }

        /** Возвращает значение или null, если ключа нет. */
        Integer get(int key) {
            Node node = find(key);
            return node == null ? null : node.value;
        }

        void delete(int key) {
            int h = hash(key);
            Node prev = null;
            for (Node node = buckets[h]; node != null; node = node.next) {
                if (node.key == key) {
                    if (prev == null) buckets[h] = node.next;
                    else prev.next = node.next;
                    return;
                }
                prev = node;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int q = Integer.parseInt(br.readLine().trim());
        HashTable table = new HashTable();

        for (int i = 0; i < q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String command = st.nextToken();
            int key = Integer.parseInt(st.nextToken());

            if (command.equals("PUT")) {
                table.put(key, Integer.parseInt(st.nextToken()));
            } else if (command.equals("GET")) {
                Integer value = table.get(key);
                sb.append(value == null ? "NONE" : value.toString()).append('\n');
            } else {
                table.delete(key);
            }
        }

        System.out.print(sb);
    }
}
